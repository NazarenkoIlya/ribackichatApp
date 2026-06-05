package com.example.rybackiapp.utils.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import androidx.core.graphics.scale

class UriToFileConverter @Inject constructor() {

    suspend fun convertUriToFile(
        uri: Uri,
        uid: String,
        context: Context
    ): File? = withContext(Dispatchers.IO) {
        try {
            // Получаем расширение файла
            //val extension = getFileExtension(uri) ?: "jpg"
            Log.d("SUPA", "convertUriToFile: ${uid}")


            val fileName = "${UUID.randomUUID()}.jpg"

            //val fileName = "${UUID.randomUUID()}.jpg"
            val file = File(context.cacheDir, fileName)


//            // Создаем временный файл в кэше приложения
//            val tempFile = File(context.cacheDir, "temp_avatar_${System.currentTimeMillis()}.$extension")

            // Копируем содержимое URI во временный файл
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val compressedFile = compressImageToSize(file)

            return@withContext compressedFile?.takeIf { it.exists() && it.length() > 0 }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }


    private fun compressImageToSize(
        imageFile: File,
        targetSizeKB: Int = 12,
        minDim: Int = 256,
        maxCompressQuality: Int = 30
    ): File? {

        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        var width = options.outWidth
        var height = options.outHeight

        var scale = 1.0
        while (width > minDim || height > minDim) {
            scale *= 0.8
            width = (options.outWidth * scale).toInt()
            height = (options.outHeight * scale).toInt()
        }

        val sampleSize = (1 / scale).toInt().coerceAtLeast(1)
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        var bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, decodeOptions) ?: return null

        var quality = 95
        var outputStream = ByteArrayOutputStream()
        var currentSizeKB = targetSizeKB + 1

        while (currentSizeKB > targetSizeKB && quality > maxCompressQuality) {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
            currentSizeKB = outputStream.size() / 1024
            quality -= 10
        }

        var currentBitmap = bitmap
        while (currentSizeKB > targetSizeKB && (currentBitmap.width > 100 || currentBitmap.height > 100)) {
            currentBitmap = currentBitmap.scale(
                (currentBitmap.width * 0.8).toInt(),
                (currentBitmap.height * 0.8).toInt()
            )
            outputStream.reset()
            currentBitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
            currentSizeKB = outputStream.size() / 1024
        }

        val resultFile = File.createTempFile("compressed_img", ".webp")
        FileOutputStream(resultFile).use { fos ->
            fos.write(outputStream.toByteArray())
        }
        currentBitmap.recycle()
        bitmap.recycle()

        Log.d("COMPRESS", "compressImageToSize: ${resultFile.length()/1024}")

        return resultFile
    }

//    private fun getFileExtension(uri: Uri): String? {
//        val mimeType = context.contentResolver.getType(uri)
//        return when {
//            mimeType?.contains("png") == true -> "png"
//            mimeType?.contains("jpeg") == true -> "jpg"
//            mimeType?.contains("jpg") == true -> "jpg"
//            mimeType?.contains("webp") == true -> "webp"
//            else -> "jpg" // дефолтное расширение
//        }
//    }
}