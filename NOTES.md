# Lab 3 — Les écrans de PokéQuiz avec Compose Multiplatform

### 1. Pourquoi des composables stateless ? (3 avantages pour `QuestionContent`)
1. **Testabilité** : Un composable stateless est très simple à tester unitairement en lui injectant directement des valeurs fixes, sans instancier de moteur de jeu ou de ViewModel.
2. **Aperçu dans l'IDE (Previews)** : On peut très facilement créer des `@Preview` pour visualiser tous les états de l'écran (avec ou sans indice, champ rempli, etc.) simplement en passant des paramètres figés.
3. **Architecture propre** : La logique métier est totalement découpée de l'IHM. L'IHM ne fait qu'afficher des données et remonter des événements via des callbacks, évitant les désynchronisations d'état.

---

### 2. Pourquoi la `contentDescription` de l'image grise doit-elle être générique ?
- **Pour éviter de gâcher le jeu** : L'arbre de sémantique est lu par les outils d'accessibilité (comme TalkBack sur Android) ou les outils d'inspection UI. Si la `contentDescription` contenait le vrai nom du Pokémon (ex: `"Pikachu"`), le lecteur d'écran le lirait à voix haute et donnerait la réponse au joueur.
- **Vérification sur Android** : On peut activer **TalkBack** dans les Paramètres d'Accessibilité Android ou utiliser le **Layout Inspector** dans Android Studio.

---

### 3. Recomposition lors de la saisie d'une lettre
- **Ce qui se recompose** : Seul le champ de texte `OutlinedTextField` et le bouton `Valider` (dont l'état `enabled` dépend du contenu du champ) se recomposent.
- **Ce qui est sauté (*skipped*)** : Le composable `PokemonImage`, le titre et la barre de progression sont ignorés car leurs paramètres n'ont pas changé.
- **L'image** : L'image n'est **pas rechargée** depuis le réseau, car Coil utilise son cache mémoire et Compose réutilise le composant existant.

---

### 4. Changement de thème ou de langue pendant une partie sur Android
- **Actuellement** : La partie en cours est réinitialisée car le changement de thème ou de langue provoque une destruction/récréation de l'Activity Android (`configuration change`), réinitialisant tous les états `remember` locaux de `App()`.
- **Au Lab 4** : L'état sera conservé car il sera géré par un `QuizViewModel` qui survit aux changements de configuration de l'Activity.

---

### 5. Pourquoi `index = quiz.results.size - 1` dans la révélation ?
Lors de la soumission d'une réponse avec `quiz.submit(...)`, le résultat est immédiatement ajouté à la liste `quiz.results`, et `quiz.currentIndex` s'incrémente pour désigner la question suivante. L'index de la réponse qu'on vient de valider et qu'on souhaite afficher correspond donc au dernier élément inséré dans `results`, c'est-à-dire `quiz.results.size - 1`.

---

### 6. Le filtre gris empêche-t-il de tricher ?
Non, le filtre gris (`setToSaturation(0f)`) est un simple effet visuel appliqué localement lors du dessin de l'image sur le client.
Deux façons de contourner le jeu :
1. Inspecter le trafic réseau ou le cache de l'application pour lire l'URL de l'image (qui contient l'ID du Pokémon).
2. Inspecter les objets en mémoire de l'application où l'objet `Pokemon` (contenant `nameFr`) est stocké.
