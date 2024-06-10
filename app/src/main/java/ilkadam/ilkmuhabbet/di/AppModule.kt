package ilkadam.ilkmuhabbet.di

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
import ilkadam.ilkmuhabbet.data.repository.AuthScreenRepositoryImpl
import ilkadam.ilkmuhabbet.data.repository.ChatScreenRepositoryImpl
import ilkadam.ilkmuhabbet.data.repository.DiscoverScreenRepositoryImpl
import ilkadam.ilkmuhabbet.data.repository.ProfileScreenRepositoryImpl
import ilkadam.ilkmuhabbet.data.repository.UserListScreenRepositoryImpl
import ilkadam.ilkmuhabbet.domain.repository.AuthScreenRepository
import ilkadam.ilkmuhabbet.domain.repository.ChatScreenRepository
import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository
import ilkadam.ilkmuhabbet.domain.repository.ProfileScreenRepository
import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository
import ilkadam.ilkmuhabbet.domain.usecase.authScreen.AuthUseCases
import ilkadam.ilkmuhabbet.domain.usecase.authScreen.IsUserAuthenticatedInFirebase
import ilkadam.ilkmuhabbet.domain.usecase.authScreen.SignIn
import ilkadam.ilkmuhabbet.domain.usecase.chatScreen.BlockFriendToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.chatScreen.ChatScreenUseCases
import ilkadam.ilkmuhabbet.domain.usecase.chatScreen.InsertMessageToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.chatScreen.LoadMessageFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.chatScreen.LoadOpponentProfileFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverCheckChatRoomExistedFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverCheckFriendListRegisterIsExistedFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverCreateChatRoomToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverCreateFriendListRegisterToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverOpenBlockedFriendToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverScreenUseCases
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.GetRandomUserFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.CreateOrUpdateProfileToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.LoadProfileFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.ProfileScreenUseCases
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.SetUserCreatedToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.SetUserStatusToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.UploadPictureToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.AcceptPendingFriendRequestToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.CheckChatRoomExistedFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.CheckFriendListRegisterIsExistedFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.CreateChatRoomToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.CreateFriendListRegisterToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.LoadAcceptedFriendRequestListFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.LoadPendingFriendRequestListFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.OpenBlockedFriendToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.RejectPendingFriendRequestToFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.SearchUserFromFirebase
import ilkadam.ilkmuhabbet.domain.usecase.userListScreen.UserListScreenUseCases

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("login")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorageInstance() = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseDatabaseInstance() = FirebaseDatabase.getInstance()

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