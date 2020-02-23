package com.example.pickupsport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.listing_fragment.view.*
import kotlinx.android.synthetic.main.listing_fragment.view.back_button
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.ArrayList

class ListingFragment : Fragment() {
    private val jsoncode = 1
    // Uncomment below if response is hardcoded instead of coming from a file asset
/*    private val response = """
    [
     {
      "name": "James",
      "email": "james@ut"
     },
     {
      "name": "Jean",
      "email": "jean@gmail"
     }
     ]
     """ */
    private var response: String? = null
    private var userlist: ListView? = null
    private var userArrayList: ArrayList<String>? = null
    private var userModelArrayList: ArrayList<User_Model>? = null
    private var customAdapter: CustomAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.listing_fragment, container, false)

        // Set an error if the password is less than 8 characters.
        view.back_button.setOnClickListener({
                // Navigate to the next Fragment.
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
            })

        userlist = view.menulist
//        userModelArrayList = getInfo(response)  // uncomment this and comment the next line if response is above
        response = loadJSONFromAssets();
        //We could use the builtin ArrayAdapter if we were dealing with just strings
        // like in the following 3 lines
        // Call getStrings to parse the JSON Array and return as an ArrayList of Strings
        userArrayList = getStrings(response!!)
        //ArrayAdapter takes 3 inputs: 1. the current context,
        //        2. a layout file specifying what each row in the list should look like,
        //        3. the data that will populate the list as arguments.
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, userArrayList!!)
        // set the custom adapter for the userlist viewing
        userlist!!.adapter =  adapter
        //Here we wan't to use a Custom Adapter that is tied to a custom Data Model
        // Call getInfo to parse the JSON Array and return as a UserModel ArrayList
        userModelArrayList = getInfo(response!!)
        // Create a Custom Adapter that gives us a way to "view" each user in the ArrayList
        customAdapter = CustomAdapter(view.context, userModelArrayList!!)
        // set the custom adapter for the userlist viewing
        userlist!!.adapter = customAdapter
        return view
        }


    fun getInfo(response: String): ArrayList<User_Model> {
        val userModelArrayList = ArrayList<User_Model>()
        try {
                val dataArray = JSONArray(response)
                for (i in 0 until dataArray.length()) {
                    val usersModel = User_Model()
                    val dataobj = dataArray.getJSONObject(i)
                    usersModel.setNames(dataobj.getString("name"))
                    usersModel.setEmails(dataobj.getString("email"))
                    userModelArrayList.add(usersModel)
                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return userModelArrayList
    }

    fun loadJSONFromAssets(): String? {
        var json: String? = null
        try {
            val inputStream = this.context?.assets?.open("users.json")
            val size = inputStream?.available()
            val buffer = ByteArray(size!!)
            inputStream.read(buffer)
            inputStream.close()

            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return json
    }

    fun getStrings(response: String): ArrayList<String> {
        val userArrayList = ArrayList<String>()
        try {
            val dataArray = JSONArray(response)
            for (i in 0 until dataArray.length()) {
                val dataobj = dataArray.getJSONObject(i)
                userArrayList.add(dataobj.toString())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return userArrayList
    }
}
