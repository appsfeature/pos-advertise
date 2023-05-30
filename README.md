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
        getPosAdvertise().httpsTutorialUrl = fileUrlTutorials
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
class MainActivity : AppCompatActivity() { 

    override fun onCreate(savedInstanceState: Bundle?) {
        //use this code before super.onCreate method.
        AppApplication.instance.registerScreenSaver(this)
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
    
    override fun onDestroy() {
        super.onDestroy()
        AppApplication.instance.unregisterPosAdvertise()
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

### Important Notes:
```text
    * Add Local Zip file in android assets folder.
           file://assets/POSAdvertise.zip
```
