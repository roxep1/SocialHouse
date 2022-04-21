package com.bashkir.socialhouse.models

import java.io.Serializable
import java.util.*

class User() : Serializable{
    constructor(uid: String, login : String, email: String, profile: String = "https://firebasestorage.googleapis.com/v0/b/socialhouse-c9503.appspot.com/o/user.jpeg?alt=media&token=ad9953c8-c0da-4eae-b052-8eb2a9ef027b") : this(){
        this.uid = uid
        this.login = login
        this.email = email
        this.search = login.toLowerCase(Locale.ROOT)
        this.profile = profile
    }

    var uid: String? = null
    var login: String? = null
    var email: String? = null
    var search: String? = null
    var profile: String = "https://firebasestorage.googleapis.com/v0/b/socialhouse-c9503.appspot.com/o/user.jpeg?alt=media&token=ad9953c8-c0da-4eae-b052-8eb2a9ef027b"
}
