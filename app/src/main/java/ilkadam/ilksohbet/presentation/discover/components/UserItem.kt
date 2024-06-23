package ilkadam.ilksohbet.presentation.discover.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.ui.theme.spacing

@Composable
fun UserItem(
    user: User,
    onclick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(MaterialTheme.spacing.small)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small)
                .clickable { onclick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(60.dp),
                shape = CircleShape
            ) {
                if (user.userProfilePictureUrl != "") {
                    SubcomposeAsyncImage(
                        model = user.userProfilePictureUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator()
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(id = R.string.full_name),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = stringResource(id = R.string.about_you),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = user.userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = user.userBio,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}