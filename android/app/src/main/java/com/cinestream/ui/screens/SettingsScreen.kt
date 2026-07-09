package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cinestream.ui.navigation.Route
import com.cinestream.ui.viewmodel.AuthViewModel
import com.cinestream.utils.AuthUtils
import com.cinestream.utils.Constants

@Composable
fun SettingsScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel? = null,
    sharedPreferences: android.content.SharedPreferences? = null
) {
    var selectedLanguage by remember { 
        mutableStateOf(
            sharedPreferences?.getString("language", "en") ?: "en"
        )
    }
    var selectedTheme by remember { 
        mutableStateOf(
            sharedPreferences?.getString("theme", "dark") ?: "dark"
        )
    }
    var showLanguageMenu by remember { mutableStateOf(false) }
    var showThemeMenu by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Section
            SettingsSection(title = "Preferences")
            
            SettingItem(
                icon = Icons.Default.Language,
                title = "Language",
                value = Constants.AVAILABLE_LANGUAGES[selectedLanguage] ?: selectedLanguage,
                onClick = { showLanguageMenu = !showLanguageMenu }
            )
            
            if (showLanguageMenu) {
                LanguageDropdown(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { language ->
                        selectedLanguage = language
                        sharedPreferences?.edit()?.putString("language", language)?.apply()
                        showLanguageMenu = false
                    }
                )
            }
            
            Divider()
            
            // Theme Section
            SettingItem(
                icon = Icons.Default.Palette,
                title = "Theme",
                value = selectedTheme.replaceFirstChar { it.uppercase() },
                onClick = { showThemeMenu = !showThemeMenu }
            )
            
            if (showThemeMenu) {
                ThemeDropdown(
                    selectedTheme = selectedTheme,
                    onThemeSelected = { theme ->
                        selectedTheme = theme
                        sharedPreferences?.edit()?.putString("theme", theme)?.apply()
                        showThemeMenu = false
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Account Section
            SettingsSection(title = "Account")
            
            SettingItem(
                icon = Icons.Default.Logout,
                title = "Logout",
                onClick = {
                    authViewModel?.logout()
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Settings.route) { inclusive = true }
                    }
                },
                isDestructive = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CineStream v1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "© 2024 CineStream",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingItem(
    icon: androidx.compose.material.icons.Icons.Filled,
    title: String,
    value: String? = null,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isDestructive) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDestructive) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
            value?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LanguageDropdown(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Constants.AVAILABLE_LANGUAGES.forEach { (code, name) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLanguageSelected(code) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = code == selectedLanguage,
                    onClick = { onLanguageSelected(code) }
                )
                Text(
                    text = name,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeDropdown(
    selectedTheme: String,
    onThemeSelected: (String) -> Unit
) {
    val themes = listOf("light", "dark", "auto")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        themes.forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeSelected(theme) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = theme == selectedTheme,
                    onClick = { onThemeSelected(theme) }
                )
                Text(
                    text = theme.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
