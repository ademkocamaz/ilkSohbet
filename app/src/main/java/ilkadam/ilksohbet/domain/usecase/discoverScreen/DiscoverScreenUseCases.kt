package ilkadam.ilksohbet.domain.usecase.discoverScreen

data class DiscoverScreenUseCases(
    val getRandomUserFromFirebase: GetRandomUserFromFirebase,
    val discoverCheckChatRoomExistedFromFirebase: DiscoverCheckChatRoomExistedFromFirebase,
    val discoverCreateChatRoomToFirebase: DiscoverCreateChatRoomToFirebase,
    val discoverCheckFriendListRegisterIsExistedFromFirebase: DiscoverCheckFriendListRegisterIsExistedFromFirebase,
    val discoverCreateFriendListRegisterToFirebase: DiscoverCreateFriendListRegisterToFirebase,
    val discoverOpenBlockedFriendToFirebase: DiscoverOpenBlockedFriendToFirebase,
    val getAllUsersFromFirebase: GetAllUsersFromFirebase
)