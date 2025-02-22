# Marvel Character Viewer

This is a sample application for Marvel API loading.

Project made for Mobbuy challenge by Marcelo S N Mancini.

## Dependencies

- Revision 24.0.3 or higher of the Android SDK Build Tools installed for using `apksigner` CLI command, usually
located at <User>/AppData/Local/Android/Sdk/build-tools/<version>/apksigner, assign it on your path
- Command line tool `keytool`, usually it is already installed when installing Android Studio, if it not the case,
it is located on the Android Sdk path

- Notes about the project

This project consists on very important classes, every which is located at the packages:

- utils
- global

Those packages includes the core functionality of the app, while the `marvel` package provides an interface for
implementing views and receiving external data for serialization.

The most used classes are for Loading HTTP data, Error Tracking, File Saving/Loading/Caching and Callback classes. Those can be found at:

### Utils

- Callback.java (Callback execution and concatenation)
- Error.java (Error tracking and exception short, including verbose mode)
- FileUtils.java
- JSONUtils.java (Simple interface for loading http as JSON)
- Web.java (Includes loading and caching http data)
- Storage.java (Assigning tasks for after-load )
  
### Global

- Favorites.java (Includes infomation on how to save and load favorites)
- GlobalState.java (Includes very important information and is a class for )
- NetworkManager.java (Current network status)


### Compiling release build

The other classes could all be considered implementation of those classes, now, for building:

Just execute the batch file(it should be easy to convert to shell) to generate a signed apk at the output folder `release`


### Executing

If you need help at executing, instead of executing buildReleaseSigned, execute installReleaseSigned.

Note that `installReleaseSigned` needs the `adb` command, so it should be located somewhere on path, it is located at Android/Sdk/platform-tools
