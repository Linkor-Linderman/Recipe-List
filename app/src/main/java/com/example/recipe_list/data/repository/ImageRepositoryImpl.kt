package com.example.recipe_list.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.recipe_list.R
import com.example.recipe_list.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageRepositoryImpl(private val context: Context, ) : ImageRepository {
    override suspend fun downloadImage(url: String, fileName: String) {
        val connection = withContext(Dispatchers.IO) {
            URL(url).openConnection()
        } as HttpURLConnection
        val inputStream = connection.inputStream
        val buffer = ByteArray(1024)
        var len = 0

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                }
                val imageUri =
                    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(imageUri!!).use { outputStream ->
                    while (inputStream.read(buffer).also { len = it } != -1) {
                        outputStream?.write(buffer, 0, len)
                    }
                }
            } else {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
                )
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { outputStream ->
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            outputStream.write(buffer, 0, len)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw IOException(context.getString(R.string.error_download_image), e)
        }
    }
}