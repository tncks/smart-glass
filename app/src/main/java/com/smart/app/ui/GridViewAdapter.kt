package com.smart.app.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.smart.app.R
import com.smart.app.common.*
import com.smart.app.model.ModelContents
import com.smart.app.model.getAlImageuri
import com.smart.app.network.ApiService
import com.smart.app.repository.category.Supglobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GridViewAdapter(context: Context, private val alMenu: ArrayList<ModelContents>, nPos2: Int, mmIndex: Int) :
    ArrayAdapter<ModelContents>(context, R.layout.adapter_photosfolder, alMenu) {
    private lateinit var viewHolder: ViewHolder
    private var nPos: Int = 0
    private val nPos2: Int
    private val mmIndex: Int

    init {
        this.nPos = nPos2
        this.nPos2 = nPos2
        this.mmIndex = mmIndex
    }


    override fun getCount(): Int {
        return alMenu[nPos].getAlImageuri().size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return if (alMenu[nPos].getAlImageuri().size > 0) {
            alMenu[nPos].getAlImageuri().size
        } else {
            1
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @Suppress("DuplicatedCode")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (convertView == null) {

            viewHolder = ViewHolder()

            val convertView2 = LayoutInflater.from(context).inflate(R.layout.adapter_photosfolder, parent, false)
            viewHolder.tvFoldern = convertView2.findViewById(R.id.tv_folder)
            viewHolder.tvFoldersize = convertView2.findViewById(R.id.tv_folder2)
            viewHolder.ivImage = convertView2.findViewById(R.id.iv_image)
            convertView2.tag = viewHolder

            retrieveAllMediaIFiles(viewHolder, position)
            changeThumbnailAndNavigateToPrevScreen(viewHolder, position)

            return convertView2
        } else {

            viewHolder = convertView.tag as ViewHolder
            retrieveAllMediaIFiles(viewHolder, position)
            changeThumbnailAndNavigateToPrevScreen(viewHolder, position)

            return convertView
        }

    }


    /*---------------------------------------------------------------------*/

    @Suppress("RemoveRedundantQualifierName")
    private fun retrieveAllMediaIFiles(vH: GridViewAdapter.ViewHolder, ps: Int) {

        vH.tvFoldern?.visibility = View.GONE
        vH.tvFoldersize?.visibility = View.GONE
        Glide.with(context).load(alMenu[nPos].getAlImageuri()[ps])
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(vH.ivImage!!)

    }


    @Suppress("RemoveRedundantQualifierName", "LiftReturnOrAssignment")
    private fun changeThumbnailAndNavigateToPrevScreen(vH: GridViewAdapter.ViewHolder, ps: Int) {
        vH.ivImage?.setOnClickListener {

            val nameStartWith = "imgFile"
            val createdTmpFile: File = TempFileIOUtility().createFileFromUri(
                context,
                nameStartWith,
                alMenu[nPos2].getAlImageuri()[ps]
            )
            val uploadSuccessfulAtFirstTry: Boolean = UploadUtility().uploadFile(createdTmpFile)
            if (uploadSuccessfulAtFirstTry) {
                doRestChoresAfterUpload(createdTmpFile.name)
            } else {
                val bitmap: Bitmap?
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(context.contentResolver, alMenu[nPos2].getAlImageuri()[ps])
                    bitmap = ImageDecoder.decodeBitmap(source)
                } else {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(context.contentResolver, alMenu[nPos2].getAlImageuri()[ps])
                }

                if (bitmap != null) {
                    secondTryToUpload(bitmap)
                }
            }

        }
    }

    /*---------------------------------------------------------------------*/

    private fun secondTryToUpload(bitmap: Bitmap) {

        val nameStartWith = "imgFile"
        val directFile = File.createTempFile(nameStartWith, ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(directFile)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // 100 값 수정 X !!
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (UploadUtility().uploadFile(directFile)) {
            doRestChoresAfterUpload(directFile.name)
        } else {
            Log.w("FAIL", "GridViewAdapter FAIL")
        }

    }

    private fun doRestChoresAfterUpload(fullFileName: String) {
        val prePathNameURL = BACK_AZURE_STATIC_WEB_MEDIA_FILE_SERVER_IMAGE_DIR_URI + fullFileName
        reviseMethod(prePathNameURL, mmIndex)
        resetSupG(prePathNameURL)
        val intent = Intent(context, ProfileAddEditActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    /*---------------------------------------------------------------------*/

    @Suppress("DuplicatedCode")
    private fun reviseMethod(thumbPhpFilePath: String, resultParam: Int) {

        val retrofit = Retrofit.Builder()
            .baseUrl(FIRE_JSON_BASEURL)
            .build()


        val service = retrofit.create(ApiService::class.java)


        val jsonObjectString: String = PrepareJsonHelper().prepareFlexibleJson(thumbPhpFilePath)


        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        CoroutineScope(Dispatchers.IO).launch {

            val resultParamStringValue: String = resultParam.toString()

            val response = service.updateItemProfileStyle(SAFEUID, resultParamStringValue, requestBody)

            withContext(Dispatchers.Main) {
                Log.i("dummy", response.isSuccessful.toString())
            }
        }
    }


    private fun resetSupG(prePathNameURL: String) {
        val tmpLs = Supglobal.mSup.split(DELIM)
        Supglobal.mSup = PatchHelperUtility().reviseHelperUtil(tmpLs, mmIndex, prePathNameURL)
    }


    /*---------------------------------------------------------------------*/
    class ViewHolder {
        var tvFoldern: TextView? = null
        var tvFoldersize: TextView? = null
        var ivImage: ImageView? = null
    }
    /*---------------------------------------------------------------------*/
}
