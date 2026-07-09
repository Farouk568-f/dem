# CineStream Android

Native Android application for CineStream, migrated from React to Kotlin + Jetpack Compose.

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/cinestream/
│   │   │   ├── ui/
│   │   │   │   ├── pages/          # Compose screens
│   │   │   │   ├── components/     # Reusable components
│   │   │   │   ├── theme/          # Material theme, colors
│   │   │   │   └── navigation/     # Jetpack Navigation
│   │   │   ├── data/
│   │   │   │   ├── api/            # Retrofit services, DTOs
│   │   │   │   ├── database/       # Room entities, DAOs
│   │   │   │   ├── repository/     # Data layer abstraction
│   │   │   │   └── cache/          # In-memory caching
│   │   │   ├── domain/
│   │   │   │   ├── models/         # Business logic models
│   │   │   │   ├── usecases/       # Reusable business logic
│   │   │   │   └── repository/     # Repository interfaces
│   │   │   ├── utils/              # Helper utilities
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── values/             # Strings, colors, dimensions
│   │   │   └── drawable/           # Icons and drawables
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Navigation**: Jetpack Navigation Compose
- **Networking**: Retrofit 2 + OkHttp
- **Serialization**: kotlinx.serialization
- **Database**: Room
- **Media**: Media3 / ExoPlayer
- **Image Loading**: Coil
- **State Management**: ViewModel + StateFlow

## Building

```bash
# Build the Android app
./gradlew build

# Run on emulator
./gradlew installDebug

# Create release build
./gradlew assembleRelease
```

## Migration Status

- [x] Phase 1: Project Setup & Foundation
- [ ] Phase 2: Authentication & Core Navigation
- [ ] Phase 3: Content Browsing
- [ ] Phase 4: Search & Discovery
- [ ] Phase 5: Video Player (Critical)
- [ ] Phase 6: Advanced Features
- [ ] Phase 7: Polish & Optimization

## Notes

- All API calls match the React implementation exactly
- The backend (Cloudflare Workers + Vercel) is unchanged
- Existing React app remains untouched in the parent directory
- This is a complete rewrite using native Android technologies
