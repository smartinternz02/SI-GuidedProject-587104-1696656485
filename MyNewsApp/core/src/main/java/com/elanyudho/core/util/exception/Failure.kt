package com.aarush.core.util.exception

import com.aarush.core.util.vo.RequestResults

data class  Failure(val requestResults: RequestResults, val throwable: Throwable, val code:String="")