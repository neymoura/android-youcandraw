package com.ioraptor.youcandraw

import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val bitmap by lazy {
        Bitmap.createBitmap(userCanvasView.width, userCanvasView.height, Bitmap.Config.ARGB_8888)
    }

    val canvas by lazy {
        Canvas(bitmap)
    }

    val paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 10f
        paint
    }

    val path by lazy {
        Path()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        userCanvasView.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    path.reset()
                    val currentPointer = event.getPointer().second
                    path.moveTo(currentPointer.x, currentPointer.y)
                    canvas.drawPoint(currentPointer.x, currentPointer.y, paint)
                    userCanvasView.invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    printPointerLocation(event)
                    draw(event)
                }
                MotionEvent.ACTION_UP -> {
                    val currentPointer = event.getPointer().second
                    canvas.drawPoint(currentPointer.x, currentPointer.y, paint)
                    userCanvasView.invalidate()
                }
            }
            true
        }
    }

    private fun draw(event: MotionEvent) {
        userCanvasView.setImageBitmap(bitmap)
        val (_, currentCoords, previousCoords) = event.getPointer()
        if (previousCoords != null) {
            path.lineTo(currentCoords.x, currentCoords.y)
            canvas.drawPath(path, paint)
        } else {
            canvas.drawPoint(currentCoords.x, currentCoords.y, paint)
        }
        userCanvasView.invalidate()
    }

    private fun printPointerLocation(event: MotionEvent) {
        val (pointerId: Int, pointerCoords) = event.getPointer()
        Log.d(BuildConfig.APPLICATION_ID, "Pointer $pointerId at x -> ${pointerCoords.x}, y -> ${pointerCoords.y}")
    }

    private fun MotionEvent.getPointer(): Triple<Int, MotionEvent.PointerCoords, MotionEvent.PointerCoords?> {
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

}