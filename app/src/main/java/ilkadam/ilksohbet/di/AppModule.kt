package ilkadam.ilksohbet.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ilkadam.ilksohbet.data.repository.AuthScreenRepositoryImpl
import ilkadam.ilksohbet.data.repository.ChatScreenRepositoryImpl
import ilkadam.ilksohbet.data.repository.DiscoverScreenRepositoryImpl
import ilkadam.ilksohbet.data.repository.ProfileScreenRepositoryImpl
import ilkadam.ilksohbet.data.repository.UserListScreenRepositoryImpl
import ilkadam.ilksohbet.domain.repository.AuthScreenRepository
import ilkadam.ilksohbet.domain.repository.ChatScreenRepository
import ilkadam.ilksohbet.domain.repository.DiscoverScreenRepository
import ilkadam.ilksohbet.domain.repository.ProfileScreenRepository
import ilkadam.ilksohbet.domain.repository.UserListScreenRepository
import ilkadam.ilksohbet.domain.usecase.authScreen.AuthUseCases
import ilkadam.ilksohbet.domain.usecase.authScreen.IsUserAuthenticatedInFirebase
import ilkadam.ilksohbet.domain.usecase.authScreen.SignIn
import ilkadam.ilksohbet.domain.usecase.chatScreen.BlockFriendToFirebase
import ilkadam.ilksohbet.domain.usecase.chatScreen.ChatScreenUseCases
import ilkadam.ilksohbet.domain.usecase.chatScreen.InsertMessageToFirebase
import ilkadam.ilksohbet.domain.usecase.chatScreen.LoadMessageFromFirebase
import ilkadam.ilksohbet.domain.usecase.chatScreen.LoadOpponentProfileFromFirebase
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverCheckChatRoomExistedFromFirebase
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverCheckFriendListRegisterIsExistedFromFirebase
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverCreateChatRoomToFirebase
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverCreateFriendListRegisterToFirebase
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverOpenBlockedFriendToFirebase
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverScreenUseCases
import ilkadam.ilksohbet.domain.usecase.discoverScreen.GetRandomUserFromFirebase
import ilkadam.ilksohbet.domain.usecase.profileScreen.CreateOrUpdateProfileToFirebase
import ilkadam.ilksohbet.domain.usecase.profileScreen.LoadProfileFromFirebase
import ilkadam.ilksohbet.domain.usecase.profileScreen.ProfileScreenUseCases
import ilkadam.ilksohbet.domain.usecase.profileScreen.SetUserCreatedToFirebase
import ilkadam.ilksohbet.domain.usecase.profileScreen.SetUserStatusToFirebase
import ilkadam.ilksohbet.domain.usecase.profileScreen.UploadPictureToFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.AcceptPendingFriendRequestToFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.CheckChatRoomExistedFromFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.CheckFriendListRegisterIsExistedFromFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.CreateChatRoomToFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.CreateFriendListRegisterToFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.LoadAcceptedFriendRequestListFromFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.LoadPendingFriendRequestListFromFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.OpenBlockedFriendToFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.RejectPendingFriendRequestToFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.SearchUserFromFirebase
import ilkadam.ilksohbet.domain.usecase.userListScreen.UserListScreenUseCases

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("login")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorageInstance() = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseDatabaseInstance() = FirebaseDatabase.getInstance("https://ilksohbet-9e1b6-default-rtdb.europe-west1.firebasedatabase.app")

