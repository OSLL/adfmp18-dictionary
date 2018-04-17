package ru.spbau.mit.dictionary.study

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    private lateinit var words: List<Word>
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
        setContentView(R.layout.activity_study)

        wordView = findViewById<TextView>(R.id.wordView)
        flowTextView = findViewById<FlowTextView>(R.id.ftv)
        imageView = flowTextView.findViewById<ImageView>(R.id.img)
        val selection = "${DictionaryContract.WordsEntry.COLUMN_HIDDEN} = 0 AND ${DictionaryContract.WordsEntry.COLUMN_STATE} = ${DictionaryContract.WordsEntry.STATE_ON_LEARNING}"
        val words = getWords(10, selection)
        for (word in words) {
            val translates = getTranslates(word)
            val wordTranslates = ArrayList<String>()
            for (translate in translates) {
                val selection = "${DictionaryContract.WordsEntry.COLUMN_HIDDEN} = 1 AND ${DictionaryContract.WordsEntry._ID} = ${translate.id}"
                val translateWord = getWords(1, selection)
                wordTranslates.add(translateWord[0].word)
            }
            word.translates = wordTranslates
        }
        if (words.isNotEmpty()) {
            show(words[0])
        }

        val nextButton = findViewById<ImageButton>(R.id.next)
        val prevButton = findViewById<ImageButton>(R.id.prev)
        prevButton.isEnabled = false

        nextButton.setOnClickListener {
            if (current == words.size - 1) {
                val intent = Intent(this, StartTestWordActivity::class.java)
                intent.putExtra(getString(R.string.words), words)
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
    }

    private fun getWords(count: Int, selection: String): ArrayList<Word> {
        val words: ArrayList<Word> = ArrayList()
        contentResolver.query(
                DictionaryProvider.CONTENT_WORDS_ENTRY,
                null,
                selection,
                null,
                null).use {
            for (i in 0..count) {
                if (it.moveToNext()) {
                    val wordId = it.getInt(it.getColumnIndex(DictionaryContract.WordsEntry._ID))
                    val word = it.getString(it.getColumnIndex(DictionaryContract.WordsEntry.COLUMN_NAME))
                    val img = it.getBlob(it.getColumnIndexOrThrow(DictionaryContract.WordsEntry.COLUMN_IMAGE))
                    val priority = it.getInt(it.getColumnIndexOrThrow(DictionaryContract.WordsEntry.COLUMN_PRIORITY))
                    words.add(Word(wordId, word, emptyList(), img, priority))
                } else {
                    break
                }
            }
        }
        return words
    }

    private fun getTranslates(word: Word): ArrayList<Word> {
        val translates = ArrayList<Word>()
        contentResolver.query(
                DictionaryProvider.CONTENT_WORDS_RELATION,
                null,
                "${DictionaryContract.WordsRelation.COLUMN_FROM} = ${word.id}",
                null,
                null).use {
            val columns = it.columnNames
            for (c in columns) {
                System.out.println(c)
            }
            while (it.moveToNext()) {
                val wordId = it.getInt(it.getColumnIndexOrThrow("to"))
                translates.add(Word(wordId))
            }
        }
        return translates
    }
}