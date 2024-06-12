package com.cesar.examenmacropay.ui.pokemon

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/*abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= totalPageCount) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val totalPageCount: Int
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean
}*/
abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isActiveSearch) {
            if (!isLoading && !isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    Log.e("loadMoreItems", "loadMoreItems")
                    load()
                    loadMoreItems()
                }
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val totalPageCount: Int
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean
    abstract val isActiveSearch: Boolean

    abstract fun load()
    abstract fun terminate()
}