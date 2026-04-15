# 钉图 (ImagePicker)

极简 Android 图片查看器 — 打开即选图，选完全屏显示，清后台重置。

[English](README_EN.md)

## 功能

- 打开应用 → 选择图片 → 全屏展示
- 毛玻璃风格 UI
- 检测 5 分钟内拍摄的照片，右下角智能推荐
- 配合「应用固定」使用，展示图片时对方无法退出
- 配合 [PinGuard](https://github.com/khiqwq/PinGuard) 使用，取消固定需指纹/密码验证

## 环境要求

- Android 15+ (API 35+)
- 目标设备：骁龙 8 Elite + Android 16

## 使用流程

1. 打开钉图，选择要展示的图片
2. 图片全屏显示，状态栏自动隐藏
3. 点击右上角「📌 固定展示」启用应用固定
4. 将手机交给对方，对方只能看到这张图片
5. 清除后台即可重置

## 技术栈

- Kotlin + Jetpack Compose (Material3)
- Coil 3 图片加载
- Gradle Kotlin DSL + AGP 8.7

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源，欢迎 Issue 与 PR。
