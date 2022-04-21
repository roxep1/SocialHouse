package com.bashkir.socialhouse.objects

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.bashkir.socialhouse.DialogActivity
import com.bashkir.socialhouse.models.Dialog
import com.bashkir.socialhouse.models.Message
import com.bashkir.socialhouse.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_dialog.*

object Database {
    private const val USERS_DIR = "Users"
    const val MESSAGES_DIR = "Messages"
    val refUsers: DatabaseReference
        get() = refDatabase.child(USERS_DIR)
    private val refDatabase: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference
    val refDialogs: DatabaseReference
        get() = refDatabase.child(MESSAGES_DIR).ref

    val refStorage: StorageReference
        get() = FirebaseStorage.getInstance().reference

    fun editDatabaseReference(
        context: Context,
        ref: DatabaseReference,
        hashMap: User,
        onSuccessful: () -> Unit
    ) {
        ref.setValue(hashMap)
            .addOnCompleteListener { task_update_refer ->
                if (task_update_refer.isSuccessful) {
                    onSuccessful()
                } else Toast.makeText(
                    context,
                    task_update_refer.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun EditText.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
    }

    fun EditText.isPasswordOrLoginValid(): Boolean {
        return this.text.length >= 6 && this.text.isNotEmpty() && this.text.isNotBlank()
    }

    fun getUser(uid: String, onSuccessful: (User?) -> Unit) {
        refUsers.child(uid).get().addOnSuccessListener {
            onSuccessful(it.getValue(User::class.java))
        }
    }
}