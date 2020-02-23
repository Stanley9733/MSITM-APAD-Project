package com.example.pickupsport

import android.content.Context

class MyPreference (context: Context) {

    val PREFERENCE_ID = "100"

    val preference = context.getSharedPreferences(PREFERENCE_ID,Context.MODE_PRIVATE)

    fun getUserId() : String? {
        return preference.getString(PREFERENCE_ID,"")
    }

    fun setUserId(userId:String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_ID,userId)
        editor.apply()
    }
}