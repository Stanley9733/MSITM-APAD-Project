package com.example.pickupsport

class Event_Model {

    var id: Int = 0
    var organizer: String? = null
    var description: String? = null
    var time: String? = null


    fun getOrganizers(): String {
        return organizer.toString()
    }

    fun setOrganizers(organizer: String) {
        this.organizer = organizer
    }


    fun getDescriptions(): String {
        return description.toString()
    }

    fun setDescriptions(description: String) {
        this.description = description
    }

    fun getIds(): Int {
        return id
    }

    fun setIds(id: Int) {
        this.id = id
    }

    fun getTimes(): String {
        return time.toString()
    }

    fun setTimes(time: String) {
        this.time = time
    }

    override fun toString(): String {
        return this.id.toString()
    }


}