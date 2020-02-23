package com.example.pickupsport

class Venue_Model {

    var vlocation : String? = null
    var vname: String? = null
    var vdescription: String? = null
    var vId: String? = null


    fun getName(): String {
        return vname.toString()
    }

    fun setName(vname: String) {
        this.vname = vname
    }


    fun getDescriptions(): String {
        return vdescription.toString()
    }

    fun setDescriptions(description: String) {
        this.vdescription = description
    }


    fun getLocation(): String {
        return vlocation.toString()
    }

    fun setLocation(vlocation: String) {
        this.vlocation = vlocation
    }

    fun setvIds(vId: Int) {
        this.vId = vId.toString()
    }

    override fun toString() : String {
        return this.vId.toString()
    }


}