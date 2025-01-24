package com.example.ketvirtas_cv

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") { ProfilePage(navController) }
        composable("contactForm") { ContactForm(navController) }
        composable("portfolio") { PortfolioPage(navController) }
    }
}