package com.softfocus.server.api.util

import java.util.*
import kotlin.collections.ArrayList

class APIErrorResponse {
    var status = 0

    val message: ArrayList<String> = ArrayList<String>()
    var timeStamp: Date = Date()

}