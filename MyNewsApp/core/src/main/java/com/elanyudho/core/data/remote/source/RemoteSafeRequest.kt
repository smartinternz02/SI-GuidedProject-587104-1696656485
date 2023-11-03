package com.project.core.data.remote.source


import com.project.core.util.exception.Failure
import com.project.core.util.extension.hasEmptyBody
import com.project.core.util.extension.isTotallySuccess
import com.project.core.util.vo.Either
import com.project.core.util.vo.RequestResults
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class RemoteSafeRequest {

    open suspend fun <T> request(apiCall: suspend () -> Response<T>) : Either<Failure, T> {
        return try {
            val response = apiCall.invoke()

            when {
                response.isTotallySuccess() -> Either.Success(response.body()!!)
                response.hasEmptyBody() -> parseError(Failure((RequestResults.DATA_NOT_MATCH), Throwable("Empty Body")))
                //response.code() in 300..599 -> parseError(Failure(RequestResults.THERE_IS_ERROR, Throwable("\"[${response.code()}] - [${response.message()}]\""),response.code().toString()))
                response.code() in 300..599 -> {
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorResponse)
                    parseError(Failure(RequestResults.THERE_IS_ERROR, Throwable(errorMessage), response.code().toString()))
                }
                else -> parseError(Failure(RequestResults.UNKNOWN_ERROR, Throwable("\"Unknown error from server\"")))
            }
        } catch (throwable: Throwable) {
            when(throwable) {
                is UnknownHostException -> parseError(Failure(RequestResults.SERVER_ERROR,Throwable("No Internet Connection")))
                is ConnectException -> parseError(Failure(RequestResults.SERVER_ERROR,throwable))
                is SocketTimeoutException -> parseError(Failure(RequestResults.TIMEOUT,throwable))
                else -> parseError(Failure(RequestResults.UNKNOWN_ERROR,throwable))
            }
        }
    }

    private fun parseError(failure: Failure) : Either.Error<Failure> = Either.Error(failure)

    private fun extractErrorMessage(errorResponse: String?): String {
        return try {
            val jsonObject = JSONObject(errorResponse ?: "")
            jsonObject.getString("message")
        } catch (e: JSONException) {
            ""
        }
    }
}