# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontshrink
-dontpreverify
-dontoptimize
-dontusemixedcaseclassnames

-flattenpackagehierarchy
-allowaccessmodification
-printmapping map.txt

-optimizationpasses 7
-verbose
-keepattributes Exceptions,InnerClasses
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes *JavascriptInterface*

-dontwarn android.support.**
-dontwarn cn.jpush.**
-dontwarn com.google.**

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#gson bean ***---------------------
-keepattributes *Annotation*
-keepattributes Signature
-keep class cn.jpush.** { *; }
-keep class com.google.** { *; }

-keep class com.markjin.artmall.db.bean.** { *; }
-keep class android.net.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.**


-keepclassmembers class io.reactivex.** { *; }
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
