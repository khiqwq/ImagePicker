<div align="center">

<img src="app/src/main/res/drawable/ic_launcher_foreground.xml" width="80" alt="icon" />

# 钉图

**极简 Android 图片查看器 — 选图、全屏、固定展示**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Android 15+](https://img.shields.io/badge/Android-15%2B-green.svg)](#环境要求)

[English](README_EN.md)

</div>

---

## 功能

- 打开应用 → 选择图片 → 全屏展示
- 毛玻璃风格 UI
- 检测 5 分钟内拍摄的照片，右下角智能推荐
- 配合「应用固定」，展示图片时对方无法退出
- 配合 [PinGuard](https://github.com/khiqwq/PinGuard)，取消固定需指纹 / 密码验证

## 环境要求

- Android 15+（API 35+）
- 目标设备：骁龙 8 Elite + Android 16

## 使用流程

```
打开钉图 → 选择图片 → 全屏展示
                        │
                        ▼
              点击「📌 固定展示」
                        │
                        ▼
              手机交给对方查看
              （无法退出应用）
                        │
                        ▼
              清除后台即可重置
```

## 技术栈

| 组件 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material3 |
| 图片加载 | Coil 3 |
| 构建 | Gradle Kotlin DSL + AGP 8.7 |

## 配套项目

- [PinGuard](https://github.com/khiqwq/PinGuard) — LSPosed 模块，取消应用固定时要求指纹 / 密码验证

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源，欢迎 Issue 与 PR。
