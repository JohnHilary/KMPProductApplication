package com.john.kmpapplication.ui.component.dialog

interface DialogData {
    val visuals: DialogVisuals
    fun onPositive()
    fun onNegative()
}