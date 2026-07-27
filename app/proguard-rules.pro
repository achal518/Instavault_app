# Preserve Gson field-name annotations and DTO members used by Retrofit reflection.
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,AnnotationDefault
-keep class com.instavault.app.data.remote.dto.** { *; }
