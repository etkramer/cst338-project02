package com.etkramer.project02.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class UserProductEdge (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: Int,
    val productId: Int,

    val isInCart: Boolean
)

@Dao
interface UserProductEdgeDao {
    @Insert
    fun insert(userProductEdge: UserProductEdge)

    @Query("SELECT * FROM UserProductEdge WHERE id = :id")
    fun findById(id: String): UserProductEdge?

    @Query("DELETE FROM UserProductEdge")
    fun deleteAll()
}
