package com.atten.presentation

import androidx.annotation.IntDef
import androidx.databinding.ObservableBoolean
import com.atten.utility.messaging.MessageHandler.Companion.log
import com.atten.presentation.extensions.NonNullObservableField
import com.atten.presentation.extensions.onChanged

/**
 * ViewModel with various loading states, primarily to be used with the [LoadingView] component.
 */
open class LoadingViewModel {
    companion object {
        const val STATE_NOT_LOADING = 1
        const val STATE_LOADING = 2
        const val STATE_COMPLETE = 3
        const val STATE_ERROR = 4

        @IntDef(
            STATE_NOT_LOADING,
            STATE_LOADING,
            STATE_COMPLETE,
            STATE_ERROR
        )
        annotation class LoadingState
    }

    val loadingState = NonNullObservableField(STATE_NOT_LOADING)
    val isLoading = ObservableBoolean(false)

    init {
        loadingState.onChanged {  newState ->
            log("LoadingState: $newState")
            isLoading.set(newState == STATE_LOADING)
        }

        isLoading.onChanged { log("isLoading: $it") }
    }
}