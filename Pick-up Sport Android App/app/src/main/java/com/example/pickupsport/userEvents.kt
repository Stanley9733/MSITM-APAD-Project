package com.example.pickupsport

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_event.*
import kotlinx.android.synthetic.main.user_event.view.*
import kotlinx.android.synthetic.main.user_event.view.joineventlist
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class userEvents : Fragment(){

    private var response: String? = null
    private var eventlist: ListView? = null
    private var eventModelArrayList: ArrayList<Event_Model>? = null
    private var eventAdapter: EventAdapter? = null
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.user_event, container, false)


        view.search_buttonevent.setOnClickListener {
            (activity as NavigationHost).navigateTo(searchEvents(), false)
        }

        view.back_buttonevent.setOnClickListener({
            // Navigate to the next Fragment.
            (activity as NavigationHost).navigateTo(user_home(), false)
        })

        doAsync {
            val events = getEvents()
            response = events
            uiThread {
                println("response")
                println(response)
                eventlist = view.joineventlist
                eventModelArrayList = getInfo(response!!)
                eventAdapter = EventAdapter(view.context, eventModelArrayList!!)
                eventlist!!.adapter = eventAdapter

                eventlist!!.setOnItemClickListener{ _, _, position, _ ->
                    val selectedEvent = eventAdapter!!.getItem(position)
                    val selectedEventId = selectedEvent.toString()
                    println("selected event id: " + selectedEventId)

                    val pref = activity!!.getPreferences(Context.MODE_PRIVATE)
                    val userId = pref.getString("userId", "empty")
                    println("user: " + userId)

                    doAsync {
                        val joinEventResp = jointEvent(userId, selectedEventId)
                        println("joinEventResp: " + joinEventResp)
                        uiThread {

                            if (joinEventResp.equals("Success")) {
                                val dur = Toast.LENGTH_SHORT
                                val message = "You have now joined this event!"
                                val toast = Toast.makeText(view.context, message, dur)
                                toast.show()
                                (activity as NavigationHost).navigateTo(user_home(), false)
                            } else if (joinEventResp.equals("Player already added")) {
                                val joinError1 = "Looks like you have already joined this event"
                                println(joinError1)
                                val duration = Toast.LENGTH_LONG
                                val toast = Toast.makeText(view.context, joinError1, duration)
                                toast.show()
                            } else if (joinEventResp.equals("User cannot be added to this event")) {
                                val joinError2 = "This event is full"
                                println(joinError2)
                                val duration = Toast.LENGTH_LONG
                                val toast = Toast.makeText(view.context, joinError2, duration)
                                toast.show()
                            }
                        }
                    }
                }
            }
        }

        return view
    }

    fun jointEvent(userId:String?, eventId:String) : String? {
        val url = "https://my-apad-project.appspot.com/app/"+userId+"/join/"+eventId

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