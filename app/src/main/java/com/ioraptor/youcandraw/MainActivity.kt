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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        userCanvasView.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    printPointerLocation(event)
                    draw(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    printPointerLocation(event)
                    draw(event)
                }
            }
            true
        }
    }

    private fun draw(event: MotionEvent) {
        userCanvasView.setImageBitmap(bitmap)
        val (_, pointerCoords) = event.getPointer()
        canvas.drawPoint(pointerCoords.x, pointerCoords.y, paint)
        userCanvasView.invalidate()
    }

    private fun printPointerLocation(event: MotionEvent) {
        val (pointerId: Int, pointerCoords) = event.getPointer()
        Log.d(BuildConfig.APPLICATION_ID, "Pointer $pointerId at x -> ${pointerCoords.x}, y -> ${pointerCoords.y}")
    }

    private fun MotionEvent.getPointer(): Pair<Int, MotionEvent.PointerCoords> {
        val pointerId: Int = this.getPointerId(this.actionIndex)
        val pointerCoords = MotionEvent.PointerCoords()
        this.getPointerCoords(pointerId, pointerCoords)
        return Pair(pointerId, pointerCoords)
    }

}
