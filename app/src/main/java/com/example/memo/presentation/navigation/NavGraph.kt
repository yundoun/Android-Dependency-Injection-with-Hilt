package com.example.memo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.memo.presentation.screens.add.AddMemoScreen
import com.example.memo.presentation.screens.list.MemoListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MemoList.route
    ) {
        composable(route = Screen.MemoList.route) {
            MemoListScreen(
                onAddClick = {
                    navController.navigate(Screen.AddMemo.route)
                }
            )
        }
        composable(route = Screen.AddMemo.route) {
            AddMemoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object MemoList : Screen("memo_list")
    object AddMemo : Screen("add_memo")
}