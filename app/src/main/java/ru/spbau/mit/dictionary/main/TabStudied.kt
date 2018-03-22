package ru.spbau.mit.dictionary.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import ru.spbau.mit.dictionary.R

class TabStudied : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootViewer = inflater!!.inflate(R.layout.tab_study, container, false)
        val listView = rootViewer.findViewById<ListView>(R.id.list_view)
        val adapter = DictAdapter(activity, getWords())
        listView.adapter = adapter
        return rootViewer
    }

    private fun getWords(): ArrayList<Word> {
        // TODO("get words from database")
        val wordList = ArrayList<Word>()
        wordList.add(Word("hello", arrayListOf("привет")))
        wordList.add(Word("name", arrayListOf("имя")))
        wordList.add(Word("family", arrayListOf("семья")))
        return wordList
    }

}