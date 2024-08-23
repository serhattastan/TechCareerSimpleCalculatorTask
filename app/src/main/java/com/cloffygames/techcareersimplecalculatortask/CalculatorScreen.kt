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
    // Hesap makinesinin ekranında gösterilen metni tutar
    var displayText by remember { mutableStateOf("0") }

    // İlk sayıyı tutar; toplama işlemi için kullanılır
    var firstNumber by remember { mutableStateOf<Long?>(null) }

    // Toplama işleminin aktif olup olmadığını belirtir
    var isAdding by remember { mutableStateOf(false) }

    // İşlem geçmişini tutar
    var history by remember { mutableStateOf(listOf<String>()) }

    // Hata mesajını tutar; çok büyük sayılar için kullanılır
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Ana UI düzeni
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Üst ve alt alan arasında eşit boşluk bırakır
    ) {
        // İşlem geçmişini göstermek için kullanılan LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Üst kısımda kalması için ağırlık verilir
                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp)) // Arka plan ve köşeleri yuvarlama
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(history) { item ->
                // Geçmişteki her işlemi bir Text bileşeni olarak gösterir
                Text(text = item, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }

        // Hesap makinesi ekranını ve butonları içeren alan
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f), // Alt kısımda daha fazla yer kaplaması için ağırlık verilir
            verticalArrangement = Arrangement.spacedBy(8.dp) // Butonlar arasında boşluk bırakır
        ) {
            // Hesap makinesi ekranı (displayText)
            Text(
                text = displayText, // Ekranda gösterilecek metin
                fontSize = 48.sp, // Yazı boyutu
                fontWeight = FontWeight.Bold, // Yazı kalınlığı
                color = Color.White, // Yazı rengi
                modifier = Modifier
                    .fillMaxWidth() // Ekranın tamamını kaplamasını sağlar
                    .background(Color(0xFF222222), shape = RoundedCornerShape(8.dp)) // Arka plan ve köşeleri yuvarlama
                    .padding(16.dp) // İçeriden boşluk bırakır
                    .align(Alignment.End) // Sağ tarafa hizalar
            )

            // Hata mesajı gösterme alanı
            errorMessage?.let {
                Text(
                    text = it, // Hata mesajı metni
                    fontSize = 16.sp, // Yazı boyutu
                    fontWeight = FontWeight.Bold, // Yazı kalınlığı
                    color = Color.Red, // Yazı rengi (kırmızı)
                    modifier = Modifier.padding(8.dp) // İçeriden boşluk bırakır
                )
            }

            // Hesap makinesi butonları
            val buttons = listOf(
                listOf("1", "2", "3", "C"), // İlk satır: rakamlar ve "C" butonu
                listOf("4", "5", "6", "+"), // İkinci satır: rakamlar ve "+" butonu
                listOf("7", "8", "9", "=")  // Üçüncü satır: rakamlar ve "=" butonu
            )

            // Her satırdaki butonları gösterir
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(), // Satırın ekran genişliğini kaplamasını sağlar
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Butonlar arasında boşluk bırakır
                ) {
                    row.forEach { label ->
                        val weight = if (label == "0") 2f else 1f // "0" butonu daha geniş olacak şekilde ağırlık verilir
                        val backgroundColor = when (label) {
                            "C" -> Color(0xFFFF3B30) // "C" butonunun rengi kırmızı
                            "+" -> Color(0xFFFF9500) // "+" butonunun rengi turuncu
                            "=" -> Color(0xFFFF9500) // "=" butonunun rengi turuncu
                            else -> Color(0xFF333333) // Diğer butonlar için koyu gri
                        }

                        // Buton oluşturma
                        Button(
                            onClick = {
                                // Butona tıklandığında onButtonClick fonksiyonu çağrılır
                                onButtonClick(
                                    label, // Butonun etiketi (ör. "1", "+", "=")
                                    displayText,
                                    firstNumber,
                                    isAdding,
                                    history,
                                    updateText = { displayText = it }, // Ekran metnini günceller
                                    updateFirstNumber = { firstNumber = it }, // İlk sayıyı günceller
                                    updateIsAdding = { isAdding = it }, // Toplama işlemi durumunu günceller
                                    updateHistory = { history = it }, // İşlem geçmişini günceller
                                    updateErrorMessage = { errorMessage = it } // Hata mesajını günceller
                                )
                            },
                            modifier = Modifier
                                .weight(weight) // Butonun ağırlığını belirler (genişliğini ayarlar)
                                .aspectRatio(1f), // Kare şekli vermek için en-boy oranı
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor, // Butonun arka plan rengi
                                contentColor = Color.White // Butonun içeriği (yazı) rengi
                            )
                        ) {
                            // Buton üzerindeki metin
                            Text(text = label, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

// Hesaplama işlemlerini gerçekleştiren fonksiyon
fun onButtonClick(
    label: String, // Tıklanan butonun etiketi
    currentText: String, // Şu anda ekranda gösterilen metin
    firstNumber: Long?, // İlk girilen sayı (eğer varsa)
    isAdding: Boolean, // Toplama işleminin aktif olup olmadığını belirtir
    history: List<String>, // İşlem geçmişi
    updateText: (String) -> Unit, // Ekran metnini güncelleyen fonksiyon
    updateFirstNumber: (Long?) -> Unit, // İlk sayıyı güncelleyen fonksiyon
    updateIsAdding: (Boolean) -> Unit, // Toplama işlemi durumunu güncelleyen fonksiyon
    updateHistory: (List<String>) -> Unit, // İşlem geçmişini güncelleyen fonksiyon
    updateErrorMessage: (String?) -> Unit // Hata mesajını güncelleyen fonksiyon
) {
    val maxLength = 12  // Maksimum 12 karaktere izin verilir; sayıların çok büyük olmasını engeller

    when (label) {
        "C" -> {
            // "C" butonuna basıldığında ekran sıfırlanır ve diğer durumlar temizlenir
            updateText("0")
            updateFirstNumber(null)
            updateIsAdding(false)
            updateErrorMessage(null)
        }
        "+" -> {
            // "+" butonuna basıldığında ilk sayı saklanır ve toplama işlemi başlatılır
            if (firstNumber == null) {
                updateFirstNumber(currentText.toLong()) // İlk sayıyı saklar
                updateText("0") // Ekranı sıfırlar
                updateIsAdding(true) // Toplama işlemini aktif eder
                updateErrorMessage(null)
            }
        }
        "=" -> {
            // "=" butonuna basıldığında toplama işlemi gerçekleştirilir
            if (isAdding && firstNumber != null) {
                val result = firstNumber + currentText.toLong() // İlk sayı ile ikinci sayı toplanır
                if (result.toString().length > maxLength) {
                    // Sonuç 12 karakterden büyükse hata mesajı gösterilir
                    updateErrorMessage("Sonuç çok büyük!")
                } else {
                    // Sonuç kabul edilebilir uzunluktaysa, ekran ve işlem geçmişi güncellenir
                    val newHistory = history + "$firstNumber + $currentText = $result"
                    updateHistory(newHistory)
                    updateText(result.toString())
                    updateFirstNumber(null) // İlk sayı sıfırlanır
                    updateIsAdding(false) // Toplama işlemi sonlandırılır
                    updateErrorMessage(null)
                }
            }
        }
        else -> {
            // Rakamlar veya diğer butonlar için (örneğin, "1", "2", ...)
            if (currentText == "0") {
                // Ekranda sadece "0" varsa, yeni değeri doğrudan yazar
                if (label.length <= maxLength) {
                    updateText(label)
                    updateErrorMessage(null)
                } else {
                    // Eğer sayı çok büyükse hata mesajı gösterilir
                    updateErrorMessage("Sayı çok büyük!")
                }
            } else {
                // Ekranda başka bir sayı varsa, yeni rakam eklenir
                if (currentText.length + label.length <= maxLength) {
                    updateText(currentText + label)
                    updateErrorMessage(null)
                } else {
                    // Eğer sayı çok büyükse hata mesajı gösterilir
                    updateErrorMessage("Sayı çok büyük!")
                }
            }
        }
    }
}