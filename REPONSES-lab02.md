# Réponses au Lab 2 — Le domaine du quiz en code partagé

### 1. Pourquoi ne peut-on pas utiliser `java.text.Normalizer` directement dans `commonMain` ?
Le code situé dans `commonMain` est destiné à être compilé pour toutes les cibles du projet, y compris iOS (qui compile en binaire natif et n'a pas accès au JDK Java). Comme `java.text.Normalizer` appartient à la bibliothèque standard Java (`java.*`), le compilateur Kotlin interdit son utilisation dans `commonMain` afin de garantir que le code partagé reste 100% multiplateforme.

---

### 2. Pourquoi `PokemonRepository` et `ScoreRepository` sont des interfaces plutôt que des `expect class` ?
1. **Testabilité** : Une interface permet de créer facilement de fausses implémentations (Fakes/Mocks en mémoire) dans les tests unitaires sans dépendre d'un réseau ou d'une base de données.
2. **Découplage** : L'interface définit le contrat métier sans s'occuper de la façon dont les données sont récupérées (API REST Ktor, SQLDelight, etc.).
3. **Flexibilité** : Une interface permet d'avoir plusieurs implémentations distinctes sur une même plateforme (ex: un repository réseau et un repository de cache local), alors qu'un `expect class` impose une seule implémentation concrète par plateforme.

---

### 3. Effet d'un `TODO()` et ajout d'une cible `wasmJs()`
- Si le corps de `removeDiacritics()` vaut `TODO()`, la compilation réussit mais l'exécution des tests échoue avec une exception `NotImplementedError`.
- Si la déclaration `actual` était totalement absente, le compilateur refuserait de builder pour cette cible.
- Si on ajoutait la cible `wasmJs()` demain, Kotlin exigerait la création d'un fichier `TextNormalization.wasmJs.kt` fournissant l'implémentation `actual` pour WebAssembly/JS.

---

### 4. Pourquoi `QuizEngine.submit` reçoit `elapsed` en paramètre ?
Recevoir le temps écoulé en paramètre rend le `QuizEngine` totalement déterministe et prévisible. Dans les tests unitaires, on peut tester immédiatement n'importe quelle durée (3s, 10s, 9.999s) sans ralentir l'exécution avec de vraies pauses (`delay()` ou `Thread.sleep()`). Cela sépare aussi la logique métier pure du chronomètre qui est géré côté UI/ViewModel.

---

### 5. `Nidoran♀` et `Nidoran♂` se normalisent tous deux en `nidoran` : bug ou fonctionnalité ?
C'est la conséquence de la règle de nettoyage qui supprime les caractères non alphanumériques `[^a-z0-9]`. Du point de vue de l'expérience utilisateur, c'est une fonctionnalité plutôt pratique car cela évite d'avoir à saisir les symboles spéciaux `♀` ou `♂` sur un clavier de smartphone.

---

### 6. Qu'apporte le placement de `AnswerCheckerTest` dans `commonTest` plutôt que `jvmTest` ?
Dans `commonTest`, les tests sont exécutés sur chaque plateforme cible (JVM Desktop, Host Android, et iOS). Cela permet de vérifier que l'implémentation `actual` de `removeDiacritics()` produit rigoureusement les mêmes résultats sur toutes les plateformes, garantissant un comportement identique de la logique de vérification sur Android, Desktop et iOS.

---

### 7. Différence entre `IllegalArgumentException` (`require`) et `IllegalStateException` (`check`)
- **`IllegalArgumentException` (`require`)** : Valide les arguments transmis par l'appelant (ex: vérifier qu'une liste de Pokémon passée au constructeur n'est pas vide).
- **`IllegalStateException` (`check`)** : Valide l'état interne de l'objet (ex: vérifier que la partie n'est pas déjà terminée avant de soumettre une nouvelle réponse).
