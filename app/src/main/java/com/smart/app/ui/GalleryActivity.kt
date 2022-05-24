@file:Suppress("RedundantCompanionReference")

package com.smart.app.ui


import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.smart.app.R
import com.smart.app.common.REQUEST_PERMISSIONS
import com.smart.app.model.*


class GalleryActivity : AppCompatActivity() {
    private var booleanFolder2 = false
    private var objAdapter: AdapterPhotosFolder? = null
    private var gvFolder: GridView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        val mIndex: Int = intent.getIntExtra("mIndex", 0)
        setPhotoClickListener(mIndex)
        setCameraIconClickListener()
        if (isValidWithCheckStepOne()) {
            if (isValidWithCheckStepTwo()) {
                Toast.makeText(this, "접근권한을 확인중입니다..", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS
                )
            }
        } else {
            myimagespath2()
        }

    }

    private fun setPhotoClickListener(mIndex: Int) {

        gvFolder = findViewById(R.id.gv_folder)
        gvFolder?.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val intent = Intent(applicationContext, PhotosActivity::class.java)
            intent.putExtra("value", i)
            intent.putExtra("mIndex", mIndex)
            startActivity(intent)
        }

    }

    private fun setCameraIconClickListener() {

        findViewById<ImageView>(R.id.iv_open_camera)?.setOnClickListener {
            startActivity(Intent(this@GalleryActivity, CameraPreviewActivity::class.java))
        }

    }

    private fun isValidWithCheckStepOne(): Boolean {
        return (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun isValidWithCheckStepTwo(): Boolean {
        return (
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                )

    }


    @Suppress("DuplicatedCode")
    private fun myimagespath2() {

        try {
            Companion.alImages2.clear()
            var nPos = 0
            var contentUri: Uri?
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val orderBy = MediaStore.Images.Media.DATE_TAKEN
            val cursor: Cursor? = applicationContext.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
            val columnIndexId: Int
            val columnIndexFolderName = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            if (cursor != null) {
                columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {

                    contentUri = ContentUris.withAppendedId(uri, cursor.getLong(columnIndexId))

                    for (i in Companion.alImages2.indices) {
                        if (Companion.alImages2[i].getStrFolder() == cursor.getString(
                                columnIndexFolderName!!
                            )
                        ) {
                            booleanFolder2 = true
                            nPos = i
                            break
                        } else {
                            booleanFolder2 = false
                        }
                    }
                    if (booleanFolder2) {
                        val alUri = ArrayList<Uri>()
                        alUri.addAll(Companion.alImages2[nPos].getAlImageuri())
                        alUri.add(contentUri)
                        Companion.alImages2[nPos].setAlImageuri(alUri)
                    } else {
                        val alUri = ArrayList<Uri>()
                        alUri.add(contentUri)
                        if (columnIndexFolderName != null) {
                            val objModel = ModelContents(cursor.getString(columnIndexFolderName), alUri)
                            objModel.setStrFolder(cursor.getString(columnIndexFolderName))
                            objModel.setAlImageuri(alUri)
                            Companion.alImages2.add(objModel)
                        }

                    }

                }
                cursor.close()
            }

            objAdapter = AdapterPhotosFolder(applicationContext, Companion.alImages2)
            gvFolder?.adapter = objAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        var numCountOfGrantResults: Int? = grantResults.size
        if (grantResults.isEmpty()) {
            numCountOfGrantResults = null
        }

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                var i = 0
                while (numCountOfGrantResults != null && i < numCountOfGrantResults) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        myimagespath2()
                    } else {
                        Toast.makeText(
                            this,
                            "권한을 확인해주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    i++
                }
            }
        }
    }

    companion object {
        var alImages2: ArrayList<ModelContents> = ArrayList()
    }

}


/*--------------------------------------------------------------*/
/*--------------------------------------------------------------*/
// Reference for studying
//    private fun myimagespath() {
//        Companion.alImages.clear()
//        var nPos = 0
//        var absolutePathOfImage: String?
//        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf(/*MediaStore.MediaColumns.DATA, */MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//        val orderBy = MediaStore.Images.Media.DATE_TAKEN
//        val cursor: Cursor? = applicationContext.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
//        // val columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        val columnIndexFolderName = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//        while (cursor != null && cursor.moveToNext()) {
//            absolutePathOfImage = "" // cursor.getString(columnIndexData!!)
//
//            for (i in Companion.alImages.indices) {
//                if (Companion.alImages[i].getStrFolder() == cursor.getString(
//                        columnIndexFolderName!!
//                    )
//                ) {
//                    booleanFolder = true
//                    nPos = i
//                    break
//                } else {
//                    booleanFolder = false
//                }
//            }
//            if (booleanFolder) {
//                val alPath = ArrayList<String>()
//                alPath.addAll(Companion.alImages[nPos].getAlImagepath())
//                alPath.add(absolutePathOfImage)
//                Companion.alImages[nPos].setAlImagepath(alPath)
//            } else {
//                val alPath = ArrayList<String>()
//                alPath.add(absolutePathOfImage)
//                if (columnIndexFolderName != null) {
//                    val objModel = ModelImages(cursor.getString(columnIndexFolderName), alPath)
//                    objModel.setStrFolder(cursor.getString(columnIndexFolderName))
//                    objModel.setAlImagepath(alPath)
//                    Companion.alImages.add(objModel)
//                }
//
//            }
//        }
//
//        // objAdapter = AdapterPhotosFolder(applicationContext, Companion.alImages)
//        // gvFolder?.adapter = objAdapter
//
//    }

/*
        private fun myimagespath() {
            Companion.alImages.clear()
            var nPos = 0
            var relativePathOfImage: String?
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val orderBy = MediaStore.Images.Media.DATE_TAKEN
            val listOfAllImages = mutableListOf<ModelImages>()
            val cursor: Cursor? = applicationContext.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
            val columnIndexId = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val columnIndexFolderName = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            // applicationContext.contentResolver.openInputStream()
            while (cursor!!.moveToNext()) {
                // relativePathOfImage = cursor.getString(columnIndexId!!)
                val contentUri = ContentUris.withAppendedId(uri, cursor.getLong(columnIndexId!!))
                var image: Bitmap
                applicationContext.contentResolver.openFileDescriptor(contentUri, "r").use { pfd ->
                    if (pfd != null) {
                        image = BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor)
                        listOfAllImages.add(AdapterImage(image))
                    }
                }

                for (i in Companion.alImages.indices) {
                    if (Companion.alImages[i].getStrFolder() == cursor.getString(
                            columnIndexFolderName!!
                        )
                    ) {
                        booleanFolder = true
                        nPos = i
                        break
                    } else {
                        booleanFolder = false
                    }
                }
                if (booleanFolder) {
                    val alPath = ArrayList<String>()
                    alPath.addAll(Companion.alImages[nPos].getAlImagepath())
                    alPath.add(relativePathOfImage)
                    Companion.alImages[nPos].setAlImagepath(alPath)
                } else {
                    val alPath = ArrayList<String>()
                    alPath.add(relativePathOfImage)
                    if (columnIndexFolderName != null) {
                        val objModel = ModelImages(cursor.getString(columnIndexFolderName), alPath)
                        objModel.setStrFolder(cursor.getString(columnIndexFolderName))
                        objModel.setAlImagepath(alPath)
                        Companion.alImages.add(objModel)
                    }

                }
            }

            objAdapter = AdapterPhotosFolder(applicationContext, Companion.alImages)
            gvFolder?.adapter = objAdapter

        }
    */