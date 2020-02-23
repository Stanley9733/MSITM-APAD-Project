package com.example.pickupsport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.ArrayList

/**
 * Orignial author: Parsania Hardik on 03-Jan-17.
 * Modified by Ramesh Yerraballi on 8/12/19
 */
class CustomAdapter(private val context: Context, private val usersModelArrayList: ArrayList<User_Model>) :
    BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return usersModelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return usersModelArrayList[position]
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
            convertView = inflater.inflate(R.layout.user, null, true)

            holder.name = convertView!!.findViewById(R.id.name) as TextView
            holder.phone = convertView.findViewById(R.id.email) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.name!!.text = "Name: " + usersModelArrayList[position].getNames()
        holder.phone!!.text = "Phone: " + usersModelArrayList[position].getPhones()

        return convertView
    }

    private inner class ViewHolder {

        var name: TextView? = null
        var phone: TextView? = null
    }

}