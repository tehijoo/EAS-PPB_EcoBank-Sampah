package com.example.registrasisiswa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.registrasisiswa.ui.screens.AddMemberScreen
import com.example.registrasisiswa.ui.screens.AddTransactionScreen
import com.example.registrasisiswa.ui.screens.AdminLoginScreen
import com.example.registrasisiswa.ui.screens.HomeScreen
import com.example.registrasisiswa.ui.screens.MemberCardScreen
import com.example.registrasisiswa.ui.screens.MemberDetailScreen
import com.example.registrasisiswa.ui.screens.NasabahLoginScreen
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
    object NasabahLogin : Screen("nasabah_login")
    object Home : Screen("home")
    object AddMember : Screen("add_member")
    object WasteCatalog : Screen("waste_catalog")
    object MemberDetail : Screen("member_detail/{memberId}") {
        fun createRoute(memberId: Int) = "member_detail/$memberId"
    }
    object MemberCard : Screen("member_card/{memberId}") {
        fun createRoute(memberId: Int) = "member_card/$memberId"
    }
    object AddTransaction : Screen("add_transaction/{memberId}") {
        fun createRoute(memberId: Int) = "add_transaction/$memberId"
    }
    object TransactionHistory : Screen("transaction_history/{memberId}") {
        fun createRoute(memberId: Int) = "transaction_history/$memberId"
    }
    object Reward : Screen("reward/{memberId}") {
        fun createRoute(memberId: Int) = "reward/$memberId"
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
                onNavigateToNasabahLogin = { navController.navigate(Screen.NasabahLogin.route) }
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

        composable(Screen.NasabahLogin.route) {
            NasabahLoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { memberId ->
                    navController.navigate(Screen.MemberDetail.createRoute(memberId)) {
                        popUpTo(Screen.NasabahLogin.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddMember = { navController.navigate(Screen.AddMember.route) },
                onNavigateToWasteCatalog = { navController.navigate(Screen.WasteCatalog.route) },
                onNavigateToMemberDetail = { memberId ->
                    viewModel.selectMember(memberId)
                    navController.navigate(Screen.MemberDetail.createRoute(memberId))
                },
                onLogout = {
                    viewModel.logout()
                    navController.popBackStack(Screen.RoleSelection.route, false)
                }
            )
        }

        composable(Screen.AddMember.route) {
            AddMemberScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.WasteCatalog.route) {
            WasteCatalogScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.MemberDetail.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: -1
            MemberDetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMemberCard = { navController.navigate(Screen.MemberCard.createRoute(memberId)) },
                onNavigateToAddTransaction = { navController.navigate(Screen.AddTransaction.createRoute(memberId)) },
                onNavigateToTransactionHistory = { navController.navigate(Screen.TransactionHistory.createRoute(memberId)) },
                onNavigateToReward = { navController.navigate(Screen.Reward.createRoute(memberId)) },
                onNavigateToWasteCatalog = { navController.navigate(Screen.WasteCatalog.route) }
            )
        }

        composable(
            route = Screen.MemberCard.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            MemberCardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddTransaction.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            AddTransactionScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TransactionHistory.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            TransactionHistoryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Reward.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            RewardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
