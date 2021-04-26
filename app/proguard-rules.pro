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

# Ignore warnings for SLF4J (for ex. the missing getSingleton() program class member warning)
-dontwarn org.slf4j.**

# Ignore warnings for Logback (for ex. the missing javax.mail.* classes warning)
-dontwarn ch.qos.**

# Ignore warnings for the jsr166e backport (the missing sun.misc.Unsafe class is handled at runtime)
-dontwarn jersey.repackaged.jsr166e.**
# Don't obfuscate the jsr166e backport
-keep class jersey.repackaged.jsr166e.** { *; }

# Ignore warnings for Commons Imaging (it is safe to ignore the missing java.awt.* warnings)
-dontwarn org.apache.commons.imaging.**

# Required to prevent the "Ignoring InnerClasses attribute for an anonymous inner class (net.foo.bar)
# that doesn't come with an associated EnclosingMethod attribute." warning
-keepattributes EnclosingMethod