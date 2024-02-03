package group.bakemate.feature.registration.data

import android.util.Log
import group.bakemate.core.entity.RegisterData
import group.bakemate.core.storage.api.UserStorage
import group.bakemate.feature.registration.domain.RegistrationRepository
import kotlin.math.log

class RegistrationRepositoryImpl(
    private val userStorage: UserStorage
) : RegistrationRepository {

    override suspend fun registerUser(registerData: RegisterData) {
        userStorage.registerUser(registerData)
    }
}