package com.example.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Rational
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.example.camera.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var imageCapture: ImageCapture?=null
    private lateinit var outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getoutputDirectory()

        if (allPermissionGranted()){
            startCamara()
        }else{
            ActivityCompat.requestPermissions(
                this, Constans.REQUIRE_PREMISSIONS,
                Constans.REQUEST_CODE_PERMISSION
            )
        }
        binding.btnTakePhoto.setOnClickListener{
            takePhoto()
        }


    }

    private fun getoutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull().let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if(mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       if (requestCode == Constans.REQUEST_CODE_PERMISSION){
           if(allPermissionGranted()){
                startCamara()
           }else{
               Toast.makeText(this,
                   "Permiso no Asignado",
                   Toast.LENGTH_LONG).show()
               finish()
           }
       }



    }

    private fun takePhoto() {
        val imageCapture = imageCapture?: return

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(image)
            .build()

        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val savedUri = Uri.fromFile(image)
                    val msg = "Photo Saved"
                    Toast.makeText(
                        this@MainActivity,
                        "$msg $savedUri",
                        Toast.LENGTH_LONG
                    ).show()
                    if (savedUri != null){
                        cambiar(image)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                   Log.d(Constans.TAG, "onerror: ${exception.message}"
                   ,exception)
                }

            }
        )
    }
fun cambiar(URI: File){
    val viewFinder = findViewById<View>(R.id.viewFinder)
    val viewCamera = findViewById<View>(R.id.viewCamera)
    val intent = Intent(this, SecondActivity::class.java)
    intent.putExtra("File", URI)


    intent.putExtra("DimensionsReference", arrayListOf(viewCamera.height, viewCamera.top, viewCamera.width, viewCamera.left))
    intent.putExtra("DimensionsFrame", arrayListOf(viewFinder.height, viewFinder.top, viewFinder.width, viewFinder.left))

    //intent.put("Dimensions", viewCamera)
    startActivity(intent)
}


    private fun startCamara() {
        val camaraProviderFuure = ProcessCameraProvider.getInstance(this)
        camaraProviderFuure.addListener({
            val cameraProvider: ProcessCameraProvider = camaraProviderFuure.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,preview,imageCapture
                )

            }catch (e:Exception){
                Log.d(Constans.TAG, "Fallo al encender la camara")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionGranted() =
        Constans.REQUIRE_PREMISSIONS.all{
            ContextCompat.checkSelfPermission(
                baseContext, it
            )== PackageManager.PERMISSION_GRANTED
        }





}