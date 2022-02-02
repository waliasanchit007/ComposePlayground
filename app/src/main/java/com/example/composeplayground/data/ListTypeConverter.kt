package com.example.composeplayground.data

import androidx.room.TypeConverter

class ListTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun gettingListFromString(genreIds: String?): List<String>? {
            val list = arrayListOf<String>()

            val array =
                genreIds?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            if (array.isNullOrEmpty()) {
                return null
            }
            for (s in array) {
                if (s.isNotEmpty()) {
                    list.add(s)
                }
            }
            return list
        }

        @TypeConverter
        @JvmStatic
        fun writingStringFromList(list: List<String>?): String? {
            var genreIds = ""
            if (genreIds.isEmpty()) {
                return null
            } else {
                for (i in list!!) {
                    genreIds += ",$i"
                }
            }

            return genreIds
        }
    }
}