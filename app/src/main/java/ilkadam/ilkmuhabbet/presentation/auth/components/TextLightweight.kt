package ilkadam.ilkmuhabbet.presentation.auth.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ilkadam.ilkmuhabbet.R
import ilkadam.ilkmuhabbet.ui.theme.spacing

@Composable
fun TextLightweight() {
    Text(
        modifier = Modifier.padding(top = MaterialTheme.spacing.large),
        text = stringResource(R.string.lightweight_instant_messaging),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.outline
    )
}