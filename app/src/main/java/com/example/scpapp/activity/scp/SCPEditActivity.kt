package com.example.scpapp.activity.scp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.scpapp.data.scp.SCP
import com.example.scpapp.data.scp.SCPUpdateRequest
import com.example.scpapp.databinding.ActivityScpeditBinding
import com.example.scpapp.utils.DialogUtils
import com.example.scpapp.viewmodel.scp.SCPEditViewModel

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
            DialogUtils.showUnsavedChangesDialog(this){
                finish()
            }
        }

        // Handle system back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                DialogUtils.showUnsavedChangesDialog(this@SCPEditActivity) {
                    finish()
                }
            }
        })

        // Delete button click listener
        binding.buttonDelete.setOnClickListener {
            if (!scpId.isNullOrEmpty()) {
                DialogUtils.showDeleteDialog(this, "Are you sure you want to delete this SCP?"){
                    viewModel.deleteSCP(scpId)
                }
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
            errorMessage?.let { DialogUtils.showErrorDialog(this, it) }
        }

        // Observe save result
        viewModel.saveSCPResult.observe(this) { result ->
            result.onSuccess {
                DialogUtils.showSuccessDialog(this, "SCP updated successfully.") {
                    finish()
                }
            }.onFailure { error ->
                DialogUtils.showErrorDialog(this, error.localizedMessage ?: "An unknown error occurred")
            }
        }

        // Observe delete result
        viewModel.deleteSCPResult.observe(this) { result ->
            result.onSuccess {
                DialogUtils.showSuccessDialog(this, "SCP deleted successfully.") {
                    finish()
                }
            }.onFailure { error ->
                DialogUtils.showErrorDialog(this, error.localizedMessage ?: "An unknown error occurred")
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
}
