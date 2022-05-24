package com.smart.app.common

class PatchHelperUtility {
    fun reviseHelperUtil(tmpParams: List<String>, index: Int, URL: String): String {
        var revisedString = ""
        for (i in 0 until index) {
            revisedString += tmpParams[i]
            revisedString += DELIM
        }
        revisedString += URL
        revisedString += DELIM
        for (i in index + 1 until tmpParams.size) {
            revisedString += tmpParams[i]
            revisedString += DELIM
        }
        revisedString.dropLast(1)
        return revisedString
    }
}