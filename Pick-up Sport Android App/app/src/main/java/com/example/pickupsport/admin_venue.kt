package com.example.pickupsport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_user.view.*
import okhttp3.*
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
import kotlinx.android.synthetic.main.admin_venue.*
import kotlinx.android.synthetic.main.admin_venue.view.venuelist
import kotlinx.android.synthetic.main.listing_fragment.view.*
import org.jetbrains.anko.uiThread

class admin_venue : Fragment(){

    private var response: String? = null
    private var venuelist: ListView? = null
    private var venueModelArrayList: ArrayList<Venue_Model>? = null
    private var venueAdapter: VenueAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.admin_venue, container, false)
        view.back_buttonvenue.setOnClickListener({
            // Navigate to the next Fragment.
            (activity as NavigationHost).navigateTo(admin_home(), false)
        })

        doAsync {
            val venues = getVenues()
            response = venues
            uiThread {
                println("response")
                println(response)
                venuelist = view.venuelist
                venueModelArrayList = getInfo(response!!)
                venueAdapter = VenueAdapter(view.context, venueModelArrayList!!)
                venuelist!!.adapter = venueAdapter

                venuelist!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedVenue = venueAdapter!!.getItem(position)
                    val selectedVenueId = selectedVenue.toString()
                    println("selected venue id: " + selectedVenueId)

                    doAsync {
                        val removeVenueResp = removeVenue(selectedVenueId)
                        println("removeVenueResp: " + removeVenueResp)
                        uiThread {

                            if (removeVenueResp.equals("Deleted venue")) {
                                val dur = Toast.LENGTH_SHORT
                                val message = "Venue was deleted"
                                val toast = Toast.makeText(view.context, message, dur)
                                toast.show()
                                (activity as NavigationHost).navigateTo(admin_home(), false)
                            } else {
                                println("Something went wrong deleting venue")
                            }
                        }
                    }

                }
            }
        }

        return view
    }

    fun removeVenue(venueId:String?) : String? {

        val url = "https://my-apad-project.appspot.com/app/admin/venues/"+venueId

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

    fun getVenues() : String? {

        val url = "https://my-apad-project.appspot.com/app/venues"

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


    fun getInfo(response: String): ArrayList<Venue_Model> {
        val venueModelArrayList = ArrayList<Venue_Model>()
        try {
            val dataArray = JSONArray(response)
            for (i in 0 until dataArray.length()) {
                val venueModel = Venue_Model()
                val dataobj = dataArray.getJSONObject(i)
                venueModel.setName(dataobj.getString("name"))
                venueModel.setLocation(dataobj.getString("location"))
                venueModel.setDescriptions(dataobj.getString("description"))
                venueModel.setvIds(dataobj.getInt("id"))
                venueModelArrayList.add(venueModel)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return venueModelArrayList
    }

}