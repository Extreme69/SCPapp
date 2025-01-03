package com.example.scpapp.activity.scp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scpapp.adapter.SCPAdapter
import com.example.scpapp.viewmodel.scp.SCPViewModel
import com.example.scpapp.databinding.ActivityScpactivityBinding
import android.text.Editable
import android.text.TextWatcher

class SCPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScpactivityBinding
    private val viewModel: SCPViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper()) // Handler to run code on the main thread
    private var searchRunnable: Runnable? = null // To keep track of the delayed search request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observe the scps LiveData for all SCPs
        viewModel.scps.observe(this) { scpList ->
            Log.d("SCPActivity", "Observed SCP list: ${scpList.size}")
            (binding.recyclerViewSCPs.adapter as SCPAdapter).submitList(scpList)
        }

        // Observe the searchResults LiveData for search results
        viewModel.searchResults.observe(this) { searchResults ->
            Log.d("SCPActivity", "Observed search results: ${searchResults.size}")
            (binding.recyclerViewSCPs.adapter as SCPAdapter).submitList(searchResults)
        }

        // Set up the search bar to trigger the search
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed for before text change
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed for on text change
            }

            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString()

                // Cancel any pending search request if user types again
                searchRunnable?.let { handler.removeCallbacks(it) }

                // Delay the search until the user stops typing
                searchRunnable = Runnable {
                    if (query.isNotEmpty()) {
                        viewModel.searchSCPs(query) // Trigger search in ViewModel
                    } else {
                        // If search bar is empty, fetch all SCPs again
                        viewModel.fetchSCPs()
                    }
                }

                // Delay the search by 500ms
                handler.postDelayed(searchRunnable!!, 500)
            }
        })

        // Set up the back button
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Navigate back to the previous activity
        }

        // Set up the add button to open SCPAdd activity
        binding.buttonAddScp.setOnClickListener {
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
