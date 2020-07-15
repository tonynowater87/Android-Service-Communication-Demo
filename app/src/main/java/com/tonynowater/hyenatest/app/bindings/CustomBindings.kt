package com.tonynowater.hyenatest.app.bindings

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.tonynowater.hyenatest.R

@BindingAdapter("bindButtonColor")
fun bindButtonColor(view: MaterialButton, connected: Boolean) {
    if (connected) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
    } else {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.grey))
    }
}

@BindingAdapter("isVisible")
fun isVisible(view: View, visible: Boolean) {
    view.isVisible = visible
}