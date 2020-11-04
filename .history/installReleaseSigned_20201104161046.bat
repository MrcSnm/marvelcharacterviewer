@echo off
::password = android

CALL buildReleaseSigned.bat && ^
adb install %ANDROID_PATH%