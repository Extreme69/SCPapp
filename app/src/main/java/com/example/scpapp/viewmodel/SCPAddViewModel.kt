package com.example.scpapp.viewmodel

import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scpapp.activity.SCPAdd

class SCPAddViewModel : ViewModel() {

    // LiveData for the classification options
    private val _classificationTypes = MutableLiveData<List<String>>()
    val classificationTypes: LiveData<List<String>> = _classificationTypes

    init {
        // Load classification types when ViewModel is created
        loadClassificationTypes()
    }

    // This function could fetch the data from a repository or static data
    private fun loadClassificationTypes() {
        val classifications = listOf(
            "Pick Classification", // Default hint text
            "Safe", "Euclid", "Keter", "Thaumiel", "Apollyon", "Archon",
            "Ticonderoga", "Explained", "Neutralized", "Decommissioned", "Pending", "Uncontained"
        )
        _classificationTypes.postValue(classifications)
    }

    // This function returns an ArrayAdapter for the spinner
    fun getClassificationAdapter(view: SCPAdd): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(view, android.R.layout.simple_spinner_item, _classificationTypes.value ?: listOf()) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).setTextSize(24f)  // Set text size for the selected item

                // Handle "Pick Classification" as a hint (non-selectable item)
                if (position == 0) {
                    (view as TextView).setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
                } else {
                    (view as TextView).setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
                }

                return view
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)

                // Set the text size for dropdown items
                (view as TextView).setTextSize(24f)

                // Handle "Pick Classification" as a hint in the dropdown (non-selectable)
                if (position == 0) {
                    (view as TextView).setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
                    view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.white))
                } else {
                    (view as TextView).setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
                    view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
                }

                return view
            }
        }
    }
}
