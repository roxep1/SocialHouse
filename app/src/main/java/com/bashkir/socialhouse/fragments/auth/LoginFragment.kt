package com.bashkir.socialhouse.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bashkir.socialhouse.objects.Database.isEmailValid
import com.bashkir.socialhouse.objects.Database.isPasswordOrLoginValid
import com.bashkir.socialhouse.MainActivity
import com.bashkir.socialhouse.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        if (EmailEdtLogin.isEmailValid() && PasswordEdtLogin.isPasswordOrLoginValid()) {
            val email = EmailEdtLogin.text.toString()
            val password = PasswordEdtLogin.text.toString()
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        else{
                            Toast.makeText(requireContext(), "Ошибка: ${task.exception!!.message.toString()}", Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
        }
        else{
            Toast.makeText(requireContext(), "Данные введены некорректно", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}