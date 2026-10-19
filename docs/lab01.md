# Lab 1 : Setup, Exploration et Écran d'accueil PokéQuiz

**Package / Group ID** : `fr.m2.pokequiz`  
**Projet** : `PokeQuiz` (Kotlin Multiplatform : Android, Desktop, iOS)

---

## Étape 5 — Questions d'exploration de l'arborescence

### 1. Combien de modules Gradle contient le projet ? Lesquels ? Où sont-ils déclarés ? `iosApp` en fait-il partie ? Pourquoi ?
- **Nombre de modules** : 3 modules Gradle.
- **Liste des modules** :
  - `:androidApp` (Application Android)
  - `:desktopApp` (Application Desktop JVM)
  - `:shared` (Bibliothèque Kotlin Multiplatform partagée)
- **Fichier de déclaration** : [settings.gradle.kts](file:///C:/Users/mathe/Desktop/PokeQuiz/settings.gradle.kts) via les lignes `include(":androidApp")`, `include(":desktopApp")`, `include(":shared")`.
- **`iosApp`** : Non, `iosApp` ne fait pas partie des modules Gradle. Il s'agit d'un projet Xcode natif (`iosApp.xcodeproj`). La compilation iOS est pilotée par Xcode, qui fait appel au binaire/framework Kotlin généré par le module Gradle `:shared` via un script d'intégration.

### 2. Quelles versions de Kotlin, Compose Multiplatform, AGP et Gradle sont utilisées ?
- **Kotlin** : `2.4.20` (déclaré dans [gradle/libs.versions.toml](file:///C:/Users/mathe/Desktop/PokeQuiz/gradle/libs.versions.toml#L13))
- **Compose Multiplatform** : `1.12.1` (déclaré dans [gradle/libs.versions.toml](file:///C:/Users/mathe/Desktop/PokeQuiz/gradle/libs.versions.toml#L11))
- **AGP (Android Gradle Plugin)** : `9.1.1` (déclaré dans [gradle/libs.versions.toml](file:///C:/Users/mathe/Desktop/PokeQuiz/gradle/libs.versions.toml#L2))
- **Gradle** : `9.5.1` (Gradle Wrapper)

### 3. Quels plugins applique `shared/build.gradle.kts` ? Et `androidApp/build.gradle.kts` ? Pourquoi `androidApp` n'applique-t-il pas `org.jetbrains.kotlin.android` ?
- **Dans `shared/build.gradle.kts`** :
  - `alias(libs.plugins.kotlinMultiplatform)` (`org.jetbrains.kotlin.multiplatform`)
  - `alias(libs.plugins.androidMultiplatformLibrary)` (`com.android.kotlin.multiplatform.library`)
  - `alias(libs.plugins.composeMultiplatform)` (`org.jetbrains.compose`)
  - `alias(libs.plugins.composeCompiler)` (`org.jetbrains.kotlin.plugin.compose`)
- **Dans `androidApp/build.gradle.kts`** :
  - `alias(libs.plugins.androidApplication)` (`com.android.application`)
  - `alias(libs.plugins.composeCompiler)` (`org.jetbrains.kotlin.plugin.compose`)
- **Pourquoi `androidApp` n'applique pas `org.jetbrains.kotlin.android`** :
  Avec AGP 9 et la nouvelle architecture Kotlin Multiplatform, le code applicatif et les composants Compose se situent dans le module `:shared`. Le module `:androidApp` est un runner natif léger appliquant `com.android.application` et dépendant du module `:shared`.

### 4. Où sont configurés le `minSdk` de la bibliothèque partagée et celui de l'application ? Sous quelle forme (valeur en dur ou catalog) ?
- Configuré sous forme de **Version Catalog** dans [gradle/libs.versions.toml](file:///C:/Users/mathe/Desktop/PokeQuiz/gradle/libs.versions.toml#L3) avec `android-minSdk = "36"`.
- Référencé dans :
  - [shared/build.gradle.kts](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/build.gradle.kts#L28) : `minSdk = libs.versions.android.minSdk.get().toInt()`
  - [androidApp/build.gradle.kts](file:///C:/Users/mathe/Desktop/PokeQuiz/androidApp/build.gradle.kts#L27) : `minSdk = libs.versions.android.minSdk.get().toInt()`

### 5. Listez les dossiers de `shared/src/`. Pourquoi n'y a-t-il pas de `iosArm64Main` alors que la cible `iosArm64()` est déclarée ?
- **Dossiers dans `shared/src/`** : `commonMain`, `commonTest`, `androidMain`, `androidHostTest`, `jvmMain`, `jvmTest`, `iosMain`, `iosTest`.
- **Explication pour `iosArm64Main`** : Grâce au mécanisme de *Default Kotlin Hierarchy Template*, Kotlin regroupe automatiquement les cibles iOS (`iosArm64()` et `iosSimulatorArm64()`) sous un sourceSet partagé intermédiaire nommé `iosMain`. Tout le code spécifique à iOS est ainsi centralisé dans `iosMain`.

### 6. Où est déclarée `expect fun getPlatform()` ? Combien d'`actual` existe-t-il, et dans quels fichiers ?
- **Déclaration `expect`** : [shared/src/commonMain/kotlin/fr/m2/pokequiz/Platform.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/src/commonMain/kotlin/fr/m2/pokequiz/Platform.kt)
- **3 implémentations `actual`** :
  1. [shared/src/androidMain/kotlin/fr/m2/pokequiz/Platform.android.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/src/androidMain/kotlin/fr/m2/pokequiz/Platform.android.kt) (Android)
  2. [shared/src/jvmMain/kotlin/fr/m2/pokequiz/Platform.jvm.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/src/jvmMain/kotlin/fr/m2/pokequiz/Platform.jvm.kt) (Desktop / JVM)
  3. [shared/src/iosMain/kotlin/fr/m2/pokequiz/Platform.ios.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/src/iosMain/kotlin/fr/m2/pokequiz/Platform.ios.kt) (iOS)

### 7. Quel est l'`applicationId` Android ? Quelle est la `mainClass` desktop, et pourquoi se termine-t-elle par `Kt` ?
- **`applicationId` Android** : `fr.m2.pokequiz` (dans [androidApp/build.gradle.kts](file:///C:/Users/mathe/Desktop/PokeQuiz/androidApp/build.gradle.kts#L26))
- **`mainClass` Desktop** : `fr.m2.pokequiz.MainKt` (dans [desktopApp/build.gradle.kts](file:///C:/Users/mathe/Desktop/PokeQuiz/desktopApp/build.gradle.kts#L20))
- **Explication de la terminaison `Kt`** : En Kotlin, les fonctions top-level (telle que `fun main()`) déclarées dans un fichier `main.kt` sont compilées par la JVM dans une classe Java générée automatiquement nommée `<NomDuFichier>Kt` (soit `MainKt`).

### 8. Où la fonction `App()` est-elle définie ? Depuis combien d'endroits est-elle appelée, et lesquels ?
- **Définition** : [shared/src/commonMain/kotlin/fr/m2/pokequiz/App.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/src/commonMain/kotlin/fr/m2/pokequiz/App.kt)
- **3 points d'entrée d'exécution principaux** :
  1. Android : [androidApp/src/main/kotlin/fr/m2/pokequiz/MainActivity.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/androidApp/src/main/kotlin/fr/m2/pokequiz/MainActivity.kt) (`setContent { App() }`)
  2. Desktop : [desktopApp/src/main/kotlin/fr/m2/pokequiz/main.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/desktopApp/src/main/kotlin/fr/m2/pokequiz/main.kt) (`Window { App() }`)
  3. iOS : [shared/src/iosMain/kotlin/fr/m2/pokequiz/MainViewController.kt](file:///C:/Users/mathe/Desktop/PokeQuiz/shared/src/iosMain/kotlin/fr/m2/pokequiz/MainViewController.kt) (`ComposeUIViewController { App() }`)

### 9. Que contient le `.gitignore` généré ? Pourquoi `local.properties` doit-il être ignoré ?
- Contient les répertoires de compilation et caches d'environnement : `.gradle/`, `build/`, `local.properties`, `.idea/`, `.kotlin/`, `*.iml`, `xcuserdata/`.
- **Pourquoi `local.properties` doit être ignoré** : Il contient les chemins d'accès locaux propres à la machine (ex: `sdk.dir`) et parfois des identifiants/clés locales. Le versionner provoquerait des conflits entre développeurs et sur les serveurs d'intégration continue (CI).

### 10. Dans `shared/src/commonMain/.../App.kt`, ajoutez temporairement `val f = java.io.File("x")` dans `App()`. Que dit l'IDE ? Pourquoi ?
- **Message IDE** : Erreur de compilation : *Unresolved reference 'java'*.
- **Explication** : Le code du sourceSet `commonMain` est destiné à être compilé pour toutes les plateformes (dont iOS / Native). Les classes du JDK Java comme `java.io.File` ne sont disponibles que sur la JVM et sont absentes sur iOS. Le code commun ne doit utiliser que la bibliothèque standard Kotlin ou des bibliothèques multiplateformes.

---

## Étape 7 — Observations Compose Hot Reload

1. **Modifications à chaud (UI et Libellé)** :
   - En modifiant le texte du bouton ou le style de fond (`Modifier.background(...)`), la modification est appliquée en temps réel sur la fenêtre Desktop sans redémarrage du processus et sans perte de l'état du compteur.
2. **Impact du renommage de `main` ou de l'ajout d'une dépendance Gradle** :
   - Modifier la signature / le nom de la fonction d'entrée `main()` ou altérer la configuration des dépendances Gradle détruit le contexte de rechargement à chaud et nécessite un redémarrage complet de l'application (`:desktopApp:run`).

---

## Étape 8 — Écran d'accueil PokéQuiz (Implémentation)

- **`Greeting.kt`** : Retourne `"Bonjour depuis ${platform.name}"`.
- **`Platform.jvm.kt`** : Retourne `"Desktop (${System.getProperty("os.name")}) · Java ${System.getProperty("java.version")}"`.
- **`App.kt`** :
  - Composant `Column` centré horizontalement et verticalement (`safeContentPadding()`, espacement de 16.dp).
  - Titre `"Bienvenue dans PokéQuiz"` avec le style `MaterialTheme.typography.headlineMedium`.
  - Texte affichant le résultat de `Greeting().greet()`.
  - Bouton Material 3 modifiant un état `remember { mutableIntStateOf(0) }` affichant `"Compteur : $count"`.

---

## Questions de réflexion et d'analyse (Fin de TP)

### 1. Pourquoi est-il intéressant de développer l'UI sur la cible Desktop même si le produit final est une app mobile ?
- **Lancement instantané** : La cible Desktop démarre directement sur la JVM sans le surcoût de démarrage et d'émulation matérielle d'un émulateur Android ou d'un simulateur iOS.
- **Compose Hot Reload** : Permet de voir les modifications graphiques et d'ergonomie en temps réel sans perdre l'état courant de l'écran.
- **Sobriété en ressources** : Économise la mémoire RAM et les ressources CPU de la machine de développement.

### 2. Quelle différence faites-vous entre un Hot Reload et un redémarrage ? Quels changements ont nécessité un redémarrage complet à l'étape 7 ?
- **Hot Reload** : Injecte le nouveau bytecode / représentation intermédiaire directement dans la JVM en cours d'exécution sans stopper le processus ni réinitialiser la mémoire ou l'état de l'UI.
- **Redémarrage** : Relance le processus depuis son point d'entrée `main()`, réinitialisant toutes les variables d'état et re-compilant le projet.
- **Changements nécessitant un redémarrage** : Renommage du point d'entrée `main()`, modification des dépendances Gradle, ou modification structurelle de bas niveau.

### 3. Que se passerait-il si quelqu'un ajoute dans `shared/build.gradle.kts` un bloc `androidTarget { }` trouvé dans un tutoriel de 2023 ?
- La compilation du script de build échouera (`Unresolved reference: androidTarget`).
- **Raison** : Avec le plugin AGP 9 (`com.android.kotlin.multiplatform.library`), la cible Android du module partagé s'exprime désormais par le bloc `android { ... }`. Le bloc `androidTarget { }` appartient à une ancienne révision du plugin KMP.

### 4. Le texte « Hello/Bonjour depuis ... » est différent sur chaque plateforme alors que `Greeting` est en code commun. Expliquez le mécanisme.
- Ce mécanisme repose sur le mot-clé **`expect` / `actual`** de Kotlin Multiplatform.
- `commonMain` définit la fonction `expect fun getPlatform(): Platform`.
- Chaque plateforme cible fournit sa propre implémentation `actual fun getPlatform(): Platform` (`AndroidPlatform`, `JVMPlatform`, `IOSPlatform`) qui fournit des informations spécifiques à son OS.

### 5. Le même `Greeting` est compilé en DEX (Android), en bytecode JVM (desktop) et en binaire natif (iOS). Selon vous, laquelle de ces formes est la plus facile à déboguer ou à décompiler pour un auditeur de sécurité ? Pourquoi ? Quels mécanismes peuvent rendre le code commun sensible dans `commonMain` plus difficile à analyser ?
- **Formes les plus faciles à décompiler** : Le bytecode JVM et le DEX Android. Ils conservent la structure des métadonnées des méthodes et classes, permettant à des outils comme JADX d'extraire du code proche du source d'origine.
- **Binaire natif iOS** : Compilé en binaire machine ARM64 via LLVM, il est plus complexe à analyser (nécessite un désassembleur natif comme Ghidra ou IDA Pro).
- **Protection du code sensible** :
  - Utilisation de l'obfuscation / minification R8/ProGuard sur Android et Desktop.
  - Stripping des symboles de débogage et obfuscation de symboles pour Kotlin/Native.
  - Éviter de stocker des clés secrètes ou de la logique métier critique directement en clair dans le code source commun.

### 6. Tâches Gradle liées à l'exécution (`./gradlew tasks --all | grep -i run`)
- Exécution Desktop : `./gradlew :desktopApp:run`
- Hot Reload Desktop : `./gradlew :desktopApp:hotRun`
- Installation/Exécution Android : `./gradlew :androidApp:installDebug`

### 7. Sorties de compilation dans `shared/build/`
- **Fichiers `.class`** : Situés dans `shared/build/classes/kotlin/jvm/main/` pour la cible JVM.
- **Fichiers `.klib`** : Situés dans `shared/build/classes/kotlin/metadata/` ou `shared/build/libs/`.
- **Rôle des `.klib`** : Ce sont les archives binaires intermédiaires Kotlin Multiplatform contenant l'IR (Intermediate Representation) et les métadonnées nécessaires à la résolution d'API multiplateformes pour les cibles non-JVM (iOS/Native) et `commonMain`.

### 8. Arbre de dépendances runtime Desktop et bibliothèque Skiko
- Commande : `./gradlew :shared:dependencies --configuration jvmRuntimeClasspath`
- **Bibliothèque Skiko** : `org.jetbrains.skiko:skiko-awt-runtime-windows-x64` (ou équivalent selon l'OS).
- **Rôle** : Skiko (Skia for Kotlin) est le moteur de rendu 2D sur lequel repose Compose Multiplatform pour dessiner directement les composants UI Canvas sur Desktop et iOS via la bibliothèque graphique Skia.

### 9. Preflight checks
- L'outil de Preflight Checks vérifie l'état de santé du poste de développement (versions du JDK 17/21, installation d'Android SDK, variables d'environnement `ANDROID_HOME`, et Xcode sur macOS).
