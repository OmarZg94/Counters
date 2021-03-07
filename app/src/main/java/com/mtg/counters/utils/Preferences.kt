package com.mtg.counters.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Preferences(val context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveData(key: Int, data: String) {
        val editor = this.preferences.edit()
        editor.putString(context.getString(key), data)
        editor.apply()
    }

    fun saveDataBool(key: Int, data: Boolean) {
        val editor = this.preferences.edit()
        editor.putBoolean(context.getString(key), data)
        editor.apply()
    }

    fun containsData(key: Int): Boolean {
        return this.preferences.getBoolean(context.getString(key), false)
    }

    fun loadDataBoolean(key: Int, defValue: Boolean): Boolean {
        return this.preferences.getBoolean(context.getString(key), defValue)
    }

    fun loadData(key: Int, def: String): String? {
        return this.preferences.getString(context.getString(key), def)
    }

    fun clearPreferences() {
        val editor = this.preferences.edit()
        editor.clear()
        editor.apply()
        return
    }

    fun clearPreference(key: Int) {
        val editor = this.preferences.edit()
        editor.remove(context.getString(key))
        editor.apply()
        return
    }

    fun saveDataInt(key: Int, data: Int) {
        val editor = this.preferences.edit()
        editor.putInt(context.getString(key), data)
        editor.apply()
    }

    fun loadDataInt(key: Int): Int {
        return this.preferences.getInt(context.getString(key), -1)
    }
}