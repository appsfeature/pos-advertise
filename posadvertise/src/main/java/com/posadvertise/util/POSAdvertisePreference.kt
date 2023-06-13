package com.posadvertise.util

import android.content.Context
import com.google.gson.Gson
import com.helper.util.BasePrefUtil
import com.posadvertise.util.common.ConfigModel

object POSAdvertisePreference {

    private const val IS_UPDATE_AVAILABLE = "is_update_available"
    private const val IS_UPDATE_CONFIRMED = "is_update_confirmed"
    private const val UPDATE_URL_DATA = "UPDATE_DOWNLOADED_DATA"
    private const val CONFIG_BANNER = "config_banner"
    private const val CONFIG_BANNER_LOCAL = "config_banner_local"
    private const val CONFIG_SCREEN_SAVER = "config_screen_saver"
    private const val CONFIG_TUTORIAL = "config_tutorial"
    private const val SCHEDULE_TYPE = "schedule_type"
    private const val SCHEDULE_NO = "schedule_no"
    private const val INTERNAL_ZIP_FILE = "internal_zip_file"
    private const val CONFIG_MAIN = "config_main"

    fun setInternalZipFileExtracted(context: Context?, versionCode: Int, value: Boolean) {
        BasePrefUtil.setBoolean(context, INTERNAL_ZIP_FILE + versionCode, value)
    }

    fun isInternalZipFileExtracted(context: Context?, versionCode: Int): Boolean {
        return BasePrefUtil.getBoolean(context, INTERNAL_ZIP_FILE + versionCode, false)
    }

    fun setUpdateAvailable(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, IS_UPDATE_AVAILABLE, value)
    }

    fun isUpdateAvailable(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, IS_UPDATE_AVAILABLE, false)
    }

    fun setUpdateConfirmed(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, IS_UPDATE_CONFIRMED, value)
    }

    fun isUpdateConfirmed(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, IS_UPDATE_CONFIRMED, true)
    }

    fun setUpdateUrlData(context: Context?, value: String?) {
        BasePrefUtil.setString(context, UPDATE_URL_DATA, value)
    }

    fun getUpdateUrlData(context: Context?): String? {
        return BasePrefUtil.getString(context, UPDATE_URL_DATA, "")
    }

    fun getConfigBannerJson(context: Context?, isDynamic: Boolean): String? {
        return if(isDynamic) {
            BasePrefUtil.getString(context, CONFIG_BANNER)
        }else{
            BasePrefUtil.getString(context, CONFIG_BANNER_LOCAL)
        }
    }

    fun setConfigBannerJson(context: Context?, isDynamic: Boolean, value: String?) {
        if(isDynamic) {
            BasePrefUtil.setString(context, CONFIG_BANNER, value)
        }else{
            BasePrefUtil.setString(context, CONFIG_BANNER_LOCAL, value)
        }
    }

    fun getConfigScreenSaverJson(context: Context?): String? {
        return BasePrefUtil.getString(context, CONFIG_SCREEN_SAVER)
    }

    fun setConfigScreenSaverJson(context: Context?, value: String?) {
        BasePrefUtil.setString(context, CONFIG_SCREEN_SAVER, value)
    }

    fun getConfigTutorialJson(context: Context?): String? {
        return BasePrefUtil.getString(context, CONFIG_TUTORIAL)
    }

    fun setConfigTutorialJson(context: Context?, value: String?) {
        BasePrefUtil.setString(context, CONFIG_TUTORIAL, value)
    }

    fun getConfigMainJson(context: Context?): String? {
        return BasePrefUtil.getString(context, CONFIG_MAIN)
    }

    fun setConfigMainJson(context: Context?, value: String?) {
        BasePrefUtil.setString(context, CONFIG_MAIN, value)
    }

    fun getScheduleType(context: Context?): String? {
        return BasePrefUtil.getString(context, SCHEDULE_TYPE)
    }

    fun setScheduleType(context: Context?, value: String?) {
        BasePrefUtil.setString(context, SCHEDULE_TYPE, value)
    }

    fun getScheduleNo(context: Context?): Int {
        return BasePrefUtil.getInt(context, SCHEDULE_NO)
    }

    fun setScheduleNo(context: Context?, value: Int) {
        BasePrefUtil.setInt(context, SCHEDULE_NO, value)
    }

    fun getAdvertiseVersionCode(context: Context?): Int {
        var defaultValue = 0
        try {
            val configModel = Gson().fromJson(getConfigMainJson(context), ConfigModel::class.java)
            defaultValue = configModel.bannerVersion
        } catch (e: Exception) {
        }
        return defaultValue
    }
}