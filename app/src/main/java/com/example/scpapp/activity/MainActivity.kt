package com.example.scpapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import com.example.scpapp.R
import com.example.scpapp.activity.scp.SCPActivity
import com.example.scpapp.activity.tale.TalesActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            false
        }

        super.onCreate(savedInstanceState)
        // Set content view to the XML layout
        setContentView(R.layout.activity_main)

        // Initialize buttons
        val scpButton: ImageButton = findViewById(R.id.button_scp)
        val talesButton: ImageButton = findViewById(R.id.button_tales)
        val settingsButton: ImageButton = findViewById(R.id.button_settings)

        // Set click listeners
        scpButton.setOnClickListener {
            // Navigate to SCP screen
            startActivity(Intent(this, SCPActivity::class.java))
        }

        talesButton.setOnClickListener {
            // Navigate to Tales screen
            startActivity(Intent(this, TalesActivity::class.java))
        }

        settingsButton.setOnClickListener {
            // Navigate to Settings screen
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
