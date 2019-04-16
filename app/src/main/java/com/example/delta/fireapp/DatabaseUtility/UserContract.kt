package com.example.delta.fireapp.DatabaseUtility

import android.provider.BaseColumns

/**
 * This contract defines the table name and column names
 */
object UserContract {

    object UserEntry: BaseColumns {
        const val TABLE_NAME = "user"
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_FIRST = "first"
        const val COLUMN_NAME_LAST = "last"
        const val COLUMN_NAME_DOB = "dob"




    }



}