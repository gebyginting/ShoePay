package com.geby.shoepay.utilities

import android.content.Context
import android.content.SharedPreferences

class UserPreference(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_UID = "uid"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    fun saveUser(uid: String, name: String, email: String) {
        prefs.edit()
            .putString(KEY_UID, uid)
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun getUid(): String = prefs.getString(KEY_UID, "") ?: ""
    fun getName(): String = prefs.getString(KEY_NAME, "") ?: ""
    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    fun clear() {
        prefs.edit().clear().apply()
    }
}