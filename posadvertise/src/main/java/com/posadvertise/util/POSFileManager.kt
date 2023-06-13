package com.posadvertise.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.helper.util.FileUtils
import com.posadvertise.banner.POSBanner
import com.posadvertise.banner.model.BannerType
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStream

const val CONFIG_BANNER_JSON = "config_banner.json"
const val CONFIG_SCREEN_SAVER_JSON = "config_screen_saver.json"
const val CONFIG_TUTORIAL_JSON = "config_tutorial.json"
const val CONFIG_JSON = "config.json"

class POSFileManager {

    private val folderBanner: String = "Banner"
    private val folderBannerLocal: String = "Banner-local"
    private val folderScreenSaver: String = "ScreenSaver"
    private val folderTutorial: String = "Tutorial"
    private val folderUpdate: String = "Update"

    fun getFileStoreDirectory(context: Context): File {
        return FileUtils.getFileStoreDirectory(context)
    }

    fun getPOSAdvertiseFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , POSAdvertiseConstants.AdvertiseFolder)
    }
    private fun getPOSBannerFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , getFolderBanner())
    }
    private fun getPOSBannerFolderLocal(context: Context): File {
        return File(getFileStoreDirectory(context) , getFolderBannerLocal())
    }
    fun getPOSBannerFolder(context: Context, bannerType: BannerType): File {
        if(bannerType == BannerType.Downloaded){
            return getPOSBannerFolder(context)
        }else if(bannerType == BannerType.Local){
            return getPOSBannerFolderLocal(context)
        }else{
            return if(POSBanner.mProperty != null) getPOSBannerFolder(context) else getPOSBannerFolderLocal(context)
        }
    }

    fun getPOSScreenSaverFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , getFolderScreenSaver())
    }
    fun getPOSTutorialFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , getFolderTutorial())
    }

    fun getPOSUpdateAPKFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , getFolderUpdate())
    }

    private fun getFolderBanner(): String {
        return folderBanner
    }

    private fun getFolderBannerLocal(): String {
        return folderBannerLocal
    }

    private fun getFolderScreenSaver(): String {
        return folderScreenSaver
    }

    private fun getFolderTutorial(): String {
        return folderTutorial
    }

    private fun getFolderUpdate(): String {
        return folderUpdate
    }

    fun getUpdatedAPKFile(context: Context): File? {
        val folderUpdate = getPOSUpdateAPKFolder(context)
        folderUpdate.listFiles()?.let {
            for (file in it){
                return file
            }
        }
        return null
    }

    fun getUriFromFile(context: Context, file: File): Uri {
        val fileProvider = context.packageName + context.getString(com.posadvertise.R.string.file_provider)
        return FileProvider.getUriForFile(context, fileProvider, file)
    }

    fun getPOSDownloadsFolder(context: Context): File {
        return getFileStoreDirectory(context)
    }

    fun readFile(file: File): String {
        try {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append("${line}\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            return stringBuilder.toString()
        }catch (e : java.lang.Exception){
            return ""
        }
    }

    fun getAssetsFile(context: Context, fileName: String): InputStream? {
        try {
            return context.assets.open(fileName)
        } catch (e: Exception) {
            return null
        }
    }
}