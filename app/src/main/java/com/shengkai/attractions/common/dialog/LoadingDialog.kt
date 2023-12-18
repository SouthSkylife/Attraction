package com.shengkai.attractions.common.dialog

import android.app.Dialog
import android.content.Context
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
import com.shengkai.attractions.data.local.ApplicationSp
import com.shengkai.attractions.databinding.DialogLanguageSelectionBinding
import com.shengkai.attractions.databinding.DialogLoadingBinding
import java.lang.Exception

class LoadingDialog : DialogFragment() {
    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_loading,
                container,
                false
            )

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

     fun cancel() {
        if (this.isAdded) {
            this.dismiss()
        }
    }
}