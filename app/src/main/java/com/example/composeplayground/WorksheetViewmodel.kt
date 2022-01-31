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
    val text: String = ""
)
class WorksheetViewModel: ViewModel() {

    private val worksheetUiState = MutableStateFlow(WorksheetUiState())
    val uiState = worksheetUiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        WorksheetUiState()
    )


    var firstName by mutableStateOf("")
    private set

    var secondName by mutableStateOf("")
        private set

    var pan by mutableStateOf("")
        private set

    var genderSelected by mutableStateOf("")
        private set

    val onFirstNameChange = {newFirstName: String->
        firstName = newFirstName
    }

    val onSecondNameChange = {newSecondName: String->
        secondName = newSecondName
    }
    val onPanChange = {newPanChange: String->
        pan = newPanChange
    }

    val updateRadioGroupSelection = { data : String ->
        genderSelected = data
    }

    private var arrayOfHobbies = mutableListOf<String>()

    val onCheckHobbies = { hobby: String, toAdd:Boolean ->
        if(toAdd){
            arrayOfHobbies.add(hobby)
        }else{
            arrayOfHobbies.remove(hobby)
        }
        Unit
    }

    var pdfUri by mutableStateOf<Uri?>(null)
        private set
    val onGetPdfUri = {
            receivedPdfUri: Uri-> pdfUri = receivedPdfUri
    }

    var imageUri by  mutableStateOf<Uri?>(null)
        private set
    val onGetImageUri = {
            receivedImageUri: Uri-> imageUri = receivedImageUri
    }

    var text by mutableStateOf("")
        private set

    fun onSave(){
         text = "first Name is $firstName\nsecond Name is $secondName\n" +
                "pan number is $pan\nGender is $genderSelected\n" +
                "hobbies are ${arrayOfHobbies.toTypedArray().contentToString()}\n" +
                "pdf Uri = $pdfUri\nimage Uri = $imageUri"
        Log.i("sanchit", text)
    }

    val latestNews:Flow<Int> = flow{
        emit(1)
    }

}