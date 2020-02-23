package com.example.pickupsport

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_user.view.*
import okhttp3.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import java.io.IOException
import kotlinx.android.synthetic.main.admin_venue.view.*
import org.json.JSONException
import java.lang.Exception
import java.util.ArrayList
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.listing_fragment.view.*
import org.jetbrains.anko.uiThread

class admin_user : Fragment(){

    private var response: String? = null
    private var userlist: ListView? = null
    private var userModelArrayList: ArrayList<User_Model>? = null
    private var customAdapter: CustomAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.admin_user, container, false)
        view.back_buttonuser.setOnClickListener({
            // Navigate to the next Fragment.
            (activity as NavigationHost).navigateTo(admin_home(), false)
        })

        doAsync {
            val users = getUsers()
            response = users
            uiThread {
                println("response")
                println(response)
                userlist = view.userlist
                userModelArrayList = getInfo(response!!)
                customAdapter = CustomAdapter(view.context, userModelArrayList!!)
                userlist!!.adapter = customAdapter

                userlist!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedUser = customAdapter!!.getItem(position)
                    val selectedUserId = selectedUser.toString()
                    println("selected user id: " + selectedUserId)

                    doAsync {
                        val removeUserResp = removeUser(selectedUserId)
                        println("removeUserResp: " + removeUserResp)
                        uiThread {

                            if (removeUserResp.equals("Deleted user")) {
                                val dur = Toast.LENGTH_SHORT
                                val message = "User was deleted"
                                val toast = Toast.makeText(view.context, message, dur)
                                toast.show()
                                (activity as NavigationHost).navigateTo(admin_home(), false)
                            } else {
                                println("Something went wrong deleting user")
                            }
                        }
                    }

                }
            }
        }

        return view
    }

    fun removeUser(userId:String?) : String? {

        val url = "https://my-apad-project.appspot.com/app/admin/users/"+userId

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Android")
            .build()
        val response = client.newCall(request).execute()
        val bodyStr =  response?.body()?.string() // this can be consumed only once
        println(bodyStr)
        return bodyStr
    }

     fun getUsers() : String? {

        val url = "https://my-apad-project.appspot.com/app/users"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Android")
            .build()
        val response = client.newCall(request).execute()
        val bodyStr =  response?.body()?.string() // this can be consumed only once
        println("bodyStr")
        println(bodyStr)
        return bodyStr
    }

    fun getInfo(response: String): ArrayList<User_Model> {
        val userModelArrayList = ArrayList<User_Model>()
        try {
            val dataArray = JSONArray(response)
            for (i in 0 until dataArray.length()) {
                val usersModel = User_Model()
                val dataobj = dataArray.getJSONObject(i)
                usersModel.setNames(dataobj.getString("name"))
                usersModel.setPhones(dataobj.getString("phone"))
                usersModel.setPasswords(dataobj.getString("password"))
                usersModel.setIds(dataobj.getInt("id"))
                usersModel.setEmails(dataobj.getString("email"))
                userModelArrayList.add(usersModel)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return userModelArrayList
    }

}