# CatEmotion
For more information, go to [Wiki](https://github.com/hammii/CatEmotion/wiki).

## Index
  - [Overview](#overview) 
  - [Development Environment](#development-environment)
  - [Getting Started](#getting-started)
  - [Authors](#authors)
  - [License](#license)

## About CatEmotion 🐈 📸
<!--Wirte one paragraph of project description -->  
This project's purpose is to **recognize emotions through the cat's tail**   and  **share the photos.**  

## Overview
<!-- Write Overview about this project -->
<!-- **If you use this template, you can use this function**
- Issue Template
- Pull Request Template
- Commit Template
- Readme Template
- Contribute Template
- Pull Request Build Test(With Github Actions) -->

| Home | Ranking | Camera  | Album |
| :---------------: | :---------------: | :---------------: | :---------------: |
| <img width="185" alt="home" src="https://user-images.githubusercontent.com/44565524/121726958-3860dd00-cb26-11eb-973d-93cea2b28cf0.png"> | <img width="185" alt="ranking" src="https://user-images.githubusercontent.com/44565524/121727241-9d1c3780-cb26-11eb-85b4-a811a8f91e59.png"> | <img width="183" alt="camera" src="https://user-images.githubusercontent.com/44565524/121727593-11ef7180-cb27-11eb-9917-8084d9eea3fc.png"> | <img width="187" alt="album" src="https://user-images.githubusercontent.com/44565524/121727752-482cf100-cb27-11eb-87eb-279319f7b342.png"> |

## Development Environment
-  Android Studio @4.0.1
    - minSdkVersion: 23
    - targetSdkVersion: 28
-  TensorFlow Lite (Mobilenet_V1_1.0_224_quant)
-  Firebase (Authentication, Realtime Database, Storage)

## Getting Started

### Step 1. Clone the CatEmotion source code
Clone the CatEmotion GitHub repository to your computer to get the demo application.
```
git clone https://github.com/hammii/CatEmotion
```

To do this, open Android Studio and select Open an existing project. <br/><br/>
<img width="469" alt="android studio" src="https://user-images.githubusercontent.com/44565524/121735440-16b92300-cb31-11eb-947a-d5abe8bae2c9.png">

### Step 2. Build the Android Studio project
Select `Build -> Make Project` and check that the project builds successfully. <br/><br/>
<img width="440" alt="build" src="https://user-images.githubusercontent.com/44565524/121736528-a4e1d900-cb32-11eb-9a5b-e606b2df30ed.png">

You will need Android SDK configured in the settings. You'll need at least SDK version 23. <br/>
The `build.gradle` file will prompt you to download any missing libraries.

<p>
<img width="44%" alt="gradle" src="https://user-images.githubusercontent.com/44565524/121737403-c5f6f980-cb33-11eb-8ea7-1db57c5f2303.png">
<img width="50%" alt="min" src="https://user-images.githubusercontent.com/44565524/121737486-e1fa9b00-cb33-11eb-979a-c3cd3f9d6245.png">
</p>

### Step 3. Install and run the app
Connect the Android device to the computer and be sure to approve any ADB permission prompts that appear on your phone. <br/>
Select `Run -> Run 'app'` Select the deployment target in the connected devices to the device on which the app will be installed. This will install the app on the device.

<img width="610" alt="run" src="https://user-images.githubusercontent.com/44565524/121738442-305c6980-cb35-11eb-99d6-5e011fadd697.png">

When you run the app the first time, the app will request permission to access the camera.

## Authors
  - [hammii](https://github.com/hammii) - **HaYeong Jang** - <hyj9829@gmail.com>
  - [201735897](https://github.com/201735897) - **EunHee Ham** - <nabi20@gc.gachon.ac.kr>
  - [sungyoonahn](https://github.com/sungyoonahn) - **SungYoon Ahn** - <sungyoonahn@daum.net>

See also the list of [contributors](https://github.com/hammii/CatEmotion/graphs/contributors)
who participated in this project.
<!--
## Used or Referenced Projects
 - [referenced Project](project link) - **LICENSE** - little-bit introduce
-->

## License
```
MIT License

Copyright (c) 2021 HaYeong Jang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
