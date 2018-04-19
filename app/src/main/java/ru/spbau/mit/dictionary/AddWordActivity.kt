package ru.spbau.mit.dictionary

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import dictionary.yandex.api.com.Language
import dictionary.yandex.api.com.Translate
import dictionary.yandex.api.com.WordDescription
import picture.bing.api.com.BingPicture
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.squareup.picasso.Transformation
import java.io.ByteArrayOutputStream


class BitmapTransform(private val maxWidth: Int, private val maxHeight: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val targetWidth: Int
        val targetHeight: Int
        val aspectRatio: Double

        if (source.width > source.height) {
            targetWidth = maxWidth
            aspectRatio = source.height.toDouble() / source.width.toDouble()
            targetHeight = (targetWidth * aspectRatio).toInt()
        } else {
            targetHeight = maxHeight
            aspectRatio = source.width.toDouble() / source.height.toDouble()
            targetWidth = (targetHeight * aspectRatio).toInt()
        }

        val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        if (result != source) {
            source.recycle()
        }
        return result
    }

    override fun key(): String {
        return maxWidth.toString() + "x" + maxHeight
    }

}

class AddWordActivity : AppCompatActivity() {
    private val translate = Translate()
    private val bingPicture = BingPicture()
    private var width = 350
    private var height = 350

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

    private fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            val task = TranslateTask(sharedText)
            task.execute()
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class TranslateTask(private val sharedText: String) : AsyncTask<Void, Void?, WordDescription?>() {
        private val progressBar = findViewById<ProgressBar>(R.id.progressbar)
        private lateinit var urlList: List<String>
        private var imageInByte: ByteArray? = null
        private var bitmap: Bitmap? = null
        private var isLoadPicture: Boolean = false
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
            contentResolver.query(
                    DictionaryProvider.CONTENT_WORDS_ENTRY,
                    null,
                    "${DictionaryContract.WordsEntry.COLUMN_NAME} = '$sharedText'",
                    null, null).use {
                if (it.moveToNext()) {
                    imageInByte = it.getBlob(it.getColumnIndex(DictionaryContract.WordsEntry.COLUMN_IMAGE))
                    isLoadPicture = true
                }
            }
        }

