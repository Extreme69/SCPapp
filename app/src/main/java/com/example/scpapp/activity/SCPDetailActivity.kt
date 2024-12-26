package com.example.scpapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scpapp.R
import com.example.scpapp.viewmodel.SCPDetailViewModel

class SCPDetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: SCPDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scpdetail)

        // Set up the back button
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

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

                // Set Classification Image
                val classificationImageView = findViewById<ImageView>(R.id.imageViewSCPClassification)
                val classificationImageRes = when (details.classification.lowercase()) {
                    "keter" -> R.drawable.ic_scp_keter
                    "euclid" -> R.drawable.ic_scp_euclid
                    "safe" -> R.drawable.ic_scp_safe
                    "apollyon" -> R.drawable.ic_scp_apollyon
                    "decommissioned" -> R.drawable.ic_scp_decommissioned
                    "explained" -> R.drawable.ic_scp_explained
                    "neutralized" -> R.drawable.ic_scp_neutralized
                    "pending" -> R.drawable.ic_scp_pending
                    "thaumiel" -> R.drawable.ic_scp_thaumiel
                    "ticonderogal" -> R.drawable.ic_scp_ticonderogal
                    "archon" -> R.drawable.ic_scp_archon
                    else -> Log.d("SCPDetailActivity", "No image for this classification: ${details.classification}")
                }
                classificationImageView.setImageResource(classificationImageRes)

                // Set classification text
                findViewById<TextView>(R.id.textViewSCPClassification).text = details.classification

                val ratingArrowView = findViewById<ImageView>(R.id.imageViewSCPRatingArrow)
                val ratingTextView = findViewById<TextView>(R.id.textViewSCPRating)

                if (details.rating >= 0) {
                    ratingArrowView.setImageResource(R.drawable.ic_up_arrow)
                } else {
                    ratingArrowView.setImageResource(R.drawable.ic_down_arrow)
                }
                ratingTextView.text = details.rating.toString()

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

                // Set up SCP Wiki Link
                val wikiLinkTextView = findViewById<TextView>(R.id.textViewSCPWikiLink)
                wikiLinkTextView.text = "Go to SCP Wiki"
                wikiLinkTextView.setOnClickListener {
                    val url = details.url // Assuming details.url contains the API-provided URL
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                    startActivity(intent)
                }

            } else {
                // Handle error or show a message
                Toast.makeText(this, "Failed to load SCP details.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
