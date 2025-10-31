package com.infinitum.labs.rickandmorty_android.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.infinitum.labs.rickandmorty_android.character.detail.CharacterDetailScreen
import com.infinitum.labs.rickandmorty_android.character.router.CharacterRouter
import com.infinitum.labs.rickandmorty_android.main.MainScreen
import com.infinitum.labs.rickandmorty_android.splash.SplashRouter
import com.infinitum.labs.rickandmorty_android.splash.SplashScreen

/**
 * Main navigation component for the app.
 * Centralizes all navigation logic and handles routing between screens.
 *
 * Uses type-safe navigation with kotlinx.serialization.
 */
@Composable
internal fun NavigateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: NavigationRoute = NavigationRoute.Splash
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash Screen
        composable<NavigationRoute.Splash> {
            SplashScreen(onNavigate = { router ->
                when (router) {
                    SplashRouter.NavigateToMain -> {
                        navController.navigate(NavigationRoute.Main) {
                            popUpTo(NavigationRoute.Splash) { inclusive = true }
                        }
                    }
                }
            })
        }

        // Main Screen (with BottomNavigationBar)
        composable<NavigationRoute.Main> {
            MainScreen(onNavigate = { router ->
                when (router) {
                    is CharacterRouter.NavigateToDetail -> {
                        navController.navigate(
                            NavigationRoute.CharacterDetail(characterId = router.characterId)
                        )
                    }
                    CharacterRouter.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            })
        }

        // Character Detail Screen
        composable<NavigationRoute.CharacterDetail> { backStackEntry ->
            val route: NavigationRoute.CharacterDetail = backStackEntry.toRoute()
            CharacterDetailScreen(
                characterId = route.characterId,
                onNavigate = { router ->
                    when (router) {
                        is CharacterRouter.NavigateToDetail -> {
                            navController.navigate(
                                NavigationRoute.CharacterDetail(characterId = router.characterId)
                            )
                        }
                        CharacterRouter.NavigateBack -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}