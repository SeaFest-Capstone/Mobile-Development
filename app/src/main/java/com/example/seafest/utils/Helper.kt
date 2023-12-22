package com.example.seafest.utils

import java.text.NumberFormat
import java.util.Locale

object Helper {
    private val localeID = Locale("id", "ID")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeID)
    init {
        currencyFormat.maximumFractionDigits = 0
    }
    fun formatRupiah(angka: Int): String {
        return currencyFormat.format(angka)
    }
}