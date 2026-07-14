package com.flowstudent.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModelProvider
import com.flowstudent.app.data.local.AppDatabase
import com.flowstudent.app.ui.screens.add_expense.AddExpenseScreen
import com.flowstudent.app.ui.screens.home.HomeScreen
import com.flowstudent.app.ui.screens.survey.SurveyScreen
import com.flowstudent.app.ui.screens.stats.StatsScreen
import com.flowstudent.app.ui.screens.stats.BudgetScreen
import com.flowstudent.app.ui.screens.stats.ProfileScreen
import com.flowstudent.app.ui.theme.ZentTheme
import com.flowstudent.app.ui.viewmodel.TransactionViewModel
import com.flowstudent.app.ui.viewmodel.TransactionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usamos la instancia Singleton de la base de datos a través de la clase Application
        val app = application as ZentApplication
        val database = app.database
        
        // Usamos un Factory para que el ViewModel sobreviva a cambios de configuración
        val viewModelFactory = TransactionViewModelFactory(application, database.transactionDao())
        val viewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]

        setContent {
            ZentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ZentApp(viewModel)
                }
            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Expenses : Screen("expenses", Icons.Default.AttachMoney, "Gastos")
    object Stats : Screen("stats", Icons.Default.Analytics, "Gráficas")
    object Budget : Screen("budget", Icons.AutoMirrored.Filled.Assignment, "Presupuesto")
    object Profile : Screen("profile", Icons.Default.AccountCircle, "Perfil")
}

@Composable
fun ZentApp(viewModel: TransactionViewModel) {
    val navController = rememberNavController()
    val isSurveyCompleted by viewModel.isSurveyCompleted.collectAsState()
    
    val startDestination = if (isSurveyCompleted) "main_nav" else "survey_screen"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("survey_screen") {
            SurveyScreen(onSurveyComplete = {
                viewModel.setSurveyCompleted()
                navController.navigate("main_nav") {
                    popUpTo("survey_screen") { inclusive = true }
                }
            })
        }
        composable("main_nav") {
            MainScaffold(viewModel, navController)
        }
        composable("add_transaction/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "GASTO"
            AddExpenseScreen(viewModel, type = type, onBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(viewModel: TransactionViewModel, rootNavController: NavHostController) {
    val navController = rememberNavController()
    val items = listOf(Screen.Expenses, Screen.Stats, Screen.Budget, Screen.Profile)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Expenses.route, Modifier.padding(innerPadding)) {
            composable(Screen.Expenses.route) { 
                HomeScreen(viewModel, onAddTransaction = { type ->
                    rootNavController.navigate("add_transaction/$type")
                })
            }
            composable(Screen.Stats.route) { StatsScreen(viewModel) }
            composable(Screen.Budget.route) { BudgetScreen(viewModel) }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}
