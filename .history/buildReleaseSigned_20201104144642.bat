@echo off
::password = android
set outputFolder=release
set keyName=release-key.jks
set outputFile=marvelcharacterviewer

set inputUnsigned="app/build/outputs/apk/release/app-release-unsigned.apk"

if NOT EXIST "%outputFolder%\" MKDIR %outputFolder%
IF NOT EXIST %outputFolder\keyName% (
    echo Generating a new keystore
    keytool -genkey -v -keystore %outputFolder%/%keyName% -keyalg RSA -keysize 2048 -validity 10000 -alias mcv
)
gradlew assembleRelease
apksigner sign --ks %outputFolder%/%keyName% --out %outputFolder%/%outputFile%.apk %inputUnsigned%
apksigner verify "%outputFolder%%outputFile%.apk"
