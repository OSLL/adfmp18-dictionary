package ru.spbau.mit.data

import android.content.*
import android.database.Cursor
import android.net.Uri
import java.lang.String.valueOf


class DictionaryProvider : ContentProvider() {
    private var dbHelper: DictionaryDbHelper? = null

    companion object {
        private const val WORDS = 1
        private const val WORD_ID = 2
        private const val RELATION = 3
        val AUTHORITY = "ru.spbau.mit"
        val CONTENT_WORDS_ENTRY: Uri = Uri.withAppendedPath(Uri.parse("content://$AUTHORITY"), DictionaryContract.WordsEntry.TABLE_NAME)
        val CONTENT_WORDS_RELATION: Uri = Uri.withAppendedPath(Uri.parse("content://$AUTHORITY"), DictionaryContract.WordsRelation.TABLE_NAME)

    }

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sURIMatcher.addURI(AUTHORITY, DictionaryContract.WordsEntry.TABLE_NAME, WORDS)
        sURIMatcher.addURI(AUTHORITY, "${DictionaryContract.WordsEntry.TABLE_NAME}/#", WORD_ID)
        sURIMatcher.addURI(AUTHORITY, DictionaryContract.WordsRelation.TABLE_NAME, RELATION)
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val match = sURIMatcher.match(uri);
        return when (match) {
            WORDS -> {
                values?.getAsString(DictionaryContract.WordsEntry.COLUMN_NAME) ?: throw IllegalArgumentException("Words requires a name")
                values.getAsInteger(DictionaryContract.WordsEntry.COLUMN_PRIORITY) ?: throw IllegalArgumentException("Words requires valid priority")
                values.getAsBoolean(DictionaryContract.WordsEntry.COLUMN_HIDDEN) ?: throw IllegalArgumentException("Words requires valid hidden")
                values.getAsInteger(DictionaryContract.WordsEntry.COLUMN_STATE) ?: throw IllegalArgumentException("Words requires valid state")
                val database = dbHelper!!.writableDatabase
                val id = database.insert(DictionaryContract.WordsEntry.TABLE_NAME, null, values)
                context.contentResolver.notifyChange(uri, null)
                ContentUris.withAppendedId(uri, id)
            }
            RELATION -> {
                values?.getAsInteger(DictionaryContract.WordsRelation.COLUMN_FROM) ?: throw IllegalArgumentException("Relation from is invalid")
                values.getAsInteger(DictionaryContract.WordsRelation.COLUMN_TO) ?: throw IllegalArgumentException("Relation from is invalid")
                val database = dbHelper!!.writableDatabase
                val id = database.insert(DictionaryContract.WordsRelation.TABLE_NAME, null, values)
                context.contentResolver.notifyChange(uri, null)
                ContentUris.withAppendedId(uri, id)
            }
            else -> throw IllegalArgumentException("Insert is not supported for $uri")
        }
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val database = dbHelper!!.readableDatabase
        val match = sURIMatcher.match(uri)
        val cursor = when (match) {
            WORDS -> database.query(DictionaryContract.WordsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            WORD_ID -> {
                val selection = DictionaryContract.WordsEntry._ID + "=?";
                val selectionArgs = Array<String>(1) { valueOf(ContentUris.parseId(uri)) }
                database.query(DictionaryContract.WordsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            RELATION -> database.query(DictionaryContract.WordsRelation.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean {
        dbHelper = DictionaryDbHelper(context);
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = sURIMatcher.match(uri)
        return when (match) {
            WORDS -> {
                val database = dbHelper!!.writableDatabase
                val r = database.update(DictionaryContract.WordsEntry.TABLE_NAME, values, selection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                r
            }
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val database = dbHelper!!.writableDatabase
        val match = sURIMatcher.match(uri)
        return when (match) {
            WORDS -> {
                val r = database.delete(DictionaryContract.WordsEntry.TABLE_NAME, selection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                r
            }
            WORD_ID -> {
                val selection = DictionaryContract.WordsEntry._ID + "=?";
                val selectionArgs = Array<String>(1) { valueOf(ContentUris.parseId(uri)) }
                val r = database.delete(DictionaryContract.WordsEntry.TABLE_NAME, selection, selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                r
            }
            else -> throw IllegalArgumentException("Deletion is not supported for $uri");
        }
    }

    override fun getType(uri: Uri?): String {
        val match = sURIMatcher.match(uri)
        return when (match) {
            WORDS -> ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + DictionaryContract.WordsEntry.TABLE_NAME
            WORD_ID -> ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + DictionaryContract.WordsEntry.TABLE_NAME
            RELATION -> ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + DictionaryContract.WordsRelation.TABLE_NAME
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

}