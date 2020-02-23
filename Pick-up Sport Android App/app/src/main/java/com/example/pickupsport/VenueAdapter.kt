package com.example.pickupsport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.ArrayList

class VenueAdapter(private val context: Context, private val venuesModelArrayList: ArrayList<Venue_Model>) :
    BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return venuesModelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return venuesModelArrayList[position]
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
            convertView = inflater.inflate(R.layout.venue, null, true)

            holder.vname = convertView!!.findViewById(R.id.name) as TextView
            holder.vlocation = convertView.findViewById(R.id.location) as TextView
            holder.vdescription = convertView.findViewById(R.id.details) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.vname!!.text = "Name: " + venuesModelArrayList[position].getName()
        holder.vlocation!!.text = "Location: " + venuesModelArrayList[position].getLocation()
        holder.vdescription!!.text = "Details: " + venuesModelArrayList[position].getDescriptions()

        return convertView
    }

    private inner class ViewHolder {

        var vname: TextView? = null
        var vdescription: TextView? = null
        var vlocation: TextView? = null
    }

}