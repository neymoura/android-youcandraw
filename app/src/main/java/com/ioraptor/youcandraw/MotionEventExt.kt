package com.ioraptor.youcandraw

import android.view.MotionEvent

fun MotionEvent.getPointer(): Pair<MotionEvent.PointerCoords?, Boolean> {
    val pointerId: Int = this.getPointerId(this.actionIndex)
    val currentCoords: MotionEvent.PointerCoords? = MotionEvent.PointerCoords()
    val hasHistory = this.historySize > 0
    try {
        this.getPointerCoords(pointerId, currentCoords)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return Pair(currentCoords, hasHistory)
}