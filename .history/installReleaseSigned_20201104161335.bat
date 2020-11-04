@echo off
::password = android

CALL buildReleaseSigned.bat && ^
%ANDROID_PATH%adb install %outputFolder%/%outputFile%.apk