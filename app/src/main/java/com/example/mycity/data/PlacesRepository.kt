package com.example.mycity.data

import com.example.mycity.R
import com.example.mycity.model.Category
import com.example.mycity.model.Place

object PlacesRepository {
    val allPlaces = listOf(
        Place(1, R.string.rest_1, R.string.rest_1_desc, R.drawable.rest_1, Category.RESTAURANTS),
        Place(2, R.string.rest_2, R.string.rest_2_desc, R.drawable.rest_2, Category.RESTAURANTS),
        Place(3, R.string.rest_3, R.string.rest_3_desc, R.drawable.rest_3, Category.RESTAURANTS),

        Place(4, R.string.coffee_1, R.string.coffee_1_desc, R.drawable.coffee_1, Category.COFFEE),
        Place(5, R.string.coffee_2, R.string.coffee_2_desc, R.drawable.coffee_2, Category.COFFEE),
        Place(6, R.string.coffee_3, R.string.coffee_3_desc, R.drawable.coffee_3, Category.COFFEE),

        Place(7, R.string.park_1, R.string.park_1_desc, R.drawable.park_1, Category.PARKS),
        Place(8, R.string.park_2, R.string.park_2_desc, R.drawable.park_2, Category.PARKS),
        Place(9, R.string.park_3, R.string.park_3_desc, R.drawable.park_3, Category.PARKS)
    )

    fun getPlacesByCategory(category: Category): List<Place> {
        return allPlaces.filter { it.category == category }
    }
}