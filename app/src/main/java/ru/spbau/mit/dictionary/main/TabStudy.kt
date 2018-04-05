package ru.spbau.mit.dictionary.main

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ru.spbau.mit.adapters.WordsCursorAdapter
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.R

class TabStudy : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    companion object {
        private val WORDS_LOADER = 0
    }

    private lateinit var cursorAdapter: WordsCursorAdapter

    override fun onLoadFinished(loader: android.support.v4.content.Loader<Cursor>?, data: Cursor?) {
        cursorAdapter.swapCursor(data)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): android.support.v4.content.Loader<Cursor> {
        return CursorLoader(this.context,
                DictionaryProvider.CONTENT_URI,
                arrayOf(DictionaryContract.WordsEntry._ID, DictionaryContract.WordsEntry.COLUMN_NAME),
                null,
                null,
                null)
    }

    override fun onLoaderReset(loader: android.support.v4.content.Loader<Cursor>?) {
        cursorAdapter.swapCursor(null)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootViewer = inflater!!.inflate(R.layout.tab_study, container, false)
        val listView = rootViewer.findViewById<ListView>(R.id.list_view)
        cursorAdapter = WordsCursorAdapter(context, null)
        listView.adapter = cursorAdapter
        loaderManager.initLoader(WORDS_LOADER, null, this)
        return rootViewer
    }
}