package com.example.platformspr1

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.platformspr1.domain.models.CatImage
import com.example.platformspr1.threadhelp.ThreadHelper
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var imagesList: ArrayList<CatImage> = ArrayList()

        val helper = ThreadHelper()
        val thread = Thread(helper)
        thread.start()
        thread.join()
        imagesList = helper.getValue()

        setContent {
            DrawImage(imagesList)
        }
    }
}

@Composable
fun DrawImage(list: ArrayList<CatImage>){
    LazyColumn{
        itemsIndexed(list){
            index: Int, item: CatImage ->
            Card(
                modifier = Modifier
                    .padding(5.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 5.dp
            ) {
                val rainbowColorsBrush = remember {
                    Brush.sweepGradient(
                        listOf(
                            Color(0xFF9575CD),
                            Color(0xFFBA68C8),
                            Color(0xFFE57373),
                            Color(0xFFFFB74D),
                            Color(0xFFFFF176),
                            Color(0xFFAED581),
                            Color(0xFF4DD0E1),
                            Color(0xFF9575CD)
                        )
                    )
                }
                val showDialog = remember { mutableStateOf(false) }
                if (showDialog.value) {

                    alert(msg = item.url,
                        showDialog = showDialog.value,
                        onDismiss = {showDialog.value = false})
                }
                Image(painter = rememberAsyncImagePainter(item.url),
                    contentDescription = "Album cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(500.dp)
                        .border(
                            BorderStroke(4.dp, rainbowColorsBrush),
                            RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showDialog.value = true }
                )
            }

        }
    }
}

@Composable
fun alert(msg : String,
          showDialog: Boolean,
          onDismiss: () -> Unit) {
    val context = LocalContext.current
    if (showDialog) {
        fun savePhoto(){
            val url = URL(msg)
            val input: InputStream = url.openStream()
            var message = "Error!"
            try {
                val storagePath: File = Environment.getExternalStorageDirectory()
                val output: OutputStream = FileOutputStream(storagePath.toString() + msg)
                try {
                    val buffer = ByteArray(500)
                    var bytesRead = 0
                    while (input.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0) {
                        output.write(buffer, 0, bytesRead)
                    }
                    message = "Photo saved!"
                } finally {
                    output.close()
                }
            } finally {
                makeToast(context, message)
                input.close()
            }
        }
        AlertDialog(
            title = {
                Text("Options")
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    Thread(Runnable{
                        savePhoto()
                    })
                }) {
                    Text("Save photo to gallery")
                }
            },
            dismissButton = {}
        )

    }
}

fun makeToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

