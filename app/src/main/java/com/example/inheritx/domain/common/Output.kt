package com.example.inheritx.domain.common


/**
 * Generic class for holding success response, error response and loading status
 */
data class Output<out T>(
    val status: Status? = null,
    val data: T?  = null,
    val error: ApiError?  = null,
    val message: String?  = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        GO_NEXT
    }

    companion object {
        fun <T> success(data: T?): Output<T> {
            return Output(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: ApiError?): Output<T> {
            return Output(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): Output<T> {
            return Output(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}