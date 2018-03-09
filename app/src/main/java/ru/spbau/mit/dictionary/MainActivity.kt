package ru.spbau.mit.dictionary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var globalContext: GlobalContext? = null
    private val REQUEST_CODE = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            globalContext!!.setLanguage(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("OnCreate", "Start")
        globalContext = GlobalContext(this, this)
        globalContext!!.onCreate()
    }

    override fun onClick(v: View) {
        globalContext!!.startActivityForResult(SearchActivity::class.java, REQUEST_CODE)
    }
}
