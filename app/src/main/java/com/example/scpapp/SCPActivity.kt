package com.example.scpapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
            (binding.recyclerViewSCPs.adapter as SCPAdapter).submitList(scpList)
        }

        // Fetch SCPs when activity starts
        lifecycleScope.launch {
            viewModel.fetchSCPs()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSCPs.apply {
            layoutManager = LinearLayoutManager(this@SCPActivity)
            adapter = SCPAdapter()
        }
    }
}
