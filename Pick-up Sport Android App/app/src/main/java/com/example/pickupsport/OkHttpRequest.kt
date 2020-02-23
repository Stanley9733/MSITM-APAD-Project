package com.example.pickupsport

import okhttp3.*

class OkHttpRequest(client: OkHttpClient) {

    internal var client = OkHttpClient()

    init {
        this.client = client
    }

    fun fetchGet(url: String, callback: Callback) : Call {
        val request = Request.Builder()
            .url(url)
            .build()

//        val response = client.newCall(request).execute()
//        val bodystr = response.body()?.string()
//        return bodystr
        val call = client.newCall(request)
        call.enqueue(callback)
        return call

    }

    fun fetchPost(url: String, parameters: HashMap<String, String>) : String? {
        val client = OkHttpClient()
//        val request = OkHttpRequest(client)

        val builder = FormBody.Builder()
        val it = parameters.entries.iterator()

        while(it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }

        val formBody = builder.build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        val response = client.newCall(request).execute()
        val bodystr = response?.body()?.string()
        return bodystr
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}