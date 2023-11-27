package com.etkramer.project02.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Product (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val description: String,
    val price: Int
)

@Dao
interface ProductDao {
    @Insert
    fun insert(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun findById(id: String): Product?

    @Query("SELECT * FROM Product WHERE name = :name")
    fun findByName(name: String): Product?

    @Query("DELETE FROM Product")
    fun deleteAll()
}
