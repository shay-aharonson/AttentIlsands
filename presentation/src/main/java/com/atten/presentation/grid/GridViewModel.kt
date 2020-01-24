package com.atten.presentation.grid

import android.annotation.SuppressLint
import com.atten.presentation.LoadingViewModel
import com.atten.presentation.extensions.NonNullObservableField
import com.atten.presentation.grid.CellViewModel.Companion.DATA_LAND
import com.atten.presentation.grid.CellViewModel.Companion.DATA_OCEAN
import com.atten.utility.messaging.MessageHandler.Companion.log
import com.atten.utility.rx.bindUntil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * ViewModel for the grid layout. Manages logic for generating the data and parsing it - finding "islands".
 */
class GridViewModel(
    rows: Int,
    columns: Int,
    randomize: Boolean,
    private val lifecycle: Observable<ActivityEvent>
) :
    LoadingViewModel() {

    companion object {
        const val DEFAULT_ISLAND_COUNT = 0

        // Max items in a row or column unless specified otherwise by user.
        const val DEFAULT_MAX_DIMENSION = 1000
    }

    // NxM grid cell data set
    val gridData: Array<Array<CellViewModel>>

    // Count of islands found
    val totalIslands = NonNullObservableField(DEFAULT_ISLAND_COUNT)

    // Shared lock for all grid cells, based on user config an solution progress.
    var gridLock = NonNullObservableField(randomize)

    init {
        gridData = createData(rows, columns, randomize)
    }

    @SuppressLint("CheckResult")
    fun onSolveClicked() {
        Single.zip(
            findIslands(),
            Single.timer(1, TimeUnit.SECONDS),
            BiFunction { islands: Int, _: Long -> islands })
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .bindUntil(lifecycle)
            .doOnSubscribe {
                loadingState.set(STATE_LOADING)
                gridLock.set(true)
            }
            .doFinally {
                loadingState.set(STATE_COMPLETE)

                // Trigger notification that search completed if count is same as default
                if (totalIslands.get() == DEFAULT_ISLAND_COUNT)
                    totalIslands.notifyChange()
            }
            .subscribe(::onSearchCompleted, ::log)
    }

    /**
     * Search completed and found the given amount of islands.
     */
    private fun onSearchCompleted(count: Int) {
        log("$count islands discovered!")
        totalIslands.set(count)
    }

    /**
     * Begin grid search for islands. An island is defined as a group of land cells surrounded by
     * ocean cells.
     */
    private fun findIslands(): Single<Int> {
        var count = 0

        return Single.just(true)
            .map {
                for (row in 0..gridData.size)
                    for (column in 0..gridData[0].size)
                        count += findNextLand(row, column, count)
                count
            }
    }

    /**
     * Recursively find the next adjacent land until all adjacent cells are ocean, when a land is
     * discovered, it is associated with an island according to the number of islands found.
     */
    private fun findNextLand(row: Int, column: Int, count: Int): Int {

        if (isCellInBounds(row, column)) {
            val cellViewModel = gridData[row][column]

            if (!cellViewModel.parsed && cellViewModel.isLand()) {

                // Mark cell as traversed
                cellViewModel.parsed = true

                // Associate with current land group, +1 due to default initialization land value
                cellViewModel.islandId.set(count + 1)

                // Find land cells in row above
                findNextLand(row - 1, column - 1, count)
                findNextLand(row - 1, column, count)
                findNextLand(row - 1, column + 1, count)

                // Find land cells in same row
                findNextLand(row, column - 1, count)
                findNextLand(row, column + 1, count)

                // Find land cells in row below
                findNextLand(row + 1, column - 1, count)
                findNextLand(row + 1, column, count)
                findNextLand(row + 1, column + 1, count)

                return DATA_LAND
            }
        }

        return DATA_OCEAN
    }

    /**
     * Index safety check
     */
    private fun isCellInBounds(row: Int, column: Int): Boolean {
        return row >= 0 && row < gridData.size &&
                column >= 0 && column < gridData[0].size
    }

    /**
     * Generate grid data and cell viewmodels, either random or all ocean according to given user
     * input.
     */
    private fun createData(
        rows: Int,
        columns: Int,
        randomize: Boolean
    ): Array<Array<CellViewModel>> {
        return Array(if (rows < 1) Random.nextInt(DEFAULT_MAX_DIMENSION + 1) else rows) {
            Array(if (columns < 1) Random.nextInt(DEFAULT_MAX_DIMENSION + 1) else columns) {
                generateIslandData(randomize)
            }
        }
    }

    /**
     * Create a single cell's data.
     */
    private fun generateIslandData(randomize: Boolean): CellViewModel {
        return CellViewModel(
            if (randomize)
            // Set island 25%
                Random.nextInt(0, 5) / 4
            else
                DATA_OCEAN
        )
    }
}