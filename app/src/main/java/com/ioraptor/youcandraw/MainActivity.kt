package com.ioraptor.youcandraw

import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val bitmap: Bitmap by lazy {
        Bitmap.createBitmap(userCanvasView.width, userCanvasView.height, Bitmap.Config.ARGB_8888)
    }

    private val canvas by lazy {
        Canvas(bitmap)
    }

    private val paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 10f
        paint
    }

    private val path by lazy {
        Path()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUserCanvas()
    }

    private fun setupUserCanvas() {
        userCanvasView.post { userCanvasView.setImageBitmap(bitmap) }
        userCanvasView.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    fingerDown(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    fingerDrag(event)
                }
                MotionEvent.ACTION_UP -> {
                    fingerUp(event)
                }
            }
            true
        }
    }

    private fun fingerUp(event: MotionEvent) {
        val currentPointer = event.getPointer().second
        canvas.drawPoint(currentPointer.x, currentPointer.y, paint)
        userCanvasView.invalidate()
    }

    private fun fingerDown(event: MotionEvent) {
        path.reset()
        val currentPointer = event.getPointer().second
        path.moveTo(currentPointer.x, currentPointer.y)
        canvas.drawPoint(currentPointer.x, currentPointer.y, paint)
        userCanvasView.invalidate()
    }

    private fun fingerDrag(event: MotionEvent) {
        val (_, currentCoords, previousCoords) = event.getPointer()
        if (previousCoords != null) {
            path.lineTo(currentCoords.x, currentCoords.y)
            canvas.drawPath(path, paint)
        } else {
            canvas.drawPoint(currentCoords.x, currentCoords.y, paint)
        }
        userCanvasView.invalidate()
    }

}