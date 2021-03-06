package com.example.composeplayground

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.get
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import com.example.composeplayground.data.WorksheetRepository
import org.w3c.dom.Text

class ComposeWorksheet : ComponentActivity() {
//
//    fun createWithFactory(
//        create: () -> ViewModel
//    ): ViewModelProvider.Factory {
//        return object : ViewModelProvider.AndroidViewModelFactory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                @Suppress("UNCHECKED_CAST")// Casting T as ViewModel
//                return create.invoke() as T
//            }
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val appContainer = (application as ComposeAppication).container
        val viewModel = WorksheetViewModel(context = applicationContext)
//        val viewmodelFactory = WorksheetViewmodelFactory(appContainer.worksheetRepository)
//        val viewModel = ViewModelProvider(viewModelStore, viewmodelFactory)[WorksheetViewModel::class.java]
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MyComposable(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MyComposable(viewModel: WorksheetViewModel){

    val uiState by viewModel.uiState.collectAsState()
    var panVisibility by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(12.dp)
        .verticalScroll(rememberScrollState(), true)
    ) {
        TextField(
            value = uiState.firstName,
            onValueChange = {
                viewModel.onFirstNameChange(it)
            },
            label = { Text("First Name") }
        )
        TextField(
            value = uiState.secondName,
            onValueChange = {
                viewModel.onSecondNameChange(it)
            },
            label = { Text("Second Name") }
        )
        TextField(
            value = uiState.pan,
            onValueChange = {
                viewModel.onPanChange(it)
            },
            visualTransformation =if (panVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text("Pan Number") }
        )
        Spacer(modifier = Modifier.size(4.dp))
        Button(
            onClick = { panVisibility = !panVisibility }
        ) {
            Text(if(!panVisibility) "View Pan" else "Hide Pan")
        }


        Spacer(modifier = Modifier.size(4.dp))

        //gender selection
        val selected = uiState.genderSelected
        RadioButtonGroup(selected, viewModel.updateRadioGroupSelection)
        Spacer(modifier = Modifier.size(4.dp))

        // hobbies
        CheckboxGroup( viewModel.onCheckHobbies )

        //pdf upload
        val pdfUri = uiState.pdfUri
        UploadPdf(pdfUri, viewModel.onGetPdfUri)
        Spacer(modifier = Modifier.size(10.dp))

        //image selection
        val imageUri = uiState.imageUri
        ShowImage(imageUri, viewModel.onGetImageUri)

        Spacer(modifier = Modifier.size(10.dp))

        //save button
        Button(onClick = { viewModel.onSave() }) {
            Text(text = "Save")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = uiState.output)
    }
}

@Composable
fun UploadPdf(pdfUri: Uri?, onGetUri:(Uri) -> Unit){
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            onGetUri(uri)
        }
    }
    Button(onClick = {launcher.launch("*/*")}){
        Text(text = "Upload Pdf")
    }
}

@Composable
fun ShowImage(imageUri: Uri?, onGetImageUri:(Uri) -> Unit) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            onGetImageUri(uri)
        }
    }

    Column {
        Box() {
            Column{
                imageUri?.let {
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images
                            .Media.getBitmap(context.contentResolver,it)

                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver,it)
                        ImageDecoder.decodeBitmap(source)
                    }

                    bitmap.let {  btm ->
                        if (btm != null) {
                            Image(bitmap = btm.asImageBitmap(),
                                contentDescription =null,
                                modifier = Modifier.size(200.dp))
                        }
                    }

                }
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    onClick = { launcher.launch("image/*") },
                ) {
                    Text(text = "Add Photo")
                }
            }
        }
    }
}
@Composable
fun RadioButtonGroup(selected: String, updateRadioGroupSelection: (String)->Unit){
    Column {
        Row {
            RadioButton(
                selected = selected == "Male",
                onClick = { updateRadioGroupSelection("Male") })
            Text(
                text = "Male",
                modifier = Modifier
                    .clickable {
                        updateRadioGroupSelection("Male")
                    }
                    .padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            RadioButton(
                selected = selected == "Female",
                onClick = { updateRadioGroupSelection("Female") })
            Text(
                text = "Female",
                modifier = Modifier
                    .clickable(onClick = { updateRadioGroupSelection("Female") })
                    .padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            RadioButton(
                selected = selected == "Other",
                onClick = { updateRadioGroupSelection("Other") })
            Text(
                text = "Other",
                modifier = Modifier
                    .clickable(onClick = { updateRadioGroupSelection("Other") })
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun CheckboxGroup(onCheckHobbies:(hobby: String, todo: Boolean) -> Unit){
    Column{
        Text(text = "Hobbies")
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Games", onCheckHobbies)
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Music", onCheckHobbies)
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Dance", onCheckHobbies)
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Reading", onCheckHobbies)
        Spacer(modifier = Modifier.size(4.dp))
    }
}

@Composable
fun CheckboxWithText(txt:String,onCheckChange: (hobby : String, toAdd: Boolean) -> Unit){
    var tick by remember{ mutableStateOf(false)}
    Row{
        Checkbox(checked =tick , onCheckedChange = {
            onCheckChange(txt, it)
            tick = it
        })
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = txt)
    }
}