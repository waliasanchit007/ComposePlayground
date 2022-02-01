package com.example.composeplayground

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.coroutines.CoroutineContext

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
class WorksheetViewModel: ViewModel() {


    private val worksheetUiState = MutableStateFlow(WorksheetUiState())
    val uiState = worksheetUiState
        .stateIn(
        viewModelScope,
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

    fun onSave(){

        worksheetUiState.update {
            val output = "first Name is ${it.firstName}\nsecond Name is ${it.secondName}\n" +
                    "pan number is ${it.pan}\nGender is ${it.genderSelected}\n" +
                    "hobbies are ${it.arrayOfHobbies.toTypedArray().contentToString()}\n" +
                    "pdf Uri = ${it.pdfUri}\nimage Uri = ${it.imageUri}"
            Log.i("sanchit", output)
            it.copy(output = output)
        }
    }

}