package ru.spbau.mit.dictionary.study

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import ru.spbau.mit.dictionary.R
import ru.spbau.mit.dictionary.main.Word
import android.text.Editable
import android.text.TextWatcher



class TestWordActivity : AppCompatActivity() {

    private lateinit var words: ArrayList<Word>
    private lateinit var imageView: ImageView
    private lateinit var wordView: TextView
    private lateinit var answerView: EditText
    private var current: Int = 0

    private fun test(word: Word) {
        wordView.text = word.word
        if (word.img != null) {
            val img = word.img!!
            val bmp = BitmapFactory.decodeByteArray(img, 0, img.size)
            imageView.setImageBitmap(Bitmap.createBitmap(bmp))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_word)
        words = intent.getSerializableExtra(getString(R.string.words)) as ArrayList<Word>
        val answers: ArrayList<Boolean> = ArrayList(words.size)
        wordView = findViewById(R.id.wordView)
        imageView = findViewById(R.id.imageView)
        answerView = findViewById(R.id.editText)
        answerView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                answerView.setBackgroundColor(Color.WHITE)
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })
        val nextButton = findViewById<ImageButton>(R.id.next)
        val prevButton = findViewById<ImageButton>(R.id.prev)
        prevButton.isEnabled = false

        if (words.isNotEmpty()) {
            test(words[0])
        }

        nextButton.setOnClickListener {
            if (current == words.size - 1) {
                val intent = Intent(this, TestEndActivity::class.java)
                intent.putExtra(getString(R.string.answers), answers)
                startActivity(intent)
            } else {
                test(words[++current])
                prevButton.isEnabled = true
            }

        }

        prevButton.setOnClickListener {
            test(words[--current])
            nextButton.isEnabled = true
            if (current == 0) {
                Log.d("prev", "clickable = false")
                it.isEnabled = false
            }
        }

        val checkButton = findViewById<Button>(R.id.checkButton)
        checkButton.setOnClickListener {
            if (words[current].translates.contains(answerView.text.toString())) {
                answerView.setBackgroundColor(Color.GREEN)
                answers[current] = true
            } else {
                answerView.setBackgroundColor(Color.RED)
                answers[current] = false
            }
        }
    }
}
