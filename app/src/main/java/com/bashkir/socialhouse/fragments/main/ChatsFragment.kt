package com.bashkir.socialhouse.fragments.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bashkir.socialhouse.DialogActivity
import com.bashkir.socialhouse.R
import com.bashkir.socialhouse.dialog
import com.bashkir.socialhouse.models.Dialog
import com.bashkir.socialhouse.objects.CurrentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_chats.view.*

class ChatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CurrentUser.refMessages?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val iterator = snapshot.children.iterator()
                view.recyclerDialogs.withModels {
                    while (iterator.hasNext()) {
                        val dialog = iterator.next().getValue(Dialog::class.java)
                        Log.d("Filling recycler: ", "started")
                        dialog {
                            id(dialog!!.dialogId)
                            username(dialog.username)
                            onClick(View.OnClickListener {
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        DialogActivity::class.java
                                    ).putExtra("User_id", dialog.userId)
                                )
                            })
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Мы не можем загрузить диалоги... ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }
}
