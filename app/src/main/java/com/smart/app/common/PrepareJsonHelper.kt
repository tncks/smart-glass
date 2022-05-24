package com.smart.app.common

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PrepareJsonHelper {

    fun prepareCleanRemoveIndexingJson(saIndexStr: String): String {
        var indexCount = 0
        val consKeyFixes = listOf("uid", "idx")
        val jsonObject = JSONObject()
        jsonObject.put(consKeyFixes[indexCount++], SAFEUID)
        val ja = JSONArray()
        ja.put(saIndexStr)
        jsonObject.put(consKeyFixes[indexCount], ja)
        return jsonObject.toString()
    }

    fun prepareLoginUserDataJson(uEmail: String?, uPassword: String?): String {
        var indexCount = 0
        val consKeyFixes = listOf("targetEmail", "targetPassword")
        val jsonObject = JSONObject()
        jsonObject.put(
            consKeyFixes[indexCount++],
            uEmail!!
        )
        jsonObject.put(
            consKeyFixes[indexCount],
            uPassword!!
        )
        return jsonObject.toString()
    }

    fun preparePasswordChangingJson(changingValueItself: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("password", changingValueItself)
        return jsonObject.toString()
    }

    fun prepareFlexibleJson(FilePath: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("thumbnail_image_url", FilePath)
        return jsonObject.toString()
    }


    fun prepareFlexibleJson(datas: List<String>): String {
        val consKeyFixes = listOf("label", "location", "period", "memo")
        val jsonObject = JSONObject()

        for (index in consKeyFixes.indices) {
            jsonObject.put(consKeyFixes[index], datas[index])
        }

        return jsonObject.toString()
    }

    fun prepareAccountJson(datas: List<String>, resultParamStringValue: String): String {
        val firstKeyFix = "uid"
        val consKeyFixes = listOf("email", "password", "nickname")
        val lastKeyFix = "point"
        val jsonObject = JSONObject()


        jsonObject.put(
            firstKeyFix,
            resultParamStringValue
        ) // auto increment value, string parsed from int value, change this later


        for (index in consKeyFixes.indices) {
            jsonObject.put(consKeyFixes[index], datas[index])
        }

        jsonObject.put(lastKeyFix, "0") // this is fixed value, 0

        return jsonObject.toString()
    }


    // Temporarily Deprecated function
    fun prepareJson(two: String, three: String): String {
        val jsonObject = JSONObject()
        val rand = Random()
        val randValue = rand.nextInt(10000)
        val randValueString = randValue.toString()
        val first = "tplan_$randValueString"

        jsonObject.put("category_id", first)
        jsonObject.put("label", two)
        jsonObject.put("thumbnail_image_url", three)
        jsonObject.put("updated", false)
        jsonObject.put("location", "")
        jsonObject.put("period", "")
        jsonObject.put("memo", "")

        return jsonObject.toString()
    }
    // End of deprecated description

}


// Reference for studying
