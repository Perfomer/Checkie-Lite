package group.bakemate.core.local.api

interface TokenDatasource {

    suspend fun saveToken(token: String)

    suspend fun getToken(): String?
}