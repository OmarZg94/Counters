package com.mtg.counters.modules.fr_create_item

import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.db.entities.Counters
import com.mtg.counters.db.managers.CountersMgr
import com.mtg.counters.net.CounterResponse
import com.mtg.counters.net.NetworkApi
import com.mtg.counters.net.TitleRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class CreateItemIterator(private val listener: CreateItemPresenter) : Iterator {

    override fun saveCounter(name: String) {
        val request = TitleRequest(name)
        NetworkApi().getNetworkService().saveCounter(request).enqueue(object : Callback<MutableList<CounterResponse>> {
            override fun onResponse(call: Call<MutableList<CounterResponse>>, response: Response<MutableList<CounterResponse>>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val result = response.body()!!
                    val newCounters = mutableListOf<Counters>()
                    result.forEach { counter ->
                        newCounters.add(Counters(counter.id, counter.title, counter.count))
                    }
                    CountersMgr.insertCounters(newCounters)
                    listener.onCounterSave()
                } else {
                    listener.onError(Application.getContext().getString(R.string.e_connection_failed_status) + response.code())
                }
            }

            override fun onFailure(call: Call<MutableList<CounterResponse>>, t: Throwable) {
                listener.onError(t.message
                        ?: Application.getContext().getString(R.string.e_connection_failed))
            }
        })
    }
}

private interface Iterator {
    fun saveCounter(name: String)
}