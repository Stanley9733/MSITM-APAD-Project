package com.example.pickupsport

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.search_events.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class searchEvents : Fragment(){

    private var response: String? = null
    private var eventlist: ListView? = null
    private var eventModelArrayList: ArrayList<Event_Model>? = null
    private var eventAdapter: EventAdapter? = null
    private lateinit var listView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.search_events, container, false)

        view.back_buttonevent.setOnClickListener{
            (activity as NavigationHost).navigateTo(user_home(), false)
        }

        view.searchEventsBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e(TAG,"Press querysubmit: " + query)
                doAsync {
                    val searchResp = searchEvents(query)
                    val response = JSONArray(searchResp)
                    println("response: " + response)
                    val responseSize = response.length()

                    uiThread {
                        if (responseSize == 0) {
                            Log.e(TAG, "Failed to get any searches")
                            val dur = Toast.LENGTH_LONG
                            val message = "Your search did not match any events! Please try with other keywords"
                            val toast = Toast.makeText(view.context, message, dur)
                            toast.show()
                        } else {
                            eventlist = view.filteredEventlist
                            eventModelArrayList = getInfo(searchResp!!)
                            eventAdapter = EventAdapter(view.context, eventModelArrayList!!)
                            eventlist!!.adapter = eventAdapter
                        }
                    }

                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                Log.e(TAG,"Press on text change: " + newText)
                return true
            }
        })

        return view
    }

    fun searchEvents(query:String?) : String? {
        val url = "https://my-apad-project.appspot.com/app/events/search?description="+query

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Android")
            .build()
        val response = client.newCall(request).execute()
        val bodyStr =  response?.body()?.string() // this can be consumed only once
        return bodyStr
    }

    fun getInfo(response: String): ArrayList<Event_Model> {
        val eventModelArrayList = ArrayList<Event_Model>()
        try {
            val dataArray = JSONArray(response)
            for (i in 0 until dataArray.length()) {
                val eventsModel = Event_Model()
                val dataobj = dataArray.getJSONObject(i)
                eventsModel.setOrganizers(dataobj.getString("organizer"))
                eventsModel.setDescriptions(dataobj.getString("description"))
                eventsModel.setTimes(dataobj.getString("time"))
                eventsModel.setIds(dataobj.getInt("id"))
                eventModelArrayList.add(eventsModel)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return eventModelArrayList
    }
}