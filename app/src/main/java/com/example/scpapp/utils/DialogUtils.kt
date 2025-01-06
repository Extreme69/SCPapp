package com.example.scpapp.utils

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Utility object for showing common dialogs
object DialogUtils {
    // Extension function to set dialog button colors to a custom hex color
    private fun AlertDialog.setButtonColors() {
        this.setOnShowListener {
            val dialogButtonColor = Color.parseColor("#226128") // Hex color code
            this.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(dialogButtonColor)
            this.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(dialogButtonColor)
            this.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(dialogButtonColor)
        }
    }

    // Shows a dialog for unsaved changes
    fun showUnsavedChangesDialog(context: Context, onPositiveAction: () -> Unit) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Unsaved Changes")
            .setMessage("Do you want to discard the changes?")
            .setPositiveButton("Discard") { _, _ ->
                onPositiveAction() // Call the callback for discard action
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setButtonColors()
        dialog.show()
    }

    // Shows a success dialog
    fun showSuccessDialog(context: Context, message: String, onPositiveAction: () -> Unit) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> onPositiveAction() }
            .create()

        dialog.setButtonColors()
        dialog.show()
    }

    // Shows an error dialog
    fun showErrorDialog(context: Context, message: String) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()

        dialog.setButtonColors()
        dialog.show()
    }

    // Shows an error dialog
    fun showDeleteDialog(context: Context, message: String, onPositiveAction: () -> Unit) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Delete SCP")
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> onPositiveAction() }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setButtonColors()
        dialog.show()
    }
}