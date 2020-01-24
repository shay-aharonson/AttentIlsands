package com.attentislands.settings

import android.content.Intent
import com.atten.presentation.settings.SettingsNavigator
import com.attentislands.grid.GridActivity
import com.attentislands.grid.GridActivity.Companion.BUNDLE_DATA_COLUMNS
import com.attentislands.grid.GridActivity.Companion.BUNDLE_DATA_RANDOMIZE
import com.attentislands.grid.GridActivity.Companion.BUNDLE_DATA_ROWS
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Default implementation of [SettingsNavigator].
 */
class DefaultSettingsNavigator(private val parent: RxAppCompatActivity) : SettingsNavigator {

    override fun navigateToGrid(rows: Int, columns: Int, randomize: Boolean) {
        parent.startActivity(Intent(parent, GridActivity::class.java).apply {
            putExtra(BUNDLE_DATA_ROWS, rows)
            putExtra(BUNDLE_DATA_COLUMNS, columns)
            putExtra(BUNDLE_DATA_RANDOMIZE, randomize)
        })
    }
}