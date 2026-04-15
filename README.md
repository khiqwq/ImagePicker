# 钉图 (ImagePicker)

极简 Android 图片查看器 — 打开即选图，选完全屏显示，清后台重置。

[简体中文](#简体中文) | [English](#english)

---

## 简体中文

### 功能

- 打开应用 → 选择图片 → 全屏展示
- 毛玻璃风格 UI
- 检测 5 分钟内拍摄的照片，右下角智能推荐
- 配合「应用固定」使用，展示图片时对方无法退出
- 配合 [PinGuard](https://github.com/khiqwq/PinGuard) 使用，取消固定需指纹/密码验证

### 环境要求

- Android 15+ (API 35+)
- 目标设备：骁龙 8 Elite + Android 16

### 使用流程

1. 打开钉图，选择要展示的图片
2. 图片全屏显示，状态栏自动隐藏
3. 点击右上角「📌 固定展示」启用应用固定
4. 将手机交给对方，对方只能看到这张图片
5. 清除后台即可重置

### 技术栈

- Kotlin + Jetpack Compose (Material3)
- Coil 3 图片加载
- Gradle Kotlin DSL + AGP 8.7

---

## English

### Features

- Open app → pick image → fullscreen display
- Frosted glass UI
- Detects photos taken within 5 minutes, shows smart suggestion
- Works with Screen Pinning — the viewer can only see the selected image
- Works with [PinGuard](https://github.com/khiqwq/PinGuard) for biometric-protected unpin

### Requirements

- Android 15+ (API 35+)
- Target: Snapdragon 8 Elite + Android 16

### Usage

1. Open the app and select an image
2. Image displays fullscreen with hidden status bar
3. Tap "📌 Pin Display" to enable screen pinning
4. Hand the phone over — they can only see the image
5. Clear from recents to reset

### Tech Stack

- Kotlin + Jetpack Compose (Material3)
- Coil 3 image loading
- Gradle Kotlin DSL + AGP 8.7

## License

[MIT](LICENSE)
