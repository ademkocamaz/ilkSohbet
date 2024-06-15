package ilkadam.ilksohbet.domain.usecase.profileScreen

import android.net.Uri
import ilkadam.ilksohbet.domain.repository.ProfileScreenRepository

class UploadPictureToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(url: Uri) = profileScreenRepository.uploadPictureToFirebase(url)
}