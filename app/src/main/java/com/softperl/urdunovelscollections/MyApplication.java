package com.softperl.urdunovelscollections;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import androidx.multidex.MultiDex;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OneSignal;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.Utility.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApplication extends Application {

    private static MyApplication mInstance;
    Context context;

    public MyApplication() {
        mInstance = this;
    }

    public MyApplication(Context context) {
        this.context = context;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
//    private static final String ONESIGNAL_APP_ID = "c90d5b44-53eb-4161-a5cb-632d2f316f66";
    @Override
    public void onCreate() {
        super.onCreate();


        Thread.setDefaultUncaughtExceptionHandler(new ThreadHandeling());

     //   FacebookSdk.sdkInitialize(getApplicationContext());

        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(this);

        PrintKeyHash();

// Enable verbose OneSignal logging to debug issues if needed.
       // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.ONESIGNAL_APP_ID));

        //Initialize One Signal OLD Method

//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();




        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
        //PRDownloader.cleanUp(25);

        mInstance = this;


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void PrintKeyHash() {

        if (!Constant.Credentials.isFacebookHashKeyRequired) {
            return;
        }

        Utility.LoggerForImportantMessages("Hash", "Generating for fb.....");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Utility.LoggerForImportantMessages("Hashkey", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}

