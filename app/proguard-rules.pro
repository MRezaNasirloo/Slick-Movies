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

-keep class com.github.slick.*
#-keep class **HomeController** {*;}
-keep public class * implements com.github.slick.OnDestroyListener { public <init>(...); }
#-keepclasseswithmembernames class * { @com.github.slick.* <methods>; }
-keepclasseswithmembernames class * { @com.github.slick.* <fields>; }

#-dontwarn javax.**
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