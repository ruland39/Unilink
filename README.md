# Unilink
A Proximity Based Social Networking Application
![file cover - 1](https://user-images.githubusercontent.com/64399691/197495406-d440898d-084e-4109-9544-343447c81e5c.png)

## Description
Unilink is an open-source social networking platform for college students that uses Bluetooth Low-Energy technology to foster new connections. Unlike other social networks, Unilink focuses on matching users based on shared interests and hobbies to create meaningful and genuine connections. The app is similar to LinkedIn and encourages students to build their network while engaging with peers on a personal level. Developed using Java within the Android Studio IDE, Unilink is a native Android application designed to enhance campus connections and create a more supportive learning community.

- This repository contains the entirety of Unilink's source code, without the API key's and secrets associated with the application. 
- Unilink was built using Java and the Android SDK. Compiled using Gradle.

## Installation
- Download the latest `.apk` instance from [here](https://drive.google.com/uc?export=download&id=1yJOC7ESYqHecNI9728Lmu_yNRu9JFQgK) or through the GitHub releases. 
- Allow installation from third-party sources on your Android application
- Current release information: `alpha-pre-release-v1.0`

## Contribution
- This repository contains the source code for Unilink, as a school project. Feel free to contribute further to the development by simply cloning this repository through Android Studio or your preferred IDE.
- You can build the application by running the following commands:
```bash
# Ensure that you are in the intended directory
git clone https://github.com/ruland39/Unilink.git
cd ./Unilink
./gradlew assembleDebug # Compiles into a debug .apk instance in the Unilink/app/build/outputs/apk/debug folder
# This .apk file should be aptly named "app-debug.apk", use adb or an emulator to run the application.
```

## Directory Tree
```bash
Unilink
├── app
│   ├── build.gradle
│   ├── google-services.json
│   ├── proguard-rules.pro
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── ic_launcher-playstore.png
│           ├── java
│           │   └── com
│           │       └── example
│           │           └── unilink
│           │               ├── Activities
│           │               │   ├── BLE
│           │               │   │   ├── BeaconWorker.java
│           │               │   │   └── MonitoringActivity.java
│           │               │   ├── ConnectionsListActivity.java
│           │               │   ├── ConnectionsRowAdapter.java
│           │               │   ├── FeaturePage
│           │               │   │   ├── FeaturePageActivity.java
│           │               │   │   ├── FpPagerAdapter.java
│           │               │   │   └── LoadingDialogBar.java
│           │               │   ├── HomescreenActivity.java
│           │               │   ├── LoginorregisterActivity.java
│           │               │   ├── LoginpageActivity.java
│           │               │   ├── MainActivity.java
│           │               │   ├── OneSignalNotificationOpenHandler.java
│           │               │   ├── OthersProfileActivity.java
│           │               │   ├── ProfileSetupActivity.java
│           │               │   └── RegisterpageActivity.java
│           │               ├── Dialogs
│           │               │   └── BluetoothHomeScreenDialog.java
│           │               ├── Fragments
│           │               │   ├── ChatFragment.java
│           │               │   ├── HomeFragment.java
│           │               │   ├── NotificationFragment.java
│           │               │   ├── NotificationRowAdapter.java
│           │               │   ├── ProfileFragment.java
│           │               │   ├── ProfileRowAdapter.java
│           │               │   └── Registration
│           │               │       ├── addBioFragment.java
│           │               │       ├── addBirthdayFragment.java
│           │               │       ├── addProfileBannerFragment.java
│           │               │       ├── addProfileInterestFragment.java
│           │               │       ├── addProfileInterest.java
│           │               │       ├── addProfilePictureFragment.java
│           │               │       ├── ChipSet.java
│           │               │       ├── ChipSetListener.java
│           │               │       └── ProfileSetupListener.java
│           │               ├── FriendRequestFragment.java
│           │               ├── Models
│           │               │   ├── BluetoothButton.java
│           │               │   ├── Interests
│           │               │   │   ├── Category.java
│           │               │   │   └── Interest.java
│           │               │   ├── UnilinkAccount.java
│           │               │   └── UnilinkUser.java
│           │               ├── othersProfileActivity.java
│           │               ├── ProfileSetupTitleFragment.java
│           │               ├── Services
│           │               │   ├── AccountService.java
│           │               │   └── UserService.java
│           │               └── UnilinkApplication.java
│           └── res
│               ├── anim
│               ├── drawable
│               ├── drawable-hdpi
│               ├── drawable-mdpi
│               ├── drawable-v24
│               ├── drawable-xhdpi
│               ├── drawable-xxhdpi
│               ├── drawable-xxxhdpi
│               ├── font
│               ├── layout
│               ├── layout-v26
│               ├── menu
│               ├── mipmap-hdpi
│               ├── mipmap-mdpi
│               ├── mipmap-xhdpi
│               ├── mipmap-xxhdpi
│               ├── mipmap-xxxhdpi
│               ├── navigation
│               ├── values
│               ├── values-night
│               └── xml
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle
```
