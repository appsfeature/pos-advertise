package com.helper.callback

import android.view.View

interface Response {

    /**
     * @param <T> object Type
     * default method type means not mandatory implementation of onFailure method
    </T> */
    interface Callback<T> {
        fun onSuccess(response: T)
        fun onFailure(e: Exception?) {}
    }

    interface Status<T> {
        fun onSuccess(response: T)
    }

    interface OnClickListener<T> {
        fun onItemClicked(view: View?, item: T)
    }

    interface OnListClickListener<T> {
        fun onItemClicked(view: View?, item: T)
        fun onDeleteClicked(view: View?, position: Int, item: T)
    }

    interface Progress {
        fun onStartProgressBar()
        fun onStopProgressBar()
    }
}