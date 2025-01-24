package com.example.ketvirtas_cv

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactForm(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TopAppBar
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

        // Name field
        Text(text = stringResource(id = R.string.name_label), color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(stringResource(id = R.string.name_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email field
        Text(text = stringResource(id = R.string.email_label), color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(stringResource(id = R.string.email_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Comment field
        Text(text = stringResource(id = R.string.comment_label), color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text(stringResource(id = R.string.comment_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && comment.isNotBlank()) {
                    errorMessage = ""
                    sendEmail(context, "mindaugaskazlavickas@gmail.com", "Contact from $name", comment)
                } else {
                    errorMessage = context.getString(R.string.error)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(id = R.string.contact_me),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

private fun sendEmail(context: android.content.Context, recipientEmail: String, subject: String, body: String) {

    if (recipientEmail.isBlank()) {
        return
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    val packageManager = context.packageManager
    val activities = packageManager.queryIntentActivities(intent, 0)


    if (activities.isNotEmpty()) {
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    } else {
        Toast.makeText(context, "No email apps found. Please install an email app.", Toast.LENGTH_LONG).show()
    }
}
