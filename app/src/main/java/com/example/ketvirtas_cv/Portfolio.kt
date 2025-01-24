package com.example.ketvirtas_cv

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalConfiguration

data class PortfolioEntry(val type: String, val duration: String, val description: String)

fun loadPortfolioEntries(context: Context): List<PortfolioEntry> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("portfolio_prefs", Context.MODE_PRIVATE)
    val entriesString = sharedPreferences.getString("portfolio_entries", "") ?: ""
    return entriesString.split("#").filter { it.isNotEmpty() }.map {
        val parts = it.split("|")
        PortfolioEntry(parts[0], parts[1], parts[2])
    }
}

fun savePortfolioEntries(context: Context, entries: List<PortfolioEntry>) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("portfolio_prefs", Context.MODE_PRIVATE)
    val entriesString = entries.joinToString("#") { "${it.type}|${it.duration}|${it.description}" }
    sharedPreferences.edit().putString("portfolio_entries", entriesString).apply()
}

fun saveCvText(context: Context, text: String) {
    val sharedPreferences = context.getSharedPreferences("PortfolioPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("cvText", text).apply()
}

fun loadCvText(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("PortfolioPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("cvText", "This is a paragraph describing the CV.") ?: ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioPage(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    var showOverlay by remember { mutableStateOf(false) }
    var newEntryType by remember { mutableStateOf("Work Experience") }
    var newEntryDuration by remember { mutableStateOf("Jan 2022 - Dec 2022") }
    var newEntryDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    var portfolioEntries by remember { mutableStateOf(loadPortfolioEntries(context).toMutableList()) }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var cvText by remember { mutableStateOf(loadCvText(context))}

        Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text(stringResource(id = R.string.profile_name)) },
            navigationIcon = {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(
                        text = "<",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp

                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isPortrait) {
        Text(text = "Curriculum Vitae", style = MaterialTheme.typography.titleMedium)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
        ) {
            if (isEditing) {
                TextField(
                    value = cvText,
                    onValueChange = { cvText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { stringResource(id = R.string.placeholder) }
                )
                Button(
                    onClick = {
                        saveCvText(context, cvText)
                        isEditing = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(id = R.string.save))
                }
            } else {
                Text(
                    text = cvText,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Button(
            onClick = { isEditing = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.edit))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Portfolio", style = MaterialTheme.typography.titleMedium)

        portfolioEntries.forEach { entry ->
            PortfolioEntryDisplay(entry = entry, onRemove = {
                portfolioEntries = portfolioEntries.filter { it != entry }.toMutableList()
                savePortfolioEntries(context, portfolioEntries)
            })
        }

        Button(
            onClick = { showOverlay = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.add))
        }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = "Curriculum Vitae", style = MaterialTheme.typography.titleMedium)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
                    ) {
                        if (isEditing) {
                            TextField(
                                value = cvText,
                                onValueChange = { cvText = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { stringResource(id = R.string.placeholder) }
                            )
                            Button(
                                onClick = {
                                    saveCvText(context, cvText)
                                    isEditing = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(stringResource(id = R.string.save))
                            }
                        } else {
                            Text(
                                text = cvText,
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Button(
                        onClick = { isEditing = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(id = R.string.edit))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = "Portfolio", style = MaterialTheme.typography.titleMedium)

                    portfolioEntries.forEach { entry ->
                        PortfolioEntryDisplay(entry = entry, onRemove = {
                            portfolioEntries = portfolioEntries.filter { it != entry }.toMutableList()
                            savePortfolioEntries(context, portfolioEntries)
                        })
                    }

                    Button(
                        onClick = { showOverlay = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(id = R.string.add))
                    }
                }
            }
        }

        if (showOverlay) {
            Overlay(
                onAdd = {
                    portfolioEntries = (listOf(PortfolioEntry(newEntryType, newEntryDuration, newEntryDescription)) + portfolioEntries).toMutableList()
                    savePortfolioEntries(context, portfolioEntries)
                    showOverlay = false
                },
                onCancel = { showOverlay = false },
                newEntryType = newEntryType,
                onEntryTypeChange = { newEntryType = it },
                onEntryDurationChange = { newEntryDuration = it },
                newEntryDescription = newEntryDescription,
                onEntryDescriptionChange = { newEntryDescription = it }
            )
        }
    }
}

@Composable
fun PortfolioEntryDisplay(entry: PortfolioEntry, onRemove: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = stringResource(id = R.string.type)+ ": ${entry.type}", color = MaterialTheme.colorScheme.primary)
        Text(text = stringResource(id = R.string.duration) + ": ${entry.duration}", color = MaterialTheme.colorScheme.primary)
        Text(text = entry.description, color = MaterialTheme.colorScheme.primary)

        Button(
            onClick = onRemove,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.remove), color = Color.White)
        }
    }
}

@Composable
fun Overlay(
    onAdd: () -> Unit,
    onCancel: () -> Unit,
    newEntryType: String,
    onEntryTypeChange: (String) -> Unit,
    onEntryDurationChange: (String) -> Unit,
    newEntryDescription: String,
    onEntryDescriptionChange: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, _ ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                onDateSelected(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(min = 350.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(stringResource(id = R.string.addNew), style = MaterialTheme.typography.titleMedium)

            // DropdownMenu Trigger
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                    Text(newEntryType)
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.workExperience)) },
                        onClick = {
                            onEntryTypeChange("Work Experience")
                            isDropdownExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.studies)) },
                        onClick = {
                            onEntryTypeChange("Studies")
                            isDropdownExpanded = false
                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { showDatePicker { date -> startDate = date } }) {
                    Text(startDate.ifEmpty { stringResource(id = R.string.startDate) })
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("to")
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showDatePicker { date -> endDate = date } }) {
                    Text(endDate.ifEmpty { stringResource(id = R.string.endDate) })
                }
            }
            onEntryDurationChange("$startDate - $endDate")


            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = newEntryDescription,
                onValueChange = onEntryDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { stringResource(id = R.string.enterDescription) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onCancel) {
                    Text(stringResource(id = R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onAdd) {
                    Text(stringResource(id = R.string.addEntry))
                }
            }
        }
    }
}
