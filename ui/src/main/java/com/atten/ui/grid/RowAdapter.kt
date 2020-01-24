package com.atten.ui.grid

import android.content.res.Resources
import android.graphics.Color
import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.core.util.set
import androidx.recyclerview.widget.RecyclerView
import com.atten.presentation.grid.CellViewModel
import com.atten.presentation.grid.CellViewModel.Companion.DEFAULT_ISLAND_ID
import com.atten.presentation.grid.GridViewModel
import com.atten.ui.R
import com.atten.ui.binding.BoundViewHolder
import com.atten.ui.databinding.ItemGridCellBinding
import com.atten.utility.messaging.MessageHandler.Companion.log
import com.atten.presentation.extensions.onChanged
import kotlin.random.Random

/**
 * Adapter for a grid row, a row of cells which represent an ocean tile or land tile.
 */
class RowAdapter(
    var rowData: Array<CellViewModel>,
    var rowIndex: Int,
    private val resources: Resources,
    private val gridViewModel: GridViewModel
) : RecyclerView.Adapter<BoundViewHolder<ItemGridCellBinding>>() {

    companion object {
        // Shared color data set of all lands
        val ISLAND_COLORS = SparseIntArray()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoundViewHolder<ItemGridCellBinding> {
        return BoundViewHolder.create(parent, ItemGridCellBinding::inflate)
    }

    override fun getItemCount(): Int {
        return rowData.size
    }

    override fun onBindViewHolder(holder: BoundViewHolder<ItemGridCellBinding>, position: Int) {
        log("Initializing cell.. rowIndex[$rowIndex], rowSize[${rowData.size}], position[$position]")
        val viewModel = rowData[position]

        holder.binding.viewModel = viewModel
        holder.binding.gridViewModel = gridViewModel
        holder.binding.itemGridButton.setBackgroundColor(getCellColor(viewModel))

        viewModel.cellType.onChanged {
            // recycled viewholders may still hold reference
            if (holder.binding.viewModel == viewModel)
                holder.binding.itemGridButton.setBackgroundColor(getCellColor(viewModel))
        }

        viewModel.islandId.onChanged {
            // recycled viewholders may still hold reference
            if (holder.binding.viewModel == viewModel)
                holder.binding.itemGridButton.setBackgroundColor(getLandColor(viewModel.islandId.get()))
        }
    }

    /**
     * Get the appropriate color for the given grid cell.
     */
    private fun getCellColor(viewModel: CellViewModel): Int {
        return if (viewModel.isLand()) getLandColor(viewModel.islandId.get()) else resources.getColor(
            R.color.DodgerBlue,
            null
        )
    }

    /**
     * Get a color based on the given land Id.
     */
    private fun getLandColor(islandId: Int): Int {
        return when {
            // Initial land
            islandId == DEFAULT_ISLAND_ID -> resources.getColor(R.color.Peru, null)

            // New discovered land
            ISLAND_COLORS[islandId] == 0 -> {
                log("Adding new color for ID#$islandId")

                // Create random - non blue color
                val newColor = Color.rgb(Random.nextInt(256), Random.nextInt(256), 0)
                ISLAND_COLORS[islandId] = newColor
                newColor
            }

            // Already existing land
            else -> ISLAND_COLORS[islandId]
        }
    }
}