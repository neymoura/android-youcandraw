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
            }
            true
        }
    }

    private fun fingerDown(event: MotionEvent) {
        path.reset()
        path.moveTo(event.x, event.y)
        canvas.drawPoint(event.x, event.y, paint)
        userCanvasView.invalidate()
    }

    private fun fingerDrag(event: MotionEvent) {
        if (event.historySize > 0) {
            path.lineTo(event.x, event.y)
            canvas.drawPath(path, paint)
        } else {
            canvas.drawPoint(event.x, event.y, paint)
        }
        userCanvasView.invalidate()
    }

}