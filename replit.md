# Android Gallery App

## Project Overview
An Android mobile application for viewing images and videos with automated GitHub CI/CD build pipeline for continuous APK deployment.

## Stack
- Android SDK with Kotlin
- Modern Android Architecture (MVVM + Repository Pattern)
- ViewBinding for UI
- Coroutines for async operations
- GitHub Actions for CI/CD
- Gradle build system with AGP 8.x
- Java 17 compatibility

## User Preferences
- Vietnamese language communication preferred
- Focus on modern Android development practices
- **iOS Photos app interface design** - match iOS Photos app screenshots exactly
- Automated CI/CD pipeline for APK generation following GitHub official documentation
- Emphasis on avoiding GitHub Actions build errors

## Project Architecture
- **app/**: Main Android application module
- **gradle/**: Gradle wrapper configuration
- **.github/workflows/**: CI/CD pipeline configuration
- **docs/**: Project documentation

## Recent Changes
- 2025-07-28: ✅ Complete Android Gallery app structure created
- 2025-07-28: ✅ GitHub Actions workflow configured for automated APK building
- 2025-07-28: ✅ Modern Android architecture implemented with MVVM + Kotlin
- 2025-07-28: ✅ **iOS Photos app interface redesign completed**
  - iOS-style header with "Photos" title and item count
  - Search and Select buttons matching iOS design
  - Staggered grid layout with variable photo sizes
  - Recent Days horizontal scroll section
  - People & Pets section with circular avatars
  - iOS color scheme and typography
- 2025-07-28: ✅ Enhanced GitHub Actions workflow following official documentation
  - Latest actions (checkout@v4, setup-java@v4, setup-gradle@v4)
  - Build matrix, caching, and proper artifact management
  - Lint checks, unit tests, and automated releases
- 2025-07-28: ✅ ExoPlayer integration for video playbook
- 2025-07-28: ✅ Glide integration for image loading with thumbnails
- 2025-07-28: ✅ Permission handling for Android 13+ storage access
- 2025-07-28: ✅ **CRITICAL BUILD FIXES COMPLETED**
  - Fixed all padding attributes (paddingHorizontal/Vertical → individual padding)
  - Fixed tint attributes (app:tint → android:tint)
  - Created missing vector drawables (ic_play_arrow, ic_check)
  - Enhanced GitHub Actions with proper permissions and error handling
  - Resolved all layout compilation errors from screenshots
- 2025-07-28: ✅ **Project ready for GitHub deployment and CI/CD**

## Features
- ✅ **iOS-style Photos interface** matching user screenshots
  - Large "Photos" title with dynamic item count
  - iOS-style search and select buttons
  - Staggered grid layout with variable photo sizes
  - Recent Days horizontal scroll section with date labels
  - People & Pets section with circular avatars
- ✅ Image gallery viewing with Glide
- ✅ Video playback support with ExoPlayer
- ✅ Selection mode with iOS-style checkmarks
- ✅ Modern UI with iOS color scheme and typography
- ✅ Enhanced GitHub Actions workflow with latest best practices
- ✅ Automated APK builds via GitHub Actions
- ✅ Support for various image formats (JPEG, PNG, GIF, WebP)
- ✅ Support for video formats (MP4, AVI, MOV)

## Build Configuration
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 35 (Android 15)
- Compile SDK: 35
- Java/Kotlin compatibility: Java 17