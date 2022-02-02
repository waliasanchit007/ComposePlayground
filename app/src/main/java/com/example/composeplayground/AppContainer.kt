package com.example.composeplayground

import android.content.Context
import com.example.composeplayground.data.WorksheetDatabase
import com.example.composeplayground.data.WorksheetRepository

interface AppContainer {
    val worksheetRepository: WorksheetRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(private val appContext: Context) : AppContainer {

    private val db = WorksheetDatabase.getInstance(appContext)
    override val worksheetRepository: WorksheetRepository by lazy {
        WorksheetRepository(db.personDao())
    }
}