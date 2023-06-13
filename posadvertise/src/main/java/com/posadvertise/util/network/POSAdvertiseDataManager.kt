package com.posadvertise.util

import android.content.Context
import com.helper.util.FileUtils
import com.helper.util.UnzipUtils
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.logBanner
import com.posadvertise.banner.model.BannerType
import com.posadvertise.logAdv
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException


object POSAdvertiseDataManager {
    fun extractLocalZipFile(context: Context, callback: POSAdvertiseCallback.Callback<Boolean>?) {
        CoroutineScope(Dispatchers.IO).launch {
            val task = async { extractLocalFiles(context, callback) }
            task.await().apply {
                POSAdvertise.resetPOSAdvertiseProperty(context)
                if(this) {
                    callback?.onSuccess(true)
                }else{
                    callback?.onFailure(Exception("Invalid Extraction"))
                }
            }
        }
    }

    private fun extractLocalFiles(
        context: Context,
        callback: POSAdvertiseCallback.Callback<Boolean>?
    ): Boolean {
        val fileManager = POSAdvertiseUtility.getStorage()
        val downloadZipFile = File(
            createDirectory(fileManager.getPOSDownloadsFolder(context)),
            POSAdvertiseConstants.AdvertiseFileName
        )
        val assetsFile = fileManager.getAssetsFile(context, POSAdvertiseConstants.AdvertiseFileName)
        if(assetsFile != null) {
            FileUtils.saveAssetsToStorage(assetsFile, downloadZipFile)
            return extractFiles(context, fileManager, true, callback)
        }else{
            return false
        }
    }

