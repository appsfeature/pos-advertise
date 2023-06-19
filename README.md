# POS Advertise

#### Library size is 1.6Mb

## Setup Project

Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        maven {
            url "https://jitpack.io"
        }
    }
}
```

Add this to your project build.gradle

#### Dependency
[![](https://jitpack.io/v/bonushub/pos-advertise.svg)](https://jitpack.io/#bonushub/pos-advertise)
```gradle
dependencies {
        implementation 'com.github.bonushub:pos-advertise:1.0'
}
```

### Docs for create config json file.

### POSAdvertise.zip file contains
```
Banner
ScreenSaver
Tutorial
config.json
```
### config.json
```
bannerVersion : zip file release version (revision id).
```
### config_banner.json
```
bannerAttachDelayTime : time to apper on the screen.
bannerTransitionTimeInSec : Transition time for each banners.
list.startDate : When to start showing banner.
list.endDate : When to stop showing banner.
list.ranking : For sorting banner list.
list.fileName : Filename of image stored in Banner Folder.
list.showIn : Showing on the following screens seperated by comma like (home,void)
     = all,
     = home,
     = emi_sale,
     = brand_emi,
     = void,
     = dynamic_qr,
list.actionType : Perform following actions when click banner. i.e We have to pass actionType = 1001 for opening an campaign 
     = DYNAMIC_FORM - 1001
     = SALE - 100
     = BANK_EMI - 110
     = BRAND_EMI - 120
     = EMI_CATALOGUE - 130
     = CROSS_SELL - 140
     = FLEXY_PAY - 150
     = MERCHANT_PROMO - 160
     = MERCHANT_REFERRAL - 170
     = DIGI_POS - 180
     = BHARAT_QR - 190
     = SALE_WITH_CASH - 200
     = SALE_TIP - 210
     = CASH_ADVANCE - 220
     = VOID_SALE - 230
     = VOID_REFUND - 240
     = REFUND - 250
     = PRE_AUTH_CATAGORY - 260
     = PREAUTH - 270
     = PREAUTH_COMPLETE - 280
     = VOID_PREAUTH - 290
     = PENDING_PREAUTH - 300
     = EMI_ENQUIRY - 310
     = EMI_PRO - 320
     = BONUS_PROMO - 330
    
list.actionText : Pass campaign config json data when open an campaign.
```
### config_screen_saver.json
```
screenTimeOut : Start ScreenSaver after mentioned time in milliseconds.
transitionTime : Transition time for each Screen Saver.
isEnableShowWhenChargerConnected : if true than only show when charger connected.
isEnableStaticQRCode : Integrate in future Update.
list.startDate : When to start showing banner.
list.endDate : When to stop showing banner.
list.ranking : For sorting banner list.
list.fileName : Filename of image stored in ScreenSaver Folder.
```
### config_tutorial.json
```
isEnable : Enable or disable this feature.
list.ranking : For sorting banner list.
list.title : Tutorial Title.
list.fileName : Filename of image stored in ScreenSaver Folder.
```
### Campaign Form Json Doc
```
campId : Campaign Unique ID.
campName : Campaign Name.
title : Title shown in Action Bar
subTitle : SubTitle shown in below Action Bar
buttonText : Campaign Submit Button Text.
popup : After Campaign Submit Popup.

fieldList.fieldName : User Input Field Name.
fieldList.maxLength : Edittext input max digit.
fieldList.paramKey : Field value sent to the server with this key.

fieldList.fieldType : There are following Input types for EditText mentioned below.
     = text
     = number
     = textPersonName
     = textEmailAddress
     = numberSigned
     = phone
     = textMultiLine
     = textCapWords         i.e. Hello World
     = textCapCharacters    i.e. HELLO WORLD
    
fieldList.inputType : There are following Input field types mentioned below.
     = text_view
     = edit_text
     = spinner
     = radio
     = check_box
     = date_picker
     = empty
    
fieldList.validation : There are following Input field validations mentioned below (Note: If no value pass than no validation check is apply.)
     = empty
     = email
     = mobile
     = gst_number
     = pin_code
     = ifsc_code
     = alpha_numeric
     = spinner
     = check_box
     = date
     = radio
     
 fieldList.fieldData : Field data is used for Spinner(DropDown), RadioButtons, Pass Json Array of List as String. 


