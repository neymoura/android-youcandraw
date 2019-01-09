package com.ioraptor.youcandraw

import android.util.Log
import android.view.MotionEvent

fun MotionEvent.getPointer(): Triple<Int, MotionEvent.PointerCoords, MotionEvent.PointerCoords?> {
    val pointerId: Int = this.getPointerId(this.actionIndex)
    val currentCoords = MotionEvent.PointerCoords()
    val previousCoords = getLastCoords(pointerId)
    this.getPointerCoords(pointerId, currentCoords)
    return Triple(pointerId, currentCoords, previousCoords)
}

private fun MotionEvent.getLastCoords(pointerId: Int): MotionEvent.PointerCoords? {
    var previousCoords: MotionEvent.PointerCoords? = null
    if (this.historySize > 1) {
        previousCoords = MotionEvent.PointerCoords()
        this.getHistoricalPointerCoords(pointerId, this.historySize - 1, previousCoords)
    }
    return previousCoords
}

fun MotionEvent.printPointerLocation() {
    val (pointerId: Int, pointerCoords) = this.getPointer()
    Log.d(BuildConfig.APPLICATION_ID, "Pointer $pointerId at x -> ${pointerCoords.x}, y -> ${pointerCoords.y}")
}