package com.example.scpapp.activity.scp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.scpapp.data.SCP
import com.example.scpapp.data.SCPUpdateRequest
import com.example.scpapp.databinding.ActivityScpeditBinding
import com.example.scpapp.viewmodel.SCPEditViewModel

class SCPEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScpeditBinding
    private val viewModel: SCPEditViewModel by viewModels()

    private lateinit var originalTitle: String
    private lateinit var originalClassification: String
    private lateinit var originalUrl: String
    private lateinit var originalDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScpeditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

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

        // Delete button click listener
        binding.buttonDelete.setOnClickListener {
            if (!scpId.isNullOrEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Delete SCP")
                    .setMessage("Are you sure you want to delete this SCP?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteSCP(scpId)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show()
            } else {
                Toast.makeText(this, "SCP ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        // Save button click listener
        binding.buttonSave.setOnClickListener {
            val title = binding.scpTitleEditText.text.toString()
            val classification = binding.classificationSpinner.selectedItem.toString()
            val url = binding.urlEditText.text.toString()
            val description = binding.scpDescriptionEditText.text.toString()

            if (viewModel.validateInputs(title, classification, url, description)) {
                // Create an SCPUpdateRequest instance with only the updated fields
                val updatedFields = SCPUpdateRequest(
                    title = if (title != originalTitle) title else null,
                    classification = if (classification != originalClassification) classification else null,
                    url = if (url != originalUrl) url else null,
                    description = if (description != originalDescription) description else null
                )

                // Call ViewModel to send only the changed fields in the PUT request
                if (updatedFields != SCPUpdateRequest()) {  // Ensure there are some updated fields
                    if (scpId != null) {
                        viewModel.saveSCP(scpId, updatedFields)  // Pass the updatedFields as SCPUpdateRequest
                    }
                } else {
                    Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                }
            }
        }

        observeViewModel()

        // Fetch and observe SCP details
        if (!scpId.isNullOrEmpty()) {
            viewModel.fetchSCPDetails(scpId).observe(this, Observer { details ->
                if (details != null) {
                    populateFields(details)
                } else {
                    // Handle error or show a message
                    Toast.makeText(this, "Failed to load SCP details.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun observeViewModel() {
        // Observe classification types and validation errors
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

        // Observe save result
        viewModel.saveSCPResult.observe(this) { result ->
            result.onSuccess {
                showSuccessDialogSave()
            }.onFailure { error ->
                showErrorDialog(error.localizedMessage ?: "An unknown error occurred")
            }
        }

        // Observe delete result
        viewModel.deleteSCPResult.observe(this) { result ->
            result.onSuccess {
                showSuccessDialogDelete()
            }.onFailure { error ->
                showErrorDialog(error.localizedMessage ?: "An unknown error occurred")
            }
        }
    }

    private fun populateFields(details: SCP) {
        // Store the original values
        originalTitle = details.title
        originalClassification = details.classification
        originalUrl = details.url
        originalDescription = details.description

        binding.scpTitleEditText.setText(originalTitle)
        binding.classificationSpinner.setSelection(getClassificationIndex(originalClassification))
        binding.urlEditText.setText(originalUrl)
        binding.scpDescriptionEditText.setText(originalDescription)
    }

    private fun getClassificationIndex(classification: String): Int {
        val classifications = viewModel.classificationTypes.value ?: return 0
        return classifications.indexOf(classification).takeIf { it >= 0 } ?: 0
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

    private fun showSuccessDialogSave() {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("SCP updated successfully.")
            .setPositiveButton("OK") { _, _ -> finish() }
            .create()
            .show()
    }

    private fun showSuccessDialogDelete() {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("SCP deleted successfully.")
            .setPositiveButton("OK") { _, _ -> finish() }
            .create()
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }
}
