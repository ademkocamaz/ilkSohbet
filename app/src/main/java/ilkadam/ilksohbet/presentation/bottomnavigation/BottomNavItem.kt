package ilkadam.ilksohbet.presentation.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    var screen_route: String,
    var arguments: String
) {
    object SignIn : BottomNavItem(
        "SignIn",
        Icons.Filled.Person,
        "signin",
        "?emailFromSignUp={emailFromSignUp}"
    ) {
        val fullRoute = screen_route + arguments
    }

    object SignUp : BottomNavItem(
        "SignUp",
        Icons.Filled.Person,
        "signup",
        "?emailFromSignIn={emailFromSignIn}"
    ) {
        val fullRoute = screen_route + arguments
    }

    object Profile : BottomNavItem(
        "Profile",
        Icons.Filled.Person,
        "profile",
        ""
    ) {
        val fullRoute = screen_route + arguments
    }

    object UserList : BottomNavItem(
        "Chat",
        Icons.AutoMirrored.Filled.Chat,
        "userlist",
        ""
    ) {
        val fullRoute = screen_route + arguments
    }

    object Chat : BottomNavItem(
        "Chat",
        Icons.AutoMirrored.Filled.Chat,
        "chat",
        "/{chatroomUUID}" + "/{opponentUUID}" + "/{registerUUID}" + "/{oneSignalUserId}"
    ) {
        val fullRoute = screen_route + arguments
    }

    object Discover : BottomNavItem(
        "Discover",
        Icons.Filled.AddReaction,
        "discover",
        ""
    ) {
        val fullRoute = screen_route
    }

    object DiscoverAll : BottomNavItem(
        "DiscoverAll",
        Icons.Filled.People,
        "discoverAll",
        ""
    ) {
        val fullRoute = screen_route
    }

}