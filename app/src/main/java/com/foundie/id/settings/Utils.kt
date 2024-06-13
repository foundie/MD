package com.foundie.id.settings

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.util.Log
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowInsetsControllerCompat
import com.foundie.id.R
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val SETTINGS_KEY = "settings"
const val delayTime: Long = 1000
const val delayTimeSlider: Long = 2000

fun lightStatusBar(window: Window, isLight: Boolean = true) {
    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = isLight
}

fun updateStatusBarTheme(window: Window, config: Configuration) {
    val isLight = when (config.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> true
        Configuration.UI_MODE_NIGHT_YES -> false
        else -> true
    }
    lightStatusBar(window, isLight)
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

fun String.addCacheBusting(): String {
    return if (this.contains("?")) {
        "$this&timestamp=${System.currentTimeMillis()}"
    } else {
        "$this?timestamp=${System.currentTimeMillis()}"
    }
}

fun ImageView.loadImageWithCacheBusting(url: String?) {
    url?.let {
        val cacheBustingUrl = it.addCacheBusting()
        Picasso.get()
            .load(cacheBustingUrl)
            .into(this, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.d("Picasso", "Success Load Image")
                }

                override fun onError(e: Exception?) {
                    Log.d("Picasso", "Error Load Image, $e")
                }
            })
    }
}

suspend fun compressFile(context: Context, file: File): File? {
    var compressedFile: File? = null
    var compressedFileSize = file.length()

    if (compressedFileSize > 1 * 1024 * 1024) {
        compressedFile = withContext(Dispatchers.Default) {
            Compressor.compress(context, file)
        }
    }

    return compressedFile ?: file
}

    fun convertDateTime(datetime: String?): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

            val date = inputFormat.parse(datetime.toString())
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertBitmap(context: Context, urlString: String): Bitmap {
        return try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_logo)
        }
    }

    fun getTimeAgo(time: Long): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - time

        val minute = 60 * 1000
        val hour = 60 * minute
        val day = 24 * hour

        return when {
            diff < minute -> "just now"
            diff < 2 * minute -> "a minute ago"
            diff < 50 * minute -> "${diff / minute} minutes ago"
            diff < 90 * minute -> "an hour ago"
            diff < 24 * hour -> "${diff / hour} hours ago"
            diff < 48 * hour -> "yesterday"
            else -> "${diff / day} days ago"
        }
    }