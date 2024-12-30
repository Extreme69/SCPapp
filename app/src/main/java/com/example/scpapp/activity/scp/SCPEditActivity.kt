package com.example.scpapp.activity.scp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.scpapp.R
import com.example.scpapp.databinding.ActivityScpeditBinding

class SCPEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScpeditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scpedit)

        binding = ActivityScpeditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scpId = intent.getStringExtra("scp_id")

        // Update the title text to include the SCP ID
        if (!scpId.isNullOrEmpty()) {
            binding.topBarTitle.text = "Editing $scpId"
        }

        // Back button click listener
        binding.buttonBack.setOnClickListener {
            showUnsavedChangesDialog()
        }

        // Handle system back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showUnsavedChangesDialog()
            }
        })
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Unsaved Changes")
            .setMessage("Do you want to discard the changes?")
            .setPositiveButton("Discard") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}