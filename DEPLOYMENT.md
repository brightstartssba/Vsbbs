# Hướng dẫn Deploy Android Gallery App

## Tình trạng hiện tại

✅ **Hoàn thành:**
- **Giao diện iOS Photos**: Thiết kế hoàn toàn giống ứng dụng Photos của iOS
  - Header với title "Photos" và số lượng item
  - Buttons Search và Select theo style iOS
  - Grid layout với kích thước ảnh đa dạng
  - Section "Recent Days" scroll ngang với date labels
  - Section "People & Pets" với avatar tròn
- Project structure Android hoàn chỉnh với Kotlin
- MainActivity với iOS-style interface
- MediaViewerActivity để xem ảnh/video fullscreen  
- **GitHub Actions workflow được cập nhật** theo tài liệu chính thức
  - Sử dụng actions mới nhất (checkout@v4, setup-java@v4, setup-gradle@v4)
  - Build matrix, caching, và artifact management
  - Lint checks, unit tests, và automated releases
- Modern Android architecture (MVVM + Repository)
- ExoPlayer integration cho video
- Glide integration cho ảnh với thumbnail generation
- Permission handling cho Android 13+

📋 **Tính năng chính:**
- Xem ảnh trong grid layout 3 cột
- Phát video với controls
- Lọc theo loại media (Tất cả/Ảnh/Video)
- Fullscreen viewer cho ảnh và video
- Dynamic permissions cho storage access
- Tự động build APK qua GitHub Actions

## Cách Deploy

### 1. Push code lên GitHub Repository

```bash
# Tạo repository mới trên GitHub
# Clone hoặc init git trong project
git init
git add .
git commit -m "Initial Android Gallery App"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/android-gallery.git
git push -u origin main
```

### 2. GitHub Actions sẽ tự động build APK

Workflow đã được cấu hình trong `.github/workflows/android-build.yml`:

- **Triggers:** Push to main/develop, Pull Requests, Manual dispatch
- **Build tools:** Java 17, Gradle 8.9, AGP 8.7.2
- **Outputs:** Debug và Release APK
- **Artifacts:** Tự động upload APK với retention 30 ngày

### 3. Download APK từ GitHub Actions

1. Vào GitHub repository
2. Click tab "Actions" 
3. Chọn workflow run thành công
4. Download artifacts:
   - `debug-apk`: APK debug để test
   - `release-apk`: APK release cho production

### 4. Install APK trên Android device

```bash
# Enable Developer Options và USB Debugging
# Install qua ADB
adb install app-debug.apk

# Hoặc copy APK file vào device và install manual
```

## Cấu hình môi trường local (Optional)

Để build local trên máy dev:

### Android SDK Setup
```bash
# Download Android Studio hoặc Command Line Tools
# Set ANDROID_HOME environment variable
export ANDROID_HOME=/path/to/android-sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# Install required SDK components
sdkmanager "platforms;android-35" "build-tools;35.0.0"
```

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK  
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## Troubleshooting

### Common Issues

1. **SDK not found**: Đảm bảo ANDROID_HOME được set đúng
2. **Build fails**: Check Java version (cần Java 17)
3. **Permission denied**: `chmod +x gradlew`
4. **Memory issues**: Tăng Gradle JVM args trong gradle.properties

### GitHub Actions Troubleshooting

1. **Build timeout**: Workflow sử dụng caching để tăng tốc
2. **Artifact not found**: Check build logs cho lỗi cụ thể
3. **Permission issues**: Repository cần enable Actions

## Security Notes

- APK release không được sign (unsigned)
- Để có signed APK, cần thêm keystore secrets vào GitHub
- Debug APK chỉ dùng để test, không distribute

## App Features

### Supported Media Formats
- **Images**: JPEG, PNG, GIF, WebP
- **Videos**: MP4, AVI, MOV, MKV, WebM, 3GP

### Android Compatibility  
- **Minimum**: API 24 (Android 7.0)
- **Target**: API 35 (Android 15)
- **Permissions**: Tự động xin quyền storage theo Android version

### Performance
- Lazy loading với Glide
- Video playback với ExoPlayer
- Coroutines cho async operations
- RecyclerView với ViewBinding