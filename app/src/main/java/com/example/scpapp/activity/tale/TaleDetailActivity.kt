package com.example.scpapp.activity.tale

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scpapp.R
import com.example.scpapp.activity.scp.SCPDetailActivity
import com.example.scpapp.viewmodel.tale.TaleDetailViewModel

class TaleDetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: TaleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tale_detail)

        // Retrieve scp_id from intent
        val taleId = intent.getStringExtra("tale_id") ?: run {
            Toast.makeText(this, "Tale ID not provided!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set up the back button
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set up the edit button
        findViewById<ImageButton>(R.id.button_scp).setOnClickListener {
            val intent = Intent(this, TaleEditActivity::class.java).apply {
                putExtra("tale_id", taleId) // Pass tale ID to the edit activity
            }
            startActivity(intent)
        }

        // Initialize ViewModel
        detailViewModel = ViewModelProvider(this).get(TaleDetailViewModel::class.java)

        // Fetch and observe SCP details
        detailViewModel.fetchTaleDetails(taleId).observe(this, Observer { details ->
            if (details != null) {
                // Update UI with details
                findViewById<TextView>(R.id.textViewTaleTitle).text = "${details.title}"

                val ratingArrowView = findViewById<ImageView>(R.id.imageViewTaleRatingArrow)
                val ratingTextView = findViewById<TextView>(R.id.textViewTaleRating)

                if (details.rating >= 0) {
                    ratingArrowView.setImageResource(R.drawable.ic_up_arrow)
                } else {
                    ratingArrowView.setImageResource(R.drawable.ic_down_arrow)
                }
                ratingTextView.text = details.rating.toString()

                findViewById<TextView>(R.id.textViewTaleContent).text = details.content

                // Populate SCPs
                val scpsLayout = findViewById<LinearLayout>(R.id.linearLayoutSCPs)
                scpsLayout.removeAllViews()

                details.scpId.forEach { scpId ->
                    val scpButton = Button(this).apply {
                        text = scpId
                        textSize = 18f
                        setTextColor(resources.getColor(android.R.color.black, null))
                        setPadding(16, 8, 16, 8)
                        setOnClickListener {
                            val intent = Intent(this@TaleDetailActivity, SCPDetailActivity::class.java).apply {
                                putExtra("scp_id", scpId) // Pass the SCP ID to SCPDetailActivity
                            }
                            startActivity(intent)
                        }
                    }
                    scpsLayout.addView(scpButton)
                }

                // Set up Tale Wiki Link
                val wikiLinkTextView = findViewById<TextView>(R.id.textViewTaleWikiLink)
                wikiLinkTextView.text = "Go to Tale Wiki"
                wikiLinkTextView.setOnClickListener {
                    val url = details.url
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                    startActivity(intent)
                }
            } else {
                // Handle error or show a message
                Toast.makeText(this, "Failed to load Tale details.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}