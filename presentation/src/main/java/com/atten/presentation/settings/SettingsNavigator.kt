package com.atten.presentation.settings

/**
 * Navigation interface for the settings activity.
 */
interface SettingsNavigator {

    /**
     * Navigate to the grid activity.
     */
    fun navigateToGrid(rows: Int, columns: Int, randomize: Boolean)
}