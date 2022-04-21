package com.bashkir.socialhouse.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bashkir.socialhouse.MainActivity
import com.bashkir.socialhouse.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_welcom.*

class  WelcomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_welcom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomFragment_to_loginFragment)
        }
        goToReg.setOnClickListener {
            findNavController().navigate(R.id.action_welcomFragment_to_registrationFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}