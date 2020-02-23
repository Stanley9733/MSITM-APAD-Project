package com.example.pickupsport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.ArrayList

class EventAdapter(private val context: Context, private val eventsModelArrayList: ArrayList<Event_Model>) :
    BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return eventsModelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return eventsModelArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            convertView = inflater.inflate(R.layout.event, null, true)
            convertView = inflater.inflate(R.layout.event, parent, false)

            holder.organizer = convertView!!.findViewById(R.id.name) as TextView
            holder.description = convertView.findViewById(R.id.description) as TextView
            holder.time = convertView.findViewById(R.id.time) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.organizer!!.text = "Name: " + eventsModelArrayList[position].getOrganizers()
        holder.description!!.text = "Details: " + eventsModelArrayList[position].getDescriptions()
        holder.time!!.text = "Time: " + eventsModelArrayList[position].getTimes()

        return convertView
    }

    private inner class ViewHolder {

        var organizer: TextView? = null
        var description: TextView? = null
        var time: TextView? = null
    }

}