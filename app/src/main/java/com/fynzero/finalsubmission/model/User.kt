package com.fynzero.finalsubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var login: String? = null,
    var name: String? = null,
    var type: String? = null,
    var avatar_url: String? = null,
    var location: String? = null,
    var blog: String? = null
) : Parcelable