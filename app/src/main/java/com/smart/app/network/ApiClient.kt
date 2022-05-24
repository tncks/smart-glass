package com.smart.app.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smart.app.common.FIRE_JSON_BASEURL
import com.smart.app.model.Category
import com.smart.app.model.CategoryDetail
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

interface ApiClient {

    @GET("users/{uid}/categories.json")
    suspend fun getCategories(@Path("uid") uid: String): List<Category>?

    @GET("detail_test.json")
    suspend fun getCategoryDetail(): CategoryDetail


    companion object {

        fun create(): ApiClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()


            return Retrofit.Builder()
                .baseUrl(FIRE_JSON_BASEURL)
                .client(client)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiClient::class.java)

        }

        class NullOnEmptyConverterFactory : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *>? {
                val gson = Gson()
                val adapter = gson.getAdapter(TypeToken.get(type))

                return object : Converter<ResponseBody, Any> {
                    override fun convert(body: ResponseBody): Any? {
                        try {
                            // 4L eqaul to Null length str null
                            if (body.contentLength() == 4L) {
                                return listOf<Category>()
                            }
                            val jsonReader = gson.newJsonReader(body.charStream())
                            return adapter.read(jsonReader)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return listOf<Category>()
                        } finally {
                            body.close()
                        }
                    }
                }
            }
        }
    }
}


