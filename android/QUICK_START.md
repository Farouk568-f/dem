# CineStream Android - Quick Start Guide

## Prerequisites

- Android Studio (latest)
- Android SDK 24+
- Java 17+
- ~2GB disk space

## Installation Steps

### 1. Open Android Project

```bash
# Navigate to project directory
cd android

# Open in Android Studio
# File > Open > Select android folder
```

### 2. Set API Keys

Create `local.properties` in the root android folder:

```properties
sdk.dir=/path/to/android/sdk
TMDB_API_KEY=your_tmdb_api_key
SCRAPER_API_KEY=your_scraper_api_key
```

Get API keys from:
- TMDB: https://www.themoviedb.org/settings/api
- Scraper: Contact support

### 3. Build the Project

```bash
# Build debug APK
./gradlew build

# Or from Android Studio:
# Build > Build Bundle(s)/APK(s) > Build APK(s)
```

### 4. Run on Emulator/Device

```bash
# Install and run on connected device
./gradlew installDebug
./gradlew run

# Or from Android Studio:
# Run > Run 'app'
```

### 5. Verify Installation

- App should launch to login screen
- Try logging in with test credentials
- Navigate through tabs
- Video playback should work

## Project Structure

```
android/
├── app/                          # Main app module
│   ├── build.gradle.kts         # App dependencies & config
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cinestream/
│   │   │   │   ├── ui/          # Screens & UI
│   │   │   │   ├── data/        # API & models
│   │   │   │   └── utils/       # Utilities
│   │   │   └── res/
│   │   │       ├── values/      # Strings, themes
│   │   │       └── xml/         # Config files
│   │   └── test/                # Unit tests
│   └── proguard-rules.pro       # Obfuscation rules
├── build.gradle.kts             # Root gradle config
├── settings.gradle.kts          # Gradle settings
└── README.md                     # Full documentation
```

## Key Features

✅ **Authentication** - Email/password + QR code login
✅ **Content Browsing** - Movies, TV shows, trending
✅ **Search** - Global search with history
✅ **Video Player** - Multi-source, subtitles, quality selection
✅ **Favorites** - Add/remove favorites
✅ **Watch History** - Track what you've watched
✅ **Settings** - Theme, language, notifications
✅ **Dark Mode** - System, light, or dark theme
✅ **TV Support** - D-pad navigation for Android TV
✅ **Accessibility** - WCAG 2.1 AA compliant

## Common Commands

```bash
# Clean and rebuild
./gradlew clean build

# Build release version (requires signing)
./gradlew assembleRelease

# Run tests
./gradlew test

# Check for code issues
./gradlew lint

# View gradle tasks
./gradlew tasks

# Update gradle wrapper
./gradlew wrapper --gradle-version=latest
```

## Testing on Different Devices

### Emulator (Phone)
```bash
# Android Studio > Device Manager > Create Virtual Device
# Select Pixel 4a with API 34
./gradlew installDebug
```

### Emulator (Tablet)
```bash
# Select Pixel Tablet with API 34
./gradlew installDebug
```

### Emulator (TV)
```bash
# Select Android TV (1080p) with API 34
./gradlew installDebug
# Use remote arrows to navigate
```

### Physical Device
```bash
# Enable Developer Mode: Settings > About > Tap Build Number 7 times
# Enable USB Debugging: Settings > Developer Options
# Connect via USB
./gradlew installDebug
```

## Debugging

### View Logs
```bash
adb logcat | grep CineStream
```

### Debug Breakpoints
1. Set breakpoint in code
2. Run in Debug mode (Shift+F9)
3. App will pause at breakpoint
4. Step through code using Android Studio

### Memory Profiler
1. View > Tool Windows > Profiler
2. Record memory usage
3. Analyze garbage collection

### Network Inspection
1. View > Tool Windows > App Inspection
2. Monitor API calls
3. View request/response

## Troubleshooting

### "SDK not found"
- File > Settings > SDK Manager
- Install Android SDK 34
- Set SDK path in local.properties

### "API Key not set"
- Check local.properties has TMDB_API_KEY
- Restart Android Studio
- Rebuild project

### "App crashes on launch"
```bash
# Check logs
adb logcat | grep FATAL

# Common issues:
# - Missing API keys
# - No internet connection
# - SDK version mismatch
```

### "Build fails"
```bash
# Full clean rebuild
./gradlew clean build --no-build-cache

# Or from Android Studio:
# Build > Clean Project
# File > Invalidate Caches > Restart
```

## Next Steps

1. **Read Documentation**: Review `DEVELOPMENT.md` for detailed guide
2. **Explore Code**: Browse screen implementations in `ui/screens/`
3. **Customize**: Modify colors, text, features as needed
4. **Release**: Follow `DEVELOPMENT.md` for building release APK
5. **Deploy**: Upload to Google Play Store

## Resources

- [Android Docs](https://developer.android.com)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [TMDB API](https://www.themoviedb.org/settings/api)

## Support

For issues:
1. Check `DEVELOPMENT.md` troubleshooting section
2. Review GitHub Issues
3. Check logcat for error messages
4. Enable debugging in Android Studio

---

**Happy Coding!** 🎬

The CineStream Android app is ready for customization and deployment.

