package com.ainrom.lbcremix.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ainrom.lbcremix.ui.albums.Albums
import com.ainrom.lbcremix.ui.album_detail.AlbumDetail

sealed class Screen(val route: String) {
    object Albums : Screen("albums")

    object AlbumDetail : Screen("album/{albumId}") {
        fun createRoute(albumId: String): String {
            return "album/$albumId"
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LbcRemixNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Albums.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Screen.Albums.route) {
            Albums(
                navController = navController,
                albumsViewModel = hiltViewModel()
            )
        }
        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) {
            AlbumDetail(
                navController = navController,
                albumDetailViewModel = hiltViewModel(),
            )
        }
    }
}