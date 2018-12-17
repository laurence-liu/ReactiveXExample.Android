package liuuu.laurence.reactivexexample.utility

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

object SetUpLayoutManager {
    fun verticalLinearLayout(context: Context, targetRecyclerView: RecyclerView) {
        val placeLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        targetRecyclerView.setHasFixedSize(true)
        targetRecyclerView.layoutManager = placeLayoutManager
    }

    fun horizontalLinearLayout(context: Context, targetRecyclerView: RecyclerView) {
        val placeLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        targetRecyclerView.setHasFixedSize(true)
        targetRecyclerView.layoutManager = placeLayoutManager
    }

    fun gridLayout(context: Context, targetRecyclerView: RecyclerView, numberOfColumn: Int) {
        val gridLayoutManager = GridLayoutManager(context, numberOfColumn)
        targetRecyclerView.setHasFixedSize(true)
        targetRecyclerView.layoutManager = gridLayoutManager
    }

    fun staggeredGridLayout(targetRecyclerView: RecyclerView) {
        val spotLayoutManager = StaggeredGridLayoutManager(3, 1)
        spotLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        targetRecyclerView.setHasFixedSize(true)
        targetRecyclerView.layoutManager = spotLayoutManager
    }
}