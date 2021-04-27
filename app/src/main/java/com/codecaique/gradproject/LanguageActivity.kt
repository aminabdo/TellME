package com.codecaique.gradproject

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LanguageActivity : AppCompatActivity() {
    private val TAG = "LanguageActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
    }


    fun shared_en(){
//        val sharedPref = this?.getSharedPreferences(
//            "language", Context.MODE_PRIVATE)
//        with (sharedPref.edit()) {
//            putString("language", "english")
//            apply()
//        }

        val locale = Locale("en")
        Locale.setDefault(locale)
        val resources: Resources = this.getResources()
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())


        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("language", "english")
            apply()
        }

    }

    fun shared_ar(){
//        val sharedPref = this?.getSharedPreferences(
//            "language", Context.MODE_PRIVATE)
//        with (sharedPref.edit()) {
//            putString("language", "english")
//            apply()
//        }

        val locale = Locale("ar")
        Locale.setDefault(locale)
        val resources: Resources = this.getResources()
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("language", "arabic")
            apply()
        }



    }

    fun arabic(view: View) {
        Log.e(TAG, "arabic: ")
        shared_ar()
        startTypeActivity()
    }
    fun english(view: View) {
        Log.e(TAG, "english: ")
        shared_en()
        startTypeActivity()
    }

    fun test(view: View) {
        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val language = sharedPref.getString("language", "english")
        Log.e(TAG, "test: lang  ->  " + language)
    }

    fun startTypeActivity(){
        var i = Intent(this, TypeActivity::class.java);
        startActivity(i)
    }
}