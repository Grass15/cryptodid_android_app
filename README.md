# Cryptodid Android App
  
  Guide to setup on your local machine
  
  Prerequistes: 

Java 11
=========
  set the JAVA_HOME environment variable pointing to java 11 JDK installation or have the java executable on the PATH environment variable

Gradle
=============
  Download the latest binary archive version of gradle: https://gradle.org/install/#manually
  Extract the distribution archive and rename the folder Gradle
    - unzip gradle-8.0.2-bin.zip
  Create a new directory C:\Gradle and copy the content folder of the extrated archive to this directory
  Add the bin folder to the PATH environment variable:
      
  Linux & MacOS userS
      $ export PATH=$PATH:/opt/gradle/gradle-8.0.2/bin

  Microsoft Windows users
      In File Explorer right-click on the This PC (or Computer) icon, then click Properties -> Advanced System Settings -> Environmental Variables.
      Under System Variables select Path, then click Edit. Add an entry for C:\Gradle\gradle-8.0.2\bin. Click OK to save.

  Confirm with the command gradle -v in a new shell
    
Android studio
================
  Download Link: https://developer.android.com/studio
    
  Setup:
  
  In a new shell do the following commands
  ### ` git clone https://github.com/Grass15/cryptodid_android_app.git`
  ### ` cd cryptodid_verifier`
  ### ` mvn package`
  ### ` target\bin\webapp (Windows) or sh target/bin/webapp`

  Good to know:
    
    There is two version of the Verifier. Heroku doesn't support pure TCP/IP. So for the deployed version (On branch Main ), 
    the verifier use websocket to communicate with the android app
