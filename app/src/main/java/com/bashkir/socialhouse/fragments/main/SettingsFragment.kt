package com.bashkir.socialhouse.fragments.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bashkir.socialhouse.AuthActivity
import com.bashkir.socialhouse.R
import com.bashkir.socialhouse.objects.CurrentUser
import com.bashkir.socialhouse.objects.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {
    companion object {
        private const val REQUEST_CODE = 12345
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.circleImageProfile.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_CODE)
        }
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refProfile =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child(CurrentUser.PROFILE_IMG)
        refProfile.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profile: String = snapshot.getValue(String::class.java)!!

                    Picasso.get().load(profile).error(R.drawable.error)
                        .placeholder(R.drawable.user).into(view.circleImageProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        view.exitBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val refPic =
                Database.refStorage.child("${CurrentUser.firebaseUserID}/${CurrentUser.PROFILE_IMG}.jpg")
            refPic.putFile(uri!!).addOnSuccessListener {
                CurrentUser.refUser!!.get().addOnSuccessListener {
                    refPic.downloadUrl.addOnSuccessListener { uri ->
                        CurrentUser.refUser!!.child(CurrentUser.PROFILE_IMG).setValue(uri.toString())
                    }
                }
            }
        }
    }

}