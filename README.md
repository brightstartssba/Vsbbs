# Android Gallery App

Ứng dụng Android Gallery hiện đại cho phép xem ảnh và video với pipeline CI/CD tự động để build APK.

## Tính năng

- ✅ Xem gallery ảnh với giao diện lưới
- ✅ Phát video với ExoPlayer
- ✅ Giao diện Material Design 3 hiện đại
- ✅ Hỗ trợ các định dạng ảnh: JPEG, PNG, GIF, WebP
- ✅ Hỗ trợ các định dạng video: MP4, AVI, MOV
- ✅ Lọc theo loại media (Tất cả, Ảnh, Video)
- ✅ Xin quyền truy cập storage động
- ✅ Build APK tự động qua GitHub Actions

## Cấu hình kỹ thuật

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35
- **Java/Kotlin**: Java 17 compatibility
- **Build Tool**: Gradle 8.9 với AGP 8.7.2

## Kiến trúc

```
app/
├── src/main/java/com/gallery/android/
│   ├── MainActivity.kt              # Activity chính
│   ├── MediaViewerActivity.kt       # Activity xem chi tiết media
│   ├── adapter/
│   │   └── MediaAdapter.kt          # Adapter cho RecyclerView
│   ├── model/
│   │   └── MediaItem.kt             # Data model
│   └── viewmodel/
│       └── MediaViewModel.kt        # ViewModel cho MVVM
├── src/main/res/
│   ├── layout/                      # Layout files
│   ├── drawable/                    # Vector icons
│   ├── values/                      # Strings, colors, themes
│   └── mipmap-*/                    # App icons
└── build.gradle                     # Module build configuration
```

## Thư viện sử dụng

### Core Android
- `androidx.core:core-ktx:1.13.1`
- `androidx.appcompat:appcompat:1.7.0`
- `com.google.android.material:material:1.12.0`

### Architecture & Lifecycle
- `androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6`
- `androidx.lifecycle:lifecycle-livedata-ktx:2.8.6`
- `androidx.activity:activity-ktx:1.9.2`

### Media & UI
- `com.github.bumptech.glide:glide:4.16.0` - Image loading
- `androidx.media3:media3-exoplayer:1.4.1` - Video playback
- `androidx.recyclerview:recyclerview:1.3.2` - Grid layout

### Async Programming
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3`

## GitHub Actions CI/CD

Pipeline tự động build APK với các tính năng:

### Workflow Features
- ✅ Build trên Ubuntu latest với Java 17
- ✅ Gradle caching để tăng tốc build
- ✅ Run unit tests trước khi build
- ✅ Build cả debug và release APK
- ✅ Upload artifacts với retention 30 ngày
- ✅ Auto-release khi có tag

### Triggers
- Push to `main` hoặc `develop` branch
- Pull requests to `main`
- Manual workflow dispatch
- Git tags (cho auto-release)

### Build Outputs
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## Cách build local

```bash
# Clone repository
git clone <repository-url>
cd AndroidGallery

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Install debug APK to device
./gradlew installDebug
```

## Permissions

Ứng dụng yêu cầu các quyền sau:

### Android 13+ (API 33+)
- `READ_MEDIA_IMAGES` - Đọc ảnh từ storage
- `READ_MEDIA_VIDEO` - Đọc video từ storage
- `READ_MEDIA_VISUAL_USER_SELECTED` - Selective media access

### Android 12 và thấp hơn
- `READ_EXTERNAL_STORAGE` - Đọc files từ external storage

## Tối ưu hóa

### Gradle Performance
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
```

### ProGuard/R8
- Minification enabled cho release builds
- Keep rules cho Glide và ExoPlayer
- Optimize cho APK size nhỏ gọn

## Hướng dẫn đóng góp

1. Fork repository
2. Tạo feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Mở Pull Request

## License

Dự án này được phát hành theo Apache License 2.0.