package com.posadvertise.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.helper.util.BaseConstants
import com.posadvertise.banner.logBanner
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.logAdv
import com.posadvertise.screensaver.logSS
import com.posadvertise.screensaver.model.ScreenSaverProperty
import com.posadvertise.tutorials.logTut
import com.posadvertise.tutorials.model.TutorialProperty
import com.posadvertise.util.common.ExtraProperty
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class POSAdvertiseUtility {

    companion object{
        fun getTimeInMillis(inputDate: String?): Long {
            try {
                inputDate?.let {
                    if(it.isNotEmpty()){
                        val date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(inputDate)
                        return date.time
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        fun toast(context: Context?, message: String) {
            context?.let {
                Toast.makeText(it, message, Toast.LENGTH_LONG).show()
            }
        }

        fun getSystemScreenOffTimeOut(context: Context): Long {
            return Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT).toLong()
        }

        fun getBannerProperty(context: Context?, isDynamic: Boolean): BannerProperty? {
            val savedJson = POSAdvertisePreference.getConfigBannerJson(context, isDynamic)
            logBanner("configBannerJson : $savedJson")
            return Gson().fromJson(savedJson, object : TypeToken<BannerProperty>() {}.type)
        }

        fun getSSProperty(context: Context?): ScreenSaverProperty? {
            val savedJson = POSAdvertisePreference.getConfigScreenSaverJson(context)
            logSS("configSSJson : $savedJson")
            return Gson().fromJson(savedJson, object : TypeToken<ScreenSaverProperty>() {}.type)
        }

        fun getTutorialProperty(context: Context?): TutorialProperty? {
            val savedJson = POSAdvertisePreference.getConfigTutorialJson(context)
            logTut("configTutorialJson : $savedJson")
            return Gson().fromJson(savedJson, object : TypeToken<TutorialProperty>() {}.type)
        }

        fun getStorage(): POSFileManager {
            return POSFileManager()
        }

        fun getBundle(mProperty : ExtraProperty): Bundle {
            return Bundle().apply {
                putParcelable(BaseConstants.EXTRA_PROPERTY, mProperty.copy())
            }
        }

        fun installAPK(context: Context) {
            val fm = getStorage()
            val file = fm.getUpdatedAPKFile(context)
            if (file?.exists() == true) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fm.getUriFromFile(context, file), "application/vnd.android.package-archive"
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                    logAdv("Error in opening the file!")
                }
            } else {
                Toast.makeText(context, "File not exists", Toast.LENGTH_LONG).show()
            }
        }
    }
}