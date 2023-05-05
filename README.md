# Cryptodid Android App
## Description
The Cryptodid Android application uses protocols from the [Cryptodid verifier](https://github.com/Grass15/cryptodid_verifier.git) (on the access-control branch) to run the [stadium use case demonstration](https://github.com/JoshuaAziake/stadium_app.git).
  
## Prerequisites

- [Java 11](https://www.oracle.com/ca-en/java/technologies/javase/jdk11-archive-downloads.html)
```terminal
  set the JAVA_HOME environment variable pointing to java 11 JDK installation or have the java executable on the PATH environment variable
  ```

- [Gradle](https://gradle.org/install/#manually)
```terminal
  1. Download the latest binary archive version of gradle 
  
  2. Extract the distribution archive and rename the folder Gradle
    (unzip gradle-8.0.2-bin.zip)
  
  3. Create a new directory C:\Gradle and copy the content folder of the extrated archive to this directory
  
  4. Add the bin folder to the PATH environment variable:
      
  Linux & MacOS userS
      
    $ export PATH=$PATH:/opt/gradle/gradle-8.0.2/bin

  Microsoft Windows users
      
    In File Explorer right-click on the This PC (or Computer) icon, then click 
    Properties -> Advanced System Settings -> Environmental Variables.
    Under System Variables select Path, then click Edit. Add an entry for C:\Gradle\gradle-8.0.2\bin. Click OK to save.

  5. Confirm with the command gradle -v in a new shell
  ```
    
- [Android studio](https://developer.android.com/studio)
```terminal
  1. Download
  2. Install and follow the instructions to download SDK and API Device for the emulator.
  ```
- [Git](https://git-scm.com/download/win)
``` terminal
Download the latest version
```

## Setup
  
  1. In a new shell do the following command:
  ``` terminal
  git clone https://github.com/Grass15/cryptodid_android_app.git
  git checkout access-control
  ```
  2. Open the project folder in Android Studio 
  3. Make sure you have the verifier and stadium web application running 
  4. Run the project 

<br>
<br>

Note: You **do not** need an Android device for this. If you do not have one, create an emulator on Android Studio; otherwise, pair your device. <br>
Note: An apk to install the app on your android device is available in app/release.
  
