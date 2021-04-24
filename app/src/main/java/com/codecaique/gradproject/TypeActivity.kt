package com.codecaique.gradproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class TypeActivity : AppCompatActivity() {
    private val TAG = "TypeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)
    }

    fun text(view: View) {
        Log.e(TAG, "text: ")
        var i = Intent(this ,TextActivity::class.java);
        startActivity(i)
    }
    fun voice(view: View) {
        Log.e(TAG, "voice: ")
        var i = Intent(this ,SpeechActivity::class.java);
        startActivity(i)
    }
}