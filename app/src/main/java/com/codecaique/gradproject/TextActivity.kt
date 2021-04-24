package com.codecaique.gradproject

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class TextActivity : AppCompatActivity() {


    private val TAG = "SpeechActivity"
    private val REQ_CODE = 100
    var textView: TextView? = null
    var imageView: ImageView? = null
    var et_text: EditText? = null

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("message")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)



        var language = "ar-EG"
        language = "us-US";

        textView = findViewById<View>(R.id.text) as TextView?
        imageView = findViewById<View>(R.id.image) as ImageView?
        et_text = findViewById<View>(R.id.et_text) as EditText?

    }

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
    fun trans(view: View) {

        var data = search(et_text?.text.toString())
        if(data == null){
            Log.e(TAG, "trans: if ---> 11111", )
            Log.e(TAG, "trans: ${this.resources.getString(R.string.error1)}")
            textView?.setText(this.resources.getString(R.string.error1) );
            imageView?.setImageDrawable(null)
            return
        }
        else{
            Log.e(TAG, "trans: else ----> 2222", )
            if(data.imgLink == "" || data.imgLink == null){
                Log.e(TAG, "trans: ${this.resources.getString(R.string.error2)}")
                textView?.setText(this.resources.getString(R.string.error2) );
                return
            }
            Log.e(TAG, "trans: sss")
            textView?.setText("");
            Picasso.get().load(data.imgLink).into(imageView)

            return
        }
        if(et_text?.text?.contains("hi") == true || et_text?.text?.contains("hello") == true){

            imageView?.setImageDrawable(resources.getDrawable(R.drawable.hi))
        }
        else{
            textView?.setText(Resources.getSystem().getString(R.string.error1));
            imageView?.setImageDrawable(null)
        }


        if(et_text?.text?.contains("اهلا") == true || et_text?.text?.contains("السلام عليكم") == true){
            textView?.setText("");
            imageView?.setImageDrawable(resources.getDrawable(R.drawable.hi))
        }
        else{
            textView?.setText(Resources.getSystem().getString(R.string.error1));
            imageView?.setImageDrawable(null)
        }
    }


    fun search(search: String):DataRow?{
        for(d in row){
            if(d?.msg_arabic?.contains(search) == true || d?.msg_english?.contains(search) == true){
                return d
            }
        }
        return null ;
    }

}