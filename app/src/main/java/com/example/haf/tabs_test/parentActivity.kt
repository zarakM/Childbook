package com.example.haf.tabs_test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

class parentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
        val addChild = findViewById<Button>(R.id.addChild)

        addChild.setOnClickListener({
            val intent = Intent(this,childSignup::class.java)
            startActivity(intent)
        })


    }
}
