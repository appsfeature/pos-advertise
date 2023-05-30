package com.example.posadvertise

import android.content.Context
import com.formbuilder.FormBuilder
import com.formbuilder.interfaces.FormResponse
import com.formbuilder.model.FBNetworkModel
import com.formbuilder.model.FormBuilderModel
import com.formbuilder.util.FBUtility
import com.posadvertise.banner.logBanner
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppApplication : TrackingApplication() {

    companion object{
        const val fileUrlAll: String = "https://appsfeature.github.io/files/POSAdvertise.zip"
        const val fileUrlPartialUpdate: String = "https://appsfeature.github.io/files/POSAdvertise-update.zip"
        const val fileUrlTutorials: String = "https://appsfeature.github.io/files/POSAdvertise-tutorials.zip"
        lateinit var instance : AppApplication
    }


    override fun isDebugMode(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        getPosAdvertise().httpsTutorialUrl = fileUrlTutorials

        initAppLibs()

    }

    private fun initAppLibs() {
        FormBuilder.getInstance()
            .setAppVersionCode(this, BuildConfig.VERSION_CODE)
            .onFormSubmit(object : FormResponse.Form{
                override fun onFormSubmit(
                    formData: FormBuilderModel?,
                    gsonData: String?,
                    callback: FormResponse.Callback<FBNetworkModel>?
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        callback?.onSuccess(FBNetworkModel(true));
                    }
                }

            })
            .setDebugModeEnabled(BuildConfig.DEBUG);
    }

    override fun onBannerItemClicked(context: Context?, item: AdvertiseModel?) {
        onClickAction(context, item)
    }

    private fun onClickAction(context: Context?, item: AdvertiseModel?) {
        item?.let {
            when (it.actionType) {
                ActionType.FORM.value -> {
                    FormBuilder.getInstance().openDynamicFormActivity(this, it.id, it.actionText, object : FormResponse.FormSubmitListener{
                        override fun onFormSubmitted(status: Boolean?) {
                            logBanner("onFormSubmitted")
                        }
                    })
                }
                else -> {
                    POSAdvertiseUtility.toast(context, "Take Action on Id : ${it.actionType}")
                }
            }
        }
    }

    override fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?) {
        POSAdvertiseUtility.toast(context, "Take Action on Id : ${item?.actionType}")
    }
}