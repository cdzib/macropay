package com.cesar.examenmacropay.data.source

import com.cesar.examenmacropay.data.Result
import com.cesar.examenmacropay.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            if(username == "cdzib238@gmail.com" && password == "cesardzib2024") {
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Cesar Dzib")
                return Result.Success(fakeUser)
            }else{
                return Result.Error(IOException("Error logging in"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}