package com.example.scpapp.activity.tale

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.scpapp.databinding.ActivityTaleAddBinding
import com.example.scpapp.utils.setButtonColors
import com.example.scpapp.viewmodel.tale.TaleAddViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaleAddBinding
    private val viewModel: TaleAddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaleAddBinding.inflate(layoutInflater)
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
            // Get input values
            val title = binding.taleTitleEditText.text.toString()
            val url = binding.urlEditText.text.toString()
            val content = binding.taleContentEditText.text.toString()

            // Collect all scp_id values from the ChipGroup
            val scpIdList = mutableListOf<String>()
            for (i in 0 until binding.relatedScpsChipGroup.childCount) {
                val chip = binding.relatedScpsChipGroup.getChildAt(i) as Chip
                scpIdList.add(chip.text.toString())
            }

            // Validate and save the tale with scp_ids
            if (viewModel.validateInputs(title, url, content)) {
                viewModel.saveTale(title, url, content, scpIdList)  // pass the scpIdList to the ViewModel
            }
        }

        binding.addSCPEditText.setOnEditorActionListener { v, actionId, event ->
            val scpId = binding.addSCPEditText.text.toString().trim()
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
    }

    private fun observeViewModel() {
        viewModel.validationError.observe(this) { errorMessage ->
            errorMessage?.let { showErrorDialog(it) }
        }

        viewModel.saveTaleResult.observe(this) { result ->
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
            .setMessage("Tale created successfully.")
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