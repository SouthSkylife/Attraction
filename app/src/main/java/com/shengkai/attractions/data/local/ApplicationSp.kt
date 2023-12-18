package com.shengkai.attractions.data.local

import android.content.Context
import com.pddstudio.preferences.encrypted.BuildConfig
import com.pddstudio.preferences.encrypted.EncryptedPreferences

class ApplicationSp(val context: Context) {

    private var sharedPreference: EncryptedPreferences =
        EncryptedPreferences.Builder(context)
            .withEncryptionPassword(BuildConfig.APPLICATION_ID).build()

    companion object {
        const val CURRENT_LANGUAGE_SIGN = "CURRENT_LANGUAGE_SIGN"
    }

    fun clear() = sharedPreference.edit().clear().apply()

    fun putString(key: String, value: String) =
        sharedPreference.edit().putString(key, value).commit()

    fun putBoolean(key: String, value: Boolean) =
        sharedPreference.edit().putBoolean(key, value).commit()

    fun putInt(key: String, value: Int) =
        sharedPreference.edit().putInt(key, value).commit()

    fun putLong(key: String, value: Long) =
        sharedPreference.edit().putLong(key, value).commit()

    fun putFloat(key: String, value: Float) =
        sharedPreference.edit().putFloat(key, value).commit()

    fun getString(key: String) =
        sharedPreference.getString(key, "") ?: ""

    fun getString(key: String, defaultValue: String) =
        sharedPreference.getString(key, defaultValue) ?: ""

    fun getBoolean(key: String) =
        sharedPreference.getBoolean(key, false)

    fun getInt(key: String) =
        sharedPreference.getInt(key, 0)

    fun getLong(key: String) =
        sharedPreference.getLong(key, 0L)

    fun getFloat(key: String) =
        sharedPreference.getFloat(key, 0f)


}