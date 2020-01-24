package com.attentislands.settings

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter
import com.atten.presentation.settings.SettingsViewModel
import com.atten.ui.listeners.TextWatcherAdapter
import com.attentislands.R
import com.attentislands.databinding.ActivitySettingsBinding
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Configuration display, the user can enter the grid dimensions and if the grid should be randomized
 * or not.
 */
class SettingsActivity : RxAppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by lazy { SettingsViewModel(DefaultSettingsNavigator(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.viewModel = viewModel
        setListeners()
    }

    /**
     * Set text listeners to validate user input.
     */
    private fun setListeners() {
        // Set row input listener
        binding.inputRowsEdit.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onRowsEntered(s.toString())
            }
        })

        // Set column input listener
        binding.inputColumnsEdit.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onColumnsEntered(s.toString())
            }
        })
    }
}