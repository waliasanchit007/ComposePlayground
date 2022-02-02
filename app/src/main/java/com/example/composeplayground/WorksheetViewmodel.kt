package com.example.composeplayground

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import com.example.composeplayground.data.DIGraph
import com.example.composeplayground.data.Person
import com.example.composeplayground.data.WorksheetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class WorksheetUiState(
    val firstName:String = "",
    val secondName:String = "",
    val pan:String = "",
    val genderSelected:String = "",
    val arrayOfHobbies:MutableList<String> = mutableListOf(),
    val pdfUri: Uri? = null,
    val imageUri: Uri? = null,
    val output: String = ""
)


class WorksheetViewModel(val context:Context) {


    private val repository: WorksheetRepository = DIGraph.createWorksheetRepo(context)
    val a = collectData()
    private val worksheetUiState = MutableStateFlow(WorksheetUiState())
    val uiState = worksheetUiState.stateIn(
        CoroutineScope(Dispatchers.Main),
        SharingStarted.Eagerly,
        worksheetUiState.value
    )

    val onFirstNameChange = {newFirstName: String->
        worksheetUiState.update {
            it.copy(firstName = newFirstName)
        }
    }

    val onSecondNameChange = {newSecondName: String->
        worksheetUiState.update {
            it.copy(secondName = newSecondName)
        }
    }

    val onPanChange = {newPanChange: String->
        worksheetUiState.update {
            it.copy(pan = newPanChange)
        }
    }

    val updateRadioGroupSelection = { data : String ->
        worksheetUiState.update {
            it.copy(genderSelected = data)
        }
    }

    val onCheckHobbies = { hobby: String, toAdd:Boolean ->
        worksheetUiState.update {
            val arrayOfHobbies = it.arrayOfHobbies
            if(toAdd){
                arrayOfHobbies.add(hobby)
            }else{
                arrayOfHobbies.remove(hobby)
            }
            it.copy(arrayOfHobbies = arrayOfHobbies)
        }
    }

    val onGetPdfUri = {receivedPdfUri: Uri->
    worksheetUiState.update {
            it.copy(pdfUri = receivedPdfUri)
        }
    }

    val onGetImageUri = { receivedImageUri: Uri->
        worksheetUiState.update {
            it.copy(imageUri = receivedImageUri)
        }
    }


     private fun collectData(){
        CoroutineScope(Dispatchers.IO).launch {
            var l: List<Person>
            repository.getPerson().collect {
                if (!it.isNullOrEmpty()) {
                    l = it
                    worksheetUiState.update { uiState ->
                        var finalOutput = ""
                        l.forEach {
                            val output =
                                "first Name is ${it.firstName}\nsecond Name is ${it.secondName}\n" +
                                        "pan number is ${it.pan}\nGender is ${it.genderSelected}\n" +
                                        "hobbies are ${
                                            it.arrayOfHobbies?.toTypedArray().contentToString()
                                        }\n" +
                                        "pdf Uri = ${it.pdfUri}\nimage Uri = ${it.imageUri}\n\n\n"
                            finalOutput += output
                        }

                        Log.i("sanchit", finalOutput)
                        uiState.copy(output = finalOutput)
                    }
                }
            }
        }
    }
    fun onSave(){
        val person = worksheetUiState.value.let {
            Person(
                it.firstName,
                it.secondName,
                it.pan,
                it.genderSelected,
                it.arrayOfHobbies,
                it.pdfUri.toString(),
                it.imageUri.toString()
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(person)
        }

        worksheetUiState.update {
            WorksheetUiState()
        }
    }
}
