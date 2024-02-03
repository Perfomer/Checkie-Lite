package group.bakemate.core.local.preferences.datasource

import android.content.Context
import group.bakemate.core.local.api.TokenDatasource

internal class TokenDatasourceImpl(context: Context) : TokenDatasource {

    private val preferences = context.getSharedPreferences("group.bakemate", Context.MODE_PRIVATE)

    override suspend fun saveToken(token: String) {
        preferences.edit().putString(TOKEN, token).apply()
    }

    override suspend fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    private companion object {
        private const val TOKEN = "token"
    }
}