package com.john.kmpapplication


import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.*
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy

actual class ImagePicker {

    actual fun pickImage(type: PickerType, onResult: (ByteArray?) -> Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

        if (type == PickerType.CAMERA) {
            if (!UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
                onResult(null)
                return
            }

            val cameraDelegate = CameraDelegate(onResult)
            val picker = UIImagePickerController().apply {
                sourceType =
                    UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                delegate = cameraDelegate
            }

            PickerRuntimeStore.activeDelegate = cameraDelegate
            rootViewController?.presentViewController(picker, animated = true, completion = null)

        } else {
            val configuration = PHPickerConfiguration().apply {
                filter = PHPickerFilter.imagesFilter()
                selectionLimit = 1
            }

            val galleryDelegate = GalleryDelegate(onResult)
            val picker = PHPickerViewController(configuration).apply {
                delegate = galleryDelegate
            }

            PickerRuntimeStore.activeDelegate = galleryDelegate
            rootViewController?.presentViewController(picker, animated = true, completion = null)
        }
    }
}

private class GalleryDelegate(
    private val onResult: (ByteArray?) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>
    ) {
        picker.dismissViewControllerAnimated(true, null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult
        val provider = result?.itemProvider

        if (provider?.hasItemConformingToTypeIdentifier("public.image") == true) {

            provider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->

                val byteArray = data?.toByteArray()

                dispatch_async(dispatch_get_main_queue()) {
                    onResult(byteArray)
                }
            }

        } else {
            onResult(null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun imageToByteArray(image: UIImage): ByteArray? {
    val data = UIImageJPEGRepresentation(image, 0.8) ?: return null
    val bytes = ByteArray(data.length.toInt())
    bytes.usePinned { pinned ->
        memcpy(pinned.addressOf(0), data.bytes, data.length)
    }
    return bytes
}

private class CameraDelegate(
    private val onResult: (ByteArray?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val bytes = image?.let { imageToByteArray(it) }

        onResult(bytes)
        picker.dismissViewControllerAnimated(true) {
            PickerRuntimeStore.activeDelegate = null
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onResult(null)
        picker.dismissViewControllerAnimated(true) {
            PickerRuntimeStore.activeDelegate = null
        }
    }
}

private object PickerRuntimeStore {
    var activeDelegate: Any? = null
}
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    return ByteArray(length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), bytes, length)
        }
    }
}