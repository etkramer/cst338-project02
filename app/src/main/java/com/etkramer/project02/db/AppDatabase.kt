package com.etkramer.project02.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Product::class, UserProductEdge::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun userProductEdgeDao(): UserProductEdgeDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                // Build new db.
                val db = Room.databaseBuilder(context, AppDatabase::class.java, "app-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                seedUsers(db)
                seedProducts(db)
                seedUserProductEdges(db)

                instance = db
                return db.also { instance = db }
            }

            return instance as AppDatabase
        }

        private fun seedUsers(db: AppDatabase) {
            // Delete any existing users
            db.userDao().deleteAll()

            // Insert seed users into db
            db.userDao().insert(User(0, false, "testuser1", "testuser1", 200))
            db.userDao().insert(User(0, true, "admin2", "admin2", 999))
        }

        private fun seedProducts(db: AppDatabase) {
            // Delete any existing products
            db.productDao().deleteAll()

            // Insert seed products into db
            val numProducts = 24
            for (i in 1..numProducts) {
                db.productDao().insert(Product(0, "Product $i", "This is a placeholder description for a placeholder item"))
            }
        }

        private fun seedUserProductEdges(db: AppDatabase) {
            // Delete any existing edges
            db.userProductEdgeDao().deleteAll()

            // Insert seed products into db
            for (username in arrayOf("testuser1", "admin2")) {
                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 4")?.id ?: -1,
                    true))

                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 8")?.id ?: -1,
                    true))

                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 9")?.id ?: -1,
                    true))

                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 2")?.id ?: -1,
                    false))
            }
        }
    }
}
