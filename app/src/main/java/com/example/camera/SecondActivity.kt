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
//        val matrix = Matrix().apply { postRotate(degrees) }
//        return Bitmap.createBitmap(this, 15, 15, width, height, matrix, true)

        val matrix = Matrix()

//        matrix.postRotate(degrees)

//        val scaledBitmap = Bitmap.createScaledBitmap(bmIm, width, height, true)

        val rotation =
            bmIm
//            Bitmap.createBitmap(
//            scaledBitmap,
//            0,
//            0,
//            scaledBitmap.width,
//            scaledBitmap.height,
//            matrix,
//            true
//        )
        val bitmapHeight: Int = rotation.height
        val bitmapWidth: Int = rotation.width



//        val FACTOR_H = 78 //5%
//        val FACTOR_W = 65
//
//        val FACTOR_HI = 11 //5%
//        val FACTOR_WI = 17

        val frame = intent.getIntegerArrayListExtra("DimensionsFrame")
        val reference = intent.getIntegerArrayListExtra("DimensionsReference")


        val heightOriginal = frame!![0]
        val widthOriginal = frame[2]
        val heightFrame = reference!![0]
        val widthFrame = reference[2]
        val leftFrame = reference[3]
        val topFrame = reference[1]
        val heightReal = rotation.height
        val widthReal = rotation.width
        val widthFinal = widthFrame * widthReal / widthOriginal
        val heightFinal = heightFrame * heightReal / heightOriginal
        val leftFinal = leftFrame * widthReal / widthOriginal
        val topFinal = topFrame * heightReal / heightOriginal
//        val FACTOR_H = 100 * viewCamera!![0]/((2 * viewCamera[1])  + viewCamera[0])
//        //val FACTOR_H = 50
//        val FACTOR_W = 100 * viewCamera!![2]/(2 * viewCamera[3]  + viewCamera[2])
//
//        val FACTOR_HI = 100 * viewCamera[1]/viewCamera[0]
//        val FACTOR_WI = 100 * viewCamera[3]/viewCamera[2]
////
//        val bitmapHeightResized: Int = FACTOR_H * bitmapHeight / 100
//
//        val bitmapWidthResized: Int = FACTOR_W  * bitmapWidth / 100
//
//        val bitmapHeightResizedY: Int = FACTOR_HI * bitmapHeight /100
//        val bitmapWidthResizedX: Int = FACTOR_WI  * bitmapWidth / 100
//
//
//
//        val bitmapWidthResized: Int = FACTOR_W  * bitmapWidth / 100
//        val bitmapHeightResized: Int = FACTOR_H * bitmapHeight / 100
//
//        val bitmapWidthResizedX: Int = FACTOR_WI  * bitmapWidth / 100
//        val bitmapHeightResizedY: Int = FACTOR_HI * bitmapHeight / 100

        return Bitmap.createBitmap(rotation,leftFinal, topFinal, widthFinal, heightFinal)

    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.getResources().getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }


}