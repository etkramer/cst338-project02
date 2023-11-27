package com.etkramer.project02.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val isAdmin: Boolean,
    val username: String,
    val password: String,

    val balance: Int
)

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    fun findById(id: String): User?

    @Query("SELECT * FROM User WHERE username = :username")
    fun findByName(username: String): User?

    @Query("DELETE FROM User")
    fun deleteAll()
}
