package ilkadam.ilkmuhabbet.presentation.commoncomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import ilkadam.ilkmuhabbet.R

@Composable
fun LogOutCustomText(
    entry: String = "Log Out",
    fontSize: TextUnit = 10.sp,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
        Text(
            text = stringResource(R.string.log_out),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onError
        )
    }

//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(percent = 50))
//            .background(
//                MaterialTheme.colorScheme.surface
//            )
//            .height(26.dp)
//            .fillMaxWidth()
//            .clickable {
//                onClick()
//            }) {
//        Text(
//            modifier = Modifier.align(Alignment.Center),
//            text = entry,
//            fontSize = fontSize,
//            color = Color.Red
//        )
//    }
}