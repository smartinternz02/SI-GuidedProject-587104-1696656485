package com.project.core.util.pagination

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class RecyclerViewPaginator : RecyclerView.OnScrollListener {
    /*
     * We pass the RecyclerView to the constructor
     * of this class to get the LayoutManager
     * */
    private var layoutManager: RecyclerView.LayoutManager

    /*
     * This variable is used to set
     * the threshold. For instance, if I have
     * set the page limit to 20, this will notify
     * the app to fetch more transactions when the
     * user scrolls to the (page limit - threshold)th item of the list.
     * */
    private var threshold = PagingConstant.THRESHOLD_SIZE

    /*
    * This high-order function is for calling back action
    * when data needs to be loaded more
    * */
    private var onLoadMore: ((Long) -> Unit)? = null

    /*
    * These 3 constructors used for each layout manager
    * which is available for android recycler view.
    * Choose it wisely
    * */
    constructor(layoutManager: LinearLayoutManager) {
        this.layoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.layoutManager = layoutManager
        threshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.layoutManager = layoutManager
        threshold *= layoutManager.spanCount
    }

    /*
     * Variable to keep track of the current page.
     * Starting page can be 0 or 1 based on preferences.
     * 0 -> if you want to give both first page loading and
     * next page loading same behaviour.
     * 1 -> if you want to determine first page loading with
     * its own behaviour, and so does the next page loading
     * */
    private var startPage: Long = 1L

    /*
     * This is a hack to ensure that the app is notified
     * only once to fetch more data. Since we use
     * scrollListener, there is a possibility that the
     * app will be notified more than once when user is
     * scrolling. This means there is a chance that the
     * same data will be fetched from the backend again.
     * This variable is to ensure that this does NOT
     * happen.
     * */
    private var isLoading = false

    /*
     * This is a variable to put previous total item (before loading state)
     * Later on, it will be used for check whether the view is still loading
     * or has been done
     * */
    private var previousTotal = 0

    /*
    * variable for auto-increment everytime it called.
    * Used for adding current page
    * */
    val currentPage: Long
        get() = ++startPage

    //check is first time get data from endpoint
    var isFirstGet = true

    /*
     * I have added a reset method here
     * that can be called from the UI because
     * if we have a filter option in the app,
     * we might need to refresh the whole data set
     * */
    fun reset() {
        startPage = 1L
        previousTotal = 0
        isLoading = false
    }

    /*
    * set onLoadMore high-order function
    * */
    fun setOnLoadMoreListener(action: (page: Long) -> Unit) {
        this.onLoadMore = action
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = when(layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)

                // get maximum element within the list
                getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            else -> 0
        }


        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }
        } else {
            if (totalItemCount <= lastVisibleItem + threshold) {
                onLoadMore?.invoke(currentPage)
                isLoading = true
            }
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}