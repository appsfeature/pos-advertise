package com.posadvertise.util


import com.helper.task.AsyncThread
import com.posadvertise.logAdv
import com.posadvertise.util.network.HttpsTrustManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HTTPSDownloadManager(private var downloadURL: String, var downloadedFilePath : File, var fileName : String, private var callback: Callback) : AsyncThread<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String {
        var input: InputStream? = null
        var output: OutputStream? = null
        val connection: HttpsURLConnection
        val outputFile = downloadedFilePath

        try {
            logAdv("HTTPS Download URL:- $downloadURL")
            val url = URL(downloadURL)
            HttpsTrustManager.allowAllSSL()
            connection = url.openConnection() as HttpsURLConnection

            connection.readTimeout = 100000
            connection.connectTimeout = 100000
            connection.requestMethod = "GET"
            connection.connect()

            val fileLength = connection.contentLength

            if (outputFile.exists()) {
                outputFile.delete()
            }
            outputFile.getParentFile()?.mkdirs();
            outputFile.createNewFile()

            input = connection.inputStream
            output = FileOutputStream(outputFile)
            val data = ByteArray(1024)
            var total: Long = 0
            //var count: Int
            while (true) {
                val length = input.read(data)
                total += length.toLong()
                if (length <= 0)
                    break
                publishProgress((total * 100 / fileLength).toInt())
                output.write(data, 0, length)
            }

            // fileUri = downloadedFilePath
            return outputFile.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onError(e)
            logAdv("Exception : ${e}")
            return ""
        } finally {
            input?.close()
            output?.close()
        }

    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        values[0]?.let { callback.onProgressUpdate(it) }
    }

    override fun onCancelled() {
        super.onCancelled()
        callback.onError(Exception("Downloading Cancelled"))
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result?.isNotEmpty() == true) {
            callback.onDownloadComplete(result, fileName, downloadedFilePath)
        }else{
            callback.onError(Exception("Failed to download file."))
        }
    }

    interface Callback {
        fun onDownloadComplete(path: String, appName: String, fileUri: File?)
        fun onProgressUpdate(progress : Int)
        fun onError(e: java.lang.Exception)
    }

}