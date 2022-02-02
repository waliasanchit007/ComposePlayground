package com.example.composeplayground.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    val firstName:String,
    val secondName:String,
    @PrimaryKey val pan:String,
    val genderSelected:String,
    val arrayOfHobbies:List<String>?,
    val pdfUri: String?,
    val imageUri: String?
)
