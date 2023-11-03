package com.project.core.util.exception

import com.project.core.util.vo.RequestResults

data class  Failure(val requestResults: RequestResults, val throwable: Throwable, val code:String="")