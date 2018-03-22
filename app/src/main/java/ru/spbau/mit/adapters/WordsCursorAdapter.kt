package ru.spbau.mit.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.dictionary.R

/**
 * Created by Oleg on 3/22/2018.
 */


open class WordsCursorAdapter constructor(context: Context?, cursor1: Cursor?) : CursorAdapter(context, cursor1, 0) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val nameView = view?.findViewById<TextView>(R.id.name)
        val nameColumnIndex = cursor?.getColumnIndex(DictionaryContract.WordsEntry.COLUMN_NAME)!!
        val name = cursor.getString(nameColumnIndex)
        nameView?.text = name
    }

}