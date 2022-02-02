package com.example.composeplayground.data

import android.content.Context
import android.graphics.Movie
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Person::class], version = 1, exportSchema = false)
@TypeConverters(ListTypeConverter::class)
abstract class WorksheetDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

    companion object {
        private var instance: WorksheetDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                WorksheetDatabase::class.java, "worksheet_database.db"
            ).build()
    }
}

object DIGraph{
    fun createWorksheetRepo(context: Context) : WorksheetRepository{
        val db = WorksheetDatabase.getInstance(context = context)
        return WorksheetRepository(db.personDao())
    }
}

