package com.karuhun.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

class LauncherAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val viewModel: MainViewModel
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: MainViewModel = hiltViewModel<MainViewModel>()
) = LauncherAppState(
    navController = navController,
    coroutineScope = coroutineScope,
    viewModel = viewModel
)