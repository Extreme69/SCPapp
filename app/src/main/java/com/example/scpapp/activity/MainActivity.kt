package com.example.scpapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import com.example.scpapp.R
import com.example.scpapp.activity.scp.SCPActivity
import com.example.scpapp.activity.tale.TalesActivity
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ActivityOptions
import android.view.View
import android.view.ViewAnimationUtils

class MainActivity : AppCompatActivity() {
    private lateinit var colorOverlay: View
    private var shouldPlayReverseAnimation = false
    private var triggeringButtonId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize overlay
        colorOverlay = findViewById(R.id.color_overlay)

        // Initialize buttons
        val scpButton: ImageButton = findViewById(R.id.button_scp)
        val talesButton: ImageButton = findViewById(R.id.button_tales)
        val settingsButton: ImageButton = findViewById(R.id.button_settings)

        // Set click listeners
        scpButton.setOnClickListener {
            triggeringButtonId = it.id
            animateColorOverlay(it) {
                navigateToActivity(SCPActivity::class.java)
            }
        }

        talesButton.setOnClickListener {
            triggeringButtonId = it.id
            animateColorOverlay(it) {
                navigateToActivity(TalesActivity::class.java)
            }
        }

        settingsButton.setOnClickListener {
            triggeringButtonId = it.id
            animateColorOverlay(it) {
                navigateToActivity(SettingsActivity::class.java)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (shouldPlayReverseAnimation) {
            triggeringButtonId?.let { buttonId ->
                val button = findViewById<View>(buttonId)
                animateReverseColorOverlay(button)
            }
            shouldPlayReverseAnimation = false
        }
    }

    private fun animateColorOverlay(button: View, onAnimationEnd: () -> Unit) {
        colorOverlay.post {
            // Ensure layout is complete
            colorOverlay.requestLayout()
            if (colorOverlay.visibility != View.VISIBLE) {
                colorOverlay.visibility = View.VISIBLE
            }

            // Calculate center of the button
            val buttonX = (button.x + button.width / 2).toInt()
            val buttonY = (button.y + button.height / 2).toInt()

            // Calculate the final radius
            val finalRadius = Math.hypot(colorOverlay.width.toDouble(), colorOverlay.height.toDouble()).toFloat()

            // Perform the circular reveal animation
            val anim = ViewAnimationUtils.createCircularReveal(colorOverlay, buttonX, buttonY, 0f, finalRadius)
            anim.duration = 600

            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    onAnimationEnd()
                }
            })

            anim.start()
        }
    }

    private fun animateReverseColorOverlay(button: View) {
        // Ensure the overlay is measured and visible before animation
        colorOverlay.post {
            // Get button position
            val buttonX = (button.x + button.width / 2).toInt()
            val buttonY = (button.y + button.height / 2).toInt()

            // Calculate the final radius
            val finalRadius = Math.hypot(colorOverlay.width.toDouble(), colorOverlay.height.toDouble()).toFloat()

            // Perform the reverse circular reveal animation
            val anim = ViewAnimationUtils.createCircularReveal(colorOverlay, buttonX, buttonY, finalRadius, 0f)
            anim.duration = 600

            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // Hide the overlay when the reverse animation ends
                    colorOverlay.visibility = View.GONE
                }
            })

            anim.start()
        }
    }

    private fun navigateToActivity(targetActivity: Class<*>) {
        shouldPlayReverseAnimation = true
        val intent = Intent(this, targetActivity).apply {
            putExtra("triggering_button_id", triggeringButtonId)
        }

        // Use ActivityOptions for custom transitions
        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
        startActivity(intent, options.toBundle())
    }
}
