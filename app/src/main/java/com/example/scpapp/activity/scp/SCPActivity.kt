package com.example.scpapp.activity.scp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scpapp.adapter.SCPAdapter
import com.example.scpapp.viewmodel.SCPViewModel
import com.example.scpapp.databinding.ActivityScpactivityBinding
import kotlinx.coroutines.launch

class SCPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScpactivityBinding
    private val viewModel: SCPViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observe ViewModel for SCP list
        viewModel.scps.observe(this) { scpList ->
            Log.d("SCPActivity", "Observed SCP list: ${scpList.size}")
            (binding.recyclerViewSCPs.adapter as SCPAdapter).submitList(scpList)
        }

        // Fetch SCPs when activity starts
        lifecycleScope.launch {
            viewModel.fetchSCPs()
        }

        // Set up the back button
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Navigate back to the previous activity
        }

        // Set up the add button to open SCPAdd activity
        binding.buttonScp.setOnClickListener {
            val intent = Intent(this, SCPAdd::class.java)
            startActivity(intent) // Navigate to the SCPAdd activity
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSCPs.apply {
            layoutManager = LinearLayoutManager(this@SCPActivity)
            adapter = SCPAdapter { selectedSCP ->
                navigateToDetail(selectedSCP.id)
            }
        }
    }

    private fun navigateToDetail(scpId: String) {
        val intent = Intent(this, SCPDetailActivity::class.java).apply {
            putExtra("scp_id", scpId)
        }
        startActivity(intent)
    }
}