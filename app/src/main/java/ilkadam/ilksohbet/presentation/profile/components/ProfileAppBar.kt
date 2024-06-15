package ilkadam.ilksohbet.presentation.profile.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ilkadam.ilksohbet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        //modifier = modifier.statusBarsPadding(),
        title = {
            Text(text = stringResource(id = R.string.app_name))
            /*Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )*/
        }
    )
}