# Vaya Backend — Agent Instructions

## Project
This is the Vaya backend, a Kotlin + Spring Boot application.

## Stack
- Kotlin
- Spring Boot
- Gradle Kotlin DSL
- PostgreSQL
- Redis
- Docker / Docker Compose
- JPA / Hibernate
- Spring Security

## Architecture
Follow the existing package/module structure.

Prefer:
- Controllers for HTTP concerns
- Services for business logic
- Repositories for persistence
- DTOs for API boundaries
- Entities for database persistence

Do not place business logic directly inside controllers.

## Kotlin
- Prefer idiomatic Kotlin.
- Use constructor injection.
- Avoid unnecessary `!!`.
- Prefer immutable data where practical.
- Do not introduce Java-style patterns when an idiomatic Kotlin solution exists.

## Spring Boot
- Use constructor-based dependency injection.
- Keep configuration in appropriate `@Configuration` classes.
- Use `application.yml` / environment variables for configuration.
- Never hardcode credentials, secrets, API keys, or passwords.

## Database
- PostgreSQL is the primary database.
- Use migrations for schema changes.
- Do not modify production database structure manually.
- Be careful with lazy-loading and transaction boundaries.

## API
- Keep API responses consistent.
- Validate incoming request DTOs.
- Do not expose JPA entities directly from controllers.
- Use appropriate HTTP status codes.

## API Compatibility
- Do not change existing response shapes, field names, or status codes without explicit approval.
- New fields should be additive and optional where possible.
- Breaking changes require a new API version, not an in-place change.

## Error Handling
- Use a global `@ControllerAdvice` / `@ExceptionHandler` for translating exceptions to HTTP responses.
- Do not catch exceptions just to swallow them; log and rethrow or handle meaningfully.
- Define a small custom exception hierarchy (e.g. `NotFoundException`, `ValidationException`) rather than throwing generic `RuntimeException`.
- Never leak stack traces or internal error details in API responses.

## Logging
- Use structured logging (SLF4J via Kotlin logging, not `println`).
- Never log secrets, tokens, passwords, or full request/response bodies containing PII.
- Log at appropriate levels: ERROR for failures needing attention, WARN for recoverable issues, INFO for key lifecycle events, DEBUG for diagnostics.

## Security
- Do not disable Spring Security filters (CSRF, auth) to make an endpoint work — fix the underlying config instead.
- All new endpoints must have explicit authorization rules; do not rely on defaults.
- Validate and sanitize any input used in queries to prevent injection, even with JPA/Hibernate.
- New dependencies with known CVEs are not acceptable — check before adding.

## Docker
The application must remain buildable using Docker Compose.

When modifying the Dockerfile:
- Keep the multi-stage build.
- Use the Gradle BuildKit cache.
- Do not run the production container as root.
- Use the JRE image for the runtime stage.

## Dependencies
Before adding a dependency:
1. Check whether the functionality already exists in the project.
2. Prefer existing libraries.
3. Avoid adding dependencies for trivial functionality.
4. Use the latest compatible version only when there is a good reason to upgrade.

## Code Changes
Before modifying code:
1. Understand the existing implementation.
2. Follow existing naming and architectural conventions.
3. Make the smallest change necessary.
4. Avoid unrelated refactoring.

## When Instructions Are Unclear
- If the correct architectural pattern isn't obvious from existing code, ask rather than guess.
- If a task seems to require a breaking API change, flag it explicitly rather than making it silently.
- Prefer leaving a `TODO`/comment and flagging uncertainty over inventing an undocumented convention.

## Testing
When changing business logic:
- Add or update tests where appropriate.
- Do not remove existing tests simply to make a build pass.
- Run `./gradlew test` (not just `build`) before considering a change complete.
- New service-layer logic requires unit tests; new endpoints require at least one integration test.
- Do not weaken assertions (e.g. loosening an equality check to a null check) just to get a test green.

## Commits & PRs
- Keep commits scoped to one logical change.
- Write commit messages describing *why*, not just *what*.
- Do not bundle unrelated fixes into the same change.

For local builds:
```bash
./gradlew build
```