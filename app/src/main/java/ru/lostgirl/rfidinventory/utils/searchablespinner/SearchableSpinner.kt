package ru.lostgirl.rfidinventory.utils.searchablespinner

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.databinding.SearchableSpinnerBinding

const val DEFAULT_ANIMATION_DURATION = 420
const val HORIZONTAL_INSET = 50
const val VERTICAL_INSET = 40

@Suppress("MemberVisibilityCanBePrivate", "RedundantSetter", "RedundantGetter")
class SearchableSpinner(private val context: Context) : DefaultLifecycleObserver {
    private lateinit var dialog: AlertDialog
    private lateinit var dialogView: View
    private lateinit var recyclerAdapter: SpinnerRecyclerAdapter
    lateinit var onItemSelectListener: OnItemSelectListener
    lateinit private var binding:SearchableSpinnerBinding
    var windowTopBackgroundColor: Int? = ContextCompat.getColor(context, android.R.color.white)
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }
    var windowTitleTextColor: Int = ContextCompat.getColor(context, android.R.color.black)
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }
    var negativeButtonBackgroundColor: Int? = null
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }

    var animationDuration: Int = DEFAULT_ANIMATION_DURATION
    var spinnerCancelable: Boolean = false
    var windowTitle: String? = null
    var searchQueryHint: String = context.getString(R.string.search)
    var dismissSpinnerOnItemClick: Boolean = true
    var highlightSelectedItem: Boolean = true
    var negativeButtonVisibility: SpinnerView = SpinnerView.VISIBLE
    var windowTitleVisibility: SpinnerView = SpinnerView.VISIBLE
    var searchViewVisibility: SpinnerView = SpinnerView.VISIBLE
    var selectedItemPosition: Int = -1
    var selectedItem: String? = null

    @Suppress("unused")
    enum class SpinnerView(val visibility: Int) {
        VISIBLE(View.VISIBLE),
        INVISIBLE(View.INVISIBLE),
        GONE(View.GONE)
    }

    init {
        initLifeCycleObserver()
    }

    fun show() {
        if (getDialogInfo(true)) {
            dialogView = View.inflate(context, R.layout.searchable_spinner, null)
            val dialogBuilder =
                AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setCancelable(spinnerCancelable || negativeButtonVisibility != SpinnerView.VISIBLE)
            binding= SearchableSpinnerBinding.bind(dialogView)
            dialog = dialogBuilder.create()
            dialog.initView()
            initDialogColorScheme()
            dialog.show()
            dialog.initAlertDialogWindow()
        }
    }

    fun dismiss() {
        if (getDialogInfo(false)) {
            CircularReveal.startReveal(
                false,
                dialog,
                object : OnAnimationEnd {
                    override fun onAnimationEndListener(isRevealed: Boolean) {
                        hideKeyBoard()
                        if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(null)
                    }
                },
                animationDuration
            )
        }
    }

    fun setSpinnerListItems(spinnerList: ArrayList<String>) {
        recyclerAdapter =
            SpinnerRecyclerAdapter(
                context, spinnerList,
                object : OnItemSelectListener {
                    override fun setOnItemSelectListener(position: Int, selectedString: String) {
                        selectedItemPosition = position
                        selectedItem = selectedString
                        if (dismissSpinnerOnItemClick) dismiss()
                        if (::onItemSelectListener.isInitialized) {
                            onItemSelectListener.setOnItemSelectListener(
                                position,
                                selectedString
                            )
                        }
                    }
                }
            )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dismissDialogOnDestroy()
    }
    private fun dismissDialogOnDestroy() {
        if (getDialogInfo(false)) {
            dialog.dismiss()
        }
    }

    private fun initLifeCycleObserver() {
        if (context is AppCompatActivity) {
            context.lifecycle.addObserver(this)
        }
        if (context is FragmentActivity) {
            context.lifecycle.addObserver(this)
        }
    }

    private fun getDialogInfo(toShow: Boolean): Boolean {
        return if (toShow) {
            !::dialog.isInitialized || !dialog.isShowing
        } else {
            ::dialog.isInitialized &&
                dialog.isShowing &&
                dialog.window != null &&
                dialog.window?.decorView != null &&
                dialog.window?.decorView?.isAttachedToWindow!!
        }
    }

    private fun AlertDialog.initView() {
        setOnShowListener {
            CircularReveal.startReveal(
                true,
                this,
                object : OnAnimationEnd {
                    override fun onAnimationEndListener(isRevealed: Boolean) {
                        if (isRevealed) {
                            hideKeyBoard()
                        }
                    }
                },
                animationDuration
            )
        }
        if (spinnerCancelable || negativeButtonVisibility != SpinnerView.VISIBLE) {
            setOnCancelListener {
                if (::recyclerAdapter.isInitialized) {
                    recyclerAdapter.filter(
                        null
                    )
                }
            }
        }
        dialog.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                this@SearchableSpinner.dismiss()
            }
            false
        }
        // init WindowTittle
        if (windowTitle != null || windowTitleVisibility.visibility == SearchView.VISIBLE) {
            binding.textViewTitle.visibility = View.VISIBLE
            binding.textViewTitle.text = windowTitle
            binding.textViewTitle.setTextColor(windowTitleTextColor)
        } else {
            binding.textViewTitle.visibility = windowTitleVisibility.visibility
        }
        // init SearchView
        if (searchViewVisibility.visibility == SearchView.VISIBLE) {
            binding.searchView.queryHint = searchQueryHint
            binding.searchView.setOnQueryTextListener(object :
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(newText)
                    return false
                }
            })
        } else {
            binding.searchView.visibility = searchViewVisibility.visibility
        }
        // init NegativeButton
        if (negativeButtonVisibility.visibility == SearchView.VISIBLE) {
            binding.buttonClose.setOnClickListener {
                it.isClickable = false
                this@SearchableSpinner.dismiss()
            }
        } else {
            binding.buttonClose.visibility = negativeButtonVisibility.visibility
        }
        // set Recycler Adapter
        if (::recyclerAdapter.isInitialized) {
            //   recyclerAdapter.highlightSelectedItem = highlightSelectedItem
            binding.recyclerView.adapter = recyclerAdapter
        }
    }

    private fun initDialogColorScheme() {
        if (windowTopBackgroundColor != null) {
            binding.headLayout.background = ColorDrawable(windowTopBackgroundColor!!)
        }
        if (negativeButtonBackgroundColor != null) {
            binding.buttonClose.backgroundTintList =
                ColorStateList.valueOf(negativeButtonBackgroundColor!!)
        }
    }

    private fun AlertDialog.initAlertDialogWindow() {
        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
        val insetBackgroundDrawable = InsetDrawable(
            colorDrawable,
            HORIZONTAL_INSET,
            VERTICAL_INSET,
            HORIZONTAL_INSET,
            VERTICAL_INSET
        )
        window?.setBackgroundDrawable(insetBackgroundDrawable)
        window?.attributes?.layoutAnimationParameters
        window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.setDecorFitsSystemWindows(false)
            dialogView.setOnApplyWindowInsetsListener { _, insets ->
                val topInset = insets.getInsets(WindowInsets.Type.statusBars()).top
                val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
                val navigationHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                val bottomInset = if (imeHeight == 0) navigationHeight else imeHeight
                dialogView.setPadding(0, topInset, 0, bottomInset)
                insets
            }
        } else {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun hideKeyBoard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(dialogView.windowToken, 0)
    }
}
