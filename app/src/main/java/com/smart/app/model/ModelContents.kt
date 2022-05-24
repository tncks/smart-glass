package com.smart.app.model

import android.net.Uri

data class ModelContents(
    var strFolder: String,
    var alImageuri: ArrayList<Uri>
)

fun ModelContents.getAlImageuri(): ArrayList<Uri> {
    return this.alImageuri
}

fun ModelContents.getStrFolder(): String {
    return this.strFolder
}

fun ModelContents.setAlImageuri(alImageuri: ArrayList<Uri>) {
    this.alImageuri = alImageuri
}

fun ModelContents.setStrFolder(strFolder: String) {
    this.strFolder = strFolder
}
