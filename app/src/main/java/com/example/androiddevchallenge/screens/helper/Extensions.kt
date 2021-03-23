package com.example.androiddevchallenge.screens.helper

import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.network.models.Weather

fun Weather.getIconResource(): Int = when(this.icon){
    "01d" -> R.drawable._01d
    "01n" -> R.drawable._01d
    "02d" -> R.drawable._02d
    "02n" -> R.drawable._02d
    "03d" -> R.drawable._03d
    "03n" -> R.drawable._03d
    "04d" -> R.drawable._04d
    "04n" -> R.drawable._04d
    "09d" -> R.drawable._50d
    "09n" -> R.drawable._50d
    "10d" -> R.drawable._50d
    "10n" -> R.drawable._50d
    "11d" -> R.drawable._11d
    "11n" -> R.drawable._11d
    "13d" -> R.drawable._13d
    "13n" -> R.drawable._13d
    "50d" -> R.drawable._50d
    "50n" -> R.drawable._50d
    else -> throw IllegalStateException("Icon resource not found")
}