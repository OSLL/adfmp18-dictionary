package ru.spbau.mit.dictionary

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_list_words.*
import ru.spbau.mit.adapters.WordsCursorAdapter
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider

class ListWordsActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                DictionaryProvider.CONTENT_URI,
                arrayOf(DictionaryContract.WordsEntry._ID, DictionaryContract.WordsEntry.COLUMN_NAME),
                null,
                null,
                null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        cursorAdapter?.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        cursorAdapter?.swapCursor(null)
    }

    private var cursorAdapter: WordsCursorAdapter? = null
    private val WORDS_LOADER = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_words)
        setSupportActionBar(toolbar)

        val listWordsView = findViewById<ListView>(R.id.list_words)
        cursorAdapter = WordsCursorAdapter(this, null)
        listWordsView.adapter = cursorAdapter

        fab.setOnClickListener { _ ->
            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, 0)
        }

        loaderManager.initLoader(WORDS_LOADER, null, this);
    }

}
