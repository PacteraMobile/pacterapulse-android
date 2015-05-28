# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/chanielyu/Library/Android/sdk/tools/proguard/proguard-android.txt
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



-ignorewarnings

# trun those 3 on to enable obfuscate
-dontoptimize
-dontobfuscate
-dontskipnonpubliclibraryclasses

# don't show warning message
-dontwarn com.google.common.**
-dontwarn com.jakewharton.**
-dontwarn java.nio.**
-dontwarn com.atermenji.**
-dontwarn com.cocosw.**
-dontwarn com.squareup.**
-dontwarn org.codehaus.**

-renamesourcefileattribute SourceFile

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-printmapping map.txt
-printseeds seed.txt

-keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String); }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View { public <init>(android.content.Context); public <init>(android.content.Context, android.util.AttributeSet); public <init>(android.content.Context, android.util.AttributeSet, int); public void set*(...); }

#-keep class com.cocosw.query.** { *; }
-keepclasseswithmembernames class * extends com.cocosw.query.AbstractViewQuery {*;}

-keepclassmembers class * extends android.app.Activity { public void *(android.view.View); }
-keepclassmembers class android.support.v4.app.Fragment { *** getActivity(); public *** onCreate(); public *** onCreateOptionsMenu(...); }

-keep public class * extends junit.framework.TestCase


-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}