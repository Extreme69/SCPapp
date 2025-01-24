package com.example.scpapp.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.scpapp.databinding.ActivitySettingsBinding
import com.example.scpapp.utils.DialogUtils
import com.example.scpapp.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var originalUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe ViewModel LiveData
        observeViewModel()

        // Set up listeners
        binding.saveButton.setOnClickListener {
            val newUsername = binding.usernameInput.text.toString()

            if (newUsername.isEmpty()) {
                // Show error dialog if username is empty
                DialogUtils.showErrorDialog(this, "Username cannot be empty.")
                return@setOnClickListener
            }

            if (newUsername != originalUsername) {
                // Show confirmation dialog before saving
                DialogUtils.showSuccessDialog(this, "Save changes to username?") {
                    viewModel.updateUsername(newUsername)
                    viewModel.saveSettings()
                    Log.d("SettingsActivity", "Username saved: $newUsername")
                }
            } else {
                // Show error dialog if no changes were made
                DialogUtils.showErrorDialog(this, "No changes made to username.")
            }
        }

        binding.buttonBack.setOnClickListener {
            if (binding.usernameInput.text.toString() != originalUsername) {
                // Show unsaved changes dialog if username has been modified
                DialogUtils.showUnsavedChangesDialog(this) {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    private fun observeViewModel() {
        // Observe the username LiveData
        viewModel.username.observe(this) { username ->
            originalUsername = username
            binding.usernameInput.setText(username)
        }

        // Observe save result LiveData
        viewModel.saveSettingsResult.observe(this) { result ->
            result.onSuccess {
                // Show success dialog on save success
                DialogUtils.showSuccessDialog(this, "Username saved successfully.") {
                    finish() // Close the activity
                }
            }.onFailure { error ->
                // Show error dialog on save failure
                DialogUtils.showErrorDialog(
                    this,
                    error.localizedMessage ?: "An unknown error occurred."
                )
            }
        }
    }
}
