package com.posadvertise.util

import android.content.Context
import com.helper.util.BasePrefUtil

object POSAdvertisePreference {

    private const val IS_UPDATE_AVAILABLE = "is_update_available"
    private const val UPDATE_DOWNLOADED = "update_downloaded"
    private const val CONFIG_BANNER = "config_banner"
    private const val CONFIG_BANNER_LOCAL = "config_banner_local"
    private const val CONFIG_SCREEN_SAVER = "config_screen_saver"
    private const val CONFIG_TUTORIAL = "config_tutorial"
    private const val SCHEDULE_TYPE = "fb_schedule_type"
    private const val SCHEDULE_NO = "fb_schedule_no"
    private const val INTERNAL_ZIP_FILE = "fb_internal_zip_file"

    fun setInternalZipFileExtracted(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, INTERNAL_ZIP_FILE, value)
    }

    fun isInternalZipFileExtracted(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, INTERNAL_ZIP_FILE, false)
    }

    fun setUpdateAvailable(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, IS_UPDATE_AVAILABLE, value)
    }

    fun isUpdateAvailable(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, IS_UPDATE_AVAILABLE, true)
    }

    fun setUpdateDownloaded(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, UPDATE_DOWNLOADED, value)
    }

    fun isUpdateDownloaded(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, UPDATE_DOWNLOADED, false)
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
}