# Android Image Search & Upload App

This Android application allows users to search for images using an external web service (such as Pixabay or Pexels), display the results in customizable views, and upload selected images to a cloud storage service such as Firebase. The app demonstrates the use of threading, network requests, dynamic UI rendering, and remote uploading in Android.

## Features

### 1. Image Search Functionality
- Users can enter a search term to look up images from a web service such as:
  - [Pixabay](https://pixabay.com/api/docs/)
  - [Pexels](https://www.pexels.com/api/)
- The app performs a network request using a background thread (e.g., using Retrofit, Volley, or AsyncTask).
- While the search is ongoing, a loading indicator (progress bar or spinner) is displayed on the UI to enhance user experience.

### 2. Displaying Images
- After fetching the image data (typically URLs for thumbnail versions), the app displays up to 15 images in a scrollable view.
  - If fewer than 15 images are returned, all are displayed.
- Image loading is optimized for performance using libraries such as Glide or Picasso.
- Images are shown in a scrollable view using a RecyclerView.

### 3. Customizable View Modes
- The user can toggle between two layouts:
  - **Single-column view**: One image per row.
  - **Double-column view**: Two images per row.
- This can be implemented using a `GridLayoutManager` in the RecyclerView with dynamic span count changes.

### 4. Image Uploading
- Users can tap on an image and choose to upload it to a cloud storage service.
- Firebase Storage is the recommended and preferred backend for uploads, but alternatives like Google Drive or Dropbox can also be integrated.
- Upload logic handles file conversion and asynchronous upload tasks with real-time feedback to the user.
- References to any external or open-source code used for uploading must be properly credited in comments or documentation.

## Technologies Used

- Android SDK
- Java or Kotlin
- Retrofit / Volley for networking
- Glide / Picasso for image loading
- Firebase SDK (for Storage)
- RecyclerView with GridLayoutManager
- Threading (Handler, AsyncTask, or Kotlin Coroutines)

## Getting Started

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
