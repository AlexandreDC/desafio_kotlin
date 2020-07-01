package com.softfocus.server.api.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ItemExceptionHandler {
    @ExceptionHandler
    fun handleException(ine: APIException): ResponseEntity<APIErrorResponse> {
        val errorResponse = APIErrorResponse()
        errorResponse.status = HttpStatus.NOT_FOUND.value()
        errorResponse.message.add(ine.message.orEmpty())
        return ResponseEntity<APIErrorResponse>(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleException(ex: Exception): ResponseEntity<APIErrorResponse> {
        val errorResponse = APIErrorResponse()
        errorResponse.status = HttpStatus.BAD_REQUEST.value()
        errorResponse.message.add(ex.message.orEmpty())
        return ResponseEntity<APIErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST)
    }
}