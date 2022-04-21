package com.bashkir.socialhouse.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bashkir.socialhouse.ProfileActivity
import com.bashkir.socialhouse.R
import com.bashkir.socialhouse.models.User
import com.bashkir.socialhouse.objects.CurrentUser
import com.bashkir.socialhouse.objects.Database
import com.bashkir.socialhouse.search
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBtn.setOnClickListener {
            if (searchBtn.text.isNotBlank()) {
                Database.refUsers.get().addOnSuccessListener {
                    searchResults.withModels {
                        val iterator = it.children.iterator()
                        while (iterator.hasNext()) {
                            val user = iterator.next().getValue(User::class.java)
                            if ((user?.search!!.contains(searchEdt.text) || user.email!!.contains(
                                    searchEdt.text
                                )) && searchEdt.text.isNotBlank() && user.uid != CurrentUser.firebaseUserID
                            ) {
                                search {
                                    id(user.uid)
                                    name(user.login)
                                    click(View.OnClickListener {
                                        startActivity(
                                            Intent(
                                                requireActivity(),
                                                ProfileActivity::class.java
                                            ).putExtra("User_info", user)
                                        )
                                    })
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}