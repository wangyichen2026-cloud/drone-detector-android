# 无人机侦测器 Android App - 详细构建指南

本指南将从零开始，一步步教您如何构建和运行这个项目。

## 第一步：安装必要的软件

### 1.1 安装 Android Studio

**Windows/Mac/Linux 用户：**

1. 访问 [Android Studio 官网](https://developer.android.com/studio)
2. 下载最新版本（2023.1 或更高）
3. 按照安装向导完成安装

**验证安装：**
- 打开 Android Studio，应该能看到欢迎界面

### 1.2 安装 Android SDK

Android Studio 首次启动时会提示安装 SDK：

1. 打开 Android Studio
2. 点击 "More Actions" → "SDK Manager"
3. 确保以下项目已安装：
   - **Android SDK Platform 35** (API Level 35)
   - **Android SDK Build-Tools 35.x.x**
   - **Android Emulator**（可选，用于测试）
   - **Android SDK Platform-Tools**

**安装步骤：**
- 选中未安装的项目
- 点击 "Apply" → "OK"
- 等待下载完成（可能需要 10-30 分钟）

### 1.3 安装 JDK 17

Android Studio 通常会自动安装 JDK，但您也可以手动安装：

1. 访问 [Oracle JDK 下载页面](https://www.oracle.com/java/technologies/downloads/#java17)
2. 下载 JDK 17
3. 按照安装向导完成安装

**验证安装：**
```bash
java -version
# 应该输出 Java 17.x.x
```

---

## 第二步：获取项目文件

### 2.1 下载项目

您已经有了项目文件在：
```
/home/ubuntu/drone-detector-android/
```

### 2.2 项目文件结构

```
drone-detector-android/
├── app/                          # 应用模块
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
│   │   │   │   │   └── device_filter.xml
│   │   │   │   └── values/
│   │   │   │       └── strings.xml
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts          # 应用构建配置
│   └── proguard-rules.pro        # 代码混淆规则
├── build.gradle.kts              # 项目构建配置
├── settings.gradle.kts           # 项目设置
└── README.md                      # 项目说明
```

---

## 第三步：在 Android Studio 中打开项目

### 3.1 打开项目

1. **启动 Android Studio**

2. **选择 "Open"**
   - 点击 "File" → "Open"
   - 或在欢迎界面点击 "Open"

3. **选择项目目录**
   - 导航到 `/home/ubuntu/drone-detector-android`
   - 点击 "Open"

4. **等待项目加载**
   - Android Studio 会自动识别项目
   - 底部会显示加载进度

### 3.2 首次打开时的配置

首次打开项目时，Android Studio 会进行以下操作：

1. **扫描项目结构** - 识别 Gradle 文件
2. **下载依赖** - 从网络下载所需的库
3. **构建缓存** - 生成项目索引

**这个过程可能需要 5-15 分钟，请耐心等待。**

---

## 第四步：同步 Gradle

### 4.1 同步依赖

1. **打开 Gradle 同步对话框**
   - 如果没有自动弹出，点击 "File" → "Sync Now"
   - 或点击顶部的 "Sync Now" 按钮

2. **等待同步完成**
   - 底部会显示进度条
   - 完成后会显示 "Gradle sync finished"

3. **解决依赖问题**（如果有）
   - 如果出现错误，点击 "Try Again"
   - 或查看 "Build" 标签页中的错误信息

### 4.2 常见同步问题

**问题：网络超时**
- 解决：检查网络连接，或使用代理

**问题：找不到依赖**
- 解决：确保 SDK 已正确安装

**问题：Kotlin 编译器版本不匹配**
- 解决：点击 "File" → "Invalidate Caches" → "Invalidate and Restart"

---

## 第五步：连接 Android 设备

### 5.1 启用开发者模式（Android 设备）

**对于 Android 7.0 及以上：**

1. 打开设置 → 关于手机
2. 连续点击 "版本号" 7 次
3. 返回设置，找到 "开发者选项"
4. 启用 "USB 调试"

### 5.2 连接 USB

1. **用 USB 线连接手机到电脑**
   - 使用原厂 USB 线或高质量第三方线
   - 确保连接稳定

2. **在手机上确认 USB 调试**
   - 手机会弹出 "允许 USB 调试吗？" 的对话框
   - 点击 "允许" 并勾选 "总是允许"

3. **验证连接**
   - 打开 Android Studio 的 "Device Manager"
   - 点击 "View" → "Tool Windows" → "Device Manager"
   - 应该能看到您的设备列出

---

## 第六步：构建项目

### 6.1 构建 APK

1. **打开 Build 菜单**
   - 点击 "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"

2. **等待构建完成**
   - 底部会显示构建进度
   - 完成后会显示 "Build successful"

3. **查找生成的 APK**
   - 构建完成后，点击 "Locate" 按钮
   - 或手动导航到：`app/build/outputs/apk/debug/app-debug.apk`

### 6.2 构建过程说明

构建过程包括以下步骤：

1. **编译 Kotlin 代码** - 将 .kt 文件编译为字节码
2. **编译 Java 代码** - 编译 Java 资源
3. **处理资源** - 打包 XML、图片等资源
4. **链接库** - 链接所有依赖库
5. **打包 APK** - 生成最终的 APK 文件
6. **签名** - 用调试密钥签名（开发版本）

---

## 第七步：运行应用

### 7.1 在真机上运行

**方法 1：使用 Run 按钮**

1. **点击 Run 按钮**
   - 点击工具栏上的绿色 "Run" 按钮（播放图标）
   - 或按快捷键 `Shift + F10`

2. **选择设备**
   - 如果只有一个设备，会自动选择
   - 如果有多个设备，会弹出选择对话框

3. **等待应用安装和启动**
   - Android Studio 会自动：
     - 构建 APK
     - 安装到设备
     - 启动应用
   - 这个过程可能需要 30 秒 - 2 分钟

**方法 2：手动安装 APK**

1. **找到 APK 文件**
   ```bash
   app/build/outputs/apk/debug/app-debug.apk
   ```

2. **通过 ADB 安装**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **手动启动应用**
   - 在设备上找到 "Drone Detector" 应用
   - 点击打开

### 7.2 在模拟器上运行（可选）

如果没有真机，可以使用 Android 模拟器：

1. **打开 Device Manager**
   - 点击 "View" → "Tool Windows" → "Device Manager"

2. **创建虚拟设备**
   - 点击 "Create Device"
   - 选择设备类型（推荐 Pixel 4）
   - 选择 Android 版本（推荐 API 35）
   - 完成创建

3. **启动模拟器**
   - 点击虚拟设备旁的 "Play" 按钮
   - 等待模拟器启动（可能需要 1-2 分钟）

4. **运行应用**
   - 点击 Run 按钮
   - 选择模拟器作为目标设备

---

## 第八步：测试应用

### 8.1 连接 ESP32

1. **准备 ESP32**
   - 确保 ESP32-C3 已上传修改后的代码（JSON 输出版本）
   - 通过 USB 连接到 Android 设备

2. **打开应用**
   - 应用启动后，进入"设备连接"标签

3. **连接设备**
   - 应用会自动扫描 USB 设备
   - 点击 ESP32 设备进行连接

4. **查看数据**
   - 进入"无人机数据"标签
   - 当 ESP32 检测到无人机时，数据会实时显示

### 8.2 测试导航功能

1. **点击导航按钮**
   - 在无人机数据中找到"高德地图导航"或"谷歌地图导航"按钮
   - 点击会打开对应的地图应用

2. **验证位置**
   - 地图应该显示无人机的位置

---

## 第九步：生成发布版本（Release APK）

### 9.1 生成签名 APK

1. **打开 Build 菜单**
   - 点击 "Build" → "Generate Signed Bundle / APK"

2. **选择 APK**
   - 选择 "APK"
   - 点击 "Next"

3. **创建或选择密钥库**
   - 如果是第一次，点击 "Create new..."
   - 填写以下信息：
     - **Key store path**：选择保存位置
     - **Password**：设置密码（记住它！）
     - **Key alias**：输入别名（如 "drone-detector"）
     - **Key password**：设置密钥密码
     - **Validity**：设置有效期（建议 25 年）
   - 点击 "OK"

4. **选择构建类型**
   - 选择 "release"
   - 点击 "Next"

5. **完成生成**
   - 点击 "Finish"
   - 等待构建完成
   - APK 会保存到 `app/build/outputs/apk/release/`

### 9.2 分发应用

生成的 Release APK 可以：
- 上传到 Google Play Store
- 分享给其他用户
- 发布到应用商店

---

## 常见问题解决

### 问题 1：构建失败 - "compileSdkVersion 35 not found"

**解决方案：**
1. 打开 SDK Manager（Tools → SDK Manager）
2. 确保 "Android SDK Platform 35" 已安装
3. 点击 "Apply" 安装
4. 重新构建项目

### 问题 2：连接设备时显示 "offline"

**解决方案：**
1. 重新拔插 USB 线
2. 在手机上重新允许 USB 调试
3. 在 Android Studio 中运行：`adb kill-server` 和 `adb start-server`

### 问题 3：应用安装失败 - "INSTALL_FAILED_INVALID_APK"

**解决方案：**
1. 清除设备上的旧版本：`adb uninstall com.example.dronedetector`
2. 重新构建并安装

### 问题 4：Gradle 同步超时

**解决方案：**
1. 检查网络连接
2. 在 Android Studio 中：File → Settings → Build, Execution, Deployment → Gradle
3. 增加超时时间或配置代理

### 问题 5：USB 设备无法识别

**解决方案：**
1. 确保 ESP32 已正确供电
2. 尝试不同的 USB 线
3. 更新 USB 驱动程序
4. 重启 Android 设备

---

## 调试技巧

### 查看日志

1. **打开 Logcat**
   - 点击 "View" → "Tool Windows" → "Logcat"

2. **过滤日志**
   - 在搜索框中输入 "DroneDetector" 或 "UsbSerialManager"
   - 只显示相关日志

3. **设置日志级别**
   - 点击右上角的下拉菜单
   - 选择 "Verbose"、"Debug" 等级别

### 使用调试器

1. **设置断点**
   - 在代码行号旁点击，设置红色断点

2. **调试运行**
   - 点击 "Debug" 按钮（甲虫图标）而不是 "Run"

3. **单步执行**
   - 使用 F10（单步）或 F11（进入函数）

---

## 下一步

完成构建后，您可以：

1. **修改代码**
   - 编辑 MainActivity.kt 或其他文件
   - 修改后会自动重新编译

2. **添加功能**
   - 数据持久化（使用 Room 数据库）
   - 实时告警（使用 NotificationManager）
   - 地理围栏（使用 Google Play Services）

3. **优化性能**
   - 使用 Android Profiler 分析性能
   - 优化 UI 渲染

4. **发布应用**
   - 生成签名 APK
   - 上传到 Google Play Store

---

## 获取帮助

如果遇到问题：

1. **查看 Build 输出**
   - 点击 "Build" 标签页查看详细错误

2. **查看 Logcat**
   - 查看应用运行时的日志

3. **搜索 Android 文档**
   - [Android Developers](https://developer.android.com/)

4. **查看项目 README**
   - `/home/ubuntu/drone-detector-android/README.md`

---

祝您构建顺利！如有问题，请随时反馈。
