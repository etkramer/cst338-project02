package com.etkramer.project02.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val isAdmin: Boolean,
    val username: String,
    val password: String
)
