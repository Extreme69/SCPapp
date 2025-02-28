package com.example.scpapp.activity.scp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.scpapp.R
import com.example.scpapp.activity.tale.TaleDetailActivity
import com.example.scpapp.data.scp.SCP
import com.example.scpapp.viewmodel.scp.SCPDetailViewModel

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

        // Set up the back button
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set up the edit button
        findViewById<ImageButton>(R.id.button_scp).setOnClickListener {
            val intent = Intent(this, SCPEditActivity::class.java).apply {
                putExtra("scp_id", scpId) // Pass SCP ID to the edit activity
            }
            startActivity(intent)
        }

        // Initialize ViewModel
        detailViewModel = ViewModelProvider(this).get(SCPDetailViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        val scpId = intent.getStringExtra("scp_id")
        if (scpId != null) {
            fetchAndDisplayDetails(scpId) // Refresh details when the activity is resumed
        }
    }

    private fun fetchAndDisplayDetails(scpId: String) {
        detailViewModel.fetchSCPDetails(scpId).observe(this) { details ->
            if (details != null) {
                updateUI(details)
            } else {
                Toast.makeText(this, "Failed to load SCP details.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun updateUI(details: SCP) {
        findViewById<TextView>(R.id.textViewSCPTitle).text = "${details.id}: ${details.title}"

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
            "ticonderoga" -> R.drawable.ic_scp_ticonderoga
            "archon" -> R.drawable.ic_scp_archon
            "uncontained" -> R.drawable.ic_scp_uncontained
            else -> {
                Log.d("SCPDetailActivity", "No image for this classification: ${details.classification}")
                0 // Default value, no image
            }
        }
        if (classificationImageRes != 0) classificationImageView.setImageResource(classificationImageRes)

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

        // Populate SCP Tales
        val talesLayout = findViewById<LinearLayout>(R.id.linearLayoutSCPTales)
        talesLayout.removeAllViews()
        details.scpTales.forEach { taleId ->
            detailViewModel.fetchTaleDetails(taleId).observe(this) { taleDetails ->
                if (taleDetails != null) {
                    val taleButton = Button(this).apply {
                        text = taleDetails.title
                        textSize = 18f
                        setTextColor(resources.getColor(android.R.color.black, null))
                        setPadding(16, 8, 16, 8)
                        setOnClickListener {
                            val intent = Intent(this@SCPDetailActivity, TaleDetailActivity::class.java).apply {
                                putExtra("tale_id", taleId) // Pass the tale ID to TaleDetailActivity
                            }
                            startActivity(intent)
                        }
                    }
                    talesLayout.addView(taleButton)
                } else {
                    Log.e("Error", "Failed to fetch details for tale ID: $taleId")
                }
            }
        }

        // Set up SCP Wiki Link
        val wikiLinkTextView = findViewById<TextView>(R.id.textViewSCPWikiLink)
        wikiLinkTextView.text = "Go to SCP Wiki"
        wikiLinkTextView.setOnClickListener {
            val url = details.url
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }
    }
}
