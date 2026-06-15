package com.example.registrasisiswa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.registrasisiswa.ui.screens.AddPenggunaScreen
import com.example.registrasisiswa.ui.screens.AddTransactionScreen
import com.example.registrasisiswa.ui.screens.AdminLoginScreen
import com.example.registrasisiswa.ui.screens.HomeScreen
import com.example.registrasisiswa.ui.screens.PenggunaCardScreen
import com.example.registrasisiswa.ui.screens.PenggunaDetailScreen
import com.example.registrasisiswa.ui.screens.PenggunaLoginScreen
import com.example.registrasisiswa.ui.screens.RewardScreen
import com.example.registrasisiswa.ui.screens.RoleSelectionScreen
import com.example.registrasisiswa.ui.screens.SplashScreen
import com.example.registrasisiswa.ui.screens.TransactionHistoryScreen
import com.example.registrasisiswa.ui.screens.WasteCatalogScreen
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object RoleSelection : Screen("role_selection")
    object AdminLogin : Screen("admin_login")
    object PenggunaLogin : Screen("pengguna_login")
    object Home : Screen("home")
    object AddPengguna : Screen("add_pengguna")
    object WasteCatalog : Screen("waste_catalog")
    object PenggunaDetail : Screen("pengguna_detail/{penggunaId}") {
        fun createRoute(penggunaId: Int) = "pengguna_detail/$penggunaId"
    }
    object PenggunaCard : Screen("pengguna_card/{penggunaId}") {
        fun createRoute(penggunaId: Int) = "pengguna_card/$penggunaId"
    }
    object AddTransaction : Screen("add_transaction/{penggunaId}") {
        fun createRoute(penggunaId: Int) = "add_transaction/$penggunaId"
    }
    object TransactionHistory : Screen("transaction_history/{penggunaId}") {
        fun createRoute(penggunaId: Int) = "transaction_history/$penggunaId"
    }
    object Reward : Screen("reward/{penggunaId}") {
        fun createRoute(penggunaId: Int) = "reward/$penggunaId"
    }
}

@Composable
fun AppNavGraph(
    viewModel: EcoBankViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.RoleSelection.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onNavigateToAdminLogin = { navController.navigate(Screen.AdminLogin.route) },
                onNavigateToPenggunaLogin = { navController.navigate(Screen.PenggunaLogin.route) }
            )
        }

        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.RoleSelection.route)
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PenggunaLogin.route) {
            PenggunaLoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { penggunaId ->
                    navController.navigate(Screen.PenggunaDetail.createRoute(penggunaId)) {
                        popUpTo(Screen.PenggunaLogin.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddPengguna = { navController.navigate(Screen.AddPengguna.route) },
                onNavigateToWasteCatalog = { navController.navigate(Screen.WasteCatalog.route) },
                onNavigateToPenggunaDetail = { penggunaId ->
                    viewModel.selectPengguna(penggunaId)
                    navController.navigate(Screen.PenggunaDetail.createRoute(penggunaId))
                },
                onLogout = {
                    viewModel.logout()
                    navController.popBackStack(Screen.RoleSelection.route, false)
                }
            )
        }

        composable(Screen.AddPengguna.route) {
            AddPenggunaScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.WasteCatalog.route) {
            WasteCatalogScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.PenggunaDetail.route,
            arguments = listOf(navArgument("penggunaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val penggunaId = backStackEntry.arguments?.getInt("penggunaId") ?: -1
            PenggunaDetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPenggunaCard = { navController.navigate(Screen.PenggunaCard.createRoute(penggunaId)) },
                onNavigateToAddTransaction = { navController.navigate(Screen.AddTransaction.createRoute(penggunaId)) },
                onNavigateToTransactionHistory = { navController.navigate(Screen.TransactionHistory.createRoute(penggunaId)) },
                onNavigateToReward = { navController.navigate(Screen.Reward.createRoute(penggunaId)) },
                onNavigateToWasteCatalog = { navController.navigate(Screen.WasteCatalog.route) }
            )
        }

        composable(
            route = Screen.PenggunaCard.route,
            arguments = listOf(navArgument("penggunaId") { type = NavType.IntType })
        ) {
            PenggunaCardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddTransaction.route,
            arguments = listOf(navArgument("penggunaId") { type = NavType.IntType })
        ) {
            AddTransactionScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TransactionHistory.route,
            arguments = listOf(navArgument("penggunaId") { type = NavType.IntType })
        ) {
            TransactionHistoryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Reward.route,
            arguments = listOf(navArgument("penggunaId") { type = NavType.IntType })
        ) {
            RewardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
