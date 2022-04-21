package com.bashkir.socialhouse.objects

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

object CurrentUser {
    const val PROFILE_IMG = "profile"
    val firebaseUserID: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid
    val refUser : DatabaseReference?
        get() =  if(firebaseUserID != null) Database.refUsers.child(firebaseUserID!!) else null
    val refMessages : DatabaseReference?
        get() = refUser?.child(Database.MESSAGES_DIR)
}