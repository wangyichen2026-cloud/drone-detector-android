# 无人机侦测器 - 原生 Android App

这是一个原生 Android 应用，使用 **Kotlin + Jetpack Compose** 开发，直接通过 USB 串口读取 ESP32-C3 的 JSON 格式数据。

## 功能特性

- ✅ **USB 串口连接**：自动检测和连接 USB 设备
- ✅ **实时 JSON 解析**：直接解析 ESP32 输出的 JSON 数据
- ✅ **无人机数据显示**：显示无人机 ID、位置、速度等信息
- ✅ **一键导航**：支持高德地图和谷歌地图导航
- ✅ **实时日志**：显示连接和数据处理日志
- ✅ **深色主题**：专业的深色 UI 设计

## 项目结构

```
drone-detector-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/dronedetector/
│   │   │   │   ├── MainActivity.kt          # 主界面
│   │   │   │   ├── model/
│   │   │   │   │   └── DroneData.kt         # 数据模型
│   │   │   │   └── usb/
│   │   │   │       └── UsbSerialManager.kt  # USB 管理器
│   │   │   ├── res/
│   │   │   │   ├── xml/
│   │   │   │   │   └── device_filter.xml    # USB 设备过滤
│   │   │   │   └── values/
│   │   │   │       └── strings.xml
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 快速开始

### 前置条件

- Android Studio 2023.1 或更高版本
- Android SDK 35（API Level 35）
- JDK 17 或更高版本

### 构建步骤

1. **打开项目**
   ```bash
   cd drone-detector-android
   # 在 Android Studio 中打开此目录
   ```

2. **同步 Gradle**
   - 点击 "Sync Now" 或 Build → Make Project

3. **连接 Android 设备**
   - 启用开发者模式和 USB 调试
   - 连接到电脑

4. **运行应用**
   - 点击 Run → Run 'app' 或按 Shift+F10

## 使用方法

### 连接 ESP32

1. 打开应用，进入"设备连接"标签
2. 应用会自动列出所有可用的 USB 设备
3. 点击要连接的设备（通常是 ESP32-C3）
4. 连接成功后状态会显示为"已连接"

### 查看无人机数据

1. 进入"无人机数据"标签
2. 当 ESP32 检测到无人机时，数据会实时显示
3. 点击"高德地图导航"或"谷歌地图导航"可跳转到地图应用

### 查看日志

- 进入"日志"标签可查看所有连接和数据处理的日志
- 日志会按颜色分类：
  - 🟢 绿色：成功操作
  - 🔴 红色：错误信息
  - 🔵 蓝色：数据接收

## ESP32 JSON 格式

应用期望接收以下格式的 JSON 数据：

```json
{
  "type": "drone_detected",
  "timestamp": 1234567890,
  "mac": "AA:BB:CC:DD:EE:FF",
  "rssi": -45,
  "uav_id": "DJI-12345",
  "operator_id": "OP-67890",
  "drone_location": {
    "latitude": "30.274085",
    "longitude": "120.155006",
    "altitude": 120,
    "speed": 5,
    "heading": 45,
    "google_maps": "https://maps.google.com/?q=30.274085,120.155006",
    "amap": "https://amap.com/?q=30.274085,120.155006"
  },
  "operator_location": {
    "latitude": "30.275000",
    "longitude": "120.156000",
    "google_maps": "https://maps.google.com/?q=30.275000,120.156000",
    "amap": "https://amap.com/?q=30.275000,120.156000"
  }
}
```

## 依赖库

- **Jetpack Compose**：现代 UI 框架
- **Gson**：JSON 解析
- **UsbSerial**：USB 串口通信

## 故障排除

### 无法检测到 USB 设备

1. 检查 USB 线连接是否正常
2. 确保 Android 设备已启用 USB 调试
3. 检查 ESP32 是否正确供电

### JSON 解析失败

1. 确保 ESP32 输出的 JSON 格式正确
2. 检查波特率是否为 115200
3. 查看日志标签中的错误信息

### 应用崩溃

1. 检查 Android 版本是否 >= 7.0（API 24）
2. 尝试重新连接 USB 设备
3. 重启应用

## 后续改进

- [ ] 支持多个无人机同时监测
- [ ] 数据本地存储和历史查询
- [ ] 实时告警和推送通知
- [ ] 地理围栏功能
- [ ] 轨迹回放功能
- [ ] 数据导出（CSV/JSON）

## 许可证

MIT License

## 联系方式

如有问题或建议，请反馈。
