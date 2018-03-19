package ru.spbau.mit.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DictionaryDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_WORDS)
        db.execSQL(SQL_CREATE_WORDS_RELATION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_WORDS)
        db.execSQL(SQL_DELETE_WORDS_RELATION)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        private const val SQL_CREATE_WORDS =
                "CREATE TABLE ${DictionaryContract.WordsEntry.TABLE_NAME} (" +
                        "${DictionaryContract.WordsEntry._ID} INTEGER PRIMARY KEY," +
                        "${DictionaryContract.WordsEntry.COLUMN_NAME} TEXT NOT NULL," +
                        "${DictionaryContract.WordsEntry.COLUMN_STATE} INTEGER NOT NULL DEFAULT ${DictionaryContract.WordsEntry.STATE_ON_LEARNING}," +
                        "${DictionaryContract.WordsEntry.COLUMN_HIDDEN} INTEGER NOT NULL DEFAULT 0," +
                        "${DictionaryContract.WordsEntry.COLUMN_PRIORITY} INTEGER NOT NULL DEFAULT 0)"

        private const val SQL_DELETE_WORDS = "DROP TABLE IF EXISTS ${DictionaryContract.WordsEntry.TABLE_NAME}"

        private const val SQL_CREATE_WORDS_RELATION =
                "CREATE TABLE ${DictionaryContract.WordsRelation.TABLE_NAME} (" +
                        "${DictionaryContract.WordsRelation.COLUMN_FROM} INTEGER NOT NULL REFERENCES ${DictionaryContract.WordsEntry.TABLE_NAME}," +
                        "${DictionaryContract.WordsRelation.COLUMN_TO} INTEGER NOT NULL REFERENCES ${DictionaryContract.WordsEntry.TABLE_NAME})"

        private const val SQL_DELETE_WORDS_RELATION = "DROP TABLE IF EXISTS ${DictionaryContract.WordsRelation.TABLE_NAME}"

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "dictionary.db"
    }
}
