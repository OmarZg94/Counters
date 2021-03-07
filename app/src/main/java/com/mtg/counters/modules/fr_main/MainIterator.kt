package com.mtg.counters.modules.fr_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.db.entities.Counters
import com.mtg.counters.db.managers.CountersMgr
import com.mtg.counters.modules.fr_main.MainFragment.Companion.OPTION_INCREMENT
import com.mtg.counters.net.CounterResponse
import com.mtg.counters.net.IdRequest
import com.mtg.counters.net.NetworkApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MainIterator : ViewModel(), Iterator {

    private lateinit var listener: MainPresenter
    private val networkApi by lazy { NetworkApi() }

    override fun setListener(listener: MainPresenter) {
        this.listener = listener
    }

    override fun getLastCounters(): LiveData<MutableList<Counters>> {
        return Application.getDatabase().countersDao().selectAll()
    }

    override fun incrementCounter(counter: Counters) {
        val request = IdRequest(counter.id)
        networkApi.getNetworkService().incrementCounter(request).enqueue(object : Callback<MutableList<CounterResponse>> {
            override fun onResponse(call: Call<MutableList<CounterResponse>>, response: Response<MutableList<CounterResponse>>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val result = response.body()!!
                    val newCounters = mutableListOf<Counters>()
                    result.forEach { newCounters.add(Counters(it.id, it.title, it.count)) }
                    CountersMgr.insertCounters(newCounters)
                } else {
                    listener.onError(counter, Application.getContext().getString(R.string.e_connection_failed) + "${response.code()}", OPTION_INCREMENT)
                }
            }

            override fun onFailure(call: Call<MutableList<CounterResponse>>, t: Throwable) {
                listener.onError(counter, t.message
                        ?: Application.getContext().getString(R.string.e_connection_failed), OPTION_INCREMENT)
            }
        })
    }

    override fun decrementCount(counter: Counters) {

    }

    override fun deleteCounter(counter: Counters) {

    }
}

private interface Iterator {
    fun setListener(listener: MainPresenter)
    fun getLastCounters(): LiveData<MutableList<Counters>>
    fun incrementCounter(counter: Counters)
    fun decrementCount(counter: Counters)
    fun deleteCounter(counter: Counters)
}