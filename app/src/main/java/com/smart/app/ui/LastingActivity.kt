package com.smart.app.ui


import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smart.app.R
import com.smart.app.common.*
import com.smart.app.model.ModelContents
import com.smart.app.model.getAlImageuri
import com.smart.app.model.setAlImageuri
import com.smart.app.model.setStrFolder
import com.smart.app.network.ApiService
import com.smart.app.repository.category.Supglobal
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream


class LastingActivity : AppCompatActivity() {


    private var alImages3: ArrayList<ModelContents> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lasting)

        val mIntent: Intent = intent
        val mPos: Int = mIntent.getIntExtra("mIndex", 0)


        setMyAlImagesOnInit()


        /*------------------------------------------------------------*/

        val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, true)
        val recycler = findViewById<View>(R.id.recycler) as RecyclerView

        val fab = findViewById<View>(R.id.fbutton) as FloatingActionButton
        fab.visibility = View.INVISIBLE

        recycler.layoutManager = gridLayoutManager
        @Suppress("RemoveRedundantQualifierName")
        val adapter = LastingActivity.MyAdapter(this@LastingActivity.alImages3, mPos)
        recycler.adapter = adapter

        /*------------------------------------------------------------*/

        if (adapter.itemCount <= 14) {
            fab.visibility = View.VISIBLE
            val params: ViewGroup.LayoutParams = recycler.layoutParams
            val myHeightStackLevel = determineStackLevelWithItemCount(adapter.itemCount)
            params.height = FIRST_BASE_HEIGHT + (myHeightStackLevel * MY_HEIGHT_INTERVAL_TOP_BOTTOM)
            recycler.layoutParams = params
        } else {
            val params: ViewGroup.LayoutParams = recycler.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            recycler.layoutParams = params
        }

        /*------------------------------------------------------------*/

        // Begin Of Handler
        lifecycleScope.launch {
            delay(10L)

            innScrollSmoothTopLogic(fab, mPos, adapter, recycler)

        }
        // End Of Handler
    }

    @Suppress("RemoveRedundantQualifierName")
    private suspend fun innScrollSmoothTopLogic(
        fab: FloatingActionButton,
        mPos: Int,
        adapter: LastingActivity.MyAdapter,
        recycler: RecyclerView
    ) {
        withContext(Dispatchers.Main) {
            fab.setOnClickListener {
                val intent = Intent(applicationContext, GalleryActivity::class.java)
                intent.putExtra("mIndex", mPos)
                startActivity(intent)
            }
            if (adapter.itemCount > 14) {
                recycler.smoothScrollToPosition(adapter.itemCount - 1)
            } else {
                fab.visibility = View.VISIBLE
            }

            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && fab.isShown) {
                        fab.hide()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(-1)) {
                        fab.show()
                    }

                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }


    @Suppress("DuplicatedCode")
    private fun setMyAlImagesOnInit() {

        // var nPos = 0
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


                val alUri = ArrayList<Uri>()
                alUri.add(contentUri)
                if (columnIndexFolderName != null) {
                    val objModel = ModelContents("", alUri)
                    objModel.setStrFolder("")
                    objModel.setAlImageuri(alUri)
                    this.alImages3.add(objModel)
                }

            }
            cursor.close()
        }

    }


    /*------------------------------------------------------------*/

    private fun determineStackLevelWithItemCount(itemCnt: Int): Int {
        return if (itemCnt <= (3 * 1)) {
            0
        } else if (itemCnt <= (3 * 2)) {
            1
        } else if (itemCnt <= (3 * 3)) {
            2
        } else if (itemCnt <= (3 * 4)) {
            3
        } else if (itemCnt <= 14) {
            4
        } else {
            4
        }
    }

    /*------------------------------------------------------------*/

    @Suppress("RemoveRedundantQualifierName")
    private class MyAdapter(
        private val alImages3: ArrayList<ModelContents>,
        private val mPos: Int
    ) :
        RecyclerView.Adapter<LastingActivity.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastingActivity.MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView: View = inflater.inflate(R.layout.item_view, parent, false)
            val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = parent.width / 3 - layoutParams.leftMargin - layoutParams.rightMargin
            itemView.layoutParams = layoutParams
            itemView.tag = mPos
            return LastingActivity.MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: LastingActivity.MyViewHolder, position: Int) {

            if (itemCount - position >= 3) {
                Glide.with(holder.itemView.context).load(alImages3[position].getAlImageuri()[0])
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.photoView)
            } else {

                if (itemCount - position == 1) {
                    holder.apply {
                        photoView.setImageResource(R.drawable.samplelandscape)
                    }
                } else {
                    holder.apply {
                        photoView.setImageResource(R.drawable.sampleportrait)
                    }
                }


            }


        }


        /*------------------------------------------------------------*/

        @Suppress("LiftReturnOrAssignment")
        override fun getItemCount(): Int {


            val n = this@MyAdapter.alImages3.size


            if (n <= 14) {
                return if (checkEmptyOrOnlyOneOrTwo(n)) {
                    matchIsEmptyOrOnlyOneOrTwoResult(n)
                } else {
                    addSampleImageForDummyGridNaturalLayout(n)
                }
            } else {
                return addSampleImageForDummyGridNaturalLayout(n)
            }
        }

        /*------------------------------------------------------------*/


        private fun checkEmptyOrOnlyOneOrTwo(n: Int): Boolean {
            return checkEmptyOrOnlyOneOrTwoInternalCalculate(n)
        }

        private fun checkEmptyOrOnlyOneOrTwoInternalCalculate(n: Int): Boolean {
            return when (n) {
                0 -> true
                1 -> true
                else -> n == 2
            }
        }


        private fun matchIsEmptyOrOnlyOneOrTwoResult(n: Int): Int {
            return if (n == 1) {
                (n + 1)
            } else {
                n
            }
        }


        private fun addSampleImageForDummyGridNaturalLayout(n: Int): Int {
            if (n % 3 == 0) {
                return (n + 2)
            }
            if ((n - 1) % 3 == 0) {
                return (n + 1)
            }
            return n
        }
        /*------------------------------------------------------------*/

    }


    private class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoView: ImageView

        init {
            photoView = itemView.findViewById(R.id.photo)
            connectEachItemSetReactionListener(photoView, itemView)
        }

        fun connectEachItemSetReactionListener(photoView: ImageView, itemView: View) {
            photoView.setOnClickListener {
                val mPos = (itemView.tag as Int)

                val bitmap: Bitmap = (it as ImageView).drawable.toBitmap()


                val nameStartWith = "imgFile"
                val directFile = File.createTempFile(nameStartWith, ".jpg", it.context.cacheDir)
                val outputStream = FileOutputStream(directFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // 100 값 수정 X !!
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        outputStream.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (UploadUtility().uploadFile(directFile)) {

                    val prePathNameURL = BACK_AZURE_STATIC_WEB_MEDIA_FILE_SERVER_IMAGE_DIR_URI + directFile.name
                    reviseMethod(prePathNameURL, mPos)
                    resetSupG(prePathNameURL, mPos)
                    val intent = Intent(it.context, ProfileAddEditActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    it.context.startActivity(intent)

                } else {
                    Log.w("FAIL", "LastingActivity FAIL")
                }

            }
        }

        @Suppress("DuplicatedCode")
        fun reviseMethod(thumbPhpFilePath: String, resultParam: Int) {

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


        fun resetSupG(prePathNameURL: String, mPos: Int) {
            val tmpLs = Supglobal.mSup.split(DELIM)
            Supglobal.mSup = PatchHelperUtility().reviseHelperUtil(tmpLs, mPos, prePathNameURL)
        }
    }


}