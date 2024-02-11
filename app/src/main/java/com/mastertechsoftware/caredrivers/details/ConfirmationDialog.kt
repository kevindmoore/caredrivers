package com.mastertechsoftware.caredrivers.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.mastertechsoftware.caredrivers.R

// Click listeners
typealias CancelButtonClickListener = () -> Unit
typealias OKButtonClickListener = () -> Unit
typealias ButtonClickListener = () -> Unit

/**
 * Class to hold details for additional buttons
 */
data class AdditionalButtonInfo(@StringRes val buttonText: Int, @StyleRes val buttonStyle: Int, val buttonClickListener: ButtonClickListener)

/**
 * Dialog Configuration. Instead of passing all of this to the function, set some defaults
 * and have only the first few be required
 */
data class DialogConfig(
        @StringRes val titleRes: Int, @StringRes val subTitleRes: Int,
        @StringRes val confirmButtonRes: Int, @StringRes val cancelButtonRes: Int,
        val okButtonClickListener: OKButtonClickListener,
        val cancelButtonClickListener: CancelButtonClickListener,
        @StyleRes val dialogTitleStyle: Int = R.style.DialogTitle,
        @StyleRes val dialogTextStyle: Int = R.style.DialogText,
        @StyleRes val dialogCancelStyle: Int = R.style.DialogCancelButton,
        @StyleRes val dialogConfirmationStyle: Int = R.style.DialogConfirmationButton,
        val additionalButtons: List<AdditionalButtonInfo> = listOf()
)

/**
 * Given a configuration, Show a Confirmation Dialog.
 * Dismiss is called for each button but the button click listeners will do the work
 */
fun showConfirmationDialog(
        context: Context, config: DialogConfig
) {
    // Use a custom view
    val layoutInflater = LayoutInflater.from(context)
    val dialogView: View = layoutInflater.inflate(R.layout.confirmation_dialog, null)

    // Use System AlertDialog class
    val alertDialog = AlertDialog.Builder(context).create()
    alertDialog.setCancelable(false)

    // Set Text, Style & listeners
    val dialogTitle: TextView = dialogView.findViewById(R.id.dialogTitle)
    dialogTitle.setText(config.titleRes)
    dialogTitle.setTextAppearance(config.dialogTitleStyle)
    val dialogText: TextView = dialogView.findViewById(R.id.dialogText)
    dialogText.setText(config.subTitleRes)
    dialogText.setTextAppearance(config.dialogTextStyle)
    val confirmButton: TextView = dialogView.findViewById(R.id.confirm_button)
    confirmButton.setText(config.confirmButtonRes)
    confirmButton.setTextAppearance(config.dialogConfirmationStyle)
    confirmButton.setOnClickListener {
        alertDialog.dismiss()
        config.okButtonClickListener()
    }

    val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)
    cancelButton.setText(config.cancelButtonRes)
    cancelButton.setTextAppearance(config.dialogCancelStyle)
    cancelButton.setOnClickListener {
        alertDialog.dismiss()
        config.cancelButtonClickListener()
    }

    val closeButton: ImageView = dialogView.findViewById(R.id.closeButton)
    closeButton.setOnClickListener {
        alertDialog.dismiss()
    }

    // If there are additional buttons, add them to the end.
    // Limit to 5 buttons
    if (config.additionalButtons.isNotEmpty()) {
        val innerLayout: ConstraintLayout = dialogView.findViewById(R.id.innerLayout)
        var currentId = confirmButton.id
        var buttonCount = 0
        // limit to 5 buttons
        config.additionalButtons.map { additionalButtonInfo ->
            if (buttonCount < 5) {
                val newLayoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                newLayoutParams.topToBottom = currentId
                newLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                newLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                val newButton = Button(context)
                currentId = View.generateViewId()
                newButton.id = currentId
                newButton.setText(additionalButtonInfo.buttonText)
                newButton.setTextAppearance(additionalButtonInfo.buttonStyle)
                newButton.setOnClickListener {
                    alertDialog.dismiss()
                    additionalButtonInfo.buttonClickListener()
                }
                innerLayout.addView(newButton, newLayoutParams)
                buttonCount++
            }
        }
    }

    alertDialog.setView(dialogView)

    alertDialog.show()

}