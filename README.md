# Android Notes Client

Android Notes App for [ktor-notes-server](https://github.com/carrot2803/ktor-notes-server.git).
You can install and test the app at [App Apk](app/release/notes-app.apk)

## Installation

<details>
<summary><code>There are several ways you can run this application</code></summary>

- [Downloading repository as ZIP](https://github.com/carrot2803/android-notes-client/archive/refs/heads/master.zip)
- Running the following command in a terminal, assuming you have [GitHub CLI](https://cli.github.com/) installed:

```sh
git clone https://github.com/carrot2803/android-notes-client.git
```

</details>

## Features

- Offline Sync and Backup: The app synchronizes notes for backup when the user is logged in and connected to Wi-Fi.
- Dark Mode Support: Provides support for dark mode for enhanced user experience in low-light environments.
- Full CRUD Operations for Notes: Allows users to create, read, update, and delete notes seamlessly.
- Search Functionality: Enables users to efficiently search through their notes, enhancing accessibility and
  organization.
- Secure Backend: Utilizes Ktor as the backend server, ensuring robust security measures for data handling and
  communication.
- User Authentication: Implements user authentication via
  backend [server](https://github.com/carrot2803/ktor-notes-server.git)

## Architecture

This app uses [
***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

## Package Structure

    com.carrot.noteapp  # Root Package
    .
    ├── adapter         # Adapts data models for use in views.
    │
    ├── datasource      # Handles data storage and retrieval.
    │   ├── local       # Manages local data storage using Room.
    |   └── remote      # Provides utility classes for remote data handling.
    |
    ├── di              # Manages dependency injection.
    |
    ├── repository      # Acts as a middle layer between data sources and viewmodels.
    |
    ├── utils           # Contains utility classes and functions.
    |
    ├── viewmodels      # Contains ViewModel classes for UI logic.
    |   
    └── views           # Contains utility classes and Kotlin extensions for views.

## Snippets

<div style="display: flex;">
    <img alt="ScreenShot1" src="https://i.imgur.com/GXFkylF.jpeg" width="150"/>
    <img alt="ScreenShot2" src="https://i.imgur.com/1uDpJ6z.jpeg" width="150"/>
</div>
<div style="display: flex;">
    <img alt="ScreenShot3" src="https://i.imgur.com/iPt2lil.jpeg" width="150"/>
    <img alt="ScreenShot4" src="https://i.imgur.com/mgQKNUe.jpeg" width="150"/>
</div>

