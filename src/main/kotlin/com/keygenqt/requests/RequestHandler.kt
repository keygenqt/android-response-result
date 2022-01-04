package com.keygenqt.requests

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object RequestHandler {

    /**
     * [Job] collect for cancel
     */
    private var job: Job? = null

    /**
     * State error handler
     */
    private val STATE: MutableSharedFlow<Exception> = MutableSharedFlow()

    /**
     * Single collect error handler
     */
    suspend fun singleCollect(action: suspend (value: Exception) -> Unit) {
        job?.cancel()
        coroutineScope {
            job = launch {
                STATE.asSharedFlow().collect {
                    action.invoke(it)
                }
            }
        }
    }

    /**
     * Emit error [Exception]
     */
    suspend fun emit(exception: Exception) {
        STATE.emit(exception)
    }

    /**
     * Try emit error [Exception]
     */
    fun tryEmit(exception: Exception) {
        STATE.tryEmit(exception)
    }

    /**
     *  Request handler execute
     */
    suspend inline fun <T> executeRequest(
        emit: Boolean = true,
        crossinline body: suspend () -> T
    ): ResponseResult<T> {
        return try {
            ResponseResult.Success(body.invoke())
        } catch (e: Exception) {
            if (emit) {
                emit(e)
            }
            ResponseResult.Error(e)
        }
    }
}