```
### Campaign Json
```
{
  "campId": 1,
  "campName": "Campaign-001",
  "title": "Bonushub Campaign",
  "subTitle": "Create all fields dynamic by json or Model structure.",
  "isShowActionbar": false,
  "buttonText": "Submit",
  "popup": {
    "buttonText": "Continue",
    "description": "You will get your updates soon",
    "title": "Thank You!"
  },
  "fieldList": [
    {
      "fieldName": "Name",
      "fieldType": 1,
      "inputType": "textCapWords",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "name",
      "validation": "empty"
    },
    {
      "fieldData": "[{\"id\":1,\"title\":\"Delhi\"},{\"id\":2,\"title\":\"Noida\"},{\"id\":3,\"title\":\"Gurugram\"},{\"id\":4,\"title\":\"Faridabad\"}]",
      "fieldName": "Select State",
      "fieldType": 2,
      "inputType": "text",
      "isSpinnerSelectTitle": true,
      "maxLength": 0,
      "paramKey": "steam",
      "validation": "spinner"
    },
    {
      "fieldData": "[\"Male\",\"Female\",\"Trans\"]",
      "fieldName": "Select Gender",
      "fieldType": 3,
      "inputType": "text",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "gender",
      "validation": "radio"
    },
    {
      "fieldData": "Select Date",
      "fieldName": "Date of Birth",
      "fieldType": 5,
      "inputType": "text",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "dob",
      "validation": "date"
    },
    {
      "fieldName": "Personal Detail",
      "fieldType": 0,
      "inputType": "text",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "personal_detail",
      "validation": ""
    },
    {
      "fieldName": "Mobile No",
      "fieldSuggestions": "[\"9891983694\"]",
      "fieldType": 1,
      "inputType": "phone",
      "isSpinnerSelectTitle": false,
      "maxLength": 10,
      "paramKey": "mobile",
      "validation": "mobile"
    },
    {
      "fieldName": "Email Id",
      "fieldSuggestions": "[\"@gmail.com\", \"@yahoo.com\", \"@hotmail.com\", \"@outlook.com\"]",
      "fieldType": 1,
      "inputType": "textEmailAddress",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "email",
      "validation": "email"
    },
    {
      "fieldName": "Address",
      "fieldType": 1,
      "inputType": "textMultiLine",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "address",
      "validation": ""
    },
    {
      "fieldName": "Subscribe for news updates",
      "fieldType": 4,
      "inputType": "text",
      "isSpinnerSelectTitle": false,
      "maxLength": 0,
      "paramKey": "agree",
      "validation": "check_box"
    }
  ]
}
```

### Usage methods

### Changes in layout.xml file
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:orientation="vertical"
        android:padding="16dp"
        android:gravity="center" >

    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:gravity="bottom"
        android:layout_height="300dp">

        <FrameLayout
            android:id="@+id/container_banner"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
    </LinearLayout>
</LinearLayout>
 
```

### Application class setup
```kotlin
// Extend Application class with POSAdvertiseApplication
class AppApplication : POSAdvertiseApplication() {
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
        getPosAdvertise()
            .setTerminalName("X990")
            .httpsTutorialUrl = fileUrlTutorials
        FormBuilder.getInstance()
            .setAppVersionCode(this, BuildConfig.VERSION_CODE)
            .setAdvertiseVersionCode(this, POSAdvertise.getAdvertiseVersionCode(this))
            .onFormSubmit { formData, gsonData, callback ->
                if (!gsonData.isNullOrEmpty()) {
                    HitServerRequestManager.syncDynamicFormData(formData, gsonData, object : HitServerRequestManager.Callback<String> {
                        override fun onSuccess(response: String) {
                            callback?.onSuccess(FBNetworkModel(true));
                        }

                        override fun onFailure(e: Exception?) {
                            callback?.onFailure(e)
                        }
                    })
                }
            }.isDebugModeEnabled = BuildConfig.DEBUG;
    }

    override fun onBannerItemClicked(context: Context?, item: AdvertiseModel?) {
        POSAdvertiseUtility.toast(context, "Take Action on Id : ${item?.actionType}")
    }

    override fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?) {
        POSAdvertiseUtility.toast(context, "Take Action on Id : ${item?.actionType}")
    }
}
```

### ScreenSaver usage methods
```kotlin

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Start screen saver for current fragment
        POSScreenSaver.startScreenSaver(this)
    } 
}
class MainActivity : AppCompatActivity() , POSAdvertiseCallback.OnAdvertiseListener{ 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //Download Zip file from https server
        downloadFiles(AppApplication.fileUrlAll)
        
        //Partial Update Zip file from https server
        downloadFiles(AppApplication.fileUrlPartialUpdate)
        
        //Download Tutorials from https server
        downloadFiles(AppApplication.fileUrlTutorials)
    }

    private fun downloadFiles(fileUrlAll: String) {
        POSAdvertise.downloadFileFromServer(requireActivity(), fileUrlAll, object  : POSAdvertiseCallback.Download<Boolean>{
            override fun onSuccess(response: Boolean) {

            }
            override fun onProgressUpdate(progress: Int) {
                progressHorizontal.progress = progress
                if(progress == 100){
                    progressHorizontal.setVisibility(View.GONE)
                }
            }
        })
    }
    
    override fun onStop() {
        super.onStop()
        Log.d("@Test", "onStop")
        POSAdvertise.removeListener(this@MainActivity.hashCode())
    }

    override fun onStart() {
        super.onStart()
        Log.d("@Test", "onStart")
        POSAdvertise.addListener(this@MainActivity.hashCode(), this)
    }

    override fun onBannerItemClicked(context: Context?, item: AdvertiseModel?) {
    }

    override fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?) {
    }

    override fun onDownloadCompletedUpdateUi() {
    }
}
```

### Tutorial usage methods
```kotlin
class DashboardFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        openTutorials()
    }

    private fun openTutorials() {
        POSAdvertise.openTutorial(requireActivity(), "Tutorials")
    }
}
```

### Banner usage methods
```kotlin
class DashboardFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        showBanner()
    }

    private fun showBanner() {
        AppApplication.instance.getPosAdvertise().showBannerOnHomeScreen(this, R.id.container_banner)
    }
}
```
### Check Provider Path
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path
        name="external_files"
        path="." />

    <external-cache-path
        name="external_cache_files"
        path="." />

    <external-path name="external_files" path="."/>
    <external-path name="external" path="." />
    <external-files-path name="external_files" path="." />
    <cache-path name="cache" path="." />
    <external-cache-path name="external_cache" path="." />
    <files-path name="files" path="." />
</paths>
```

### Important Notes:
```text
    * Add Local Zip file in android assets folder.
           file://assets/POSAdvertise.zip
    * Check Provider Path.
```
