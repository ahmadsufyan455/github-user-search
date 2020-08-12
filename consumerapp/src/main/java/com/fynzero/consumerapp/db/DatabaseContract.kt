package com.fynzero.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.fynzero.finalsubmission"
    const val SCHEME = "content"

    class UserColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val TYPE = "type"
            const val AVATAR = "avatar"

            // create content://com.fynzero.finalsubmission/user
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}