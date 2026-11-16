# BukuKu

> Android book collection manager: add, view, edit, and delete books in a local SQLite table

[![License](https://img.shields.io/badge/license-MIT-2ea44f?style=flat-square)](LICENSE)
[![Stack](https://img.shields.io/badge/stack-Android%20%7C%20Java%20%7C%20SQLite-000?style=flat-square)](#tech-stack)

BukuKu is a small Android app for keeping a personal reading list. Books are stored in
a local SQLite database and managed through a list screen, an add/edit form, a detail
view, and a delete confirmation. This is coursework: the repository description is
`VSGA-JMP` and the package is `com.sertifikasi.bukuku`, so treat it as a class
assignment rather than a product.

The interface is written in Indonesian, which matches the coursework it was built for.

## Features

- **Splash screen**: a five second cover screen, then the login form.
- **Login**: the app checks the `admin` / `admin` credentials that are hard-coded in
  `LoginActivity`, and rejects empty fields with inline errors.
- **Book list**: every book from SQLite in a `RecyclerView`, sorted by title, with an
  empty state when the table has no rows.
- **Add and edit**: one form for both actions, covering title, author, year published,
  genre, and reading status (`Belum dibaca` or `Sudah dibaca`).
- **Validation**: required fields are checked before saving, and the year must parse as
  a number between 1000 and 2100.
- **Book actions**: tapping a row opens a popup with View Details, Edit, and Delete.
  Delete asks for confirmation first.
- **Detail screen**: a read-only view that refreshes when you return from an edit.
- **About screen**: usage steps, version, and the author credit shown in the app.
- **Logout**: the overflow menu returns to the login screen and clears the task.

## Tech stack

| Layer | Choice |
|-------|--------|
| Language | Java 11 source/target |
| Platform | minSdk 29, targetSdk 35, compileSdk 35 |
| Build | Gradle 8.11.1 wrapper, AGP 8.9.0, version catalog in `gradle/libs.versions.toml` |
| UI | AppCompat 1.7.1, Material 1.12.0, RecyclerView 1.3.2, CardView 1.0.0, ConstraintLayout 2.2.0, CoordinatorLayout 1.2.0 |
| Data | `SQLiteOpenHelper` with a single `books` table in `bukuku.db` |
| Tests | JUnit 4.13.2, AndroidX test runner, Espresso 3.6.1 |

## Getting started

### Prerequisites

- Android Studio with support for AGP 8.9.0 (the wrapper pins Gradle 8.11.1)
- Android SDK 35 installed
- A device or emulator on Android 10 (API 29) or newer

### Run it

1. Open Android Studio, then **File > Open** and select this repository folder.
2. Let Gradle sync, choose the `app` run configuration, and press **Run**.
3. On the login screen enter `admin` for both the email and the password. The form
   compares those two fields directly, it does not check an email format.

Or build the debug APK from a terminal:

```bash
sh gradlew :app:assembleDebug
```

The wrapper script is committed without the executable bit, so call it through `sh`
instead of `./gradlew`.

## Project structure

```text
app/src/main/java/com/sertifikasi/bukuku/
  SplashActivity.java, LoginActivity.java     entry flow
  BookListActivity.java, BookDetailActivity.java, AddEditBookActivity.java
  adapter/BookAdapter.java                    list rows and popup actions
  model/Book.java, database/DatabaseHelper.java
  InfoActivity.java                           about screen
```

## Notes and limitations

- Authentication is a placeholder: the credentials are constants in `LoginActivity`.
  There are no user accounts, hashing, or session storage.
- Interface text is hardcoded in the layouts and activities, and the reading status
  values are fixed strings, so there is no localization support yet.
- `DatabaseHelper.onUpgrade` drops the table, so a schema bump wipes existing books.
- The repository contains only the generated `ExampleUnitTest` and
  `ExampleInstrumentedTest`; there are no behavior tests.
- There is no backup, export, or sync. Books live only in the app's private database.
- Scope is a class assignment: one table, one user, five fields per book.

## Credit

Built by Roekhan Dani Maulana (2320506020), as shown on the app's about screen, for the
VSGA coursework that the repository description refers to.

## License

MIT, see [LICENSE](LICENSE). Copyright (c) 2026 Mai.
