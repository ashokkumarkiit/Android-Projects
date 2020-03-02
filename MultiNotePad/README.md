# Multi-Note Pad Application (JAVA)

## Home Screen
<img src="./Screenshot/image1.png" width="200" height="400" />

## Features

- This app allows the creation and maintenance of multiple notes. Any number of notes are allowed (including no notes at all). 
- Notes are made up of a Title and Note Text.
- Notes is being saved to (and loaded from) the internal file system in JSON format.
- In case no file is found upon loading, the application start with no existing notes and no errors.
- JSON file loading is happen in an AsyncTask that is started in the onCreate method. I am saving to JSON file in the onPause method.
- A simple java Note class is used to represent each individual note in the application.
