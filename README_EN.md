# ImagePicker

Minimal Android image viewer — open, pick, fullscreen display, clear to reset.

[简体中文](README.md)

## Features

- Open app → pick image → fullscreen display
- Frosted glass UI
- Detects photos taken within 5 minutes, shows smart suggestion
- Works with Screen Pinning — the viewer can only see the selected image
- Works with [PinGuard](https://github.com/khiqwq/PinGuard) for biometric-protected unpin

## Requirements

- Android 15+ (API 35+)
- Target: Snapdragon 8 Elite + Android 16

## Usage

1. Open the app and select an image
2. Image displays fullscreen with hidden status bar
3. Tap "📌 Pin Display" to enable screen pinning
4. Hand the phone over — they can only see the image
5. Clear from recents to reset

## Tech Stack

- Kotlin + Jetpack Compose (Material3)
- Coil 3 image loading
- Gradle Kotlin DSL + AGP 8.7

## 📄 License

This project is open-sourced under the [MIT License](LICENSE). Issues and PRs are welcome.
