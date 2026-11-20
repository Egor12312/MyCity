package com.example.mycity.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mycity.R

enum class Category(@StringRes val titleRes: Int) {
    RESTAURANTS(R.string.category_restaurants),
    PARKS(R.string.category_parks),
    COFFEE(R.string.category_coffee)
}

data class Place(
    val id: Int,
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int,
    val category: Category
)