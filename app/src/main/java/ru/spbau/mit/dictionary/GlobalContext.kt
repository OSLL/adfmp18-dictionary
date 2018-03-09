package ru.spbau.mit.dictionary

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import dictionary.yandex.api.com.Language
import dictionary.yandex.api.com.Translate
import dictionary.yandex.api.com.WordDescription
import picture.bing.api.com.BingPicture

class GlobalContext constructor(private val activity: AppCompatActivity, private val clickListener: View.OnClickListener) {
    private val translate = Translate()
    private val bingPicture = BingPicture()
    private val data = arrayOf("English", "Russian", "French", "German", "Ukrainian")
    private val lang = arrayOf(Language.ENGLISH, Language.RUSSIAN, Language.FRENCH, Language.GERMAN, Language.UKRAINIAN)
    private var langFrom = Language.ENGLISH
    private var langTo = Language.RUSSIAN
    private val inputText: EditText
    private val spinnerLangFrom: Spinner
    private val spinnerLangTo: Spinner
    private val btnTranslate: Button

    val MESSAGE_FOR_SEARCH = "MESSAGE"
    val LANG_FROM = "LANG_FROM"
    val LANG_TO = "LANG_TO"
    val FROM_PREV_ACTIVITY = "FROM_PREV_ACTIVITY"


    init {
        translate.setKey("dict.1.1.20160306T093514Z.bbb9e3db01cef073.d0356e388174436a1f2c93cce683819103ec4579")
        bingPicture.setKey("ObWAz+L07325riezjA4+F0O/KkK6Hq2qPWAIiCCIadA=")
        inputText = activity.findViewById(R.id.input_text)
        spinnerLangFrom = activity.findViewById(R.id.lang_from)
        spinnerLangTo = activity.findViewById(R.id.lang_to)
        btnTranslate = activity.findViewById(R.id.button_translate)
    }

    private fun getID(l: Language): Int {
        return lang.indices.firstOrNull { lang[it] === l }
                ?: -1
    }

    fun onCreate() {
        btnTranslate.setOnClickListener(clickListener)
        run {
            val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, data)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLangFrom.adapter = adapter
            spinnerLangFrom.prompt = activity.getString(R.string.from)
            spinnerLangFrom.setSelection(0)
            spinnerLangFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    langFrom = lang[position]
                }

                override fun onNothingSelected(arg0: AdapterView<*>) {}
            }
        }
        run {
            val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, data)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerLangTo.adapter = adapter
            spinnerLangTo.prompt = activity.getString(R.string.to)
            spinnerLangTo.setSelection(1)
            spinnerLangTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    langTo = lang[position]
                }

                override fun onNothingSelected(arg0: AdapterView<*>) {}
            }
        }
        val intent = activity.intent
        setLanguage(intent)
    }

    fun setLanguage(intent: Intent) {
        inputText.setText(intent.getStringExtra(MESSAGE_FOR_SEARCH))
        langFrom = Language.fromString(if (intent.getStringExtra(LANG_FROM) == null) "" else intent.getStringExtra(LANG_FROM)) ?: langFrom
        langTo = Language.fromString(if (intent.getStringExtra(LANG_TO) == null) "" else intent.getStringExtra(LANG_TO)) ?: langTo
        if (getID(langFrom) != -1) {
            spinnerLangFrom.setSelection(getID(langFrom))
        }

        if (getID(langTo) != -1) {
            spinnerLangTo.setSelection(getID(langTo))
        }
    }

    private fun putExtra(intent: Intent) {
        intent.putExtra(MESSAGE_FOR_SEARCH, inputText.text.toString())
        intent.putExtra(LANG_FROM, langFrom.toString())
        intent.putExtra(LANG_TO, langTo.toString())
    }

    fun putExtra() {
        val intent = activity.intent
        putExtra(intent)
    }

    fun putExtraWithCode(resultCode: Int) {
        val intent = activity.intent
        putExtra(intent)
        activity.setResult(resultCode, intent)
        activity.finish()
    }

    fun startActivityForResult(cl: Class<*>, requestCode: Int) {
        val intent = Intent(activity, cl)
        val message = inputText.text.toString()
        intent.putExtra(MESSAGE_FOR_SEARCH, message)
        intent.putExtra(LANG_FROM, langFrom.toString())
        intent.putExtra(LANG_TO, langTo.toString())
        intent.putExtra(FROM_PREV_ACTIVITY, true)
        activity.startActivityForResult(intent, requestCode)
    }

    @Throws(Exception::class)
    fun getTranslate(inputText: String): WordDescription {
        return translate.execute(inputText, langFrom, langTo)
    }

    @Throws(Exception::class)
    fun getBingPicturesUrl(inputText: String, count: Int): List<String> {
        return bingPicture.execute(inputText, count).contentUrlList
    }
}