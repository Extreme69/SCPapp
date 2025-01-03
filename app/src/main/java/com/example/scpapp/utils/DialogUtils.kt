package com.example.scpapp.utils

import android.graphics.Color
import androidx.appcompat.app.AlertDialog

// Extension function to set dialog button colors to a custom hex color
fun AlertDialog.setButtonColors() {
    this.setOnShowListener {
        val dialogButtonColor = Color.parseColor("#226128") // Hex color code
        this.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(dialogButtonColor)
        this.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(dialogButtonColor)
        this.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(dialogButtonColor)
    }
}