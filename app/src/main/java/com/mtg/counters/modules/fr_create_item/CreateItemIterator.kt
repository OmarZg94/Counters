package com.mtg.counters.modules.fr_create_item

import com.mtg.counters.net.CounterResponse
import com.mtg.counters.net.NetworkApi
import com.mtg.counters.net.TitleRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateItemIterator(private val listener: CreateItemFragment.Presenter) : Iterator {
    override fun saveCounter(name: String) {
        val request = TitleRequest(name)
        NetworkApi().getNetworkService().saveCounter(request).enqueue(object : Callback<MutableList<CounterResponse>>{
            override fun onResponse(call: Call<MutableList<CounterResponse>>, response: Response<MutableList<CounterResponse>>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<MutableList<CounterResponse>>, t: Throwable) {

            }
        })
    }
}

private interface Iterator {
    fun saveCounter(name: String)
}