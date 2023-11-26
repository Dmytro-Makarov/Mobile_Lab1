package uni.makarov.lab1.backend

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun readFile(context: Context, fileName: String) : String {
    val stringBuilder = StringBuilder()

    try {
        // Create a File object pointing to the internal storage directory
        val file = File(context.filesDir, fileName)

        // Create a FileInputStream to read from the file
        val fileInputStream = FileInputStream(file)

        // Create an InputStreamReader to read characters from the FileInputStream
        val inputStreamReader = InputStreamReader(fileInputStream)

        // Create a BufferedReader to read lines from the InputStreamReader
        val bufferedReader = BufferedReader(inputStreamReader)

        // Read each line from the file and append it to the StringBuilder
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
            if (line != null) {
                // If there is another line, add a newline character
                stringBuilder.append("\n")
            }
        }

        // Close the streams to release resources
        bufferedReader.close()
        inputStreamReader.close()
        fileInputStream.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun writeFile(context: Context, fileName: String, content: String) {
    try {
        // Create a File object pointing to the internal storage directory
        val file = File(context.filesDir, fileName)

        // Create a FileOutputStream to write into the file
        val fileOutputStream = FileOutputStream(file)

        // Create an OutputStreamWriter to write characters to the FileOutputStream
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)

        // Write the content to the file
        outputStreamWriter.write(content)

        // Close the streams to release resources
        outputStreamWriter.close()
        fileOutputStream.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}