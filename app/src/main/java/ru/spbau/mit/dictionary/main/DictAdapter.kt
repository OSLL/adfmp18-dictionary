package ru.spbau.mit.dictionary.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ru.spbau.mit.dictionary.R

class DictAdapter(context: Context, private val wordList: ArrayList<Word>)
    : BaseAdapter() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var viewer: View
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        viewer = inflater.inflate(R.layout.word, p2, false)
        val wordView = viewer.findViewById<TextView>(R.id.word)
        val translateView = viewer.findViewById<TextView>(R.id.translate)
        wordView.text = wordList[p0].word
        translateView.text = wordList[p0].translate
        return viewer
    }

    override fun getItem(p0: Int): Any {
        return wordList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return wordList.size
    }
}