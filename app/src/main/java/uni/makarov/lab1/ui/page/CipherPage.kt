@file:OptIn(ExperimentalMaterial3Api::class)

package uni.makarov.lab1.ui.page

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.views.chart.column.columnChart
import uni.makarov.lab1.backend.CipherAlgorithm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CipherPage(cipher: CipherAlgorithm) {
    var cipherAlgorithm = cipher
    var isEncryptionOn by remember { mutableStateOf(true) }
    var isHistogramOn by remember { mutableStateOf(false) }

    fun processText(text: String): String {
        return if(isEncryptionOn) {
            cipherAlgorithm.encrypt(text)
        } else {
            cipherAlgorithm.decrypt(text)
        }
    }

    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf(processText(inputText)) }

    var pickedDocumentUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        println("selected file URI ${it.data?.data}")
        pickedDocumentUri = it.data?.data
    }
    pickedDocumentUri?.let {
        Text(it.toString())
    }



    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ElevatedButton(
                onClick = { val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    .apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/txt"
                    }
                    launcher.launch(intent) }
            ) {
                Text("Open File")
            }
            ElevatedButton(
                onClick = {  }
            ) {
                Text("Save File")
            }
        }
        cipherAlgorithm.returnSpecialVariables()
        if(isEncryptionOn) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputText,
                label = { Text("Pure Text")},
                onValueChange = { inputText = it
                    outputText = processText(inputText)
                },
                minLines = 5,
                maxLines = 5
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedIconButton(
                    onClick = {
                        isEncryptionOn = false
                        val i = inputText
                        inputText = outputText
                        outputText = i
                    }
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decode text")
                }
                OutlinedIconToggleButton(
                    checked = isHistogramOn,
                    onCheckedChange = { isHistogramOn = it }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Toggle Chart")
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = outputText,
                label = { Text("Encoded Text")},
                onValueChange = { },
                minLines = 5,
                maxLines = 5,
                readOnly = true
            )
            if(isHistogramOn) {
                FrequencyHistogram(outputText)
            }
        } else {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputText,
                label = { Text("Encoded Text")},
                onValueChange = { inputText = it
                    outputText = processText(inputText)
                },
                minLines = 5,
                maxLines = 5
            )
            OutlinedIconButton(
                onClick = {
                    isEncryptionOn = true
                    val i = inputText
                    inputText = outputText
                    outputText = i
                },
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Encode text")
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = outputText,
                label = { Text("Decoded Text")},
                onValueChange = { },
                minLines = 5,
                maxLines = 5,
                readOnly = true
            )
            if(isHistogramOn) {
                FrequencyHistogram(outputText)
            }
        }

    }

}
@Composable
fun FrequencyHistogram(encodedText: String) {
    val alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray()

    val refreshDataset = remember { mutableIntStateOf(0)}
    val datasetForModel = remember { mutableStateListOf<FloatEntry>()}
    
    LaunchedEffect(key1 = refreshDataset.intValue) {
        datasetForModel.clear()

        for (letter in alphabet) {
            var xPos = (alphabet.indexOf(letter)).toFloat()
            if (xPos.isNaN()) {
                xPos = 0f
            }
            var yPos = encodedText.count {it == letter}.toFloat()
            if (yPos.isNaN()) {
                yPos = 0f
            }
            datasetForModel.add(FloatEntry(xPos, yPos))

        }
    }

    return Chart(
        chart = columnChart(LocalContext.current),
        model = entryModelOf(datasetForModel),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis()

    )
}