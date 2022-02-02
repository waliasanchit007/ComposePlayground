package com.example.composeplayground.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class WorksheetRepository(val personDao: PersonDao) {
    suspend fun getPerson(): Flow<List<Person>> {
        return personDao.getAll()
    }

    suspend fun insert(person: Person){
        personDao.insert(person)
    }
}