# Weather Forecast Predictor App using Firebase ML Kit

This is an Android application that predicts the next 10 weather forecasts based on the user input values for weather, temperature, and humidity. It uses Firebase ML Kit to train a model on a remote server to predict the weather and an external weather API to get the actual weather forecast.

## Setup

To run this application, you will need:

- Android Studio
- A Firebase project with ML Kit enabled
- A server with the weather forecast prediction API endpoint

### Firebase Setup

1. Create a Firebase project and enable ML Kit.
2. Upload the `weather_forecast.tflite` model to the Firebase project.
3. Copy the Firebase project ID and API key and replace the values in the `google-services.json` file.

### Server Setup

1. Create an API endpoint for weather forecast prediction on your server.
2. Copy the server URL and replace the value of `API_ENDPOINT` in the `MainActivity.kt` file.

### Running the App

1. Clone this repository and open the project in Android Studio.
2. Run the app on an Android device or emulator.

## Usage

1. Enter the values for weather, temperature, and humidity.
2. Click the `Predict` button to get the predicted weather forecasts.
3. The app will display the actual weather forecast and the predicted weather forecast for the next 10 days.

## Programming languages and tools used
<a href="https://developer.android.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/android/android-original-wordmark.svg" alt="android" width="40" height="40"/></a><a href="https://firebase.google.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/firebase/firebase-icon.svg" alt="firebase" width="40" height="40"/> </a>  <a href="https://kotlinlang.org" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/kotlinlang/kotlinlang-icon.svg" alt="kotlin" width="40" height="40"/> </a>
 
## Developer
*	[@abhineetraj1](https://github.com/abhineetraj1)