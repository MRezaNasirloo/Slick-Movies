# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-dontobfuscate
#-keep class com.mrezanasirloo.slick.*
#-keep public class * implements com.mrezanasirloo.slick.OnDestroyListener { *; }
#-keepclasseswithmembernames class * { @com.mrezanasirloo.slick.* <methods>; }
#-keepclasseswithmembernames class * { @com.mrezanasirloo.slick.* <fields>; }

#-dontwarn javax.**
-dontwarn net.bytebuddy.**
-dontwarn org.mockito.**
-dontwarn com.squareup.**
-dontwarn retrofit2.**
-dontwarn rx.internal.**
-dontwarn okio.**

-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**

-keep class org.hamcrest.** {
   *;
}

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-keep class junit.** { *; }
-dontwarn junit.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**

#Retrolambda
-dontwarn java.lang.invoke.*

#Firebase
-keepattributes Signature
-keepattributes *Annotation*

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.github.pedramrn.slick.parent.datasource.database.model.** {
  *;
}

#Fabric
-keepattributes SourceFile,LineNumberTable
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-dontwarn org.bouncycastle.**

# AutoValueGson
# Retain generated classes that end in the suffix
-keepnames class **_GsonTypeAdapter

# Prevent obfuscation of types which use @GenerateTypeAdapter since the simple name
# is used to reflectively look up the generated adapter.
-keepnames @com.ryanharter.auto.value.gson.GenerateTypeAdapter class *
-dontwarn com.ryanharter.auto.value.gson.GenerateTypeAdapter
-dontwarn com.google.gson.TypeAdapterFactory

# Okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Dagger
-dontwarn com.google.errorprone.annotations.*

# Arch Lifecycle
-dontwarn android.arch.**