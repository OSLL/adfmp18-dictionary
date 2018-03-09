package ru.spbau.mit.dictionary

import android.app.Activity
import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dictionary.yandex.api.com.WordDescription


class SearchActivity : AppCompatActivity(), View.OnClickListener {

    private var globalContext: GlobalContext? = null

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        globalContext = GlobalContext(this, this)
        globalContext!!.onCreate()
        val intent = intent
        if (intent.getBooleanExtra(globalContext!!.FROM_PREV_ACTIVITY, true)) {
            val task = TranslateTask()
            task.execute()
            intent.putExtra(globalContext!!.FROM_PREV_ACTIVITY, false)
        }
    }

    override fun onClick(v: View) {
        val task = TranslateTask()
        task.execute()
    }

    private inner class TranslateTask : AsyncTask<Void, Void?, Void?>() {
        private var progressDialog: ProgressDialog? = null
        private val COUNT_COLUMN = 2
        private val COUNT_ROW = 8
        private var globalCounter = COUNT_COLUMN * COUNT_ROW
        private val COUNT_PICTURES_REQUEST = 50
        private val RESIZE_HEIGHT = 800
        private val RESIZE_WIDTH = 800
        private var description: WordDescription? = null
        private var inputText: String? = null
        private var picturesUrl: List<String>? = null

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog.show(this@SearchActivity, getString(R.string.translating), getString(R.string.please_wait), true)
            inputText = (findViewById<EditText>(R.id.input_text)).text.toString()
        }

        override fun doInBackground(vararg params: Void): Void? {
            try {
                description = globalContext!!.getTranslate(inputText!!)
                picturesUrl = globalContext!!.getBingPicturesUrl(inputText!!, COUNT_PICTURES_REQUEST)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return null;
        }

        override fun onPostExecute(result: Void?) {
            Log.d("POST", "start");
            super.onPostExecute(result)
            val tv = findViewById<TextView>(R.id.panel_for_show_translated_text)
            val tableForPictures = findViewById<TableLayout>(R.id.table_for_pictures)
            tableForPictures.removeAllViews()
            if (description == null) {
                tv.text = getString(R.string.err_common)
            } else {
                if (description!!.toString().isEmpty()) {
                    tv.text = getString(R.string.err_not_found_in_dictionary)
                } else {
                    tv.text = Html.fromHtml(description!!.toString())
                    tableForPictures.isShrinkAllColumns = true
                    var x = 0
                    end@ for (j in 0 until COUNT_ROW) {
                        val tr = TableRow(this@SearchActivity)
                        tr.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
                        tr.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                        for (i in 0 until COUNT_COLUMN) {
                            if (picturesUrl == null || x >= picturesUrl!!.size) {
                                break@end
                            }
                            val iv = ImageView(this@SearchActivity)
                            iv.adjustViewBounds = true
                            tr.addView(iv)
                            Picasso.with(this@SearchActivity)
                                    .load(picturesUrl!![x])
                                    .resize(RESIZE_WIDTH, RESIZE_HEIGHT)
                                    .placeholder(R.drawable.snake_load)
                                    .error(R.drawable.snake_error)
                                    .into(iv, object : Callback {
                                        override fun onSuccess() {

                                        }

                                        override fun onError() {
                                            iferror()
                                        }

                                        fun iferror() {
                                            if (picturesUrl != null && globalCounter < picturesUrl!!.size) { //protocol specific
                                                Picasso.with(this@SearchActivity)
                                                        .load(picturesUrl!![globalCounter++])
                                                        .resize(RESIZE_WIDTH, RESIZE_HEIGHT)
                                                        .placeholder(R.drawable.snake_load)
                                                        .error(R.drawable.snake_error)
                                                        .into(iv, object : Callback {

                                                            override fun onSuccess() {

                                                            }

                                                            override fun onError() {
                                                                iferror()
                                                            }
                                                        })
                                            }
                                        }
                                    })
                            x++
                        }
                        tableForPictures.addView(tr)

                    }
                }
            }
            progressDialog!!.dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        globalContext!!.putExtra()
    }

    override fun onBackPressed() {
        globalContext!!.putExtraWithCode(Activity.RESULT_OK)
    }
}
