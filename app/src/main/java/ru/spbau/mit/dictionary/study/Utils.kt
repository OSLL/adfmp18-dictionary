package ru.spbau.mit.dictionary.study

import android.content.ContentResolver
import ru.spbau.mit.data.DictionaryContract
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.main.Word


public fun wordsToLearn(count: Int, contentResolver: ContentResolver): ArrayList<Word> {
    val selection = "${DictionaryContract.WordsEntry.COLUMN_HIDDEN} = 0 AND ${DictionaryContract.WordsEntry.COLUMN_STATE} = ${DictionaryContract.WordsEntry.STATE_ON_LEARNING}"
    val words = getWords(count, selection, contentResolver)
    for (word in words) {
        val translates = getTranslates(word, contentResolver)
        val wordTranslates = ArrayList<String>()
        for (translate in translates) {
            val selection = "${DictionaryContract.WordsEntry.COLUMN_HIDDEN} = 1 AND ${DictionaryContract.WordsEntry._ID} = ${translate.id}"
            val translateWord = getWords(1, selection, contentResolver)
            wordTranslates.add(translateWord[0].word)
        }
        word.translates = wordTranslates
    }
    return words
}

private fun getWords(count: Int, selection: String, contentResolver: ContentResolver): ArrayList<Word> {
    val words: ArrayList<Word> = ArrayList()
    contentResolver.query(
            DictionaryProvider.CONTENT_WORDS_ENTRY,
            null,
            selection,
            null,
            null).use {
        for (i in 0..count) {
            if (it.moveToNext()) {
                val wordId = it.getInt(it.getColumnIndex(DictionaryContract.WordsEntry._ID))
                val word = it.getString(it.getColumnIndex(DictionaryContract.WordsEntry.COLUMN_NAME))
                val img = it.getBlob(it.getColumnIndexOrThrow(DictionaryContract.WordsEntry.COLUMN_IMAGE))
                val priority = it.getInt(it.getColumnIndexOrThrow(DictionaryContract.WordsEntry.COLUMN_PRIORITY))
                words.add(Word(wordId, word, emptyList(), img, priority))
            } else {
                break
            }
        }
    }
    return words
}

private fun getTranslates(word: Word, contentResolver: ContentResolver): ArrayList<Word> {
    val translates = ArrayList<Word>()
    contentResolver.query(
            DictionaryProvider.CONTENT_WORDS_RELATION,
            null,
            "${DictionaryContract.WordsRelation.COLUMN_FROM} = ${word.id}",
            null,
            null).use {
        val columns = it.columnNames
        for (c in columns) {
            System.out.println(c)
        }
        while (it.moveToNext()) {
            val wordId = it.getInt(it.getColumnIndexOrThrow("to"))
            translates.add(Word(wordId))
        }
    }
    return translates
}
