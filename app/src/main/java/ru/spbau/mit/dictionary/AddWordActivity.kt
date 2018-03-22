package ru.spbau.mit.dictionary

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import dictionary.yandex.api.com.Language
import dictionary.yandex.api.com.Translate
import dictionary.yandex.api.com.WordDescription
import picture.bing.api.com.BingPicture
import android.content.Intent
import android.text.Html
import android.widget.ImageView
import com.squareup.picasso.Picasso


class AddWordActivity : AppCompatActivity() {
    private val translate = Translate()
    private val bingPicture = BingPicture()
    private val width = 200
    private val height = 200

    init {
        translate.setKey("dict.1.1.20160306T093514Z.bbb9e3db01cef073.d0356e388174436a1f2c93cce683819103ec4579")
        bingPicture.setKey("ObWAz+L07325riezjA4+F0O/KkK6Hq2qPWAIiCCIadA=")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent)
            }
        }
    }

    fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            val findViewById = findViewById<TextView>(R.id.wordTextView)
            val t = Thread(Runnable {
                val translateWord = translate.execute(sharedText, Language.ENGLISH, Language.RUSSIAN)
//                val pictureURL = bingPicture.execute(sharedText, 1).contentUrlList
                findViewById.text = Html.fromHtml(translateWord.toString())
//                Picasso.with(this@AddWordActivity)
//                        .load(pictureURL[0])
//                        .resize(width, height)
//                        .into(findViewById<ImageView>(R.id.wordImage))
            })
            t.start()
            t.join()
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
