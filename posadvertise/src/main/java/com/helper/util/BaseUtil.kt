package com.helper.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.posadvertise.R


class BaseUtil {

    companion object{
        fun showNoData(view: View?, visibility: Int) {
            showNoData(view, BaseConstants.NO_DATA, visibility)
        }

        fun showNoData(view: View?, message: String, visibility: Int) {
            if (view != null) {
                view.visibility = visibility
                if (visibility == View.VISIBLE) {
                    view.findViewById<View>(R.id.pb_progress_bar)?.let {
                        it.visibility = View.GONE
                    }
                    view.findViewById<TextView>(R.id.tv_no_data)?.let {
                        it.visibility = View.VISIBLE
                        it.text = getNoDataMessage(view.getContext(), message)
                    }
                }
            }
        }

        fun showNoDataProgress(view: View?) {
            if (view != null) {
                view.visibility = View.VISIBLE
                view.findViewById<View>(R.id.pb_progress_bar)?.let {
                    it.visibility = View.VISIBLE
                }
                view.findViewById<View>(R.id.tv_no_data)?.let {
                    it.visibility = View.GONE
                }
            }
        }

        private fun getNoDataMessage(context: Context, message: String): String? {
            return if (!isConnected(context)) BaseConstants.NO_INTERNET_CONNECTION else message
        }

        fun isConnected(context: Context?): Boolean {
            var isConnected = false
            try {
                if (context?.getSystemService(Context.CONNECTIVITY_SERVICE) != null && context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                    ) is ConnectivityManager
                ) {
                    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val network = connectivityManager.activeNetwork
                        if (network != null) {
                            val nc = connectivityManager.getNetworkCapabilities(network)
                            isConnected =
                                nc != null && (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || nc.hasTransport(
                                    NetworkCapabilities.TRANSPORT_CELLULAR
                                ) || nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || nc.hasTransport(
                                    NetworkCapabilities.TRANSPORT_BLUETOOTH
                                ))
                        }
                    } else {
                        val activeNetwork = connectivityManager.activeNetworkInfo
                        isConnected = activeNetwork != null && activeNetwork.isConnected
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return isConnected
        }

        fun isValidUrl(url: String): Boolean {
            return if (!TextUtils.isEmpty(url)) {
                url.startsWith("file://") || url.startsWith("http://") || url.startsWith("https://")
            } else {
                false
            }
        }

        fun getValidUrl(appHostDownloadURL: String, ftpIPPort: String?, downloadAppFileName : String): String {
            return getValidUrl(appHostDownloadURL, ftpIPPort, downloadAppFileName, "X990")
        }
        fun getValidUrl(appHostDownloadURL: String, ftpIPPort: String?, downloadAppFileName : String, terminalName: String): String {
            var finalUrl : String = appHostDownloadURL
            if(!ftpIPPort.isNullOrEmpty() && !appHostDownloadURL.contains(ftpIPPort)) {
//            if (!appHostDownloadURL.contains(ftpIPPort)) {
//                finalUrl = appHostDownloadURL.replace("/app", ":$ftpIPPort")
//                finalUrl = finalUrl + "app" + "/" + "X990" + "/" + downloadAppFileName
//            }else{
                finalUrl = finalUrl.replace("co.in", "co.in:${ftpIPPort}") + terminalName + "/" + downloadAppFileName
//            }
            }else{
                finalUrl = finalUrl + terminalName + "/" + downloadAppFileName
            }
            return finalUrl
        }

        fun showToast(context: Context?, string: String?) {
            if(context != null && string != null) {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
            }
        }
    }
}