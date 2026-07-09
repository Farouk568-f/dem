# CineStream Android - Development Guide

## Project Structure

```
android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cinestream/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/         # Composable screens
│   │   │   │   │   ├── components/      # Reusable UI components
│   │   │   │   │   ├── navigation/      # Navigation setup
│   │   │   │   │   ├── viewmodel/       # ViewModels for screens
│   │   │   │   │   └── theme/           # Material 3 theming
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/            # Retrofit services & DTOs
│   │   │   │   │   └── repository/     # Data repositories
│   │   │   │   ├── domain/
│   │   │   │   │   └── auth/           # Authentication service
│   │   │   │   ├── utils/              # Utilities & helpers
│   │   │   │   └── CineStreamApplication.kt
│   │   │   └── res/
│   │   │       ├── values/             # Strings, themes, dimens
│   │   │       └── xml/                # Network security config
│   │   └── test/
│   │       └── java/                   # Unit tests
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/wrapper/
```

## Setup Instructions

### Prerequisites
- Android Studio (latest)
- Android SDK 24+ (API level 24 minimum, target 34+)
- Gradle 8.0+
- Java 17+

### First Run
```bash
# Clone repository
git clone https://github.com/Farouk568-f/dem.git
cd dem/android

# Build the project
./gradlew build

# Run on emulator or device
./gradlew installDebug
```

### Environment Configuration
1. Create `local.properties` if not present:
```properties
sdk.dir=/path/to/android/sdk
```

2. Add API keys to `gradle.properties`:
```properties
TMDB_API_KEY=your_api_key
SCRAPER_API_KEY=your_api_key
```

## Development Workflow

### Running the App
```bash
# Debug build
./gradlew installDebug

# Release build (requires signing)
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Code Style
- Follow [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- Use ktlint for code formatting:
```bash
./gradlew ktlintFormat
```

### Adding Dependencies
1. Add to `app/build.gradle.kts`
2. Run `./gradlew build` to sync
3. Import in code

### Creating New Features
1. **Create ViewModel** in `ui/viewmodel/`
2. **Create Screen/Composable** in `ui/screens/`
3. **Add Route** in `ui/navigation/Routes.kt`
4. **Add Navigation** in `ui/navigation/NavGraph.kt`
5. **Test locally** on emulator/device

## Debugging

### Using Android Studio Debugger
1. Set breakpoints in code
2. Run app in debug mode: `./gradlew installDebug`
3. Use "Debug" configuration in Android Studio

### Inspecting Network Requests
- Use Android Studio Network Inspector
- Install [Charles Proxy](https://www.charlesproxy.com/)
- Configure in app (localhost only in debug builds)

### Performance Profiling
```bash
# CPU Profiling
./gradlew profileDebug

# Memory Profiling
# Use Android Studio Profiler: View > Tool Windows > Profiler
```

### View Logs
```bash
adb logcat | grep CineStream
```

## Testing

### Unit Tests
Located in `app/src/test/` - run with:
```bash
./gradlew test
```

### Integration Tests
Located in `app/src/androidTest/` - run with:
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
```bash
./gradlew testDebugUnitTestCoverage
```

## Building for Release

### Prerequisites
- Keystore file for signing
- Keystore password & key alias

### Build Release APK
```bash
./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=path/to/keystore.jks \
  -Pandroid.injected.signing.store.password=keystorepass \
  -Pandroid.injected.signing.key.alias=keyalias \
  -Pandroid.injected.signing.key.password=keypass
```

### Build Release AAB (for Google Play)
```bash
./gradlew bundleRelease \
  -Pandroid.injected.signing.store.file=path/to/keystore.jks \
  -Pandroid.injected.signing.store.password=keystorepass \
  -Pandroid.injected.signing.key.alias=keyalias \
  -Pandroid.injected.signing.key.password=keypass
```

## Troubleshooting

### Build Fails
```bash
# Clean build
./gradlew clean build

# Update gradle
./gradlew wrapper --gradle-version=latest
```

### App Crashes on Startup
1. Check logcat: `adb logcat | grep FATAL`
2. Verify API keys are set
3. Check internet connectivity

### Memory Issues
- Use Android Studio Profiler
- Check for memory leaks in ViewModel
- Optimize image loading with Coil

## Resources
- [Android Documentation](https://developer.android.com)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [TMDB API Docs](https://www.themoviedb.org/settings/api)

## Contributing
1. Create feature branch: `git checkout -b feature/name`
2. Commit changes: `git commit -m "Feature: description"`
3. Push to branch: `git push origin feature/name`
4. Create Pull Request

