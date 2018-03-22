package ru.spbau.mit.data

import android.provider.BaseColumns;

object DictionaryContract {
    object WordsEntry : BaseColumns {
        const val TABLE_NAME = "words"

        const val _ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_STATE = "state"
        const val COLUMN_HIDDEN = "hidden"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_IMAGE = "image"

        const val STATE_ON_LEARNING = 1
        const val STATE_STUDIED = 2
    }

    object WordsRelation : BaseColumns {
        const val TABLE_NAME = "words_relation"

        const val COLUMN_FROM = "[from]"
        const val COLUMN_TO = "[to]"
    }
}
