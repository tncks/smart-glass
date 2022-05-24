package com.smart.app.util

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@Suppress("unused")
fun Fragment.requestPermissions(request: ActivityResultLauncher<Array<String>>, permissions: Array<String>) =
    request.launch(permissions)

@Suppress("unused")
fun Fragment.isAllPermissionsGranted(permissions: Array<String>) = permissions.all {
    ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
}