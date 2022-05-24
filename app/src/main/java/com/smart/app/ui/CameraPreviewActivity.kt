package com.smart.app.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.smart.app.R
import com.smart.app.common.PERMISSION_CAMERA
import java.util.*


class CameraPreviewActivity : AppCompatActivity() {

    private val startCameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && validAccessAvailablePermission()) {
                // snackbar -> notice user for 사진 ~에 저장됨
                finish()
            } else {
                // different snackbar -> notice user for ERROR or throw exception and catch print
                finish()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_preview)

        requirePermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            PERMISSION_CAMERA
        )
    }


    @Suppress("SameParameterValue")
    private fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            val isAllPermissionsGranted =
                permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            if (isAllPermissionsGranted) {
                permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    private fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERMISSION_CAMERA -> openCamera()
            else -> finish()
        }
    }

    private fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                // change to failWithCamera()
                Toast.makeText(
                    this,
                    "권한이 필요합니다. 권한을 다시 확인해주세요.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun openCamera() {
        try {
            var realUri: Uri
            createImageUri(newFileName(), "image/jpg")?.let { uri ->
                realUri = uri
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
                startCameraForResult.launch(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    private fun newFileName(): String {
        val rand = Random()
        val randValue = rand.nextInt(10000)
        val randValueString = randValue.toString()
        return "$randValueString.jpg"
    }

    @Suppress("SameParameterValue")
    private fun createImageUri(filename: String, mimeType: String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }


    private fun validAccessAvailablePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this@CameraPreviewActivity,
            Manifest.permission.CAMERA
        )) == PackageManager.PERMISSION_GRANTED
    }


}

// Reference and revised history
// layout xml revised
/*
<ImageView
    android:id="@+id/imagePreview"
    android:layout_width="0dp"
    android:layout_height="0dp"
    android:contentDescription="@string/label_category"
    android:maxWidth="200dp"
    android:maxHeight="200dp"
    android:scaleType="centerCrop"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
 */
