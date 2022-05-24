package com.smart.app.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileOutputStream

class TempFileIOUtility {

    @Suppress("unused")
    fun createFileFromAbsPath(context: Context, name: String, absolutePath: String): File {

        val stream = File(absolutePath).inputStream()
        val formatSuffix = ".jpg"
        val file = File.createTempFile(name, formatSuffix, context.cacheDir)
        FileUtils.copyInputStreamToFile(stream, file)

        return file
    }

    @Suppress("FoldInitializerAndIfToElvis", "IfThenToElvis")
    fun createFileFromUri(context: Context?, name: String, uri: Uri): File {

        val extForBitFileDotJpg = ".jpg"
        var file: File? = null
        var fos: FileOutputStream?

        fos = FileOutputStream(File.createTempFile("iniName", extForBitFileDotJpg, context?.cacheDir))

        try {
            file = File.createTempFile(name, extForBitFileDotJpg, context?.cacheDir)

            fos.close()
            fos = FileOutputStream(file)

            val bitmap: Bitmap? = if (context != null) resizeBitmapFormUri(uri, context) else null
            if (bitmap == null) {
                return File.createTempFile("errorDesc", extForBitFileDotJpg, context?.cacheDir)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            bitmap.recycle()

            fos.flush()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }

        return if (file != null) {
            file
        } else {
            File.createTempFile("errorDesc", extForBitFileDotJpg, context?.cacheDir)
        }

    }

    private fun resizeBitmapFormUri(uri: Uri, context: Context): Bitmap? {
        val input = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        var bitmap: Bitmap?
        BitmapFactory.Options().run {
            inSampleSize = calculateInSampleSize(options)
            bitmap = BitmapFactory.decodeStream(input, null, this)
        }
        bitmap = bitmap?.let { bitmapItself ->
            rotateImageIfRequired(context, bitmapItself, uri)
        }
        input?.close()
        return bitmap
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        var height = options.outHeight
        var width = options.outWidth
        var inSampleSize = 1
        while (height > MAX_HEIGHT || width > MAX_WIDTH) {
            height /= 2
            width /= 2
            inSampleSize *= 2
        }
        return inSampleSize
    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val exif = if (Build.VERSION.SDK_INT > 23) {
            ExifInterface(input)
        } else {
            ExifInterface(uri.path!!)
        }
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        const val MAX_WIDTH = 1440
        const val MAX_HEIGHT = 1050
    }
}

// Reference
/*

import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max

class ImageUtil {
    companion object {
        fun resizeBitmap(bitmap: Bitmap?, width: Int, height: Int): Bitmap {
            return Bitmap.createScaledBitmap(bitmap!!, width, height, false)
        }

        fun resizeBitmapCenterCrop(bitmap: Bitmap, width: Int, height: Int): Bitmap? {
            val widthTargetRatio = width * 1.0f / bitmap.width
            val heightTargetRatio = height * 1.0f / bitmap.height
            val ratio = max(widthTargetRatio, heightTargetRatio)

            val resizedBitmap = resizeBitmap(bitmap, (bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt())

            return Bitmap.createBitmap(
                resizedBitmap,
                (resizedBitmap.width - width) / 2,
                (resizedBitmap.height - height) / 2,
                width,
                height
            )
        }

        fun blend(bitmap: Bitmap, maskBitmap: Bitmap): Bitmap {
            val blendedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(blendedBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawBitmap(maskBitmap, 0f, 0f, paint)

            return blendedBitmap
        }

        fun blend(bitmap: Bitmap, maskBitmap: Bitmap, backgroundBitmap: Bitmap): Bitmap {
            val blendedBitmap = blend(bitmap, maskBitmap)

            val blendedBitmapWithBackground =
                Bitmap.createBitmap(backgroundBitmap.width, backgroundBitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(blendedBitmapWithBackground)
            canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            canvas.drawBitmap(blendedBitmap, 0f, 0f, paint)

            return blendedBitmapWithBackground
        }


        fun bitmapToByteArray(bitmap: Bitmap): ByteArray? {
            val byteArray = ByteArray(4 * bitmap.width * bitmap.height * 3)
            var byteArrayIndex = 0
            for (y in 0 until bitmap.height) {
                for (x in 0 until bitmap.width) {
                    val pixel = bitmap.getPixel(x, y)
                    val r = (pixel shr 16) and 0xFF
                    val g = (pixel shr 8) and 0xFF
                    val b = pixel and 0xFF
                    //val r: Int = Color.red(pixel)
                    //val g: Int = Color.green(pixel)
                    //val b: Int = Color.blue(pixel)
                    byteArray[byteArrayIndex++] = r.toByte()
                    byteArray[byteArrayIndex++] = g.toByte()
                    byteArray[byteArrayIndex++] = b.toByte()
                }
            }

            return byteArray
        }


        fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
            val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * bitmap.width * bitmap.height * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            for (y in 0 until bitmap.height) {
                for (x in 0 until bitmap.width) {
                    val pixel: Int = bitmap.getPixel(x, y)
                    val r = (pixel shr 16) and 0xFF
                    val g = (pixel shr 8) and 0xFF
                    val b = pixel and 0xFF
                    //val r = Color.red(pixel)
                    //val g = Color.green(pixel)
                    //val b = Color.blue(pixel)
                    byteBuffer.putFloat(r.toFloat())
                    byteBuffer.putFloat(g.toFloat())
                    byteBuffer.putFloat(b.toFloat())
                }
            }

            return byteBuffer
        }


        fun mediaImageToBitmap(mediaImage: Image): Bitmap {
            val byteArray = mediaImageToByteArray(mediaImage)
            val bitmap: Bitmap?
            if (mediaImage.format == ImageFormat.JPEG) {
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            } else if (mediaImage.format == ImageFormat.YUV_420_888) {
                val yuvImage = YuvImage(byteArray, ImageFormat.NV21, mediaImage.width, mediaImage.height, null)
                val out = ByteArrayOutputStream()
                yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
                val imageBytes: ByteArray = out.toByteArray()
                bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            }
            //imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 5)
            else {
                val planes = mediaImage.planes
                val imageBuffer = planes[0].buffer.rewind()

                val pixelStride = planes[0].pixelStride
                val rowStride = planes[0].rowStride
                val rowPadding = rowStride - pixelStride * mediaImage.width

                bitmap = Bitmap.createBitmap(
                    mediaImage.width + rowPadding / pixelStride,
                    mediaImage.height,
                    Bitmap.Config.ARGB_8888
                )
                bitmap.copyPixelsFromBuffer(imageBuffer)
            }

            return bitmap!!
        }

        fun mediaImageToByteArray(mediaImage: Image): ByteArray? {
            var byteArray: ByteArray? = null
            if (mediaImage.format == ImageFormat.JPEG) {
                val buffer0 = mediaImage.planes[0].buffer
                buffer0.rewind()
                val buffer0Size = buffer0.remaining()
                byteArray = ByteArray(buffer0Size)
                buffer0[byteArray, 0, buffer0Size]
            } else if (mediaImage.format == ImageFormat.YUV_420_888) {
                val buffer0 = mediaImage.planes[0].buffer
                val buffer2 = mediaImage.planes[2].buffer
                val buffer0Size = buffer0.remaining()
                val buffer2Size = buffer2.remaining()
                byteArray = ByteArray(buffer0Size + buffer2Size)
                buffer0[byteArray, 0, buffer0Size]
                buffer2[byteArray, buffer0Size, buffer2Size]
            }

            return byteArray
        }

        fun mediaImageToByteBuffer(mediaImage: Image): ByteBuffer {

            return (mediaImageToByteArray(mediaImage)?.let { ByteBuffer.wrap(it) })!!

        }

        fun getRotationDegrees(imagePath: String): Float {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            var rotationDegrees = 0
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 ->
                    rotationDegrees = 90
                ExifInterface.ORIENTATION_ROTATE_180 ->
                    rotationDegrees = 180
                ExifInterface.ORIENTATION_ROTATE_270 ->
                    rotationDegrees = 270
            }

            return rotationDegrees.toFloat()
        }

        fun getRotationDegrees(file: File): Float {
            return getRotationDegrees(file.absolutePath)
        }

//        fun getRotationDegrees(context: Context?, uri: Uri?): Float {
//            val path = getPathFromUri(context!!, uri!!)
//            var rotationDegrees = 0f
//            if (path != null) rotationDegrees = getRotationDegrees(path)
//
//            return rotationDegrees
//        }
//
//        private fun getPathFromUri(context: Context, uri: Uri): String? {
//            var result: String? = null
//            val proj = arrayOf(MediaStore.Images.Media.DATA)
//            val cursor = context.contentResolver.query(uri, proj, null, null, null)
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    val columnIndex = cursor.getColumnIndexOrThrow(proj[0])
//                    result = cursor.getString(columnIndex)
//                }
//                cursor.close()
//            }
//            if (result == null) {
//                result = "Not found"
//            }
//            return result
//        }

        fun getBitmapFromUri(context: Context, uri: Uri?): Bitmap? {

            return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(degree)

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun flipHorizontallyBitmap(bitmap: Bitmap): Bitmap? {
            val matrix = Matrix()
            matrix.setScale(-1.0f, 1.0f)

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun flipVerticallyBitmap(bitmap: Bitmap): Bitmap? {
            val matrix = Matrix()
            matrix.setScale(1.0f, -1.0f)

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun makeRect(aspectRatio: Size, boundingRect: Rect): Rect {
            val heightPerWidthRatio = aspectRatio.height.toFloat() / aspectRatio.width
            if (aspectRatio.width >= aspectRatio.height) {
                val width = boundingRect.width()
                val height = (width * heightPerWidthRatio).toInt()
                val top = ((boundingRect.height() - height) / 2)
                val left = 0
                val right = left + width
                val bottom = top + height

                return Rect(left, top, right, bottom)
            } else {
                val height = boundingRect.height()
                val width = (height / heightPerWidthRatio).toInt()
                val left = ((boundingRect.width() - width) / 2)
                val top = 0
                val right = left + width
                val bottom = top + height

                return Rect(left, top, right, bottom)
            }
        }

        fun normalizedRectForImageRect(imageRect: Rect, imageWidth: Int, imageHeight: Int): RectF {
            val left = imageRect.left.toFloat() / imageWidth
            val top = imageRect.top.toFloat() / imageHeight
            val right = imageRect.right.toFloat() / imageWidth
            val bottom = imageRect.bottom.toFloat() / imageHeight

            return RectF(left, top, right, bottom)
        }

        fun normalizedRectForImageRect(imageRect: RectF, imageWidth: Int, imageHeight: Int): RectF {
            return normalizedRectForImageRect(
                Rect(
                    imageRect.left.toInt(),
                    imageRect.top.toInt(),
                    imageRect.right.toInt(),
                    imageRect.bottom.toInt()
                ), imageWidth, imageHeight
            )
        }

        fun normalizedPointForImagePoint(imagePoint: Point, imageWidth: Int, imageHeight: Int): PointF {
            val x = imagePoint.x.toFloat() / imageWidth
            val y = imagePoint.y.toFloat() / imageHeight

            return PointF(x, y)
        }

        fun imageRectForNormalizedRect(normalizedRect: RectF, imageWidth: Int, imageHeight: Int): RectF {
            val left = normalizedRect.left * imageWidth
            val top = normalizedRect.top * imageHeight
            val right = normalizedRect.right * imageWidth
            val bottom = normalizedRect.bottom * imageHeight

            return RectF(left, top, right, bottom)
        }

        fun imagePointForNormalizedPoint(normalizedPoint: PointF, imageWidth: Int, imageHeight: Int): PointF {
            val x = normalizedPoint.x * imageWidth
            val y = normalizedPoint.y * imageHeight

            return PointF(x, y)
        }
    }
}
*/

