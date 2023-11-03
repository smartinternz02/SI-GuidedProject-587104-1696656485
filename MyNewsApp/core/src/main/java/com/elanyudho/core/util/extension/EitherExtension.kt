package com.project.core.util.extension

import com.project.core.util.vo.Either

inline fun <L, R> Either<L, R>.onError(error : (L) -> Unit) : Either<L, R> {
    if (this is Either.Error) {
        error(this.failure)
    }
    return this
}

inline fun <L, R> Either<L, R>.onSuccess(success : (R) -> Unit) : Either<L, R> {
    if (this is Either.Success) {
        success(this.body)
    }
    return this
}