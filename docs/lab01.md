# Lab 1 : Setup, Exploration et Écran d'accueil PokéQuiz

**Package** : `fr.m2.pokequiz`  
**Projet** : `PokeQuiz` (Android, Desktop, iOS)

---

## Étape 5 — Exploration de l'arborescence

### 1. Combien de modules Gradle contient le projet ? Lesquels ? Où sont-ils déclarés ? `iosApp` en fait-il partie ? Pourquoi ?
- Il y a **3 modules Gradle** :
  - `:androidApp` (l'application Android)
  - `:desktopApp` (l'application Desktop)
  - `:shared` (le code partagé)
- Ils sont déclarés dans `settings.gradle.kts` avec `include(":androidApp")`, `include(":desktopApp")`, `include(":shared")`.
- **`iosApp` n'en fait pas partie** : C'est un projet Xcode natif (`.xcodeproj`). Il est géré par Xcode, qui utilise le framework généré par Gradle lors du build.

### 2. Quelles versions de Kotlin, Compose Multiplatform, AGP et Gradle sont utilisées ?
- **Kotlin** : `2.4.20`
- **Compose Multiplatform** : `1.12.1`
- **AGP (Android Gradle Plugin)** : `9.4.1`
- **Gradle** : `9.6.0`  
*(Toutes ces versions sont définies dans `gradle/libs.versions.toml` et `gradle-wrapper.properties`)*.

### 3. Quels plugins applique `shared/build.gradle.kts` ? Et `androidApp/build.gradle.kts` ? Pourquoi `androidApp` n'applique-t-il pas `org.jetbrains.kotlin.android` ?
- **Dans `shared/build.gradle.kts`** :
  - `kotlinMultiplatform`
  - `androidMultiplatformLibrary`
  - `composeMultiplatform`
  - `composeCompiler`
- **Dans `androidApp/build.gradle.kts`** :
  - `androidApplication`
  - `composeCompiler`
- **Pourquoi `androidApp` n'utilise pas `kotlin.android`** :
  Parce que tout le code Kotlin et l'UI Compose sont centralisés dans le module `:shared`. Le module `:androidApp` sert juste de lanceur pour Android et applique `com.android.application`.

### 4. Où sont configurés le `minSdk` de la bibliothèque partagée et celui de l'application ? Sous quelle forme ?
- Il est défini dans le **Version Catalog** (`gradle/libs.versions.toml`) sous `android-minSdk = "36"`.
- Il est ensuite récupéré dans `shared/build.gradle.kts` et `androidApp/build.gradle.kts` avec `minSdk = libs.versions.android.minSdk.get().toInt()`.

### 5. Listez les dossiers de `shared/src/`. Pourquoi n'y a-t-il pas de `iosArm64Main` alors que la cible `iosArm64()` est déclarée ?
- **Dossiers présents** : `commonMain`, `commonTest`, `androidMain`, `androidHostTest`, `jvmMain`, `jvmTest`, `iosMain`, `iosTest`.
- **Explication pour `iosArm64Main`** : Kotlin regroupe automatiquement les différentes cibles iOS (`iosArm64()` et `iosSimulatorArm64()`) dans un seul dossier parent `iosMain`. On écrit donc tout le code iOS dans `iosMain`.

### 6. Où est déclarée `expect fun getPlatform()` ? Combien d'`actual` existe-t-il, et dans quels fichiers ?
- **`expect`** : déclarée dans `shared/src/commonMain/kotlin/fr/m2/pokequiz/Platform.kt`.
- **3 `actual`** :
  1. `Platform.android.kt` (dans `androidMain`)
  2. `Platform.jvm.kt` (dans `jvmMain`)
  3. `Platform.ios.kt` (dans `iosMain`)

### 7. Quel est l'`applicationId` Android ? Quelle est la `mainClass` desktop, et pourquoi se termine-t-elle par `Kt` ?
- **`applicationId` Android** : `fr.m2.pokequiz` (dans `androidApp/build.gradle.kts`).
- **`mainClass` Desktop** : `fr.m2.pokequiz.MainKt` (dans `desktopApp/build.gradle.kts`).
- **Pourquoi la terminaison `Kt`** : La fonction `main()` est déclarée hors d'une classe dans `main.kt`. Le compilateur Kotlin génère donc automatiquement une classe Java nommée `MainKt`.

### 8. Où la fonction `App()` est-elle définie ? Depuis combien d'endroits est-elle appelée, et lesquels ?
- **Définie dans** : `shared/src/commonMain/kotlin/fr/m2/pokequiz/App.kt`.
- **Appelée depuis 3 endroits** :
  1. Android : `MainActivity.kt` (`setContent { App() }`)
  2. Desktop : `main.kt` (`Window { App() }`)
  3. iOS : `MainViewController.kt` (`ComposeUIViewController { App() }`)

### 9. Que contient le `.gitignore` généré ? Pourquoi `local.properties` doit-il être ignoré ?
- Il contient les dossiers générés par le build et l'IDE (`build/`, `.gradle/`, `.idea/`, `.kotlin/`, `local.properties`, etc.).
- `local.properties` doit être ignoré car il contient les chemins d'accès propres à l'ordinateur du développeur (comme le chemin du SDK Android local).

### 10. Dans `shared/src/commonMain/.../App.kt`, ajoutez temporairement `val f = java.io.File("x")` dans `App()`. Que dit l'IDE ? Pourquoi ?
- **Erreur de l'IDE** : `Unresolved reference: java`.
- **Pourquoi** : Le dossier `commonMain` doit pouvoir être compilé sur toutes les plateformes, y compris iOS. Or `java.io.File` fait partie du JDK Java et n'existe pas sur iOS.

---

## Étape 7 — Observations Compose Hot Reload

1. **Modifications d'UI (libellé, couleur)** : Le Hot Reload applique les changements directement sur l'application Desktop sans la redémarrer et sans réinitialiser la valeur du compteur.
2. **Renommage de `main` ou ajout de dépendances** : Modifier le point d'entrée ou ajouter une dépendance dans Gradle nécessite un redémarrage complet du projet.

---

## Étape 8 — Écran d'accueil PokéQuiz

- **`Greeting.kt`** : Modifié pour retourner `"Bonjour depuis ${platform.name}"`.
- **`Platform.jvm.kt`** : Mis à jour pour retourner `"Desktop (${System.getProperty("os.name")}) · Java ${System.getProperty("java.version")}"`.
- **`App.kt`** :
  - `Column` centrée avec espacement de `16.dp` et `safeContentPadding()`.
  - Titre `"Bienvenue dans PokéQuiz"` en `headlineMedium`.
  - Texte de salutation `Greeting().greet()`.
  - Bouton Material 3 incrémentant un état `count` (`remember { mutableIntStateOf(0) }`) et affichant `"Compteur : $count"`.

---

## Questions de réflexion (Fin de TP)

### 1. Pourquoi est-il intéressant de développer l'UI sur la cible Desktop même si le produit final est une app mobile ?
C'est beaucoup plus rapide à lancer qu'un émulateur Android ou un simulateur iOS, ça consomme moins de RAM/CPU sur le PC, et le Hot Reload permet de tester l'UI instantanément.

### 2. Quelle différence faites-vous entre un Hot Reload et un redémarrage ? Quels changements ont nécessité un redémarrage complet à l'étape 7 ?
- **Hot Reload** : Injecte les modifications de code dans l'application en cours d'exécution en gardant l'état de l'écran.
- **Redémarrage** : Ferme l'application et la relance depuis le début (l'état est perdu).
- Modifier la fonction `main()` ou le fichier `build.gradle.kts` a nécessité un redémarrage.

### 3. Que se passerait-il si quelqu'un ajoute dans `shared/build.gradle.kts` un bloc `androidTarget { }` trouvé dans un tutoriel de 2023 ?
Il y aura une erreur Gradle à la compilation (`Unresolved reference: androidTarget`), car avec les versions récentes d'AGP 9, on utilise maintenant le bloc `android { ... }`.

### 4. Le texte « Hello/Bonjour depuis ... » est différent sur chaque plateforme alors que `Greeting` est en code commun. Expliquez le mécanisme.
`Greeting` appelle `getPlatform()`, qui est déclarée avec le mot-clé `expect` dans `commonMain`. Chaque plateforme fournit ensuite sa version `actual` avec ses propres informations système (Android, Desktop, iOS).

### 5. Le même `Greeting` est compilé en DEX (Android), en bytecode JVM (desktop) et en binaire natif (iOS). Selon vous, laquelle de ces formes est la plus facile à déboguer ou à décompiler pour un auditeur de sécurité ? Pourquoi ? Quels mécanismes peuvent rendre le code commun sensible dans `commonMain` plus difficile à analyser ?
- **Le DEX et le bytecode JVM** sont les plus faciles à décompiler (avec des outils comme JADX) car ils conservent la structure du code source.
- **Le binaire natif iOS** est plus difficile car il est compilé en code binaire machine ARM64.
- Pour protéger le code, on peut utiliser des outils d'obfuscation comme ProGuard/R8.

### 6. Tâches Gradle liées à l'exécution (`./gradlew tasks --all | grep -i run`)
- Exécution Desktop : `./gradlew :desktopApp:run`
- Hot Reload Desktop : `./gradlew :desktopApp:hotRun`
- Android : `./gradlew :androidApp:installDebug`

### 7. Sorties de compilation dans `shared/build/`
- Les fichiers `.class` sont dans `shared/build/classes/kotlin/jvm/main/`.
- Les fichiers `.klib` sont les bibliothèques intermédiaires Kotlin Multiplatform contenant les métadonnées pour le code commun et iOS.

### 8. Arbre de dépendances runtime Desktop et bibliothèque Skiko
- Commande : `./gradlew :shared:dependencies --configuration jvmRuntimeClasspath`
- **Skiko** (`skiko-awt-runtime-...`) apparaît car c'est la bibliothèque graphique basée sur Skia qui permet à Compose de dessiner l'interface sur Desktop.

### 9. Preflight checks
L'outil Preflight Checks permet de vérifier si l'environnement de dev (JDK, Android SDK, Xcode) est bien configuré.
