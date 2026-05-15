# Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# UsbSerial
-keep class com.felhr.** { *; }
-keep interface com.felhr.** { *; }

# DroneData model
-keep class com.example.dronedetector.model.** { *; }
