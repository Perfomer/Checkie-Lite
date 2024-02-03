package group.bakemate.core.storage.api

import group.bakemate.core.entity.RegisterData

interface UserStorage {

    suspend fun registerUser(registerData: RegisterData)
}