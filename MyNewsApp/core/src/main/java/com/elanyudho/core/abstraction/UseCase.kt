package com.aarush.core.abstraction

import com.aarush.core.util.exception.Failure
import com.aarush.core.util.vo.Either

abstract class UseCase<out Type, in Params> {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    object None

}