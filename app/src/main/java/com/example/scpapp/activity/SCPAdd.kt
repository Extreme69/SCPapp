package com.example.scpapp.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.scpapp.databinding.ActivityScpaddBinding
import com.example.scpapp.viewmodel.SCPAddViewModel

class SCPAdd : AppCompatActivity() {
    private lateinit var binding: ActivityScpaddBinding
    private val viewModel: SCPAddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScpaddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the back button in the UI
        binding.buttonBack.setOnClickListener {
            showUnsavedChangesDialog()
        }

        // Handle system back button with OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showUnsavedChangesDialog()
            }
        })

        // Observe the classification types LiveData
        viewModel.classificationTypes.observe(this) { _ ->
            // Get the adapter from ViewModel and set it to the spinner
            val adapter = viewModel.getClassificationAdapter(this)
            binding.classificationSpinner.adapter = adapter
        }
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Unsaved Changes")
            .setMessage("Do you want to discard the changes?")
            .setPositiveButton("Discard") { _, _ ->
                finish() // Closes the activity and navigates back
            }
            .setNegativeButton("Cancel", null) // Dismiss the dialog
            .create()
            .show()
    }
}
