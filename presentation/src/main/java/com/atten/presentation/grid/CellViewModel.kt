package com.atten.presentation.grid

import androidx.annotation.IntDef
import com.atten.presentation.extensions.NonNullObservableField

/**
 * Viewmodel for the single grid cell.
 */
class CellViewModel(@LandDataType landType: Int, islandId: Int = DEFAULT_ISLAND_ID) {
    companion object {
        // Default value during land initialization
        const val DEFAULT_ISLAND_ID = 0

        const val DATA_OCEAN = 0
        const val DATA_LAND = 1

        @IntDef(
            DATA_OCEAN,
            DATA_LAND
        ) annotation class LandDataType
    }

    // State of cell whether land or ocean
    val cellType = NonNullObservableField(landType)

    // Island id to distinguish from other islands
    val islandId = NonNullObservableField(islandId)

    // Has this cell been traversed during search algo.
    var parsed = false

    fun isLand() = cellType.get() == DATA_LAND

    /**
     * User clicked on cell, should be accessible before solution is triggered, to be toggled by user.
     */
    fun cellClicked() {
        cellType.set(if (isLand()) DATA_OCEAN else DATA_LAND)
    }
}