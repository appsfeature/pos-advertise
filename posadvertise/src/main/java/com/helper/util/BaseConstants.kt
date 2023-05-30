package com.helper.util

interface BaseConstants {

    companion object {
        const val EXTRA_PROPERTY: String = "extra_property"
        const val DIRECTORY_DOWNLOADS: String = "/BonusHub/"

        const val NO_DATA = "No Data"
        const val NO_INTERNET_CONNECTION = "No Internet Connection"
        const val ACTIVE = 1
        const val DE_ACTIVE = 0
        const val ID = "id"
        const val TITLE = "title"
    }

    interface Error {
        companion object {
            const val UPDATE_LATER = "Update later!"
            const val MSG_ERROR = "Error, please try later."
            const val DATA_NOT_FOUND = "Error, Data not found"
            const val CATEGORY_NOT_FOUND = "Error, This is not supported. Please update"
            const val INTEGRATION = "Error : Integration, Please contact to developer."
        }
    }
}
