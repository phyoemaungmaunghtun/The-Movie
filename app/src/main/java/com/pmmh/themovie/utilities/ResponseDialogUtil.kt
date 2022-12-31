package com.pmmh.themovie.utilities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.pmmh.themovie.databinding.ErrorDialogBinding
import javax.inject.Inject

class ResponseDialogUtil @Inject
constructor(private val context: Activity) {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private lateinit var errorAlertDialog: AlertDialog

    fun showErrorDialog(message: String) {
        val dialogBinding =
            ErrorDialogBinding.inflate(inflater)
        val builder = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
        if (!this::errorAlertDialog.isInitialized || !errorAlertDialog.isShowing) {
            errorAlertDialog = builder.show()
            errorAlertDialog.setCancelable(false)
            errorAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.apply {
                tvMessage.text = message
                btnOk.setOnClickListener {
                    errorAlertDialog.dismiss()
                }
            }
        }

    }
}