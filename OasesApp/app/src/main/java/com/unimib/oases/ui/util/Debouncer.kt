package com.unimib.oases.ui.util

fun (() -> Unit).debounce(delay: Long = 500L): () -> Unit {
    var lastClickTime = 0L
    return {
        val now = System.currentTimeMillis()
        if (now - lastClickTime > delay) {
            lastClickTime = now
            this()
        }
    }
}
