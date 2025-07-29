# Android Application Projects – Curtin University

This repository contains three Android applications developed for the Mobile Application Development unit. Each project showcases key Android development concepts such as custom UI design, local and remote data handling, multithreading, and third-party integrations.

---

##  Assignment 1 – Tic-Tac-Toe Game

### Overview
A fully-featured Tic-Tac-Toe game app supporting both multiplayer and AI gameplay with custom board settings and user profiles.

### Features
- Supports 2-player and vs AI (random logic) modes
- Selectable board sizes: 3x3, 4x4, 5x5
- Customizable win condition (3–5 in a row)
- Player profiles with editable name and avatar
- Game stats: Wins, losses, draws
- In-game features: 
  - Turn display
  - Move counter
  - Timer
  - Result popup
  - Undo and restart
- Fragment-based UI for modular navigation
- Supports portrait and landscape orientations
- Avatar selection via scrollable RecyclerView

---

##  Assignment 2 – Contact Manager App

### Overview
A contact management app that allows users to create, edit, delete, and import contacts. Contacts can have profile photos captured using the device camera and are stored in a local SQLite database.

### Features
- Contact list using RecyclerView
- Add, edit, and delete contact records
- Contact details include:
  - Name
  - Phone number
  - Email
  - Profile image
- Capture profile photos via camera
- Store images locally or via URI
- Import contacts from the device’s address book
- Duplicate contact detection
- Persistent storage using SQLite
- Fragment-based navigation

---

##  Assignment 3 – Image Search & Upload App

### Overview
An image search and upload app demonstrating Android threading, networking, and remote file uploads. Users can search and view images from online APIs and upload selected ones to cloud storage.

### Features
- Search images using a web API (e.g., Pixabay or Pexels)
- Progress bar shown during API calls
- Display up to 15 search results
- Toggle views:
  - Single-column (1 image per row)
  - Double-column (2 images per row)
- Scrollable image list with thumbnails
- Upload selected images to cloud storage
- Cloud options: Firebase (preferred), Google Drive, or Dropbox
- Background upload using asynchronous threading

---

## Project Structure

```plaintext
/
├── Tic-Tac-Toe/
├── Contact App/
├── Image Search Upload/
└── README.md
