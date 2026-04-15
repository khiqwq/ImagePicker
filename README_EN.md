<div align="center">

<img src="app/src/main/res/drawable/ic_launcher_foreground.xml" width="80" alt="icon" />

# ImagePicker

**Minimal Android image viewer — pick, fullscreen, pin & display**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Android 15+](https://img.shields.io/badge/Android-15%2B-green.svg)](#requirements)

[简体中文](README.md)

</div>

---

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

```
Open app → Pick image → Fullscreen display
                            │
                            ▼
                  Tap "📌 Pin Display"
                            │
                            ▼
                  Hand phone to viewer
                  (can't exit the app)
                            │
                            ▼
                  Clear from recents to reset
```

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Image Loading | Coil 3 |
| Build | Gradle Kotlin DSL + AGP 8.7 |

## Related

- [PinGuard](https://github.com/khiqwq/PinGuard) — LSPosed module for biometric-protected screen unpin

## 📄 License

This project is open-sourced under the [MIT License](LICENSE). Issues and PRs are welcome.
