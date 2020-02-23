package com.example.pickupsport

class User_Model {

    var admin: String? = null
    var email: String? = null
    var id: String? = null
    var name: String? = null
    var password: String? = null
    var phone: String? = null


    fun getPasswords(): String {
        return password.toString()
    }

    fun setPasswords(password: String) {
        this.password = password
    }

    fun getIds(): String {
        return id.toString()
    }

    fun setIds(id: Int) {
        this.id = id.toString()
    }

    fun getPhones(): String {
        return phone.toString()
    }

    fun setPhones(phone: String) {
        this.phone = phone
    }

    fun getAdmins(): String {
        return admin.toString()
    }

    fun setAdmins(admin: String) {
        this.admin = admin
    }

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getEmails(): String {
        return email.toString()
    }

    fun setEmails(name: String) {
        this.email = name
    }

    override fun toString(): String {
        return this.id.toString()
    }
}