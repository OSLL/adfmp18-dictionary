package ru.spbau.mit.dictionary

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView


class AddWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        val intent = intent
        val wordTextBox = findViewById<View>(R.id.wordTextView) as TextView
        wordTextBox.text = intent.getStringExtra("word")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val view = window.decorView
        val lp = view.layoutParams as WindowManager.LayoutParams
        lp.gravity = Gravity.BOTTOM
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
        windowManager.updateViewLayout(view, lp)
    }
}
