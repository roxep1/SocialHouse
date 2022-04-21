package com.bashkir.socialhouse

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashkir.socialhouse.models.Dialog
import com.bashkir.socialhouse.models.Message
import com.bashkir.socialhouse.models.User
import com.bashkir.socialhouse.objects.CurrentUser
import com.bashkir.socialhouse.objects.Database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {
    var dialogId: String = ""

    companion object {
        private const val MAX_LENGTH_OF_MESSAGE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        val uid: String = intent.getStringExtra("User_id").toString()
        Database.getUser(uid){
            nameOfUser.text = it?.login
        }
        CurrentUser.refMessages?.get()?.addOnSuccessListener {
            val iterator = it.children.iterator()
            var exists = false
            dialogId = CurrentUser.firebaseUserID + uid
            while (iterator.hasNext()) {
                val dialog = iterator.next()
                if (dialog.key == uid) {
                    dialogId = dialog.getValue(Dialog::class.java)!!.dialogId!!
                    exists = true
                    break
                }
            }
            if (!exists)
                CurrentUser.refUser!!.get().addOnSuccessListener { currentSnap ->
                    Database.getUser(uid){ user ->
                        addDialogToUsers(currentSnap.getValue(User::class.java)!!.login!!, Dialog(user!!.login, dialogId, user.uid))
                    }
                }
            addDialogListener()
            sendBtn.setOnClickListener {
                val message = messageEdt.text.toString()
                if (message.isNotBlank() && message.length <= MAX_LENGTH_OF_MESSAGE) {
                    Database.refDialogs.child(dialogId).push()
                        .setValue(Message(CurrentUser.firebaseUserID, message))
                    messageEdt.text.clear()
                }
            }
        }
    }

    private fun addDialog(toUser: String, dialog: Dialog) {
        Database.refUsers.child(toUser).child(Database.MESSAGES_DIR).child(dialog.userId!!)
            .setValue(dialog)
    }

    private fun addDialogToUsers(currentUsername: String, dialog: Dialog) {
        addDialog(CurrentUser.firebaseUserID!!, dialog)
        addDialog(dialog.userId!!, Dialog(currentUsername, dialog.dialogId, CurrentUser.firebaseUserID))
    }

    private fun addDialogListener() {
        Database.refDialogs.child(dialogId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recyclerMessages.withModels {
                    val iterator = snapshot.children.iterator()
                    while (iterator.hasNext()) {
                        val item = iterator.next()
                        val msg: Message = item.getValue(Message::class.java)!!
                        val align = if (msg.author == CurrentUser.firebaseUserID)
                            View.TEXT_ALIGNMENT_VIEW_END
                        else
                            View.TEXT_ALIGNMENT_VIEW_START
                        message {
                            id(item.key)
                            alignment(align)
                            text(msg.message)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@DialogActivity,
                    "Не удалось загрузить диалог... ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}