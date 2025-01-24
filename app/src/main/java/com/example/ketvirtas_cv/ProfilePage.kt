package com.example.ketvirtas_cv

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.graphicsLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val showLanguagePicker = remember { mutableStateOf(false) }
        val context = LocalContext.current
        var currentLanguage by rememberSaveable { mutableStateOf("en") }
        val scrollState = rememberScrollState()

        val flagIcon = if (currentLanguage == "en") {
            R.drawable.ic_british_flag
        } else {
            R.drawable.ic_lithuanian_flag
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState) // Enable vertical scrolling
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                TopAppBar(
                    modifier = Modifier.padding(16.dp),
                    title = {
                        Text(
                            stringResource(id = R.string.app_name),
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { showLanguagePicker.value = true },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = flagIcon),
                                contentDescription = stringResource(id = R.string.change_locale_description),
                                contentScale = ContentScale.Crop,
                            )
                        }

                        if (showLanguagePicker.value) {
                            LanguagePickerOverlay(
                                onDismiss = { showLanguagePicker.value = false },
                                onSelectLanguage = { selectedLanguage ->
                                    updateLocale(selectedLanguage, context)
                                    showLanguagePicker.value = false
                                    currentLanguage = selectedLanguage
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.secondary
                    )
                )

                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_photo),
                        contentDescription = stringResource(id = R.string.profile_name),
                        modifier = Modifier
                            .size(72.dp)
                            .padding(end = 16.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = stringResource(id = R.string.profile_name),
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contact Info Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = stringResource(id = R.string.phone_label),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "${stringResource(id = R.string.phone_label)} ${stringResource(id = R.string.phone_number)}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = stringResource(id = R.string.email_label),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "${stringResource(id = R.string.email_label)} ${stringResource(id = R.string.email_address)}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(48.dp))
                        Text(
                            "${stringResource(id = R.string.group_label)} ${stringResource(id = R.string.group_number)}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.navigate("portfolio") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(stringResource(id = R.string.view_portfolio))
                    }

                    Button(
                        onClick = { navController.navigate("contactForm") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(stringResource(id = R.string.contact_me))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                var isExpanded by remember { mutableStateOf(false) }
                val transition =
                    updateTransition(targetState = isExpanded, label = "QR Code Animation")

                val buttonSize by transition.animateDp(label = "Button Size") { state ->
                    if (state) 256.dp else 256.dp
                }

                val qrOpacity by transition.animateFloat(label = "QR Opacity") { state ->
                    if (state) 1f else 0f
                }

                if (isExpanded) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_qr),
                        contentDescription = stringResource(id = R.string.qr_code),
                        modifier = Modifier
                            .size(buttonSize)
                            .align(Alignment.CenterHorizontally)
                            .graphicsLayer { alpha = qrOpacity }
                    )
                } else {
                    Button(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                            .size(buttonSize)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(stringResource(id = R.string.download))
                    }
                }
            }
        }
    }
}

@Composable
fun LanguagePickerOverlay(
    onDismiss: () -> Unit,
    onSelectLanguage: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Language", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_british_flag),
                        contentDescription = "English flag",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ClickableText(
                        text = AnnotatedString("EN"),
                        onClick = { onSelectLanguage("en") },
                        style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lithuanian_flag),
                        contentDescription = "Lithuanian flag",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ClickableText(
                        text = AnnotatedString("LT"),
                        onClick = { onSelectLanguage("lt") },
                        style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Close", color = MaterialTheme.colorScheme.secondary)
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary
    )
}

fun updateLocale(languageCode: String, context: Context) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val resources = context.resources
    val config = resources.configuration
    config.setLocale(locale)

    resources.updateConfiguration(config, resources.displayMetrics)

    if (context is Activity) {
        context.recreate()
    }
}