    fun downloadUpdate(context: Context, downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>?) {
        val fileName = POSAdvertiseConstants.AdvertiseFileName
        val fileManager = POSAdvertiseUtility.getStorage()
        val downloadedFilePath = File(fileManager.getPOSDownloadsFolder(context), fileName)
        HTTPSDownloadManager(
            downloadFileUrl,
            downloadedFilePath,
            fileName,
            object : HTTPSDownloadManager.Callback {
                override fun onDownloadComplete(path: String, appName: String, fileUri: File?) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val task = async { extractFiles(context, fileManager, false, callback) }
                        launch(Dispatchers.Main) {
                            task.await().apply {
                                POSAdvertise.resetPOSAdvertiseProperty(context)
                                POSAdvertisePreference.setUpdateAvailable(context, this)
                                callback?.onSuccess(this)
                            }
                        }
                    }
                }

                override fun onProgressUpdate(progress: Int) {
                    callback?.onProgressUpdate(progress)
                }

                override fun onError(e: Exception) {
                    callback?.onFailure(e)
                }
            }).execute()
    }

    private fun extractFiles(
        context: Context,
        fileManager: POSFileManager,
        isLocal: Boolean,
        callback: POSAdvertiseCallback.Callback<Boolean>?
    ): Boolean {
        try {
            //existing folder rename
            val downloadsFolder = createDirectory(fileManager.getPOSDownloadsFolder(context))
            //create POSAdvertise folder for extract files in it.
            createDirectory(fileManager.getPOSAdvertiseFolder(context))

            val downloadZipFile = File(
                fileManager.getPOSDownloadsFolder(context),
                POSAdvertiseConstants.AdvertiseFileName
            )
            if (downloadZipFile.exists()) {
                UnzipUtils.unzip(downloadZipFile, downloadsFolder)
                downloadZipFile.delete()
            }
            copyFiles(context, fileManager, isLocal)
            readFiles(context, fileManager)
            return true
        } catch (e: Exception) {
            logAdv(e.toString())
            callback?.onFailure(e)
            return false
        }
    }

    private fun copyFiles(context: Context, fileManager: POSFileManager, isLocal: Boolean) {
        val folderBanner = createDirectory(fileManager.getPOSBannerFolder(context, BannerType.Downloaded))
        val folderBannerLocal = createDirectory(fileManager.getPOSBannerFolder(context, BannerType.Local))
        val folderScreenSaver = createDirectory(fileManager.getPOSScreenSaverFolder(context))
        val folderTutorial = createDirectory(fileManager.getPOSTutorialFolder(context))
        val folderUpdate = createDirectory(fileManager.getPOSUpdateAPKFolder(context))
        val mainFolder = fileManager.getFileStoreDirectory(context)
        val advFolder = fileManager.getPOSAdvertiseFolder(context)
        if(advFolder.exists()){
            val folders = advFolder.listFiles()
            folders?.let {
                for (folder in it){
                    if (equalsString(folder.name, POSAdvertiseConstants.zipFolderBanner)) {
                        if(isLocal) {
                            copyFiles(folderBannerLocal, folder.listFiles(), true)
                        }else{
                            copyFiles(folderBanner, folder.listFiles(), true)
                        }
                    }
                    //For update local banners from server side.
                    else if (equalsString(folder.name, POSAdvertiseConstants.zipFolderBannerLocal)) {
                        copyFiles(folderBannerLocal, folder.listFiles(), true)
                    }
                    else if (equalsString(folder.name, POSAdvertiseConstants.zipFolderScreenSaver)) {
                        copyFiles(folderScreenSaver, folder.listFiles(), true)
                    }
                    else if (equalsString(folder.name, POSAdvertiseConstants.zipFolderTutorial)) {
                        copyFiles(folderTutorial, folder.listFiles(), false)
                    }
                    else if (equalsString(folder.name, POSAdvertiseConstants.zipFolderUpdate)) {
                        copyFiles(folderUpdate, folder.listFiles(), true)
                    }else if (equalsString(folder.name, POSAdvertiseConstants.zipFileConfig)) {
                        logBanner("copyFile config.json")
                        copyFile(mainFolder, folder)
                    }
                }
            }
            advFolder.deleteRecursively()
        }
    }

    private fun equalsString(val1: String, val2: String): Boolean {
        return val1.equals(val2, ignoreCase = true)
    }

    @Throws(IOException::class)
    private fun readFiles(context: Context, fileManager: POSFileManager) {
        val folderMain = fileManager.getPOSDownloadsFolder(context)
        if(folderMain.exists()){
            val folderBanner = File(fileManager.getPOSBannerFolder(context, BannerType.Downloaded), CONFIG_BANNER_JSON)
            val folderBannerLocal = File(fileManager.getPOSBannerFolder(context, BannerType.Local), CONFIG_BANNER_JSON)
            val folderScreenSaver = File(fileManager.getPOSScreenSaverFolder(context), CONFIG_SCREEN_SAVER_JSON)
            val folderTutorial = File(fileManager.getPOSTutorialFolder(context), CONFIG_TUTORIAL_JSON)
            val configMainFile = File(fileManager.getFileStoreDirectory(context), CONFIG_JSON)

            if(folderBanner.exists()){
                val configJson = fileManager.readFile(folderBanner)
                POSAdvertisePreference.setConfigBannerJson(context, true, configJson)
            }
            if (folderBannerLocal.exists()) {
                val configJson = fileManager.readFile(folderBannerLocal)
                POSAdvertisePreference.setConfigBannerJson(context, false, configJson)
            }
            if(folderScreenSaver.exists()){
                val configJson = fileManager.readFile(folderScreenSaver)
                POSAdvertisePreference.setConfigScreenSaverJson(context, configJson)
            }
            if(folderTutorial.exists()){
                val configJson = fileManager.readFile(folderTutorial)
                POSAdvertisePreference.setConfigTutorialJson(context, configJson)
            }
            if(configMainFile.exists()){
                val configJson = fileManager.readFile(configMainFile)
                POSAdvertisePreference.setConfigMainJson(context, configJson)
            }
            //9810862622 gayanender
        }
    }

    private fun createDirectory(file: File): File {
        file.run {
            if (!exists()) {
                mkdirs()
            }
        }
        return file
    }


    private fun copyFiles(targetFilePath: File, files: Array<File>?, isFolderClear: Boolean) {
        if(isFolderClear) {
            targetFilePath.deleteRecursively()
        }
        files?.let {
            for (file in it){
                copyFile(targetFilePath, file)
            }
        }
    }

    private fun copyFile(targetFilePath: File, file: File) {
        val targetFile = File(targetFilePath, file.name)
        if (targetFile.exists()) {
            targetFile.delete()
        }
        logBanner("copyFile ${file.name}")
        file.copyRecursively(targetFile, true)
    }


}