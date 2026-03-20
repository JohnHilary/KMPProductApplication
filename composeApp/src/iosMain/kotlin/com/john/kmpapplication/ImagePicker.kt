package com.john.kmpapplication


import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.*
import platform.darwin.NSObject
import platform.posix.memcpy

actual class ImagePicker {

    actual fun pickImage(type: PickerType, onResult: (ByteArray?) -> Unit) {

        val picker = UIImagePickerController()

        picker.sourceType = when (type) {
            PickerType.CAMERA -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            PickerType.GALLERY -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        }

        val delegate = object : NSObject(),
            UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                val data = image?.let { UIImageJPEGRepresentation(it, 1.0) }

                val byteArray = data?.toByteArray()

                onResult(byteArray)
                picker.dismissViewControllerAnimated(true, null)
            }
        }

        picker.delegate = delegate

        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(picker, true, null)
    }
}
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val byteArray = ByteArray(length.toInt())

    byteArray.usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }

    return byteArray
}