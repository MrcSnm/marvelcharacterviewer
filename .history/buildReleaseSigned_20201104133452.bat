::password = android
IF NOT EXIST "release-key.jks" (
    echo "Generating a new keystore"
    keytool -genkey -v -keystore release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias mcv
)
gradlew assembleRelease
apk