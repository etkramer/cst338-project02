package com.etkramer.project02.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userProductEdge: UserProductEdge)

    @Update
    fun update(userProductEdge: UserProductEdge)

    @Query("SELECT * FROM UserProductEdge WHERE id = :id")
    fun findById(id: String): UserProductEdge?

    @Query("SELECT * FROM UserProductEdge WHERE userId = :userId AND productId = :productId")
    fun findWithIds(userId: Int, productId: Int): UserProductEdge?

    @Query("DELETE FROM UserProductEdge")
    fun deleteAll()
}
