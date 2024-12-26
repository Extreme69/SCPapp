package com.example.scpapp.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.scpapp.R
import com.example.scpapp.viewmodel.SCPDetailViewModel

class SCPDetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: SCPDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scpdetail)

        // Retrieve scp_id from intent
        val scpId = intent.getStringExtra("scp_id") ?: run {
            Toast.makeText(this, "SCP ID not provided!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize ViewModel
        detailViewModel = ViewModelProvider(this).get(SCPDetailViewModel::class.java)

        // Fetch and observe SCP details
        detailViewModel.fetchSCPDetails(scpId).observe(this, Observer { details ->
            if (details != null) {
                // Update UI with details
                findViewById<TextView>(R.id.textViewSCPTitle).text = details.title
                findViewById<TextView>(R.id.textViewSCPClassification).text =
                    "Classification: ${details.classification}"
                findViewById<TextView>(R.id.textViewSCPRating).text = "Rating: ${details.rating}"
                findViewById<TextView>(R.id.textViewSCPDescription).text = details.description

                /*
                // Load image using Glide
                val imageView = findViewById<ImageView>(R.id.imageViewSCP)
                Glide.with(this).load(details.photoUrl).into(imageView)
                */

                // Populate SCP Tales
                val talesLayout = findViewById<LinearLayout>(R.id.linearLayoutSCPTales)
                talesLayout.removeAllViews()
                details.scpTales.forEach { tale ->
                    val taleTextView = TextView(this).apply {
                        text = tale
                        textSize = 16f
                        setTextColor(resources.getColor(android.R.color.black, null))
                        setPadding(16, 8, 16, 8)
                    }
                    talesLayout.addView(taleTextView)
                }
            } else {
                // Handle error or show a message
                Toast.makeText(this, "Failed to load SCP details.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
