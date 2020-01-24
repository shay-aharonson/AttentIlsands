package com.atten.ui.grid

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atten.presentation.grid.GridViewModel
import com.atten.ui.binding.BoundViewHolder
import com.atten.ui.databinding.ItemGridRowBinding
import com.atten.ui.listeners.ItemSnapHelper
import com.atten.utility.messaging.MessageHandler.Companion.log

/**
 * Adapter for entire grid. Maintains synchronized scrolling between sub-recyclerviews.
 */
class GridAdapter(private val gridViewModel: GridViewModel, private val callback: RecyclerView.OnScrollListener) :
    RecyclerView.Adapter<BoundViewHolder<ItemGridRowBinding>>() {

    private var touchedRowTag: Int = -1
    private var horizontalOffset: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoundViewHolder<ItemGridRowBinding> {
        return BoundViewHolder.create(parent, ItemGridRowBinding::inflate)
    }

    override fun getItemCount(): Int {
        return gridViewModel.gridData.size
    }

    override fun onBindViewHolder(holder: BoundViewHolder<ItemGridRowBinding>, position: Int) {
        val gridRow = holder.binding.itemGridRowList
        val rowAdapter = gridRow.adapter as RowAdapter?
        val rowData = gridViewModel.gridData[position]
        val context = holder.binding.root.context

        // Initialize a new row's components
        if (rowAdapter == null) {
            log("Creating new row#[${position}] at offset[$horizontalOffset]")
            val snapHelper = ItemSnapHelper()

            gridRow.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            gridRow.adapter = RowAdapter(rowData, position, context.resources, gridViewModel)
            gridRow.setItemViewCacheSize(0)
            gridRow.tag = position

            // Touch listener to identify which RV initiated scroll event.
            val touchListener = object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    touchedRowTag = rv.tag as Int
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            }

            // Scrolllistener to measure scrolling of touched RV and forward the event to all other RV's that did not trigger it.
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (touchedRowTag == recyclerView.tag) {
                        callback.onScrolled(recyclerView, dx, dy)

                        // Save offset for newly initialized or updated RV's
                        val newOffset = gridRow.getChildAdapterPosition(gridRow.getChildAt(0))
                        log("Offset changed: $horizontalOffset <--- $newOffset")
                        horizontalOffset = newOffset
                    }
                }
            }

            // Set listeners and offset
            snapHelper.attachToRecyclerView(gridRow)
            gridRow.scrollToPosition(horizontalOffset)
            gridRow.addOnItemTouchListener(touchListener)
            gridRow.addOnScrollListener(scrollListener)

            // Update an existing row's components
        } else {
            rowAdapter.rowIndex = position
            rowAdapter.rowData = rowData
            log("Updating existing row#[${rowAdapter.rowIndex}] at offset[$horizontalOffset]")

            rowAdapter.notifyDataSetChanged()
            gridRow.scrollToPosition(horizontalOffset)
        }
    }
}