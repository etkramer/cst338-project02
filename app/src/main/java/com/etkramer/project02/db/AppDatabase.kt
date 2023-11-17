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

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                // Build new db.
                val db = Room.databaseBuilder(context, AppDatabase::class.java, "app-database")
                    .allowMainThreadQueries()
                    .build()

                // Delete any existing users
                db.userDao().deleteAll()

                val user1 = User(0, false, "testuser1", "testuser1")
                val admin1 = User(0, true, "admin2", "admin2")

                // Insert seed users into db
                db.userDao().insert(user1)
                db.userDao().insert(admin1)

                instance = db
                return db.also { instance = db }
            }

            return instance as AppDatabase
        }
    }
}
