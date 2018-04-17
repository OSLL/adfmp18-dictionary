package ru.spbau.mit.adapters

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.R


/**
 * Created by Oleg on 3/22/2018.
 */


open class WordsCursorAdapter constructor(context: Context?, cursor1: Cursor?, private val studyMode: Int) : CursorAdapter(context, cursor1, 0) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.word, parent, false);
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val wordView = view!!.findViewById<TextView>(R.id.word)
        val translateView = view.findViewById<TextView>(R.id.translate)
        val nameColumnIndex = cursor?.getColumnIndex(DictionaryContract.WordsEntry.COLUMN_NAME)!!
        wordView.text = cursor.getString(nameColumnIndex)
        val id = cursor.getInt(cursor.getColumnIndex(DictionaryContract.WordsEntry._ID))

        view.setOnClickListener {

            val popup = PopupMenu(context, view)
            popup.menuInflater.inflate(R.menu.word_popup_menu, popup.menu)
            popup.show()

            popup.menu.findItem(R.id.word_popup_add_to_learned).title = if (studyMode == DictionaryContract.WordsEntry.STATE_STUDIED) "Изучать" else "Изучено"

            Toast.makeText(view.context, "Hello ${wordView.text} $id", Toast.LENGTH_LONG).show()
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.word_popup_remove -> {
                        context!!.contentResolver.delete(DictionaryProvider.CONTENT_WORDS_ENTRY,
                                "${DictionaryContract.WordsEntry._ID} = $id"
                                , null)
                        true
                    }
                    R.id.word_popup_add_to_learned -> {

                        val values = ContentValues()
                        values.put(DictionaryContract.WordsEntry.COLUMN_STATE, if (studyMode == DictionaryContract.WordsEntry.STATE_STUDIED) DictionaryContract.WordsEntry.STATE_ON_LEARNING else DictionaryContract.WordsEntry.STATE_STUDIED)
                        context!!.contentResolver.update(DictionaryProvider.CONTENT_WORDS_ENTRY,
                                values
                                , "${DictionaryContract.WordsEntry._ID} = $id", null)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }

        context!!.contentResolver.query(
                DictionaryProvider.CONTENT_WORDS_RELATION,
                null,
                "${DictionaryContract.WordsRelation.COLUMN_FROM} = ${cursor.getInt(0)}",
                null,
                null).use {

            if (it.moveToNext()) {
                val id = it.getInt(1)
                context!!.contentResolver.query(
                        DictionaryProvider.CONTENT_WORDS_ENTRY,
                        null,
                        "${DictionaryContract.WordsEntry._ID} = $id",
                        null,
                        null).use {
                    if (it.moveToNext()) {
                        translateView.text = it.getString(1)
                    }
                }
            }

        }
    }

}