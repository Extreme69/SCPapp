package com.example.scpapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private var isReady = false  // To control when the splash screen can be dismissed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install the splash screen
        val splashScreen = installSplashScreen()

        // Keep the splash screen visible until isReady is true
        splashScreen.setKeepOnScreenCondition { !isReady }

        // Perform async work (for example, user login check or initializations)
        CoroutineScope(Dispatchers.Main).launch {
            initialize()
        }
    }

    private suspend fun initialize() {
        // Perform any necessary initialization work, e.g., network checks, database setup, etc.
        // Simulating a 2-second delay for initialization
        kotlinx.coroutines.delay(2000)

        // Mark the splash screen as ready to transition
        isReady = true

        // After initialization, navigate to the main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Close SplashActivity
    }
}
