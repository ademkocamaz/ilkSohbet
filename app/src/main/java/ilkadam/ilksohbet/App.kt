package ilkadam.ilksohbet

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import com.onesignal.OneSignalNotificationManager
import com.onesignal.OneSignalRemoteParams
import dagger.hilt.android.HiltAndroidApp
import ilkadam.ilksohbet.core.Constants

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
    }

}