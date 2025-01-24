package com.example.scpapp.activity.scp

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.scpapp.SharedPreferencesRepository
import com.example.scpapp.api.SCPRepository
import com.example.scpapp.databinding.ActivityScpaddBinding
import com.example.scpapp.utils.DialogUtils
import com.example.scpapp.viewmodel.scp.SCPAddViewModel

class SCPAdd : AppCompatActivity() {
    private lateinit var binding: ActivityScpaddBinding
    private lateinit var viewModel: SCPAddViewModel // No longer using `by viewModels()`

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the ViewModel with the repository
        val sharedPreferencesRepository = SharedPreferencesRepository(this)
        viewModel = SCPAddViewModel(
            repository = SCPRepository(),
            sharedPreferencesRepository = sharedPreferencesRepository
        )

        binding = ActivityScpaddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Back button click listener
        binding.buttonBack.setOnClickListener {
            DialogUtils.showUnsavedChangesDialog(this) {
                finish()
            }
        }

        // Handle system back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                DialogUtils.showUnsavedChangesDialog(this@SCPAdd) {
                    finish()
                }
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
            errorMessage?.let { DialogUtils.showErrorDialog(this, it) }
        }

        viewModel.saveSCPResult.observe(this) { result ->
            result.onSuccess {
                DialogUtils.showSuccessDialog(this, "SCP created successfully.") {
                    finish()
                }
            }.onFailure { error ->
                DialogUtils.showErrorDialog(this, error.localizedMessage ?: "An unknown error occurred")
            }
        }
    }
}
