package ilkadam.ilkmuhabbet.presentation.discover

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ilkadam.ilkmuhabbet.R
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.presentation.profile.components.ChooseProfilePicFromGallery
import ilkadam.ilkmuhabbet.presentation.userlist.UserListViewModel
import ilkadam.ilkmuhabbet.ui.theme.spacing

@Composable
fun DiscoverScreen(
    discoverScreenViewModel: DiscoverScreenViewModel = hiltViewModel(),
    userListViewModel: UserListViewModel = hiltViewModel()
) {
    //rastgele kullanıcı gelecek -> checked
    //resim, ad ve hakkında görüntüleyeceğiz -> checked
    //arkadaşlık isteği gönderilebilecek
    //beğenmediğinde başka rastgele kullanıcı gelecek -> Geç tıklandığında

    var isLoading by remember {
        mutableStateOf(false)
    }
    isLoading = discoverScreenViewModel.isLoading.value

    var userDataFromFirebase by remember { mutableStateOf(User()) }
    userDataFromFirebase = discoverScreenViewModel.userDataStateFromFirebase.value

    var name by remember { mutableStateOf("") }
    name = userDataFromFirebase.userName

    var bio by remember { mutableStateOf("") }
    bio = userDataFromFirebase.userBio

    var userDataPictureUrl by remember { mutableStateOf("") }
    userDataPictureUrl = userDataFromFirebase.userProfilePictureUrl

    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        if (userDataPictureUrl != "") {
                            Image(
                                painter = rememberAsyncImagePainter(userDataPictureUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(400.dp)
                                //.border(1.dp, Color.Black),
                                //.clip(CircleShape),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = name,
                                modifier = Modifier
                                    .size(400.dp)
                                //.border(1.dp, Color.Black)
                            )

                        }

                    }
                    Card() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(id = R.string.full_name) + ":")
                            Text(text = name)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(id = R.string.about_you) + ":")
                            Text(text = bio)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            userListViewModel.createFriendshipRegisterToFirebase(userDataFromFirebase.profileUUID)
                        }) {
                            Icon(imageVector = Icons.Filled.PersonAddAlt1, contentDescription = "")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = stringResource(id = R.string.add_user))
                        }
                        Button(onClick = {
                            discoverScreenViewModel.getRandomUserFromFirebase()
                        }) {
                            Icon(imageVector = Icons.Filled.PersonSearch, contentDescription = "")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = stringResource(R.string.next))
                        }
                    }

                }
            }
        }
    }
}