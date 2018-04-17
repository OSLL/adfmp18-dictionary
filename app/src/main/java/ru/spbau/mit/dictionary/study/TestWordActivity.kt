package ru.spbau.mit.dictionary.study

import android.content.ContentValues
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
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.MainActivity
import android.content.Intent
import android.util.DisplayMetrics


class TestWordActivity : AppCompatActivity() {

    private lateinit var words: ArrayList<Word>
    private lateinit var imageView: ImageView
    private lateinit var wordView: TextView
    private lateinit var answerView: EditText
    private var current: Int = 0
    private val STUDIED_COUNT: Int = 1

    private fun test(word: Word) {
        wordView.text = word.word
        answerView.text.clear()
        if (word.img != null) {
            val img = word.img!!
            val bmp = BitmapFactory.decodeByteArray(img, 0, img.size)
            imageView.setImageBitmap(Bitmap.createBitmap(bmp))
        } else {
            imageView.setImageResource(R.drawable.imagenotavailable)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_word)
        words = intent.getSerializableExtra(getString(R.string.words)) as ArrayList<Word>
        val answers: ArrayList<Boolean> = ArrayList()
        words.forEach { answers.add(false) }
        wordView = findViewById(R.id.wordView)
        imageView = findViewById(R.id.imageView)
        // display dimensions
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val params = imageView.layoutParams
        params.height = height / 3
        imageView.layoutParams = params

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
                moveStudiedWords(answers)
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

    private fun moveStudiedWords(answers: ArrayList<Boolean>) {
        for (i in 0 until words.size) {
            if (answers[i]) {
                words[i].priority += 1
            }
        }
        words.forEach {
            val contentValue = ContentValues()
            contentValue.put(DictionaryContract.WordsEntry.COLUMN_PRIORITY, it.priority)
            if (it.priority > STUDIED_COUNT) {
                contentValue.put(DictionaryContract.WordsEntry.COLUMN_STATE, DictionaryContract.WordsEntry.STATE_STUDIED)
            }
            contentResolver.update(
                    DictionaryProvider.CONTENT_WORDS_ENTRY,
                    contentValue,
                    "${DictionaryContract.WordsEntry._ID} = ${it.id}",
                    null
            )
        }
        val intent = Intent(applicationContext,
                MainActivity::class.java)
        startActivity(intent)
    }
}
