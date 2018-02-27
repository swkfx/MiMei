package com.fangx.mimei.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
object DelegatesExt {
    fun <T> notNullSingleValue() = NotNullSingleValueVar<T>()

    fun <T> preference(context: Context, key: String, default: T) = Preference(context, key, default)
}

object Utils {

    fun formatTime(source: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.getDefault())
        val date = sdf.parse(source)
        val newSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return newSdf.format(date)
    }

    fun formatParam(source: String): String {
        return formatTime(source).replace("-", "/")
    }
}

class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} " +
                "not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")

    }
}

class Preference<T>(private val context: Context,
                    private val key: String,
                    private val default: T) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any, property: KProperty<*>): T = findPreference(key, default)

    @Suppress("UNCHECKED_CAST")
    private fun findPreference(key: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(key, default)
            is Int -> getInt(key, default)
            is String -> getString(key, default)
            is Boolean -> getBoolean(key, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as T
    }


    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        putPreference(key, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putPreference(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }.apply()
    }

}

