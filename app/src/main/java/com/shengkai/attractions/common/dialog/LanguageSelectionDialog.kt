package com.shengkai.attractions.common.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shengkai.attractions.R
import com.shengkai.attractions.common.adapter.LanguageSelectAdapter
import com.shengkai.attractions.databinding.DialogLanguageSelectionBinding
import com.shengkai.attractions.databinding.FragmentAttributionDetailBinding
import java.lang.Exception

class LanguageSelectionDialog : DialogFragment() {
    private lateinit var binding: DialogLanguageSelectionBinding
    private lateinit var adapter: LanguageSelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_language_selection,
                container,
                false
            )

        binding.flDialog.setOnClickListener {
            cancel()
        }

        initLanguageComponent()

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            removeDefaultDialogPadding(dialog.window)
        }

        return dialog
    }

    private fun removeDefaultDialogPadding(window: Window?) {
        window?.setBackgroundDrawable(ColorDrawable(0))
        window?.decorView?.setPadding(0, 0, 0, 0)
        window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded && !this.isVisible && !this.isRemoving) {
            try {
                val fragmentTransaction = manager.beginTransaction()
                fragmentTransaction.remove(this).commit()
                super.show(manager, tag);
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun cancel() {
        if (this.isAdded) {
            this.dismiss()
        }
    }

    private fun initLanguageComponent() {
        adapter = LanguageSelectAdapter {
            println("選擇語言${it.languageName}")
        }

        binding.rcyLanguage.adapter = adapter
        binding.rcyLanguage.layoutManager = LinearLayoutManager(context)
    }
}