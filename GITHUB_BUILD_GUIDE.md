# 使用 GitHub Actions 自动构建 APK

这是最简单的方法！您只需要一个 GitHub 账户，就能自动构建 APK。

## 快速开始（5 分钟）

### 第 1 步：创建 GitHub 账户

1. 访问 [GitHub 官网](https://github.com)
2. 点击 "Sign up"
3. 填写邮箱、密码、用户名
4. 验证邮箱
5. 完成！

### 第 2 步：创建新仓库

1. 登录 GitHub
2. 点击右上角 "+" → "New repository"
3. 填写以下信息：
   - **Repository name**: `drone-detector-android`
   - **Description**: `无人机侦测器 Android App`
   - **Public**: 选择（这样可以免费使用 GitHub Actions）
4. 点击 "Create repository"

### 第 3 步：上传代码

**方法 A：使用 Git 命令行（推荐）**

```bash
# 进入项目目录
cd /home/ubuntu/drone-detector-android

# 初始化 Git 仓库
git init

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit: Drone Detector Android App"

# 添加远程仓库（替换 YOUR_USERNAME）
git remote add origin https://github.com/YOUR_USERNAME/drone-detector-android.git

# 推送到 GitHub（首次需要输入 GitHub 用户名和密码/Token）
git branch -M main
git push -u origin main
```

**方法 B：使用 GitHub Desktop（图形界面）**

1. 下载 [GitHub Desktop](https://desktop.github.com)
2. 登录 GitHub 账户
3. 点击 "File" → "Clone repository"
4. 选择您刚创建的仓库
5. 选择本地路径
6. 点击 "Clone"

**方法 C：直接在 GitHub 网页上上传**

1. 打开您的仓库
2. 点击 "Add file" → "Upload files"
3. 拖拽项目文件夹中的所有文件
4. 点击 "Commit changes"

### 第 4 步：等待自动构建

1. 上传代码后，GitHub 会自动开始构建
2. 点击 "Actions" 标签页查看构建进度
3. 等待构建完成（通常需要 5-10 分钟）

### 第 5 步：下载 APK

构建完成后：

1. 打开 "Actions" 标签页
2. 点击最新的构建记录
3. 向下滚动找到 "Artifacts"
4. 点击 "app-debug" 下载 APK 文件

**APK 文件名**: `app-debug.apk`

### 第 6 步：安装到手机

1. 将 APK 文件传到 Android 手机
2. 打开文件管理器，找到 APK 文件
3. 点击安装
4. 允许安装来自未知来源的应用
5. 完成！

---

## 常见问题

### Q: 如何更新应用？

A: 修改代码后：
1. 提交更改：`git add . && git commit -m "更新说明"`
2. 推送到 GitHub：`git push`
3. GitHub 会自动重新构建
4. 下载新的 APK

### Q: GitHub Actions 免费吗？

A: 是的！免费账户每月有 2000 分钟的免费构建时间，足够构建数百个 APK。

### Q: 如何生成发布版本（Release APK）？

A: 创建一个 Git 标签：
```bash
git tag v1.0.0
git push origin v1.0.0
```

GitHub 会自动构建并创建 Release。

### Q: 构建失败怎么办？

A: 
1. 点击 "Actions" 查看失败日志
2. 常见原因：
   - SDK 版本不匹配 → 更新 `app/build.gradle.kts` 中的 `compileSdk`
   - 依赖下载失败 → 重新运行构建
   - 代码错误 → 修复代码后重新提交

### Q: 如何在多个设备上安装？

A: 
1. 下载 APK 后分享给其他人
2. 或在 GitHub 上创建 Release，其他人可以直接下载

---

## 项目文件说明

关键文件：

| 文件 | 说明 |
|------|------|
| `.github/workflows/build.yml` | GitHub Actions 配置文件 |
| `gradlew` | Gradle 包装脚本（自动下载 Gradle） |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle 版本配置 |
| `app/build.gradle.kts` | 应用构建配置 |
| `build.gradle.kts` | 项目构建配置 |

---

## 高级用法

### 自动发布到 Google Play Store

修改 `.github/workflows/build.yml`，添加以下步骤：

```yaml
- name: Upload to Play Store
  uses: r0adkll/upload-google-play@v1
  with:
    serviceAccountJson: ${{ secrets.PLAY_STORE_KEY }}
    packageName: com.example.dronedetector
    releaseFiles: app/build/outputs/apk/release/app-release.apk
    track: beta
```

### 自动发送通知

当构建完成时发送邮件或 Slack 通知：

```yaml
- name: Send Slack Notification
  uses: slackapi/slack-github-action@v1
  with:
    payload: |
      {
        "text": "APK 构建完成！",
        "blocks": [
          {
            "type": "section",
            "text": {
              "type": "mrkdwn",
              "text": "无人机侦测器 APK 已构建完成\n下载链接: ${{ github.server_url }}/${{ github.repository }}/actions/runs/${{ github.run_id }}"
            }
          }
        ]
      }
```

---

## 获取帮助

- GitHub 文档：https://docs.github.com
- GitHub Actions 文档：https://docs.github.com/en/actions
- Android 构建文档：https://developer.android.com/build

---

祝您构建顺利！🚀
