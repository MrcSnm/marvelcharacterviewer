@echo off
::password = android

CALL buildReleaseSigned.bat && ^
adb install 