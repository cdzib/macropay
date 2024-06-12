package com.cesar.examenmacropay.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class LocalData private constructor() {
    fun setLogin(isLogin: Boolean) {
        sharedPreferences.edit().putBoolean("isLogin", isLogin).commit()
    }
    fun getLogin(): Boolean {
        return sharedPreferences.getBoolean("isLogin", false)
    }
    fun remove(){
        sharedPreferences.edit().clear().apply()
    }
    companion object {
        private val sharePref = LocalData()
        private lateinit var sharedPreferences: SharedPreferences
        fun getInstance(context: Context): LocalData {
            if (!::sharedPreferences.isInitialized) {
                synchronized(LocalData::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }
}