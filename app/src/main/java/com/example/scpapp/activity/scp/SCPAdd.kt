package com.example.scpapp.activity.scp

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.scpapp.databinding.ActivityScpaddBinding
import com.example.scpapp.utils.setButtonColors
import com.example.scpapp.viewmodel.scp.SCPAddViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SCPAdd : AppCompatActivity() {
    private lateinit var binding: ActivityScpaddBinding
    private val viewModel: SCPAddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScpaddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
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

        // Save button click listener
        binding.buttonSave.setOnClickListener {
            val id = binding.scpIdEditText.text.toString()
            val title = binding.scpTitleEditText.text.toString()
            val classification = binding.classificationSpinner.selectedItem.toString()
            val url = binding.urlEditText.text.toString()
            val description = binding.scpDescriptionEditText.text.toString()

            if (viewModel.validateInputs(id, title, classification, url, description)) {
                viewModel.saveSCP(id, title, classification, url, description)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.classificationTypes.observe(this) { classifications ->
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                classifications
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.classificationSpinner.adapter = adapter
        }

        viewModel.validationError.observe(this) { errorMessage ->
            errorMessage?.let { showErrorDialog(it) }
        }

        viewModel.saveSCPResult.observe(this) { result ->
            result.onSuccess {
                showSuccessDialog()
            }.onFailure { error ->
                showErrorDialog(error.localizedMessage ?: "An unknown error occurred")
            }
        }
    }

    private fun showUnsavedChangesDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Unsaved Changes")
            .setMessage("Do you want to discard the changes?")
            .setPositiveButton("Discard") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setButtonColors()
        dialog.show()
    }

    private fun showSuccessDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Success")
            .setMessage("SCP created successfully.")
            .setPositiveButton("OK") { _, _ -> finish() }
            .create()

        dialog.setButtonColors()
        dialog.show()
    }

    private fun showErrorDialog(message: String) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()

        dialog.setButtonColors()
        dialog.show()
    }
}
