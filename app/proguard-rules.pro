# Keep Crashlytics to be readable
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Crashlytics
# Note: https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
