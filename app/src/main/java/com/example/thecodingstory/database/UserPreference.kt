package com.example.thecodingstory.database

import android.content.Context

internal class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val ID = "id"
        private const val NAME = "name"
        private const val LOGIN_STATUS = "login_status"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)


    fun setSession(token: String, id: String, name: String){
        val editor = preferences.edit()
        editor.putBoolean(LOGIN_STATUS,true)
        editor.putString(TOKEN,token)
        editor.putString(ID,id)
        editor.putString(NAME,name)
        editor.apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN,"none")
    }

    fun isLogged(): Boolean{
        return preferences.getBoolean(LOGIN_STATUS,false)
    }

    fun resetSession(){
        val editor = preferences.edit()
        editor.putBoolean(LOGIN_STATUS,false)
        editor.putString(TOKEN,"none")
        editor.putString(ID,"none")
        editor.putString(NAME,"none")
        editor.apply()
    }


}