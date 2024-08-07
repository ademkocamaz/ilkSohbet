package ilkadam.ilksohbet.domain.usecase.profileScreen

data class ProfileScreenUseCases(
    val createOrUpdateProfileToFirebase: CreateOrUpdateProfileToFirebase,
    val loadProfileFromFirebase: LoadProfileFromFirebase,
    val setUserStatusToFirebase: SetUserStatusToFirebase,
    //val signOut: SignOut,
    val uploadPictureToFirebase: UploadPictureToFirebase,
    val setUserCreatedToFirebase: SetUserCreatedToFirebase
)