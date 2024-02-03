package group.bakemate.feature.registration.domain

import group.bakemate.core.entity.RegisterData

interface RegistrationRepository {

    suspend fun registerUser(registerData: RegisterData)
}