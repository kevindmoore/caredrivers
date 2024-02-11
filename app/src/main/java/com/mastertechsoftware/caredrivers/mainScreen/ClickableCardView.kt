package com.mastertechsoftware.caredrivers.mainScreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.cardview.widget.CardView

class ClickableCardView(context: Context, attr: AttributeSet? = null) : CardView(context, attr) {

    var itemClickListener: ClickListener? = null

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Touch down event
                return false // super.onInterceptTouchEvent(event) // Return true to consume the event
            }

            MotionEvent.ACTION_MOVE -> {
                // Touch move event
                true // Return true to consume the event
            }

            MotionEvent.ACTION_UP -> {
                // Touch up event
                itemClickListener?.invoke()
                true // Return true to consume the event
            }

            else ->  super.onInterceptTouchEvent(event) // Return true to consume the event
            // Return false if the event is not handled
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Touch down event
                return true // super.onInterceptTouchEvent(event) // Return true to consume the event
            }

            MotionEvent.ACTION_MOVE -> {
                // Touch move event
                true // Return true to consume the event
            }

            MotionEvent.ACTION_UP -> {
                // Touch up event
                itemClickListener?.invoke()
                true // Return true to consume the event
            }

            else ->  super.onTouchEvent(event) // Return true to consume the event
            // Return false if the event is not handled
        }
    }
}