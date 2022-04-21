package com.bashkir.socialhouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bashkir.socialhouse.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user: User? = snapshot.getValue(User::class.java)
                    userName.text = user!!.login
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}