package ru.spbau.mit.dictionary.study

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.R
import ru.spbau.mit.dictionary.main.Word
import uk.co.deanwild.flowtextview.FlowTextView


class StudyActivity : AppCompatActivity() {
    private lateinit var flowTextView: FlowTextView
    private lateinit var imageView: ImageView
    private lateinit var wordView: TextView
    private var current: Int = 0

    private fun show(word: Word) {
        wordView.text = word.word
        flowTextView.text = word.translates.fold("") { acc, s ->
            "$acc;\n$s"
        }
        if (word.img != null) {
            val img = word.img!!
            val bmp = BitmapFactory.decodeByteArray(img, 0, img.size)
            imageView.setImageBitmap(Bitmap.createBitmap(bmp))
        } else {
            imageView.setImageResource(R.drawable.imagenotavailable)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val words = wordsToLearn(10, contentResolver)
        if (words.isNotEmpty()) {
            setContentView(R.layout.activity_study)
            wordView = findViewById<TextView>(R.id.wordView)
            flowTextView = findViewById<FlowTextView>(R.id.ftv)
            imageView = flowTextView.findViewById<ImageView>(R.id.img)
            // display dimensions
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            val params = imageView.layoutParams
            params.width = width / 2
            params.height = height / 3
            imageView.layoutParams = params

            show(words[0])
            val nextButton = findViewById<ImageButton>(R.id.next)
            val prevButton = findViewById<ImageButton>(R.id.prev)
            prevButton.isEnabled = false

            nextButton.setOnClickListener {
                if (current == words.size - 1) {
                    val intent = Intent(this, StartTestWordActivity::class.java)
                    startActivity(intent)
                } else {
                    show(words[++current])
                    prevButton.isEnabled = true
                }

            }

            prevButton.setOnClickListener {
                show(words[--current])
                nextButton.isEnabled = true
                if (current == 0) {
                    Log.d("prev", "clickable = false")
                    it.isEnabled = false
                }
            }
        } else {
            setContentView(R.layout.activity_nothing_to_study)
            val prevButton = findViewById<ImageButton>(R.id.prev)
            prevButton.setOnClickListener {
                this@StudyActivity.finish()
            }
        }
    }
}