package com.atten.ui.listeners

import android.text.Editable
import android.text.TextWatcher

/**
 * This adapter class provides empty implementations of the methods from [TextWatcher]. Any custom
 * listener that cares only about a subset of the methods of this listener can simply subclass
 * this adapter class instead of implementing the interface directly.
 */
open class TextWatcherAdapter : TextWatcher {

    override fun afterTextChanged(s: Editable) = Unit

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
}