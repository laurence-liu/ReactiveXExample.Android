package liuuu.laurence.reactivexexample.utility

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

class RecyclerTouchListener(context: Context, recycleView: RecyclerView, onItemClickListener: OnItemClickListener) : RecyclerView.OnItemTouchListener {

    private var mOnItemClickListener: OnItemClickListener = onItemClickListener
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recycleView.findChildViewUnder(e.x, e.y)
                if (child != null && onItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(child, recycleView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && mOnItemClickListener != null && gestureDetector.onTouchEvent(e)) {
            mOnItemClickListener.onItemClick(child, rv.getChildAdapterPosition(child))
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

}