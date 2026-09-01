<div align="center">

![Icon for the app](app/src/main/ic_launcher-playstore.png)

</div>

[![Get it on Obtainium](https://raw.githubusercontent.com/ImranR98/Obtainium/main/assets/graphics/badge_obtainium.png)](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/First-Non-Interesting-Username/projects-list)

# Projects List App

An AOSP app for (side) project management.

## Why?

There's no reason for you to use this app over alternatives.
I made it because I wanted to try AOSP dev and create something for myself.

## Features

Everything you might expect from basic todo app plus:

- Search with filtering and sorting
- Taskception (projects in projects)
- Random project selector
- Motivation and priority setting
- Awful UI/UX (if it's not a bug, it's a feature)

## Installation

Get the APK from release tab and install it in your preffered way.

## Building

You'll need the following things:

- JDK 21
- Android SDK (command:
  `sdkmanager "platforms;android-36" \
"build-tools;36.0.0" \
"platform-tools" \
"cmdline-tools;latest"`
  )

```bash
git clone https://github.com/First-Non-Interesting-Username/projects-list.git
cd projects-list

# Make sure ANDROID_HOME points to your SDK
# On Linux typically ~/Android/Sdk

chmod +x gradlew
./gradlew assembleRelease
# APK outputs to: app/build/outputs/apk/release/app-release-unsigned.apk
```

## Developement setup

Use devenv, [CONTRIBUTING.md](/CONTRIBUTING.md) for more details

## Credits

The application icon was created using [IconKitchen](https://icon.kitchen/)
and incorporates icons from [Material Icons](https://fonts.google.com/icons).

Material Icons are provided by Google under the
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
