# Contact Management App (COMP2008 Assignment 2)

## Project Overview

This Android mobile application is a **Contact Management System** developed as part of the COMP2008 Mobile Application Development course. The app enables users to manage their contacts efficiently by allowing them to view, add, edit, delete, and import contacts, with full integration of device camera functionality to capture contact photos.

The project demonstrates practical knowledge of key Android components including **Fragments**, **RecyclerView**, **SQLite database**, and **Camera integration**, alongside interaction with the device’s native contact list.

---

## Key Features

### User Interface (UI)
- Implemented using a **fragment-based architecture**, separating the app into reusable UI components:
  - **Contacts List Fragment**: Displays all contacts in a scrollable list using `RecyclerView`.
  - **Contact Detail Fragment**: Allows viewing, adding, and editing contact information.
- UI elements provide intuitive controls for adding, editing, and deleting contacts.
- A dedicated button launches the device’s camera to capture photos linked to contacts.
- When viewing a contact, all details including name, phone number, email, and photo are displayed clearly.

### SQLite Database
- Designed a local SQLite database schema to store contact details: name, phone number, email, and photo URI.
- Implemented full **CRUD (Create, Read, Update, Delete)** operations ensuring persistent storage.
- Data persists reliably even after app closure or device restart.

### Camera Integration
- Integrated the device camera to allow users to capture photos for their contacts.
- Captured photos are saved to device storage, with their paths stored in the database to optimize storage and performance.

### Import Contacts from Device
- Implemented functionality to import contacts from the device’s native contacts app using Android’s Contacts Provider API.
- The app checks for duplicate contacts during import to prevent redundancy.

---

## Technical Implementation Details

- The app uses **RecyclerView** for efficient display and management of large lists of contacts.
- Fragments manage different screens and their lifecycles, supporting a smooth user experience and better modularity.
- SQLite database interactions are managed with helper classes that abstract SQL queries and database transactions.
- Runtime permissions are handled carefully for camera and contacts access to comply with Android security standards.
- Contact photos are handled as URIs referencing device storage, avoiding bloating the database with image data.

---

## How to Use the App

1. **View Contacts**: Launch the app to see the list of contacts.
2. **Add Contact**: Tap the add button, enter contact details, optionally capture a photo using the device camera, and save.
3. **Edit Contact**: Select a contact from the list to modify any details or update the photo.
4. **Delete Contact**: Remove a contact via the delete option in the contact detail view.
5. **Import Contacts**: Use the import feature to add contacts from your device’s address book, ensuring no duplicates are added.

---

## Technologies Used

- Android Studio (Java or Kotlin)
- Android Fragments and RecyclerView UI components
- SQLite Database for data persistence
- Android Camera API for photo capture
- Android Contacts Provider API for importing device contacts

---

## Summary

This project allowed me to practically apply Android development fundamentals, especially working with UI components, local databases, and device hardware integration. It emphasizes modular design, data persistence, and user experience in mobile app development.

---

If you have any questions or would like to see specific parts of the implementation or architecture, please feel free to ask!
