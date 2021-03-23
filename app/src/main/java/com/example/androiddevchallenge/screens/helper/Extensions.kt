/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.screens.helper

import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.network.models.Weather

fun Weather.getIconResource(): Int = when (this.icon) {
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
