# Keep kotlinx.serialization generated serializers.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.hotel.kitchenpos.data.** {
    *** Companion;
}
-keepclasseswithmembers class com.hotel.kitchenpos.data.** {
    kotlinx.serialization.KSerializer serializer(...);
}
