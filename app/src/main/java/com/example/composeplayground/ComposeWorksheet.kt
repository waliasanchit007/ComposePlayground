package com.example.composeplayground

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme

class ComposeWorksheet : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MyComposable()
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MyPreview() {
    ComposePlaygroundTheme {
        MyComposable()
    }
}

@Composable
fun MyComposable(){
    var firstName by remember { mutableStateOf("") }
    var secondName by remember { mutableStateOf("") }
    var pan by remember { mutableStateOf("") }
    var panVisibility by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .padding(12.dp)
    ) {
        TextField(
            value = firstName,
            onValueChange = {
                firstName = it
            },
            label = { Text("First Name") }
        )

        TextField(
            value = secondName,
            onValueChange = {
                secondName = it
            },
            label = { Text("Second Name") }
        )

        TextField(
            value = pan,
            onValueChange = {
                pan = it
            },
            visualTransformation =if (panVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text("Pan Number") }
        )

        Button(
            onClick = { panVisibility = !panVisibility }
        ) {
                Text(if(!panVisibility) "View Pan" else "Hide Pan")
        }

        RadioButtonGroup()

        Spacer(modifier = Modifier.size(4.dp))

        CheckboxGroup()

        UploadPdf()

        Spacer(modifier = Modifier.size(10.dp))

        ShowImage()
    }
}

@Composable
fun UploadPdf(){
    var resultUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        resultUri = uri
    }
    Log.i("sanchit", "resultUri = {$resultUri}")
    Button(onClick = {launcher.launch("*/*")}){
        Text(text = "Upload Pdf")
    }
}
@Composable
fun ShowImage() {
    var resultUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        resultUri = uri
    }

    Column {
        Box(modifier = Modifier.fillMaxSize()) {
            Column{

                resultUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images
                            .Media.getBitmap(context.contentResolver,it)

                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver,it)
                        bitmap = ImageDecoder.decodeBitmap(source)
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
fun RadioButtonGroup(){
    Column(
    ) {

        var selected by remember {
            mutableStateOf("")
        }
        val updateRadioGroupSelection = { data : String ->
            selected = data
        }
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
fun CheckboxGroup(){
    Column{
        Text(text = "Hobbies")
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Games")
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Music")
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Dance")
        Spacer(modifier = Modifier.size(4.dp))
        CheckboxWithText(txt = "Reading")
        Spacer(modifier = Modifier.size(4.dp))
    }
}

@Composable
fun CheckboxWithText(txt:String){
    var tick by remember{ mutableStateOf(false)}
    Row{
        Checkbox(checked =tick , onCheckedChange ={tick = !tick} )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = txt)
    }

}