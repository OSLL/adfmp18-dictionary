package ru.spbau.mit.dictionary.study

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_study.*
import ru.spbau.mit.dictionary.R
import ru.spbau.mit.dictionary.main.Word


class StudyActivity : AppCompatActivity() {


    private lateinit var words: List<Word>
    private var current: Int = 0

    private fun show(word: Word) {
        wordView.text = word.word
        ftv.text = word.translate.reduce{
            acc, s ->
            "$acc;\n$s"
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        val nextButton = findViewById<ImageButton>(R.id.next)
        val prevButton = findViewById<ImageButton>(R.id.prev)
        prevButton.isClickable = false

        nextButton.setOnClickListener {
            show(words[++current])
            prevButton.isClickable = true
            if (current == words.size - 1) {
                Log.d("next", "clickable = false")
                it.isClickable = false
                current = words.size - 1
            }
        }

        prevButton.setOnClickListener {
            show(words[--current])
            nextButton.isClickable = true
            if (current == 0) {
                Log.d("prev", "clickable = false")
                it.isClickable = false
            }
        }

        words = intent.getSerializableExtra("words") as List<Word>
        if (words.isNotEmpty()) {
            show(words[0])
        }
    }
}