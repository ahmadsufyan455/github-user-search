package com.fynzero.finalsubmission.helper

import android.database.Cursor
import com.fynzero.finalsubmission.db.DatabaseContract
import com.fynzero.finalsubmission.model.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn._ID))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.USERNAME))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.TYPE))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR))
                userList.add(User(id, username, null, type, avatar))
            }
        }
        return userList
    }
}