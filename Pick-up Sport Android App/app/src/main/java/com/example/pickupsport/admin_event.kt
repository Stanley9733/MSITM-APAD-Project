package com.example.pickupsport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.admin_event.view.*
import kotlinx.android.synthetic.main.listing_fragment.view.*
import org.jetbrains.anko.uiThread

class admin_event : Fragment(){

    private var response: String? = null
    private var eventlist: ListView? = null
    private var eventModelArrayList: ArrayList<Event_Model>? = null
    private var eventAdapter: EventAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.admin_event, container, false)

        view.back_buttonevent.setOnClickListener({
            // Navigate to the next Fragment.
            (activity as NavigationHost).navigateTo(admin_home(), false)
        })

        doAsync {
            val events = getEvents()
            response = events
            uiThread {
                println("response")
                println(response)
                eventlist = view.eventlist
                eventModelArrayList = getInfo(response!!)
                eventAdapter = EventAdapter(view.context, eventModelArrayList!!)
                eventlist!!.adapter = eventAdapter

                eventlist!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedEvent = eventAdapter!!.getItem(position)
                    val selectedEventId = selectedEvent.toString()
                    println("selected event id: " + selectedEventId)

                    doAsync {
                        val removeEventResp = removeEvent(selectedEventId)
                        println("removeEventResp: " + removeEventResp)
                        uiThread {

                            if (removeEventResp.equals("Deleted event")) {
                                val dur = Toast.LENGTH_SHORT
                                val message = "Event was deleted"
                                val toast = Toast.makeText(view.context, message, dur)
                                toast.show()
                                (activity as NavigationHost).navigateTo(admin_home(), false)
                            } else {
                                println("Something went wrong deleting event")
                            }
                        }
                    }

                }
            }
        }

        return view
    }

    fun removeEvent(eventId:String?) : String? {

        val url = "https://my-apad-project.appspot.com/app/admin/events/"+eventId

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

    fun getEvents() : String? {

        val url = "https://my-apad-project.appspot.com/app/events"

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