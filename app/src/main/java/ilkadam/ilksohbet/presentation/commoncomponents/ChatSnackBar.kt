package ilkadam.ilksohbet.presentation.commoncomponents

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ChatSnackBar(
    snackbarData: SnackbarData,
    actionOnNewLine: Boolean = false,
) {
    val actionLabel = snackbarData.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                onClick = { snackbarData.performAction() },
                content = { Text(actionLabel) }
            )
        }
    } else {
        null
    }
    Snackbar(
        content = { Text(snackbarData.visuals.message) },
        action = actionComposable,
        actionOnNewLine = actionOnNewLine,
    )
}