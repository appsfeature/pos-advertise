package com.helper.util

import android.content.Context
import android.content.SharedPreferences
import com.posadvertise.R


class BasePrefUtil {

    companion object{
        private const val TAG = "BasePrefUtil"
        private const val DOWNLOAD_DIRECTORY = "DownloadDirectory"
        private var sharedPreferences: SharedPreferences? = null


        fun getDefaultSharedPref(context: Context?): SharedPreferences? {
            if (sharedPreferences == null && context != null) sharedPreferences =
                context.getSharedPreferences(context.packageName + context.getString(R.string.app_name), Context.MODE_PRIVATE)
            return sharedPreferences
        }

        fun setString(context: Context?, key: String?, value: String?) {
            if (context != null) {
                val editor = getDefaultSharedPref(context)!!.edit()
                editor.putString(key, value)
                editor.commit()
            }
        }

        fun getString(context: Context?, key: String?): String? {
            return getString(context, key, "")
        }

        fun getString(context: Context?, key: String?, defaultValue: String?): String? {
            return if (context == null) {
                defaultValue
            } else getDefaultSharedPref(context)!!.getString(key, defaultValue)
        }

        fun setInt(context: Context?, key: String?, value: Int) {
            if (context != null) {
                val editor = getDefaultSharedPref(context)!!.edit()
                editor.putInt(key, value)
                editor.apply()
            }
        }

        fun getInt(context: Context?, key: String?): Int {
            return getInt(context, key, 0)
        }

        fun getInt(context: Context?, key: String?, defaultValue: Int): Int {
            return if (context == null) {
                defaultValue
            } else getDefaultSharedPref(context)!!.getInt(key, defaultValue)
        }

        fun setFloat(context: Context?, key: String?, value: Float) {
            if (context != null) {
                val editor = getDefaultSharedPref(context)!!.edit()
                editor.putFloat(key, value)
                editor.apply()
            }
        }

        fun getFloat(context: Context?, key: String?): Float {
            return getFloat(context, key, 0f)
        }

        fun getFloat(context: Context?, key: String?, defaultValue: Float): Float {
            return if (context == null) {
                defaultValue
            } else getDefaultSharedPref(context)!!.getFloat(key, defaultValue)
        }

        fun setLong(context: Context?, key: String?, value: Long) {
            if (context != null) {
                val editor = getDefaultSharedPref(context)!!.edit()
                editor.putLong(key, value)
                editor.apply()
            }
        }

        fun getLong(context: Context?, key: String?): Long {
            return if (context == null) {
                0
            } else getDefaultSharedPref(context)!!.getLong(key, 0)
        }

        fun setBoolean(context: Context?, key: String?, value: Boolean) {
            if (context != null) {
                val editor = getDefaultSharedPref(context)!!.edit()
                editor.putBoolean(key, value)
                editor.commit()
            }
        }

        fun getBoolean(context: Context?, key: String?): Boolean {
            return getBoolean(context, key, false)
        }

        fun getBoolean(context: Context?, key: String?, defaultValue: Boolean): Boolean {
            return if (context == null) {
                defaultValue
            } else getDefaultSharedPref(context)!!.getBoolean(key, defaultValue)
        }

        fun removeKey(context: Context?, key: String?) {
            if (context != null) {
                getDefaultSharedPref(context)!!.edit().remove(key).apply()
            }
        }

        fun clearPreferences(context: Context?) {
            if (context != null) {
                val editor = getDefaultSharedPref(context)!!.edit()
                editor.clear()
                editor.apply()
            }
        }

        @JvmStatic
        fun getDownloadDirectory(context: Context?): String {
            return getString(context, DOWNLOAD_DIRECTORY, BaseConstants.DIRECTORY_DOWNLOADS) ?: BaseConstants.DIRECTORY_DOWNLOADS
        }

    }

}