        override fun doInBackground(vararg params: Void): WordDescription? {
            if (!isLoadPicture)
                urlList = bingPicture.execute(sharedText, 10).contentUrlList
            return try {
                val temp = translate.execute(sharedText, Language.ENGLISH, Language.RUSSIAN)
                Log.d("temp", temp.toString())
                temp
            } catch (e: Exception) {
                null
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: WordDescription?) {
            super.onPostExecute(result)
            val view = findViewById<TextView>(R.id.wordTextView)
            val imgView = findViewById<ImageView>(R.id.wordImage)
            if (!isLoadPicture) {
                Log.d("LOAD", "NO")
                var counter = 0
                Picasso.with(this@AddWordActivity)
                        .load(urlList.get(counter))
                        .transform( BitmapTransform(width, height))
                        .resize(Math.ceil(Math.sqrt((width * height).toDouble())).toInt(), Math.ceil(Math.sqrt((width * height).toDouble())).toInt())
                        .centerInside()
                        .error(R.drawable.imagenotavailable)
                        .into(imgView, object : com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                this@TranslateTask.bitmap = (imgView.drawable as BitmapDrawable).bitmap
                                progressBar.visibility = View.INVISIBLE
                                Log.d("S", "OK")
                            }

                            override fun onError() {
                                isError()
                            }

                            fun isError() {
                                if (counter < urlList.size) {
                                    Picasso.with(this@AddWordActivity)
                                            .load(urlList.get(counter++))
                                            .transform( BitmapTransform(width, height))
                                            .resize(Math.ceil(Math.sqrt((width * height).toDouble())).toInt(), Math.ceil(Math.sqrt((width * height).toDouble())).toInt())
                                            .centerInside()
                                            .error(R.drawable.imagenotavailable)
                                            .into(imgView, object : com.squareup.picasso.Callback {
                                                override fun onSuccess() {
                                                    this@TranslateTask.bitmap = (imgView.drawable as BitmapDrawable).bitmap
                                                    progressBar.visibility = View.INVISIBLE
                                                    Log.d("S", "OK")
                                                }

                                                override fun onError() {
                                                    isError()
                                                }
                                            })
                                }
                            }
                        })
            } else {
                Log.d("LOAD", "YES")
                if (imageInByte != null) {
                    val decodeByteArray = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte!!.size)
                    imgView.setImageBitmap(decodeByteArray)
                }
                progressBar.visibility = View.INVISIBLE

            }
            if (result != null && !result.ranking.isEmpty()) {


                view.text = "$sharedText -> ${result.ranking.first { it.text != null }.translation.first { it.text != null }.text}"
                val saveButton = findViewById<Button>(R.id.saveWord)
                if (!isLoadPicture) {
                    saveButton.visibility = View.VISIBLE
                }
                saveButton.setOnClickListener {
                    val values = ContentValues()
                    values.put(DictionaryContract.WordsEntry.COLUMN_NAME, sharedText)
                    values.put(DictionaryContract.WordsEntry.COLUMN_HIDDEN, false)
                    values.put(DictionaryContract.WordsEntry.COLUMN_STATE, DictionaryContract.WordsEntry.STATE_ON_LEARNING)
                    values.put(DictionaryContract.WordsEntry.COLUMN_PRIORITY, 0)
                    if (this@TranslateTask.bitmap != null) {
                        val baos = ByteArrayOutputStream()
                        this@TranslateTask.bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        imageInByte = baos.toByteArray()
                        values.put(DictionaryContract.WordsEntry.COLUMN_IMAGE, imageInByte)
                    }
                    try {
                        contentResolver.insert(DictionaryProvider.CONTENT_WORDS_ENTRY, values)
                    } catch (e: Exception) {
                    }

                    for (translate in result.ranking.first { it.text != null }.translation) {
                        if (translate.text != null) {
                            val contentValues = ContentValues()
                            contentValues.put(DictionaryContract.WordsEntry.COLUMN_NAME, translate.text)
                            contentValues.put(DictionaryContract.WordsEntry.COLUMN_HIDDEN, true)
                            contentValues.put(DictionaryContract.WordsEntry.COLUMN_STATE, DictionaryContract.WordsEntry.STATE_STUDIED)
                            contentValues.put(DictionaryContract.WordsEntry.COLUMN_PRIORITY, 0)
                            try {
                                contentResolver.insert(DictionaryProvider.CONTENT_WORDS_ENTRY, contentValues)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    var idWord = -1
                    contentResolver.query(
                            DictionaryProvider.CONTENT_WORDS_ENTRY,
                            null,
                            "${DictionaryContract.WordsEntry.COLUMN_NAME} = \"$sharedText\"",
                            null,
                            null).use {
                        if (it.moveToNext()) {
                            idWord = it.getInt(0)
                        }
                    }

                    if (idWord == -1) {
                        return@setOnClickListener
                    }

                    for (translate in result.ranking.first { it.text != null }.translation) {
                        if (translate.text != null) {
                            contentResolver.query(
                                    DictionaryProvider.CONTENT_WORDS_ENTRY,
                                    null,
                                    "${DictionaryContract.WordsEntry.COLUMN_NAME} = \"${translate.text}\"",
                                    null,
                                    null).use {
                                if (it.moveToNext()) {
                                    val id = it.getInt(0)
                                    val contentValues = ContentValues()
                                    contentValues.put(DictionaryContract.WordsRelation.COLUMN_FROM, idWord)
                                    contentValues.put(DictionaryContract.WordsRelation.COLUMN_TO, id)
                                    try {
                                        contentResolver.insert(DictionaryProvider.CONTENT_WORDS_RELATION, contentValues)
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                        }
                    }
                    finish()
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
