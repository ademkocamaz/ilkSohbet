package ilkadam.ilkmuhabbet.presentation.userlist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ilkadam.ilkmuhabbet.R

@Composable
fun AlertDialogChat(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val dialogText = stringResource(R.string.add_user_via_email).trimIndent()
    var emailInput by remember {
        mutableStateOf("")
    }
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Filled.Person, contentDescription = null)
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.add_user),
                textAlign = TextAlign.Center
            )
        },

        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(emailInput)
                }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },

        text = {
            AlertDialogCustomOutlinedTextField(
                entry = emailInput,
                hint = stringResource(id = R.string.email),
                onChange = { emailInput = it })
        }
    )
}