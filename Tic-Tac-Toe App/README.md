# Tic-Tac-Toe Android Application

## Project Overview

This project is developed as part of **COMP2008 – Mobile Application Development** at Curtin University. The goal of this assignment is to build a feature-rich **Tic-Tac-Toe** game application for Android, which demonstrates proficiency in Android UI design, user interaction, customisation features, game logic, and proper software architecture using modern development practices.

This application is designed for casual gameplay and educational use, with an emphasis on flexibility, usability, and responsiveness across various Android devices and screen orientations.

---

## Assignment Objective

The purpose of this project is to build a fully functional Android Tic-Tac-Toe application that includes:

- Game modes supporting both two-player and human-vs-AI interaction.
- Personalised gameplay experiences through customizable board sizes, win conditions, and player markers.
- Support for user profiles with editable avatars.
- In-game tracking of statistics such as win rates and total games.
- A modular and responsive interface implemented using fragments.
- Reliable performance in both portrait and landscape orientations without loss of state.

The assignment requires following standard Android development guidelines, ensuring a polished user experience with reusable components, proper activity and fragment lifecycle handling, and modern UI design.

---

## Features

### 1. Game Modes

- **Two-player mode:** Two users take turns on the same device, placing markers (X or O) on the grid.
- **AI mode:** A single player plays against a non-strategic AI that places its marker randomly on available cells.

### 2. Game Personalisation

The application allows users to configure various aspects of the gameplay:

- **Board Size Selection:** Users can choose from 3x3, 4x4, or 5x5 board sizes.
- **Win Conditions:** Users can define how many markers in a row are required to win (between 3 to 5).
- **Player Marker Selection:** Users may choose between standard markers (X and O) or custom icons available in the app.

### 3. User Profiles and Avatar Selection

- Users can create a profile consisting of their username and avatar.
- Profiles can be edited during gameplay through a settings screen.
- A collection of avatar images is presented in a scrollable list using a `RecyclerView`.
- Note: Profile data is **volatile** — all data is lost once the application is closed.

### 4. Gameplay Statistics

- The app tracks gameplay statistics for each user session:
  - Total games played
  - Number of wins
  - Number of losses
  - Number of draws
  - Win percentage
- All statistical data is stored in memory for the session only (no persistent storage is implemented in this version).

### 5. In-Game Interface Features

During gameplay, the app provides a comprehensive UI with the following elements:

- **Game Board:** Visually displays the current state of the game using a grid layout.
- **Player Turn Indicators:** Shows which player's turn it is, along with their name and avatar.
- **Move Count and Progress Tracker:** Displays number of moves taken and remaining slots.
- **Countdown Timer:** Limits the time a player has to make their move.
- **Game Status Messages:** Alerts for win, draw, and invalid moves.
- **Undo and Reset Options:** Allows players to undo the last move or restart the match.
- **Settings Access:** Users can pause the game and access the main menu or settings from within the game.
- **Player Information Display:** Displays user profile info including avatar and username throughout gameplay.

---

## Technical and Architectural Requirements

The application adheres to standard Android development practices with a focus on modularity and maintainability.

### Device Orientation and Layout Handling

- All UI components are designed to **adapt to both landscape and portrait orientations**.
- The layout is responsive and compatible with **phones and tablets** of various screen sizes.
- The application ensures that **no data is lost** when orientation changes (state restoration is handled appropriately).

### Fragment-Based Architecture

- The app uses **fragment-based navigation** to manage UI screens and lifecycles efficiently.
- Separate fragments are created for:
  - Main Menu
  - Settings Screen
  - User Profile
  - Game Board

### Avatar Selection with RecyclerView

- A scrollable `RecyclerView` is used to display a list/grid of avatar options.
- Users can select an avatar from this list when creating or editing their profile.

---

## Technologies Used

- **Programming Language:** Kotlin
- **Platform:** Android SDK
- **IDE:** Android Studio (Giraffe or later recommended)
- **Architecture:** MVVM pattern with fragment-based navigation
- **UI Components:** ConstraintLayout, FragmentManager, RecyclerView, DialogFragment, AlertDialog
- **Others:** CountDownTimer, GridLayoutManager, Drawable Resources

---

## Installation Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/tic-tac-toe-android.git
