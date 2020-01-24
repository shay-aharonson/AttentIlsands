package com.atten.presentation.settings

import com.atten.presentation.extensions.NonNullObservableField
import com.atten.presentation.extensions.onChanged

/**
 * ViewModel for settings activity, manages grid dimension data entry and user selection for random
 * grid.
 */
class SettingsViewModel(private val navigator: SettingsNavigator) {

    val isValid = NonNullObservableField(false)

    private val rows = NonNullObservableField(0)
    private val columns = NonNullObservableField(0)

    init {
        // Data entry validation
        rows.onChanged { isValid.set(isValidEntry()) }
        columns.onChanged { isValid.set(isValidEntry()) }
    }

    /**
     * Row input changed, if no number was entered set to 0.
     */
    fun onRowsEntered(rowsText: String) {
        (rowsText.toIntOrNull() ?: 0).let(rows::set)
    }

    /**
     * Column input changed, if no number was entered set to 0.
     */
    fun onColumnsEntered(columnsText: String) {
        (columnsText.toIntOrNull() ?: 0 ).let(columns::set)
    }

    /**
     * User clicked on randomize button, navigate to grid display in random state.
     */
    fun onRandomizeClicked() {
        navigator.navigateToGrid(rows.get(), columns.get(), true)
    }

    /**
     * User clicked on draw button, navigate to grid display in draw state.
     */
    fun onDrawClicked() {
        navigator.navigateToGrid(rows.get(), columns.get(), false)
    }

    /**
     * Validate both row and column entries must have a numeric value larger than 0.
     */
    private fun isValidEntry(): Boolean {
        return rows.get() > 0 && columns.get() > 0
    }
}