package com.atten.ui.binding

import android.view.animation.Animation
import android.view.animation.Animation.INFINITE
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.databinding.BindingAdapter
import com.atten.presentation.LoadingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.atten.presentation.extensions.onChanged


/**
 * Loading animation for FAB, disappears when loading is complete.
 */
@BindingAdapter("loadingViewModel")
fun FloatingActionButton.load(viewModel: LoadingViewModel) {
    viewModel.isLoading.onChanged { isLoading ->
        if (isLoading) {
            startAnimation(RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f).apply {
                interpolator = LinearInterpolator()
                duration = 250
                repeatCount = INFINITE
            })
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) { hide() }
            })
        } else {
            this.animation.cancel()
        }
    }
}