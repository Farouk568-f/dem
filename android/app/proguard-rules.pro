# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**
-dontnote retrofit2.**

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# kotlinx.serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * implements kotlinx.serialization.KSerializer {
    *** INSTANCE;
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

# Kotlin
-keep class kotlin.** { *; }
-keepclassmembers class kotlin.Metadata {
    *** memberName (...);
}
-dontwarn kotlin.**

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Media3
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Your app classes
-keep class com.cinestream.** { *; }
