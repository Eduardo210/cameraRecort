package com.example.camera

import android.R.attr
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.File

import android.widget.ImageView
import android.graphics.Bitmap

import android.graphics.BitmapFactory
import android.view.View
import java.io.ByteArrayOutputStream
import kotlin.math.min
import android.R.attr.bitmap
import android.content.Context
import com.example.camera.Constans.TAG
import android.util.DisplayMetrics
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import kotlin.math.roundToInt


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var Intent1: Intent
        Intent1 = getIntent()
//Here first is key and 0 is default value
        var obj: File = Intent1.getSerializableExtra("File") as File
        Log.d("mytag", "VAlue " + obj)

        var bmImg: Bitmap =
            BitmapFactory.decodeFile(obj.toString())
        val img: ImageView = findViewById(R.id.imgFoto)
        img.setImageBitmap(bmImg.rotate(90F, bmImg))
    }

    fun Bitmap.rotate(degrees: Float, bmIm: Bitmap): Bitmap {


        val viewCamera = intent.getIntegerArrayListExtra("DimensionsReference")

        val bitmapHeight: Int = bmIm.height
        val bitmapWidth: Int = bmIm.width

        val FACTOR_H = 100 * viewCamera!![0] / ((2 * viewCamera[1]) + viewCamera[0])
        val FACTOR_HI = FACTOR_H * viewCamera[1] / viewCamera[0]

        if (bmIm.height < bmIm.width) {

            val bitmapWidthResized: Int = FACTOR_H * bitmapWidth / 100


            val bitmapHeightResized: Int = (bitmapWidthResized / 1.6).roundToInt()


            val bitmapWidthResizedX: Int = FACTOR_HI * bitmapWidth / 100
            val bitmapHeightResizedY: Int = (bitmapHeight - bitmapHeightResized) / 2

            val matrix = Matrix().apply { postRotate(degrees) }


            return Bitmap.createBitmap(
                bmIm,
                bitmapWidthResizedX,
                bitmapHeightResizedY,
                bitmapWidthResized,
                bitmapHeightResized,
                matrix,
                true
            )


        } else {

            val bitmapHeightResized: Int = FACTOR_H * bitmapHeight / 100


            val bitmapWidthResized: Int = (bitmapHeightResized / 1.6).roundToInt()


            val bitmapHeightResizedY: Int = FACTOR_HI * bitmapHeight / 100
            val bitmapWidthResizedX: Int = ((bitmapWidth - bitmapWidthResized) / 2).toInt()

            return Bitmap.createBitmap(
                bmIm,
                bitmapWidthResizedX,
                bitmapHeightResizedY,
                bitmapWidthResized,
                bitmapHeightResized
            )


        }


    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.getResources().getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }


}