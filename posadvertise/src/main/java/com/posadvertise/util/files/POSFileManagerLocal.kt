//package com.posadvertise.util.files
//
//import android.content.Context
//import com.helper.util.FileUtils
//import com.posadvertise.util.POSAdvertiseConstants
//import com.posadvertise.util.POSFileManager
//import java.io.File
//
//class POSFileManagerLocal : POSFileManager {
//
//    private val folderBanner: String = "Banner-local"
//    private val folderScreenSaver: String = "ScreenSaver"
//    private val folderTutorial: String = "Tutorial"
//    private val folderUpdate: String = "Update"
//
//    override fun isDynamic(): Boolean {
//       return false
//    }
//
//    override fun getFileStoreDirectory(context: Context): File {
//        return FileUtils.getFileStoreDirectory(context)
//    }
//
//    override fun getPOSAdvertiseFolder(context: Context): File {
//        return File(getFileStoreDirectory(context) , POSAdvertiseConstants.AdvertiseFolder)
//    }
//    override fun getPOSBannerFolder(context: Context): File {
//        return File(getFileStoreDirectory(context) , getFolderBanner())
//    }
//    override fun getPOSScreenSaverFolder(context: Context): File {
//        return File(getFileStoreDirectory(context) , getFolderScreenSaver())
//    }
//    override fun getPOSTutorialFolder(context: Context): File {
//        return File(getFileStoreDirectory(context) , getFolderTutorial())
//    }
//
//    override fun getPOSUpdateAPKFolder(context: Context): File {
//        return File(getFileStoreDirectory(context) , getFolderUpdate())
//    }
//
//    override fun getFolderBanner(): String {
//        return folderBanner
//    }
//
//    override fun getFolderScreenSaver(): String {
//        return folderScreenSaver
//    }
//
//    override fun getFolderTutorial(): String {
//        return folderTutorial
//    }
//
//    override fun getFolderUpdate(): String {
//        return folderUpdate
//    }
//
//    override fun getUpdatedAPKFile(context: Context): File? {
//        val folderUpdate = getPOSUpdateAPKFolder(context)
//        folderUpdate.listFiles()?.let {
//            for (file in it){
//                return file
//            }
//        }
//        return null
//    }
//}