package com.john.kmpapplication

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

actual class ImagePicker(private val activity: ComponentActivity) {

    private var callback: ((ByteArray?) -> Unit)? = null

    private val galleryLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            val bytes = uri?.let {
                activity.contentResolver.openInputStream(it)?.readBytes()
            }
            callback?.invoke(bytes)
        }

    private val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            callback?.invoke(null)
        }
    }

    private val cameraLauncher = activity.registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: android.graphics.Bitmap? ->
        if (bitmap != null) {
            val stream = java.io.ByteArrayOutputStream()
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, stream)
            val byteArray = stream.toByteArray()
            callback?.invoke(byteArray)
        } else {
            callback?.invoke(null)
        }
    }

    actual fun pickImage(type: PickerType, onResult: (ByteArray?) -> Unit) {
        callback = onResult

        when (type) {
            PickerType.GALLERY -> {
                galleryLauncher.launch("image/*")
            }

            PickerType.CAMERA -> {
                val status = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                if (status == PackageManager.PERMISSION_GRANTED) {
                    launchCamera()
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    }
    private fun launchCamera() {
        cameraLauncher.launch(null)
    }
}