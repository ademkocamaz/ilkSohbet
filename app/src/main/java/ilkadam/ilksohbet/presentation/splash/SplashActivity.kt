package ilkadam.ilksohbet.presentation.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ilkadam.ilksohbet.MainActivity

class SplashActivity : Activity() {
    //val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition {
            true
            //splashViewModel.isSplashShow.value
        }
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}