package com.attentislands.grid

import android.os.Bundle
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atten.presentation.extensions.onChanged
import com.atten.presentation.grid.GridViewModel
import com.atten.ui.grid.GridAdapter
import com.atten.utility.messaging.MessageHandler
import com.atten.utility.messaging.MessageHandler.Companion.LENGTH_LONG
import com.attentislands.R
import com.attentislands.databinding.ActivityGridBinding
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Displays a grid of ocean and land cells according to user input.
 * Will identify islands among cells and color them upon request.
 */
class GridActivity : RxAppCompatActivity() {

    companion object {
        const val BUNDLE_DATA_ROWS = "bundleDataRows"
        const val BUNDLE_DATA_COLUMNS = "bundleDataColumns"
        const val BUNDLE_DATA_RANDOMIZE = "bundleDataRandomized"
    }

    @Inject
    lateinit var messageHandler: MessageHandler

    private lateinit var binding: ActivityGridBinding

    private var rows: Int = -1
    private var columns: Int = -1
    private var randomize: Boolean = true

    private val viewModel: GridViewModel by lazy { GridViewModel(rows, columns, randomize, lifecycle()) }

    /*
     * Callback for scroll events, forward all events to every OTHER recyclerview displayed.
     */
    private val onScrolledCallback = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            binding.gridlayout.children.forEach { view ->
                (view as RecyclerView?)?.let { gridRow ->
                    if (gridRow.tag != recyclerView.tag)
                        gridRow.scrollBy(dx, dy)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_grid
        )

        getArguments()
        initViewModel()
        initGrid()
    }

    /**
     * Initialize viewmodel related depencencies
     */
    private fun initViewModel() {
        binding.viewModel = viewModel
        viewModel.totalIslands.onChanged { totalIslands ->
            messageHandler.message(binding.root, LENGTH_LONG, resources.getString(R.string.total_island_count, totalIslands))
        }
    }

    /**
     * Get user input submitted from the settings display.
     */
    private fun getArguments() {
        rows = intent.getIntExtra(BUNDLE_DATA_ROWS, -1)
        columns = intent.getIntExtra(BUNDLE_DATA_COLUMNS, -1)
        randomize = intent.getBooleanExtra(BUNDLE_DATA_RANDOMIZE, true)
    }

    /**
     * Initialize the grid view components.
     */
    private fun initGrid() {
        binding.gridlayout.layoutManager = LinearLayoutManager(this)
        binding.gridlayout.setItemViewCacheSize(0)
        binding.gridlayout.adapter = GridAdapter(
            viewModel,
            onScrolledCallback
        )
    }
}
