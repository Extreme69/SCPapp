package com.example.scpapp.activity.tale

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scpapp.adapter.TaleAdapter
import com.example.scpapp.databinding.ActivityTalesBinding
import com.example.scpapp.viewmodel.tale.TalesViewModel

class TalesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTalesBinding
    private val viewModel: TalesViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper()) // Handler to run code on the main thread
    private var searchRunnable: Runnable? = null // To keep track of the delayed search request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observe the tales LiveData for all Tales
        viewModel.tales.observe(this) { taleList ->
            Log.d("TalesActivity", "Observed Tale list: ${taleList.size}")
            (binding.recyclerViewTales.adapter as TaleAdapter).submitList(taleList)
        }

        // Observe the searchResults LiveData for search results
        viewModel.searchResults.observe(this) { searchResults ->
            Log.d("SCPActivity", "Observed search results: ${searchResults.size}")
            (binding.recyclerViewTales.adapter as TaleAdapter).submitList(searchResults)
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
                        viewModel.searchTales(query) // Trigger search in ViewModel
                    } else {
                        // If search bar is empty, fetch all tales again
                        viewModel.fetchTales()
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

        // Set up the add button to open TaleAdd activity
        binding.buttonAddTale.setOnClickListener {
            val intent = Intent(this, TaleAddActivity::class.java)
            startActivity(intent) // Navigate to the TaleAddActivity
        }
    }

    // Override onResume to reload the tales when the activity comes back into view
    override fun onResume() {
        super.onResume()
        // Refresh the list of tales when the activity is resumed
        viewModel.fetchTales() // Fetch all tales again when returning to the activity
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTales.apply {
            layoutManager = LinearLayoutManager(this@TalesActivity)
            adapter = TaleAdapter { selectedTale ->
                navigateToDetail(selectedTale.id)
            }
        }
    }

    private fun navigateToDetail(taleId: String) {
        val intent = Intent(this, TaleDetailActivity::class.java).apply {
            putExtra("tale_id", taleId)
        }
        startActivity(intent)
    }
}
