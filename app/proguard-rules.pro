# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/apple/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-dontwarn com.tendcloud.**

#保持com.umeng.**这个包里面的所有类和所有方法不被混淆。(没有友盟的集成时删除此句)
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keep public class org.codehaus.**
-keep public class java.nio.**

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {  # 保持自定义控件类不被混淆
    public void *(android.view.View);
}

-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
 public static final android.os.Parcelable$Creator *;
}
#友盟
-keep public class com.haitao.R$*{
public static final int *;
}
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
 }
-dontwarn com.umeng.**
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keepattributes InnerClasses
-keep class **.R$* {
    <fields>;
}

-dontwarn android.webkit.WebView
-keep public class android.webkit.**


#talkingdata
-keep public class com.tendcloud.tenddata.** { public protected *;}


-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class cn.sharesdk.**{*;}
 -keep class com.sina.**{*;}
 -keep class **.R$* {*;}
 -keep class **.R{*;}
 -dontwarn cn.sharesdk.**
 -dontwarn **.R$*
 -dontwarn org.support.v4.**
 -keepclassmembers class * {
     void onEvent*(**);
 }
 -keep class com.haitao.wxapi.** {*;}
## ----------------------------------
##   ########## jpush混淆    ##########
## ----------------------------------
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
## ----------------------------------
##   ########## gson && protobuf混淆    ##########
## ----------------------------------
#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}


-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**

-dontwarn org.apache.http.**
-dontwarn org.apache.**

-keep class com.alibaba.fastjson.** { *; }
-keepattributes Signature

-keep class com.haitao.model.** {*;}
-keep class com.haitao.db.** {*;}
-keep class com.haitao.view.**{*;}
-keep class com.haitao.framework.codec.**{*;}
-keep class com.shantao.model.** {*;}
-keep class com.shantao.db.** {*;}
-keep class com.haitao.utils.verifycode.** {*;}


#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
#-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*

-keepattributes Signature
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.app.Notification
-dontwarn com.squareup.**
-dontwarn okio.**


-keep class org.jsoup.**

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
# Okio
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn rx.**

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keep  class com.haitao.activity.TopicDetailActivity{
    public <methods>;
}
 -keep class com.haitao.activity.TopicDetailActivity$JavascriptInterface {
    public <methods>;
 }
-keep class com.haitao.activity.TopicDetailActivity$JavascriptInterface {
    *;
}

-keep class android.webkit.JavascriptInterface {*;}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
 }

-keep class com.facebook.** { *; }
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**




-keep class io.swagger.client.model.** {*;}

## ----------------------------------
##   ########## Gson混淆    ##########
## ----------------------------------
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.Android.model.** { *; }
-keep class com.google.**{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# # -------------------------------------------
# #  ############### volley混淆  ###############
# # -------------------------------------------
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }



# # -------------------------------------------
# #  ############### litepal数据库  ###############
# # -------------------------------------------
-dontwarn org.litepal.*
-keep class org.litepal.** { *; }
-keep enum org.litepal.**
-keep interface org.litepal.** { *; }
-keep public class * extends org.litepal.**
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keepclassmembers class * extends org.litepal.crud.DataSupport{*;}


# # -------------------------------------------
# #  ############### 小能客服  ###############
# # -------------------------------------------

-dontwarn cn.xiaoneng.**
-dontwarn android.support.v4xn.**
-dontwarn orgxn.fusesource.**
-keep class cn.xiaoneng.** {*;}
-keep class android.support.v4xn.** {*;}
-keep class orgxn.fusesource.** {*;}


# # -------------------------------------------
# #  ############### eventbus  ###############
# # -------------------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# # -------------------------------------------
# #  ############### talkingdata app  ###############
# # -------------------------------------------

-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**


-keep class org.apache.** {*;}


# # -------------------------------------------
# #  ############### 魔窗  ###############
# # -------------------------------------------
-keep class com.tencent.mm.sdk.** {*;}
-keep class cn.magicwindow.** {*;}
-dontwarn cn.magicwindow.**


# # -------------------------------------------
# #  ############### Bugly  ###############
# # -------------------------------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

## ----------------------------------
##      RxJava
## ----------------------------------
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# # -------------------------------------------
# #  ############### 阿里hotfix  ###############
# # -------------------------------------------
#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/buidl/outputs/mapping/release路径下，移动到/app路径下

#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt

#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.ut.device.**{*;}
-dontwarn  com.taobao.sophix.**
-dontwarn  com.ta.utdid2.device.**
-dontwarn  com.ut.device.**


# # -------------------------------------------
# #  ############### ShareSDK  ###############
# # -------------------------------------------
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*


# # -------------------------------------------
# #  ############### fresco  ###############
# # -------------------------------------------

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**



# retrolambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**