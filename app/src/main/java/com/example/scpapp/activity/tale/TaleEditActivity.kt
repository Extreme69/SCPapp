package com.example.scpapp.activity.tale

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.scpapp.data.scp.TaleUpdateRequest
import com.example.scpapp.data.tale.Tale
import com.example.scpapp.databinding.ActivityTaleEditBinding
import com.example.scpapp.utils.DialogUtils
import com.example.scpapp.utils.DialogUtils.setButtonColors
import com.example.scpapp.viewmodel.tale.TaleEditViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaleEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaleEditBinding
    private val viewModel: TaleEditViewModel by viewModels()

    private lateinit var originalTitle: String
    private lateinit var originalContent: String
    private lateinit var originalUrl: String
    private lateinit var originalscp_id: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaleEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taleId = intent.getStringExtra("tale_id")

        // Back button click listener
        binding.buttonBack.setOnClickListener {
            DialogUtils.showUnsavedChangesDialog(this){
                finish()
            }
        }

        // Handle system back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                DialogUtils.showUnsavedChangesDialog(this@TaleEditActivity) {
                    finish()
                }
            }
        })

        // Save button click listener
        binding.buttonSave.setOnClickListener {
            val title = binding.taleTitleEditText.text.toString()
            val url = binding.urlEditText.text.toString()
            val content = binding.taleContentEditText.text.toString()
            val scp_id = mutableListOf<String>()
            for (i in 0 until binding.relatedScpsChipGroup.childCount) {
                val chip = binding.relatedScpsChipGroup.getChildAt(i) as Chip
                scp_id.add(chip.text.toString())
            }

            if (viewModel.validateInputs(title, url, content)) {
                // Create an TaleUpdateRequest instance with only the updated fields
                val updatedFields = TaleUpdateRequest(
                    title = if (title != originalTitle) title else null,
                    url = if (url != originalUrl) url else null,
                    content = if (content != originalContent) content else null,
                    scp_id = if(scp_id != originalscp_id) scp_id else null
                )

                // Call ViewModel to send only the changed fields in the PUT request
                if (updatedFields != TaleUpdateRequest()) {  // Ensure there are some updated fields
                    if (taleId != null) {
                        viewModel.saveSCP(taleId, updatedFields)
                    }
                } else {
                    Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.addSCPEditText.setOnEditorActionListener { v, actionId, event ->
            val scpId = binding.addSCPEditText.text.toString().trim().uppercase()
            if (scpId.isNotEmpty()) {
                val chip = Chip(this).apply {
                    text = scpId
                    isCloseIconVisible = true
                    setOnCloseIconClickListener {
                        binding.relatedScpsChipGroup.removeView(this)
                    }
                }
                binding.relatedScpsChipGroup.addView(chip)
                binding.addSCPEditText.text?.clear()
            }
            true
        }

        // Delete button click listener
        binding.buttonDelete.setOnClickListener {
            if (!taleId.isNullOrEmpty()) {
                DialogUtils.showDeleteDialog(this, "Are you sure you want to delete this tale?"){
                    viewModel.deleteTale(taleId)
                }
            } else {
                Toast.makeText(this, "Tale ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()

        // Fetch and observe SCP details
        if (!taleId.isNullOrEmpty()) {
            viewModel.fetchTaleDetails(taleId).observe(this, Observer { details ->
                if (details != null) {
                    populateFields(details)
                } else {
                    // Handle error or show a message
                    Toast.makeText(this, "Failed to load tale details.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.validationError.observe(this) { errorMessage ->
            errorMessage?.let { DialogUtils.showErrorDialog(this, it) }
        }

        // Observe save result
        viewModel.saveTaleResult.observe(this) { result ->
            result.onSuccess {
                DialogUtils.showSuccessDialog(this, "Tale updated successfully.") {
                    finish()
                }
            }.onFailure { error ->
                DialogUtils.showErrorDialog(this, error.localizedMessage ?: "An unknown error occurred")
            }
        }

        // Observe delete result
        viewModel.deleteTaleResult.observe(this) { result ->
            result.onSuccess {
                DialogUtils.showSuccessDialog(this, "Tale deleted successfully.") {
                    finish()
                }
            }.onFailure { error ->
                DialogUtils.showErrorDialog(this, error.localizedMessage ?: "An unknown error occurred")
            }
        }
    }

    private fun populateFields(details: Tale) {
        // Store the original values
        originalTitle = details.title
        originalContent = details.content
        originalUrl = details.url
        originalscp_id = details.scpId

        binding.taleTitleEditText.setText(originalTitle)
        binding.urlEditText.setText(originalUrl)
        binding.taleContentEditText.setText(originalContent)

        // Clear existing chips in the ChipGroup
        binding.relatedScpsChipGroup.removeAllViews()

        // Iterate through the list of scp_ids and add them as chips
        originalscp_id.forEach { scpId ->
            val chip = Chip(this).apply {
                text = scpId  // Set the SCP ID as the text for the chip
                isCloseIconVisible = true  // Optional: Show a close icon for removal
                setOnCloseIconClickListener {
                    binding.relatedScpsChipGroup.removeView(this)  // Remove the chip when the close icon is clicked
                }
            }
            // Add the chip to the ChipGroup
            binding.relatedScpsChipGroup.addView(chip)
        }
    }
}
