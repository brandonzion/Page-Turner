# Page-Turner
An Android app that uses voice commands to turn between music sheets. This app is a solution for people who have trouble turning pages or can't turn them in the middle of a performance. Page Turner solves this by using voice recognition with a trained Tensorflow model. For example, if you are playing piano, and you cannot turn the pages by hand, this app will do it for you. All you need to do is speak to your Android device.
## Functionality
The core of the app is voice recognition. The ten commands are trained using a Tensorflow neural network. These examples can be seen on the 
[Tensorflow official tutorial](https://www.tensorflow.org/tutorials/sequences/audio_recognition). In this app, I chose "go" to move to the next page and "stop" to move to the previous page. 

If you are opening the app for the first time, you can click the Create button and take multiple pictures of music. When you are finished, click the forward button to start playing the song. If you have already taken pictures, the next time you launch the app, there will be a list of these pages. You can multi-select these pages to start playing. When you are done, close the app.
## Requirements
Android device with camera and microphone. The app requires permission to use files, camera, and microphone.
## Setup
Install the APK file [here](https://drive.google.com/open?id=1gBZF0fee0xSnS_Dg_H3VLV8iTpQhA-_Z).
After you install it, transfer it onto an android device to use. 
## Warning
I have been focusing on learning machine learning and training models in this project. I have limited knowledge of android app development. These are some issues I have noticed so far.
1. Sometimes, the model prediction can be slow and inaccurate if you have more then 3 pages of a song
2. The user interface is not perfect.
3. Crashes may happen when you are navigating back and forth between scenes
I will come back to fix these bugs at a later time.
