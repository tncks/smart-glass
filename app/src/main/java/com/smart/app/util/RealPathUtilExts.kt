package com.smart.app.util

import android.content.Context
import android.net.Uri


@Suppress("unused")
fun Uri?.getFilePath(context: Context): String {
    return this?.let { uri -> RealPathUtil.getRealPath(context, uri) ?: "" } ?: ""
}


// Refer

/*------ Extension on intent ------*/

//fun Intent?.getFilePath(context: Context): String {
//    return this?.data?.let { data -> RealPathUtil.getRealPath(context, data) ?: "" } ?: ""
//}


//fun ClipData.Item?.getFilePath(context: Context): String {
//    return this?.uri?.getFilePath(context) ?: ""
//}
