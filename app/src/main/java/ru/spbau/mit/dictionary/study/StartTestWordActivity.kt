package ru.spbau.mit.dictionary.study

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import ru.spbau.mit.dictionary.R

class StartTestWordActivity : AppCompatActivity() {
    private lateinit var startTest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_test_word)
        startTest = findViewById(R.id.start_test)
        startTest.setOnClickListener {
            startTestWordActivity()
        }
    }

    private fun startTestWordActivity() {
        val intent = Intent(this, TestWordActivity::class.java)
        startActivity(intent)
    }
}
