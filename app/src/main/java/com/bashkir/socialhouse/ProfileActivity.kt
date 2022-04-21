package com.bashkir.socialhouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bashkir.socialhouse.models.User
import com.bashkir.socialhouse.objects.Database
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = intent.getSerializableExtra("User_info") as User
        Picasso.get().load(user.profile).placeholder(R.drawable.user).error(R.drawable.error).into(profileImage)
        userName.text = user.login
        email.text = user.email
        sendMsgBtn.setOnClickListener {
            startActivity(Intent(this, DialogActivity::class.java).putExtra("User_id", user.uid))
        }
    }
}