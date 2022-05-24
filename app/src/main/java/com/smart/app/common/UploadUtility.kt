package com.smart.app.common

import android.webkit.MimeTypeMap
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UploadUtility {

    private val serverURL = BACK_AZURE_STATIC_WEB_MEDIA_FILE_SERVER_URL
    private val client = OkHttpClient()
    private var isSuccess: Boolean = false

    @Suppress("unused", "BooleanMethodIsAlwaysInverted")
    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null): Boolean {
        return uploadFile(File(sourceFilePath), uploadedFileName)
    }


    @Suppress("FoldInitializerAndIfToElvis", "RedundantExplicitType", "BooleanMethodIsAlwaysInverted")
    fun uploadFile(sourceFile: File, uploadedFileName: String? = null): Boolean {

        val t = Thread {
            val mimeType: String? = getMimeType(sourceFile)
            if (mimeType == null) {
                return@Thread
            }
            val fileName: String = (uploadedFileName ?: sourceFile.name)

            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "imgFile",
                            fileName,
                            sourceFile.asRequestBody(mimeType.toMediaTypeOrNull())
                        )
                        .build()

                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {

                    val rawBodyData: ResponseBody? = response.body
                    val translatedStringData = rawBodyData!!.string()
                    val dataHeader: String = translatedStringData.split("<")[0]
                    var lastnChars: String = ""
                    if (dataHeader.length > 2) {
                        lastnChars = dataHeader.substring(dataHeader.length - 2, dataHeader.length)
                    }
                    isSuccess = when (lastnChars) {
                        "11" -> false
                        "00" -> true
                        else -> false
                    }

                } else {
                    isSuccess = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        t.start()
        t.join()
        return getterOfIsSuccess()
    }


    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


    private fun getterOfIsSuccess(): Boolean {
        return this.isSuccess
    }


}


// deprecated
// Refer
// revised from
// private val serverUploadDirectoryPath = BACK_AZURE_STATIC_WEB_MEDIA_FILE_SERVER_IMAGE_DIR_URI