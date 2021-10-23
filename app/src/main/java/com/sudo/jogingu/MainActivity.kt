package com.sudo.jogingu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sayHi()
        sad()
    }
    fun sayHi(){
        print("hello")
    }
    fun sad(){
        print("fdsfds")
    }

    fun duong(){
        print("duong ne ahihihi")
    }

}