@file:JvmName("EditTextUtils")

package dev.namhyun.bitcoinsimulator.extensions

import android.widget.EditText

fun EditText.vaildInput(): String? {
    if (text.toString().isNotBlank()) return text.toString()
    else return null
}