package com.john.kmpapplication

import androidx.compose.runtime.staticCompositionLocalOf

expect class ImagePicker {
    fun pickImage(type: PickerType,onResult: (ByteArray?) -> Unit)

}
enum class PickerType {
    CAMERA,
    GALLERY
}

val LocalImagePicker = staticCompositionLocalOf<ImagePicker> {
    error("ImagePicker not provided")
}