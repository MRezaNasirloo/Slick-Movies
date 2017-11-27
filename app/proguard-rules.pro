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
#-keep class com.github.slick.*
#-keep public class * implements com.github.slick.OnDestroyListener { *; }
#-keepclasseswithmembernames class * { @com.github.slick.* <methods>; }
#-keepclasseswithmembernames class * { @com.github.slick.* <fields>; }

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