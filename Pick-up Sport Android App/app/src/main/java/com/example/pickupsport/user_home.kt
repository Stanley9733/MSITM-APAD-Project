package com.example.pickupsport
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_home.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class user_home: Fragment() {
    private var response: String? = null
    private var eventlist: ListView? = null
    private var eventModelArrayList: ArrayList<Event_Model>? = null
    private var eventAdapter: EventAdapter? = null
    private var userId:String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.user_home, container, false)
        val pref = activity!!.getPreferences(Context.MODE_PRIVATE)
        val id = pref.getString("userId", "empty")
        val username = pref.getString("userName", "empty")
        println("user: " + id)
        println("name: " + username)

        view.nameView.text = "Welcome " + username +"!"

        userId = id
        view.joinevent.setOnClickListener {
            (activity as NavigationHost).navigateTo(userEvents(), false)
        }

        view.leavevent.setOnClickListener {
            (activity as NavigationHost).navigateTo(userEventsLeave(), false)
        }

        view.logoutUser.setOnClickListener({
            // Navigate to the next Fragment.
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
        })
        doAsync {
            val events = getEvents()
            response = events
            uiThread {
                println("response")
                println(response)
                if (response == "None"){
                    view.hidetext.text = "Currently you don't have any events on your account."
                    view.hidetext.textSize = 15f
                    view.leavevent.visibility = View.INVISIBLE
                }
                else{
                    eventlist = view.eventlist1
                    eventModelArrayList = getInfo(response!!)
                    eventAdapter = EventAdapter(view.context, eventModelArrayList!!)
                    eventlist!!.adapter = eventAdapter
                }
            }
        }
        return view
    }
    fun getEvents() : String? {
        val idStr = 3.toString()
        val url = "https://my-apad-project.appspot.com/app/events"+"/"+userId
        //val url = "https://my-apad-project.appspot.com/app/events"+"/"+8
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
                eventModelArrayList.add(eventsModel)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return eventModelArrayList
    }
}
