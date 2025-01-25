# Plateforme de Réservation d'Hôtels

## Description du Projet
Ce projet est une API de gestion de réservations d'hôtels conçue pour permettre aux utilisateurs de créer, consulter, modifier et supprimer des réservations. Le système est scalable, capable de gérer des millions de requêtes, et compatible avec des environnements multi-utilisateurs. Il prend en charge des volumes de données variables (petits, moyens et grands messages).

### Le projet comprend :
- **Backend** : Développé avec Spring Boot pour gérer la logique métier et l'API.
- **Frontend** : Développé avec Android Studio (kotlin) pour fournir une interface utilisateur mobile.

## Fonctionnalités
### Authentification
- **Inscription (Signup)** : Les utilisateurs peuvent créer un compte en fournissant des informations de base (nom, email, mot de passe).
- **Connexion (Login)** : Les utilisateurs authentifiés peuvent se connecter pour accéder aux fonctionnalités de réservation.

### Gestion des Réservations
- **Créer une réservation** :
  - Les utilisateurs peuvent soumettre des informations sur le client, les dates de séjour, et les préférences de chambre.
  - Le système valide les dates et la disponibilité des chambres.
- **Consulter une réservation** :
  - Les utilisateurs peuvent récupérer les informations détaillées d'une réservation existante (dates, chambre réservée, informations client, etc.).
- **Modifier une réservation** :
  - Les utilisateurs peuvent mettre à jour les dates de séjour ou les informations client.
  - Le système vérifie la disponibilité des chambres pour les nouvelles dates.
- **Supprimer une réservation** :
  - Une réservation peut être annulée par un utilisateur ou un administrateur.
  - La suppression libère la chambre réservée pour d'autres utilisateurs.

### Gestion des Chambres (Admin)
- **Créer une chambre** : Ajouter une nouvelle chambre avec des détails tels que le type, le prix, et les équipements.
- **Consulter une chambre** : Afficher les détails d'une chambre spécifique.
- **Modifier une chambre** : Mettre à jour les informations d'une chambre existante.
- **Supprimer une chambre** : Supprimer une chambre du système.

## Technologies Utilisées
### Backend
- **Framework** : Spring Boot (pour la création d'API RESTful).
- **Langage** : Java.
- **Base de données** : MySQL (ou autre base de données relationnelle).
- **Authentification** : JWT (JSON Web Tokens) pour sécuriser les endpoints.
- **Gestion des dépendances** : Maven ou Gradle.
- **Gestion des erreurs** : Handler global pour les exceptions avec des réponses HTTP appropriées.

### Frontend
- **IDE** : Android Studio.
- **Langage** : kotlin.
- **Bibliothèque de réseau** : Volley pour les requêtes HTTP vers l'API backend.
- **Interface utilisateur** : XML pour les layouts, avec des composants Material Design pour une expérience utilisateur moderne.
- **Gestion des sessions** : Stockage des tokens JWT localement pour maintenir la session utilisateur.
  ## Architecture de l'application
  <img width="896" alt="Image" src="https://github.com/user-attachments/assets/ae4762f5-8265-4b5d-8ccc-b187f5478eba" />


## Prérequis
### Backend
- **Java JDK** : Version 17 ou supérieure.
- **Spring Boot** : Version 3.4.2 ou supérieure.
- **Base de données** : MySQL.
- **Outils** : Maven ou Gradle pour la gestion des dépendances.

### Frontend
- **Android Studio** : Version 4.x ou supérieure.
- **SDK Android** : Version 34 ou supérieure.
- **Émulateur ou appareil physique** : Pour tester l'application.

## Configuration et Exécution
### Backend
1. **Cloner le dépôt** :
```bash
git clone https://github.com/fatikhalil/Reservation-kotlin-volley.git
cd votre-projet/backend
```
2. **Configurer la base de données** :
Modifiez le fichier `application.properties` pour configurer les informations de connexion à la base de données.

Exemple pour MySQL :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reservation
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

3. **Exécuter l'application** :
   - Avec Maven :
     ```bash
     ./mvnw spring-boot:run
     ```
   - Avec Gradle :
     ```bash
     ./gradlew bootRun
     ```

4. **Accéder à l'API** :
   L'API sera disponible à l'adresse : `http://localhost:8082`.

### Frontend
1. **Ouvrir le projet dans Android Studio** :
   - Ouvrez Android Studio et sélectionnez "Open an existing project".
   - Naviguez jusqu'au dossier `frontend` et ouvrez-le.

2. **Configurer l'URL de l'API** :
   Modifiez l'URL de l'API dans le fichier `NetworkUtils.java` pour pointer vers votre backend.

   Exemple :
   ```java
   public static final String BASE_URL = "http://localhost:8082/api/";
   ```

3. **Exécuter l'application** :
   - Connectez un appareil Android ou utilisez un émulateur.
   - Cliquez sur "Run" dans Android Studio pour lancer l'application.

## Tests
### Backend
- Exécutez les tests unitaires et d'intégration avec :
  - Maven :
    ```bash
    ./mvnw test
    ```
  - Gradle :
    ```bash
    ./gradlew test
    ```

### Frontend
- Testez l'application sur différents appareils et versions d'Android pour assurer la compatibilité.
- Utilisez des outils comme Postman pour tester les endpoints de l'API backend.

## Améliorations Possibles
### Sécurité
- Implémenter une validation plus robuste des entrées utilisateur pour prévenir les attaques par injection SQL.
- Ajouter une authentification à deux facteurs (2FA) pour renforcer la sécurité des comptes utilisateurs.

### Performance
- Utiliser un système de cache (comme Redis) pour améliorer les performances des requêtes fréquentes.
- Optimiser les requêtes SQL pour réduire les temps de réponse.

### Expérience Utilisateur
- Ajouter des notifications push pour informer les utilisateurs des mises à jour de leurs réservations.
- Intégrer un système de recherche avancée pour les chambres (filtres par prix, équipements, etc.).

## Auteurs
- **KHALIL Fatima** - Développeur principal - **fatimakhalil929@gmail.com**
## Démonstration vidéo
https://github.com/user-attachments/assets/4c0ca0ee-b678-485d-b347-3799546043f3

