package com.cloffygames.techcareersimplecalculatortask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorScreen() {
    var displayText by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf<Long?>(null) }
    var isAdding by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(listOf<String>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // İşlem Geçmişi
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(history) { item ->
                Text(text = item, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }

        // Hesap Makinesi Ekranı
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = displayText,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF222222), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .align(Alignment.End)
            )

            errorMessage?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            val buttons = listOf(
                listOf("1", "2", "3", "C"),
                listOf("4", "5", "6", "+"),
                listOf("7", "8", "9", "=")
            )

            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { label ->
                        val weight = if (label == "0") 2f else 1f
                        val backgroundColor = when (label) {
                            "C" -> Color(0xFFFF3B30)
                            "+" -> Color(0xFFFF9500)
                            "=" -> Color(0xFFFF9500)
                            else -> Color(0xFF333333)
                        }

                        Button(
                            onClick = {
                                onButtonClick(
                                    label,
                                    displayText,
                                    firstNumber,
                                    isAdding,
                                    history,
                                    updateText = { displayText = it },
                                    updateFirstNumber = { firstNumber = it },
                                    updateIsAdding = { isAdding = it },
                                    updateHistory = { history = it },
                                    updateErrorMessage = { errorMessage = it }
                                )
                            },
                            modifier = Modifier
                                .weight(weight)
                                .aspectRatio(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = label, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

fun onButtonClick(
    label: String,
    currentText: String,
    firstNumber: Long?,
    isAdding: Boolean,
    history: List<String>,
    updateText: (String) -> Unit,
    updateFirstNumber: (Long?) -> Unit,
    updateIsAdding: (Boolean) -> Unit,
    updateHistory: (List<String>) -> Unit,
    updateErrorMessage: (String?) -> Unit
) {
    val maxLength = 12  // Maksimum 12 karaktere izin verilir

    when (label) {
        "C" -> {
            updateText("0")
            updateFirstNumber(null)
            updateIsAdding(false)
            updateErrorMessage(null)
        }
        "+" -> {
            if (firstNumber == null) {
                updateFirstNumber(currentText.toLong())
                updateText("0")
                updateIsAdding(true)
                updateErrorMessage(null)
            }
        }
        "=" -> {
            if (isAdding && firstNumber != null) {
                val result = firstNumber + currentText.toLong()
                if (result.toString().length > maxLength) {
                    updateErrorMessage("Sonuç çok büyük!")
                } else {
                    val newHistory = history + "$firstNumber + $currentText = $result"
                    updateHistory(newHistory)
                    updateText(result.toString())
                    updateFirstNumber(null)
                    updateIsAdding(false)
                    updateErrorMessage(null)
                }
            }
        }
        else -> {
            if (currentText == "0") {
                if (label.length <= maxLength) {
                    updateText(label)
                    updateErrorMessage(null)
                } else {
                    updateErrorMessage("Sayı çok büyük!")
                }
            } else {
                if (currentText.length + label.length <= maxLength) {
                    updateText(currentText + label)
                    updateErrorMessage(null)
                } else {
                    updateErrorMessage("Sayı çok büyük!")
                }
            }
        }
    }
}
