# field-service-web

Spring Boot based field service management web app for customer/site registration, estimate tracking, and field photo management.

## Core features
- Login with a basic admin account (`admin` / `admin1234`)
- Customer and site registration
- Site detail view with estimate history
- Estimate registration with dynamic item rows
- Progress status management (`QUOTE`, `CONSTRUCTION`, `COMPLETED_AS`)
- Photo upload and inline viewing
- Search UI for jobs and estimates
- Responsive layout for desktop and mobile

## Tech stack
- Java 17
- Spring Boot 3.3
- Spring MVC
- Spring Data JPA
- Thymeleaf
- Spring Security
- H2 for local development
- PostgreSQL profile for later deployment

## Run modes
- Default profile: `local`
- Optional profile: `postgres`

## Requirements
- Java 17+
- Maven 3.9+

## Run locally
```bash
mvn spring-boot:run
```

Open:
- App: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console`

## Run with PostgreSQL
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Environment variables:
- `DB_URL` default `jdbc:postgresql://localhost:5432/field_service`
- `DB_USERNAME` default `postgres`
- `DB_PASSWORD` default `postgres`

## Storage
- Upload directory: `./uploads`

## Team workflow
1. Initialize a GitHub repository and add it as `origin`.
2. Push the current `main` branch.
3. Each team member works on a short-lived branch such as `feature/job-edit` or `feature/mobile-ux`.
4. Open pull requests and review before merging back into `main`.

Example commands:
```bash
git init
git add .
git commit -m "Initial field service web app"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```
