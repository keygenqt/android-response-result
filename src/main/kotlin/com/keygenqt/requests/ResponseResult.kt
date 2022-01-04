package com.keygenqt.requests

import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Response processing class
 */
sealed class ResponseResult<out R> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val exception: Exception) : ResponseResult<Nothing>()
}

/**
 * The number of items in the response
 */
val ResponseResult<*>?.size
    get() = if (this != null
        && this is ResponseResult.Success
        && data != null
        && data is List<*>
    ) {
        data.size
    } else if (this != null
        && this is ResponseResult.Success
        && data != null
    ) {
        1
    } else {
        0
    }

/**
 * Checking if the response is empty
 */
val ResponseResult<*>?.isEmpty get() = this.size == 0

/**
 * Checking that the request was successful
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
val ResponseResult<*>?.isSucceeded get() = this != null && this is ResponseResult.Success && data != null

/**
 * Checking that the request was with an error
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
val ResponseResult<*>?.isError get() = this != null && this is ResponseResult.Error

/**
 * Response Result success
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
inline infix fun <T> ResponseResult<T>.success(predicate: (data: T) -> Unit): ResponseResult<T> {
    if (this is ResponseResult.Success && this.data != null) {
        predicate.invoke(this.data)
    }
    return this
}

/**
 * Response Result success null data
 *
 * @since 0.0.10
 * @author Vitaliy Zarubin
 */
inline infix fun <T> ResponseResult<T>.empty(predicate: () -> Unit): ResponseResult<T> {
    if (this is ResponseResult.Success && this.data == null) {
        predicate.invoke()
    }
    return this
}

/**
 * Response Result error
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
inline infix fun <T> ResponseResult<T>.error(predicate: (data: Exception) -> Unit): ResponseResult<T> {
    if (this is ResponseResult.Error) {
        if (this.exception !is UnknownHostException && this.exception !is SocketTimeoutException) {
            predicate.invoke(this.exception)
        }
    }
    return this
}

/**
 * No internet error
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
inline infix fun <T> ResponseResult<T>.errorUnknownHost(predicate: (data: Exception) -> Unit): ResponseResult<T> {
    if (this is ResponseResult.Error) {
        if (this.exception is UnknownHostException) {
            predicate.invoke(this.exception)
        }
    }
    return this
}

/**
 * No internet error
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
inline infix fun <T> ResponseResult<T>.errorTimeout(predicate: (data: Exception) -> Unit): ResponseResult<T> {
    if (this is ResponseResult.Error) {
        if (this.exception is SocketTimeoutException) {
            predicate.invoke(this.exception)
        }
    }
    return this
}

/**
 * End of request for any outcome
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
inline infix fun <T> ResponseResult<T>.done(predicate: () -> Unit): ResponseResult<T> {
    predicate.invoke()
    return this
}