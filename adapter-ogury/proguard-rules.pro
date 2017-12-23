# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\a\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

# ---- PRESAGE - start

-dontnote io.presage.**
-dontwarn shared_presage.**
-dontwarn org.codehaus.**

-keepattributes Signature

-keep class shared_presage.** { *; }
-keep class io.presage.** { *; }
-keepclassmembers class io.presage.** {
 *;
}

-keepattributes *Annotation*
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ---- OKHTTP
-dontnote okhttp3.**
-dontnote okio.**
-dontwarn okhttp3.**
-dontwarn okio.**

-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

-dontnote sun.misc.Unsafe
-dontnote android.net.http.*

-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-dontwarn org.apache.commons.collections.BeanMap
-dontwarn java.beans.**

# ---- GOOGLE
-dontnote com.google.gson.**
-dontnote com.google.android.gms.ads.**
-dontnote com.google.android.**
-dontnote com.google.ads.**

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ---- PRESAGE - end
