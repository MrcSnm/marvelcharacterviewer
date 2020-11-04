::password = android
set outputFolder="release\"

if NOT EXIST "release\" MKDIR release
IF NOT EXIST "release-key.jks" (
    echo "Generating a new keystore"
    keytool -genkey -v -keystore release/release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias mcv
)
gradlew assembleRelease
apksigner sign --ks release/release-key.jks --out release\marvelcharacterviewer.apk app/build/outputs/apk/release/app-release-unsigned.apk
apksigner verify release/marvelcharacterviewer.apk
