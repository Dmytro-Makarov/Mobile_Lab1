package uni.makarov.lab1.backend

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

abstract class CipherAlgorithm {
    // Function to encrypt a given string
    abstract fun encrypt(input: String): String

    // Function to decrypt a given string
    abstract fun decrypt(input: String): String

    @Composable
    abstract fun returnSpecialVariables()

}


class CeasarCipher : CipherAlgorithm() {
    private var shift : Int? = 3

    fun setShift(shift : Int) {
        this.shift = shift
    }

    override fun encrypt(input: String): String {
        return input.map { encryptSymbol(it) }.joinToString("")
    }

    override fun decrypt(input: String): String {
        return input.map { decryptSymbol(it) }.joinToString("")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun returnSpecialVariables() {
        var rawShift by remember { mutableStateOf("") }
        return OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = rawShift,
            label = { Text("Symbol Shift") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            onValueChange = { rawShift = it
                shift = rawShift.toIntOrNull()
                if (shift == null)
                    shift = 3
            },
            singleLine = true
        )
    }

    // Function to encrypt a single ASCII symbol
    fun encryptSymbol(symbol: Char): Char {
        if (symbol.isLetter()) {
            val base = if (symbol.isUpperCase()) 'A' else 'a'
            return ((symbol.code - base.code + shift!! + 26) % 26 + base.code).toChar()
        }
        return symbol
    }

    // Function to decrypt a single ASCII symbol
    fun decryptSymbol(symbol: Char): Char {
        if (symbol.isLetter()) {
            val base = if (symbol.isUpperCase()) 'A' else 'a'
            return ((symbol.code - base.code - shift!! + 26) % 26 + base.code).toChar()
        }
        return symbol
    }
}

class AtbashCipher : CipherAlgorithm() {

    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val reversedAlphabet = alphabet.reversed()

    override fun encrypt(input: String): String {
        return input.map { char ->
            when {
                char.isLetter() && char.isUpperCase() -> reversedAlphabet[alphabet.indexOf(char)]
                char.isLetter() && char.isLowerCase() -> reversedAlphabet[alphabet.indexOf(char.uppercaseChar())].lowercaseChar()
                else -> char
            }
        }.joinToString("")
    }

    override fun decrypt(input: String): String {
        return encrypt(input) // Atbash is its own inverse
    }

    @Composable
    override fun returnSpecialVariables() { }
}

fun pickCipher(cypherRaw : String) : CipherAlgorithm {
    when (cypherRaw) {
        "Ceasar" -> return CeasarCipher()
        "Atbash" -> return AtbashCipher()
        else -> return AtbashCipher()
    }

}