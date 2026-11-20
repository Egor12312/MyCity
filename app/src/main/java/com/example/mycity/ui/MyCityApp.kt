package com.example.mycity.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mycity.model.Category
import com.example.mycity.model.Place

enum class Routes(val route: String) {
    RESTAURANTS("restaurants"),
    PARKS("parks"),
    COFFEE("coffee")
}

@Composable
fun MyCityApp(
    windowSizeClass: WindowSizeClass,
    viewModel: MyCityViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.RESTAURANTS.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            Category.entries.forEachIndexed { index, category ->
                val route = Routes.entries[index].route
                item(
                    selected = currentRoute == route,
                    onClick = {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        viewModel.selectCategory(category)
                    },
                    icon = {
                        val icon = when (category) {
                            Category.RESTAURANTS -> Icons.Default.Home
                            Category.PARKS -> Icons.Default.Place
                            Category.COFFEE -> Icons.Default.Favorite
                        }
                        Icon(imageVector = icon, contentDescription = null)
                    },
                    label = { Text(stringResource(category.titleRes)) }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Routes.RESTAURANTS.route) {
            Routes.entries.forEach { route ->
                composable(route.route) {
                    AdaptiveCategoryScreen(
                        uiState = uiState,
                        windowSizeClass = windowSizeClass,
                        onPlaceClick = { viewModel.selectPlace(it) },
                        onBack = { viewModel.clearSelection() }
                    )
                }
            }
        }
    }
}

@Composable
fun AdaptiveCategoryScreen(
    uiState: MyCityUiState,
    windowSizeClass: WindowSizeClass,
    onPlaceClick: (Place) -> Unit,
    onBack: () -> Unit
) {
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    if (isCompact) {
        if (uiState.selectedPlace == null) {
            PlacesList(places = uiState.places, onPlaceClick = onPlaceClick)
        } else {
            PlaceDetail(place = uiState.selectedPlace, onBack = onBack)
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                PlacesList(places = uiState.places, onPlaceClick = onPlaceClick, selectedPlace = uiState.selectedPlace)
            }
            VerticalDivider()
            Box(modifier = Modifier.weight(2f)) {
                if (uiState.selectedPlace != null) {
                    PlaceDetail(place = uiState.selectedPlace, onBack = null)
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Выберите место из списка")
                    }
                }
            }
        }
    }
}

@Composable
fun PlacesList(places: List<Place>, onPlaceClick: (Place) -> Unit, selectedPlace: Place? = null) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(places) { place ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onPlaceClick(place) },
                colors = CardDefaults.cardColors(
                    containerColor = if (place == selectedPlace) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = stringResource(place.nameRes), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun PlaceDetail(place: Place, onBack: (() -> Unit)?) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
        }
        Text(
            text = stringResource(place.nameRes),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = place.imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(place.descriptionRes),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}