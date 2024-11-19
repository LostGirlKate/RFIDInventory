package ru.lostgirl.rfidinventory.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.databinding.CustomDialogBinding
import ru.lostgirl.rfidinventory.databinding.CustomFilterDialogBinding
import ru.lostgirl.rfidinventory.databinding.CustomSettingsDialogBinding
import ru.lostgirl.rfidinventory.databinding.LayoutLoadingDialogBinding
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState

const val DIALOG_PERCENT_WIDTH = 90.0
const val DIM_AMOUNT = 0.5f
const val ABSOLUTE_SIZE_SPAN = 50
const val HORIZONTAL_INSET = 50
const val VERTICAL_INSET = 40

object UIHelper {

    // Отображение Tast с настройкой цвета текста
    fun showToastMessage(context: Context, msg: String, textColor: Int) {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    context,
                    textColor
                )
            ),
            0,
            spannableString.length,
            0
        )
        spannableString.setSpan(
            AbsoluteSizeSpan(ABSOLUTE_SIZE_SPAN),
            0,
            spannableString.length,
            0
        )
        val toast = Toast.makeText(context, spannableString, Toast.LENGTH_SHORT)
        toast.show()
    }

    // Показать ProcessDialog с настройкой текста
    fun alertProcessDialog(context: Context, msg: String): AlertDialog {
        val binding = LayoutLoadingDialogBinding.inflate(LayoutInflater.from(context))
        binding.msgTextView.text = msg
        val dialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.MaterialAlertDialog_rounded
        )
            .setView(binding.root)
            .setCancelable(false)
        return dialogBuilder.create()
    }

    // Показать AlertDialog с подтверждением действия
    fun alertDialog(context: Context, msg: String, onOkClickListener: () -> Unit) {
        val binding = CustomDialogBinding.inflate(LayoutInflater.from(context))
        binding.messageText.text = msg
        val dialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.MaterialAlertDialog_rounded
        )
            .setView(binding.root)
            .setCancelable(false)
        val dialog: AlertDialog = dialogBuilder.show()
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
            onOkClickListener()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        alertDialogSetParams(dialog)
    }

    // Показать AlertDialog с настройками
    fun alertSettingsDialog(
        context: Context,
        itemState: InventoryItemState,
        onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) {
        val binding = CustomSettingsDialogBinding.inflate(LayoutInflater.from(context))
        binding.cbResetState.visibility =
            if (itemState == InventoryItemState.STATE_FOUND_IN_WRONG_PLACE) View.VISIBLE else View.GONE
        binding.cbFoundState.visibility =
            if (itemState == InventoryItemState.STATE_NOT_FOUND) View.VISIBLE else View.GONE
        binding.stateCaption.visibility = if (binding.cbFoundState.isVisible ||
            binding.cbResetState.isVisible
        ) {
            View.VISIBLE
        } else {
            View.GONE
        }
        val dialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.MaterialAlertDialog_rounded
        )
            .setView(binding.root)
            .setCancelable(false)
        val dialog: AlertDialog = dialogBuilder.show()
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
            val resetState = binding.cbResetState.isChecked
            val setStateFound = binding.cbFoundState.isChecked
            val rfidNotWork = if (binding.cbRfidNotWork.isChecked) {
                """${binding.cbRfidNotWork.text}
                |
                """.trimMargin()
            } else {
                ""
            }
            val barcodeNotWork =
                if (binding.cbBarcodeNotWork.isChecked) {
                    """${binding.cbBarcodeNotWork.text}
                    |
                    """.trimMargin()
                } else {
                    ""
                }
            val comment = "$rfidNotWork$barcodeNotWork${binding.teComment.text}"
            onOkClickListener(resetState, setStateFound, comment)
        }

        alertDialogSetParams(dialog)
        initAlertDialogWindow(dialog, binding.root)
    }

    private fun initAlertDialogWindow(
        dialog: AlertDialog,
        dialogView: View,
    ) {
        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
        val insetBackgroundDrawable = InsetDrawable(
            colorDrawable,
            HORIZONTAL_INSET,
            VERTICAL_INSET,
            HORIZONTAL_INSET,
            VERTICAL_INSET
        )
        dialog.window?.setBackgroundDrawable(insetBackgroundDrawable)
        dialog.window?.attributes?.layoutAnimationParameters
        dialog.window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.setDecorFitsSystemWindows(false)
            dialogView.setOnApplyWindowInsetsListener { _, insets ->
                val topInset = insets.getInsets(WindowInsets.Type.statusBars()).top
                val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
                val navigationHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                val bottomInset = if (imeHeight == 0) navigationHeight else imeHeight
                dialogView.setPadding(0, topInset, 0, bottomInset)
                insets
            }
        } else {
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    // Показать FilterDialog с подсказкой по фильтрам со статусами ТМЦ
    fun detailDialog(
        context: Context,
        message: String,
        boxColor: Int,
        showBox: Boolean,
        textColor: Int,
        cancelable: Boolean = false,
    ): AlertDialog {
        val binding = CustomFilterDialogBinding.inflate(LayoutInflater.from(context))
        binding.messageText.text = message
        binding.messageText.setTextColor(context.getColor(textColor))
        if (showBox) {
            binding.cardView.findViewById<ConstraintLayout>(R.id.card_view_color).background =
                AppCompatResources.getDrawable(context, boxColor)
        } else {
            binding.cardView.visibility = View.GONE
        }
        val dialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.MaterialAlertDialog_rounded
        )
            .setView(binding.root)
            .setCancelable(cancelable)
        val dialog: AlertDialog = dialogBuilder.show()
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        alertDialogSetParams(dialog)
        return dialog
    }

    // Настройка размеров для AlertDialog
    private fun alertDialogSetParams(dialog: AlertDialog) {
        val percent = DIALOG_PERCENT_WIDTH / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = percentWidth.toInt()
        layoutParams.dimAmount = DIM_AMOUNT
        dialog.window?.attributes = layoutParams
    }

    // настройка кнопки фильтра (отображение подсказки при LongClick и настройка OnCheckedChangeListener)
    fun setOnCheckedChangeListenerToFilterButton(
        context: Context,
        button: ToggleButton,
        message: String,
        boxColor: Int,
        action: () -> Unit,
    ) {
        button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                button.setTextColor(context.getColor(R.color.white))
            } else {
                button.setTextColor(context.getColor(R.color.textColor))
            }
            action()
        }
        button.setOnLongClickListener {
            detailDialog(context, message, boxColor, true, R.color.textColor)
            true
        }
    }

    // Обновление текста на кнопке фильтра
    fun refreshToggleButton(button: ToggleButton, text: String) {
        button.textOff = text
        button.textOn = text
        button.isChecked = button.isChecked
    }
}
