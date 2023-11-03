package com.project.core.abstraction

import com.project.core.util.exception.Failure
import com.project.core.util.vo.Either

abstract class UseCase<out Type, in Params> {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    object None

}