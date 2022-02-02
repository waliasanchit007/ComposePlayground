package com.example.composeplayground.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): Flow<List<Person>>

    @Query("SELECT * FROM person WHERE firstName LIKE :first AND " +
            "secondName LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Person

    @Insert
    fun insert(vararg users: Person)

}