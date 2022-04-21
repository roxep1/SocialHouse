package com.bashkir.socialhouse.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bashkir.socialhouse.MainActivity
import com.bashkir.socialhouse.R
import com.bashkir.socialhouse.models.User
import com.bashkir.socialhouse.objects.CurrentUser
import com.bashkir.socialhouse.objects.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.*
import kotlin.collections.HashMap

class RegistrationFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private var firebaseUserID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RegBtn.setOnClickListener {
            regUser()
        }
    }

    private fun regUser() {
        val login = LoginEdtReg.text.toString()
        val email = EmailEdtReg.text.toString()
        val password = PasswordEdtReg.text.toString()
        if (login.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUserID = mAuth.currentUser!!.uid
                        val userHashMap = User(firebaseUserID, login, email)

                        Database.editDatabaseReference(requireContext(), CurrentUser.refUser!!, userHashMap){
                            startActivity(
                                Intent(
                                    requireActivity(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка: ${task.exception!!.message.toString()}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Данные введены некорректно", Toast.LENGTH_SHORT)
                .show()
        }
    }
}