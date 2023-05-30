package com.helper.util

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


/**
 * UnzipUtils class extracts files and sub-directories of a standard zip file to
 * a destination directory.
 *
 */
object UnzipUtils {
    /**
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: File) {
        destDirectory.run {
            if (!exists()) {
                mkdirs()
            }
        }
        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val filePath = File(destDirectory, entry.name)
                    if (!entry.isDirectory) {
                        // if the entry is a file, extracts it
                        extractFile(input, filePath)
                    } else {
                        // if the entry is a directory, make the directory
                        val dir = filePath
                        dir.mkdir()
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    fun unzipAssets(zipFilePath: InputStream?, destDirectory: File) {
        if(zipFilePath == null){
            return
        }
        val zin = ZipInputStream(zipFilePath)
        var ze: ZipEntry
        while (zin.nextEntry.also { ze = it } != null) {
            val filePath = File(destDirectory , ze.name)
            if (ze.isDirectory()) {
                // if the entry is a directory, make the directory
                val dir = filePath
                dir.mkdir()
            } else {
                val fout = FileOutputStream(filePath)
                var c = zin.read()
                while (c != -1) {
                    fout.write(c)
                    c = zin.read()
                }
                zin.closeEntry()
                fout.close()
            }
        }
        zin.close()
    }

    /**
     * Extracts a zip entry (file entry)
     * @param inputStream
     * @param destFilePath
     * @throws IOException
     */
    @Throws(IOException::class)
    fun extractFile(inputStream: InputStream?, destFilePath: File) {
        inputStream?.let {
            val bos = BufferedOutputStream(FileOutputStream(destFilePath))
            val bytesIn = ByteArray(BUFFER_SIZE)
            var read: Int
            while (it.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
            bos.close()
        }
    }

    /**
     * Size of the buffer to read/write data
     */
    private const val BUFFER_SIZE = 4096

}