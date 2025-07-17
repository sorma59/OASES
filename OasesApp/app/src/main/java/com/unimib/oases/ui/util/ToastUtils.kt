package com.unimib.oases.ui.util

import android.content.Context
import android.widget.Toast

object ToastUtils {
    private var toast: Toast? = null

    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, message, duration)
        } else {
            toast?.setText(message)
        }
        toast?.show()
    }
}
