package com.example.airplanegame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class GameView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {
    private var gameListener: GameListener? = null

    private val desiredWidth = 250
    private val desiredHeight = 250
    var h = 0f

    private val paint = Paint()

    private val movableBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.plane_player).let {
        Bitmap.createScaledBitmap(it, desiredWidth, desiredHeight, false)
    }
    private var movableBitmapX = 0f
    private var movableBitmapY = 0f
    private var movableBitmapDragging = false
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    private val bullets = mutableListOf<Bullet>()
    private val bulletSpeed = 10f

    private val topBullets = mutableListOf<Bullet>()
    private val bulletTopSpeed = 10f
    private val bulletRadius = 10f
    private val bulletColor1 = Color.RED
    private val bulletColor2 = Color.BLUE

    private val topLeftBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.plane1).let {
        Bitmap.createScaledBitmap(it, desiredWidth, desiredHeight, false)
    }
    private val topRightBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.plane2).let {
        Bitmap.createScaledBitmap(it, desiredWidth, desiredHeight, false)
    }

    private var redBlueCollisionCount = 0
    private var blackBulletCollisionCount = 0

    private val handler = Handler()
    private val runnable = object : Runnable {
        override fun run() {
            triggerTopAnimation()
            handler.postDelayed(this, 10000)
        }
    }

    init {
        handler.postDelayed(runnable, 10000)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(movableBitmap, movableBitmapX, movableBitmapY, paint)
        canvas.drawBitmap(topLeftBitmap, 0f, 0f, paint)
        canvas.drawBitmap(topRightBitmap, width - topRightBitmap.width.toFloat(), 0f, paint)

        updateAndDrawBullets(canvas)
        updateAndDrawTopBullets(canvas)
        checkCollisions()
    }

    private fun updateAndDrawBullets(canvas: Canvas) {
        val iterator = bullets.iterator()
        while (iterator.hasNext()) {
            val bullet = iterator.next()
            bullet.y -= bullet.speed
            if (bullet.y + bulletSpeed < 0) {
                iterator.remove()
            } else {
                paint.color = Color.BLACK
                canvas.drawCircle(bullet.x, bullet.y, 10f, paint)
            }
        }
    }

    private fun updateAndDrawTopBullets(canvas: Canvas) {
        val iterator = topBullets.iterator()
        while (iterator.hasNext()) {
            val bullet = iterator.next()
            bullet.y += bullet.speed
            if (bullet.y - bulletRadius > height) {
                iterator.remove()
            } else {
                paint.color = bullet.color
                canvas.drawCircle(bullet.x, bullet.y, bulletRadius, paint)
            }
        }
    }

    private fun checkCollisions() {
        val iterator = topBullets.iterator()
        while (iterator.hasNext()) {
            val bullet = iterator.next()
            if (bullet.color == bulletColor1 || bullet.color == bulletColor2) {
                if (checkIntersection(bullet, movableBitmapX, movableBitmapY, movableBitmap.width, movableBitmap.height)) {
                    redBlueCollisionCount++
                    iterator.remove()
                    if (redBlueCollisionCount >= 3) {
                        gameListener?.onGameEnd(false)
                    }
                }
            }
        }

        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            if (checkIntersection(bullet, 0f, 0f, topLeftBitmap.width, topLeftBitmap.height) ||
                checkIntersection(bullet, width - topRightBitmap.width.toFloat(), 0f, topRightBitmap.width, topRightBitmap.height)) {
                blackBulletCollisionCount++
                bulletIterator.remove()
                if (blackBulletCollisionCount >= 5) {
                    gameListener?.onGameEnd(true)
                }
            }
        }
    }

    private fun checkIntersection(bullet: Bullet, x: Float, y: Float, width: Int, height: Int): Boolean {
        return bullet.x > x && bullet.x < x + width && bullet.y > y && bullet.y < y + height
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        h = (bottom - top).toFloat()
        val centerX = (right - left) / 2
        movableBitmapX = (centerX - movableBitmap.width / 2).toFloat()
        movableBitmapY = (h - movableBitmap.height).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x >= movableBitmapX && event.x <= movableBitmapX + movableBitmap.width && event.y >= movableBitmapY && event.y <= movableBitmapY + movableBitmap.height) {
                    movableBitmapDragging = true
                    lastTouchX = event.x
                    lastTouchY = event.y
                    createBullet()
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (movableBitmapDragging) {
                    val dx = event.x - lastTouchX
                    moveMovableBitmap(dx)
                    lastTouchX = event.x
                    invalidate()
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                movableBitmapDragging = false
                return true
            }
            MotionEvent.ACTION_DOWN -> {
                if (!movableBitmapDragging) {
                    createBullet()
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun createBullet() {
        val bulletX = movableBitmapX + movableBitmap.width / 2
        val bulletY = movableBitmapY
        bullets.add(Bullet(bulletX, bulletY, bulletSpeed, Color.BLACK))
    }

    private fun moveMovableBitmap(dx: Float) {
        val newLeft = movableBitmapX + dx
        val newRight = newLeft + movableBitmap.width

        if (newLeft >= 0 && newRight <= width) {
            movableBitmapX = newLeft
        } else {
            if (newLeft < 0) movableBitmapX = 0f
            if (newRight > width) movableBitmapX = (width - movableBitmap.width).toFloat()
        }
    }

    private fun triggerTopAnimation() {
        val randomX1 = Random.nextInt((width - bulletRadius).toInt() / 2, (width - bulletRadius).toInt())
        val randomX2 = Random.nextInt(bulletRadius.toInt(), (width - bulletRadius).toInt() / 2)
        val bullet1 = Bullet(randomX1.toFloat(), 0f, bulletTopSpeed, bulletColor1)
        val bullet2 = Bullet(randomX2.toFloat(), 0f, bulletTopSpeed, bulletColor2)
        topBullets.add(bullet1)
        topBullets.add(bullet2)
        invalidate()
    }

    fun setGameListener(listener: GameListener) {
        gameListener = listener
    }

    data class Bullet(var x: Float, var y: Float, val speed: Float, val color: Int)

    interface GameListener {
        fun onGameEnd(won: Boolean)
    }
}
