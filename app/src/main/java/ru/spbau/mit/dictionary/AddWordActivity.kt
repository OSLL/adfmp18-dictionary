package ru.spbau.mit.dictionary

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dictionary.yandex.api.com.Language
import dictionary.yandex.api.com.Translate
import dictionary.yandex.api.com.WordDescription
import picture.bing.api.com.BingPicture
import picture.bing.api.com.PictureDescription
import picture.bing.api.com.PicturesDescription
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import java.io.ByteArrayOutputStream
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


class AddWordActivity : AppCompatActivity() {
    private val translate = Translate()
    private val bingPicture = BingPicture()
    private val width = 200
    private val height = 200
    private var imageInByte : ByteArray? = null

    init {
        translate.setKey("dict.1.1.20160306T093514Z.bbb9e3db01cef073.d0356e388174436a1f2c93cce683819103ec4579")
        bingPicture.setKey("e48b80a0cf264666a40eb3896e27dfbf")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        val intent = intent
        val action = intent.action
        val type = intent.type

        val saveButton = findViewById<Button>(R.id.saveWord)
        saveButton.visibility = View.INVISIBLE


        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent)
            }
        }
    }

    fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            val task = TranslateTask(sharedText)
            task.execute()
        }
    }

    internal inner class FindPicture(private val sharedText: String,
                                     private val syncObj : Lock,
                                     private val condObj : Condition) : AsyncTask<Void, Void?, PicturesDescription?>() {
        override fun doInBackground(vararg p0: Void?): PicturesDescription? {
            return try {
                bingPicture.execute(sharedText, 10)
            } catch (e : Exception) {
                null
            }
        }


//        override fun onPreExecute() {
//            super.onPreExecute()
//        }

        override fun onPostExecute(result: PicturesDescription?) {
            super.onPostExecute(result)
            syncObj.lock()
            try {
                val imgView = findViewById<ImageView>(R.id.wordImage)
                val urlList = result!!.contentUrlList
                Picasso.with(this@AddWordActivity)
                        .load(urlList[0])
                        .resize(width, height)
                        .into(imgView)
                val bitmap = (imgView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                imageInByte = baos.toByteArray()
                condObj.signal()
            } finally {
                syncObj.unlock()
            }
        }
    }

    internal inner class TranslateTask(private val sharedText: String) : AsyncTask<Void, Void?, WordDescription?>() {
        private val progressBar = findViewById<ProgressBar>(R.id.progressbar)

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void): WordDescription? {
            return try {
                translate.execute(sharedText, Language.ENGLISH, Language.RUSSIAN)
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: WordDescription?) {
            super.onPostExecute(result)
            progressBar.visibility = View.INVISIBLE
            val view = findViewById<TextView>(R.id.wordTextView)
            val syncObj : Lock = ReentrantLock()
            val condObj : Condition = syncObj.newCondition()
            val findPicture = FindPicture(sharedText, syncObj, condObj)
            findPicture.execute()
            if (result != null && !result.ranking.isEmpty()) {

                view.text = "$sharedText -> ${result.ranking.first { it.text != null }.translation.first { it.text != null }.text}"
                val saveButton = findViewById<Button>(R.id.saveWord)
                saveButton.visibility = View.VISIBLE
                syncObj.lock()
                try {
                    while (imageInByte == null) {
                        condObj.await()
                    }
                } finally {
                    syncObj.unlock()
                }
                saveButton.setOnClickListener {
                    val values = ContentValues()
                    values.put(DictionaryContract.WordsEntry.COLUMN_NAME, sharedText)
                    values.put(DictionaryContract.WordsEntry.COLUMN_HIDDEN, false)
                    values.put(DictionaryContract.WordsEntry.COLUMN_STATE, DictionaryContract.WordsEntry.STATE_ON_LEARNING)
                    values.put(DictionaryContract.WordsEntry.COLUMN_PRIORITY, 0)
                    values.put(DictionaryContract.WordsEntry.COLUMN_IMAGE, imageInByte)
                    try {
                        contentResolver.insert(DictionaryProvider.CONTENT_WORDS_ENTRY, values)
                    } catch (e: Exception) {
                    }

                    for (translate in result.ranking.first { it.text != null }.translation) {
                        if (translate.text != null) {
                            val values = ContentValues()
                            values.put(DictionaryContract.WordsEntry.COLUMN_NAME, translate.text)
                            values.put(DictionaryContract.WordsEntry.COLUMN_HIDDEN, true)
                            values.put(DictionaryContract.WordsEntry.COLUMN_STATE, DictionaryContract.WordsEntry.STATE_STUDIED)
                            values.put(DictionaryContract.WordsEntry.COLUMN_PRIORITY, 0)
                            try {
                                contentResolver.insert(DictionaryProvider.CONTENT_WORDS_ENTRY, values)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    var idWord = -1
                    contentResolver.query(
                            DictionaryProvider.CONTENT_WORDS_ENTRY,   // The content URI of the words table
                            null,                        // The columns to return for each row
                            "${DictionaryContract.WordsEntry.COLUMN_NAME} = \"$sharedText\"",                  // Selection criteria
                            null,                     // Selection criteria
                            null).use {
                        if (it.moveToNext()) {
                            idWord = it.getInt(0);
                        }
                    }

                    if (idWord == -1) {
                        return@setOnClickListener
                    }

                    for (translate in result.ranking.first { it.text != null }.translation) {
                        if (translate.text != null) {
                            contentResolver.query(
                                    DictionaryProvider.CONTENT_WORDS_ENTRY,   // The content URI of the words table
                                    null,                        // The columns to return for each row
                                    "${DictionaryContract.WordsEntry.COLUMN_NAME} = \"${translate.text}\"",                  // Selection criteria
                                    null,                     // Selection criteria
                                    null).use {
                                if (it.moveToNext()) {
                                    val id = it.getInt(0)
                                    val values = ContentValues()
                                    values.put(DictionaryContract.WordsRelation.COLUMN_FROM, idWord)
                                    values.put(DictionaryContract.WordsRelation.COLUMN_TO, id)
                                    try {
                                        contentResolver.insert(DictionaryProvider.CONTENT_WORDS_RELATION, values)
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                view.text = "Problem with translation"
            }
        }
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
