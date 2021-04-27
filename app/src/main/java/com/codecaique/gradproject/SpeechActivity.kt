package com.codecaique.gradproject

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*


class SpeechActivity : AppCompatActivity() {

    private val TAG = "SpeechActivity"
    private val REQ_CODE = 100
    var textView: TextView? = null
    var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech)

        var language = "ar-EG"
        language = "us-US";

        var langa = this.getPreferences(Context.MODE_PRIVATE).getString("language","english");

        val resources: Resources = this.getResources()
        val config: Configuration = resources.getConfiguration()

        if(config.locale.language.toString().contains("ar")){
            language = "ar-sa" ;
        }else{
            language = "us-US";
        }
        Log.e(TAG, "onCreate: langa" +langa)
        Log.e(TAG, "onCreate: language ${language}" )
        setContentView(R.layout.activity_speech)
        textView = findViewById<View>(R.id.text) as TextView?
        imageView = findViewById<View>(R.id.image) as ImageView?
        val speak: ImageView = findViewById(R.id.speak)
        speak.setOnClickListener{
            Log.e(TAG, "onCreate: speak.setOnClickListener")
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak")
            try {
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,language);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
                startActivityForResult(intent, REQ_CODE)


            } catch (a: ActivityNotFoundException) {
                Toast.makeText(applicationContext,
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show()
            }



        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult: $requestCode")
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && null != data) {
                    var result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    var txt:String? = result?.get(0)

                    if(txt == null){
                        textView?.setText(this.resources.getString(R.string.error1));
                        imageView?.setImageDrawable(null)
                    }
                    var data = search(txt!!)
                    if(data == null){
                        textView?.setText(this.resources.getString(R.string.error1));
                        imageView?.setImageDrawable(null)
                        return
                    }
                    else{

                        if(data.imgLink == "" || data.imgLink == null){
                            textView?.setText(this.resources.getString(R.string.error2));
                            return
                        }
                        textView?.setText("");
                        Picasso.get().load(data.imgLink).into(imageView)

                        return
                    }

                    if(txt?.contains("hi") == true || txt?.contains("hello") == true){
                        textView?.setText(result?.get(0));
                        imageView?.setImageDrawable(resources.getDrawable(R.drawable.hi))
                    }
                    else{
                        textView?.setText("هذه الكلمه غير مترجمه يرجي اضافتها لقاعده البيانات");
                        imageView?.setImageDrawable(null)
                    }
                }
            }
        }
    }

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("message")

    override fun onStart() {
        super.onStart()
        init()
        read()
    }
    fun init(){
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("message")

    }
    fun write(){
        myRef.push().setValue(DataRow("تجربة", "test", ""))
    }
    var row:ArrayList<DataRow?> = ArrayList<DataRow?>()
    fun read(){
        row = ArrayList<DataRow?>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                val value = dataSnapshot.getValue(DataRow::class.java)
//                val value2 = dataSnapshot.child("message")
                row.clear()
                for (ds in dataSnapshot.children) {
                    var d:DataRow? = ds.getValue(DataRow::class.java)
                    row.add(d)
//                    val name = ds.child("msg_arabic").getValue(String::class.java)
//                    val key1 = ds.child("msg_english").child("0").getValue(String::class.java)
//                    val key2 = ds.child("imgLink").child("1").getValue(String::class.java)

                }
                //Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
    fun search(search: String):DataRow?{

        Log.e(TAG, "search: $search")
        for(d in row){
            Log.e(TAG, "search: en -> ${d?.msg_english}")
            Log.e(TAG, "search: ar -> ${d?.msg_arabic}")
            if(d?.msg_arabic?.contains(search) == true || d?.msg_english?.contains(search) == true){
                Log.e(TAG, "search:done ---? $search")
                return d
            }
        }


        return null ;
    }
}