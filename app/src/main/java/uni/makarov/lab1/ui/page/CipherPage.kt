@file:OptIn(ExperimentalMaterial3Api::class)

package uni.makarov.lab1.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import uni.makarov.lab1.backend.CipherAlgorithm
import uni.makarov.lab1.backend.readFile
import uni.makarov.lab1.backend.writeFile


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CipherPage(cipher: CipherAlgorithm) {
    var cipherAlgorithm = cipher
    var isEncryptionOn by remember { mutableStateOf(true) }

    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("")}

    if(isEncryptionOn) {
        Column {
            Row() {
                ElevatedButton(
                    onClick = { inputText = readFile() }) {

                }
                ElevatedButton(
                    onClick = { writeFile(outputText) }) {

                }
            }
            cipherAlgorithm.returnSpecialVariables()
            OutlinedTextField(
                value = inputText,
                label = { Text("Pure Text")},
                onValueChange = { inputText = it
                    outputText = cipherAlgorithm.encrypt(inputText)
                }
            )
            OutlinedIconButton(
                onClick = {
                    isEncryptionOn = false
                    val i = inputText
                    inputText = outputText
                    outputText = i
                },
                ) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decode text")
            }
            OutlinedTextField(
                value = outputText,
                label = { Text("Encoded Text")},
                onValueChange = { },
                readOnly = true
            )
        }
    } else {
        Column {
            cipherAlgorithm.returnSpecialVariables()
            OutlinedTextField(
                value = inputText,
                label = { Text("Encoded Text")},
                onValueChange = { inputText = it
                    outputText = cipherAlgorithm.decrypt(inputText)
                }
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
                value = outputText,
                label = { Text("Decoded Text")},
                onValueChange = { },
                readOnly = true
            )
        }
    }



}