package ilkadam.ilksohbet.presentation.userlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.domain.model.FriendListRegister
import ilkadam.ilksohbet.ui.theme.spacing

@Composable
fun PendingFriendRequestList(
    item: FriendListRegister,
    onAcceptClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.spacing.medium,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
//                text = item.requesterEmail,
                text = item.requesterUserName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row {
                TextButton(
                    onClick = { onCancelClick() }
                ) {
                    Text(text = stringResource(R.string.decline), color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = { onAcceptClick() }) {
                    Text(text = stringResource(R.string.accept))
                }
            }

        }
    }
}