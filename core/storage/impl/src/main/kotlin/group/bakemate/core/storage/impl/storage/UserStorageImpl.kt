package group.bakemate.core.storage.impl.storage

import android.util.Log
import group.bakemate.core.entity.RegisterData
import group.bakemate.core.local.api.TokenDatasource
import group.bakemate.core.storage.api.UserStorage

class UserStorageImpl(
//    private val userDatasource: UserDatasource,
    private val tokenDatasource: TokenDatasource,
) : UserStorage {

    override suspend fun registerUser(registerData: RegisterData) {
//        val token = userDatasource.registerUser(registerData)
//
//        tokenDatasource.saveToken(token)
//
        Log.d("TOKKKKKEEEEEENNNN111", tokenDatasource.getToken()!!)
    }
}