package com.etkramer.project02.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase::class.simpleName)
                    .build().also { instance = it }
    }
}