//    @Provides
//    fun provideSharedPreferences(application: Application) =
//        application.getSharedPreferences("login", Context.MODE_PRIVATE)

    @Provides
    fun providesDataStore(application: Application) = application.dataStore

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): AuthScreenRepository = AuthScreenRepositoryImpl(auth)

    @Provides
    fun provideChatScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): ChatScreenRepository = ChatScreenRepositoryImpl(auth, database)

    @Provides
    fun provideProfileScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
        storage: FirebaseStorage
    ): ProfileScreenRepository = ProfileScreenRepositoryImpl(auth, database, storage)

    @Provides
    fun provideUserListScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): UserListScreenRepository = UserListScreenRepositoryImpl(auth, database)

    @Provides
    fun provideDiscoverScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
        storage: FirebaseStorage,
        profileScreenRepository: ProfileScreenRepository
    ): DiscoverScreenRepository =
        DiscoverScreenRepositoryImpl(auth, database, storage, profileScreenRepository)


    @Provides
    fun provideAuthScreenUseCase(authRepository: AuthScreenRepository) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticatedInFirebase(authRepository),
        signIn = SignIn(authRepository),
        //signUp = SignUp(authRepository)
    )

    @Provides
    fun provideChatScreenUseCase(chatScreenRepository: ChatScreenRepository) = ChatScreenUseCases(
        blockFriendToFirebase = BlockFriendToFirebase(chatScreenRepository),
        insertMessageToFirebase = InsertMessageToFirebase(chatScreenRepository),
        loadMessageFromFirebase = LoadMessageFromFirebase(chatScreenRepository),
        opponentProfileFromFirebase = LoadOpponentProfileFromFirebase(chatScreenRepository)
    )

    @Provides
    fun provideProfileScreenUseCase(profileScreenRepository: ProfileScreenRepository) =
        ProfileScreenUseCases(
            createOrUpdateProfileToFirebase = CreateOrUpdateProfileToFirebase(
                profileScreenRepository
            ),
            loadProfileFromFirebase = LoadProfileFromFirebase(profileScreenRepository),
            setUserStatusToFirebase = SetUserStatusToFirebase(profileScreenRepository),
            //signOut = SignOut(profileScreenRepository),
            uploadPictureToFirebase = UploadPictureToFirebase(profileScreenRepository),
            setUserCreatedToFirebase = SetUserCreatedToFirebase(profileScreenRepository)
        )

    @Provides
    fun provideUserListScreenUseCase(userListScreenRepository: UserListScreenRepository) =
        UserListScreenUseCases(
            acceptPendingFriendRequestToFirebase = AcceptPendingFriendRequestToFirebase(
                userListScreenRepository
            ),
            checkChatRoomExistedFromFirebase = CheckChatRoomExistedFromFirebase(
                userListScreenRepository
            ),
            checkFriendListRegisterIsExistedFromFirebase = CheckFriendListRegisterIsExistedFromFirebase(
                userListScreenRepository
            ),
            createChatRoomToFirebase = CreateChatRoomToFirebase(userListScreenRepository),
            createFriendListRegisterToFirebase = CreateFriendListRegisterToFirebase(
                userListScreenRepository
            ),
            loadAcceptedFriendRequestListFromFirebase = LoadAcceptedFriendRequestListFromFirebase(
                userListScreenRepository
            ),
            loadPendingFriendRequestListFromFirebase = LoadPendingFriendRequestListFromFirebase(
                userListScreenRepository
            ),
            openBlockedFriendToFirebase = OpenBlockedFriendToFirebase(userListScreenRepository),
            rejectPendingFriendRequestToFirebase = RejectPendingFriendRequestToFirebase(
                userListScreenRepository
            ),
            searchUserFromFirebase = SearchUserFromFirebase(userListScreenRepository),
        )

    @Provides
    fun provideDiscoverScreenUseCases(discoverScreenRepository: DiscoverScreenRepository) =
        DiscoverScreenUseCases(
            getRandomUserFromFirebase = GetRandomUserFromFirebase(discoverScreenRepository),
            discoverCheckChatRoomExistedFromFirebase = DiscoverCheckChatRoomExistedFromFirebase(
                discoverScreenRepository
            ),
            discoverCreateChatRoomToFirebase = DiscoverCreateChatRoomToFirebase(
                discoverScreenRepository
            ),
            discoverCheckFriendListRegisterIsExistedFromFirebase = DiscoverCheckFriendListRegisterIsExistedFromFirebase(
                discoverScreenRepository
            ),
            discoverCreateFriendListRegisterToFirebase = DiscoverCreateFriendListRegisterToFirebase(
                discoverScreenRepository
            ),
            discoverOpenBlockedFriendToFirebase = DiscoverOpenBlockedFriendToFirebase(
                discoverScreenRepository
            )
        )
}