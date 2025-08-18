package com.eduardo.webappclass.adapter.controller.exception

import com.eduardo.webappclass.domain.exception.AmmoClipException
import com.eduardo.webappclass.domain.exception.IncompatiblePistolCaliberException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.IllegalArgumentException
import java.util.stream.Collectors

@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(AmmoClipException::class)
    fun handleAmmoClipException(exception: AmmoClipException, request: HttpServletRequest): ResponseEntity<ApiErrorInfo> {
        val apiErrorInfo = ApiErrorInfo(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.toString(),
            exception.message ?: "unknown error",
            request.requestURI,
            emptyList<String>()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorInfo)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
        fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
        ): ResponseEntity<ApiErrorInfo> {
            val errorList = exception
                .bindingResult
                .fieldErrors
                .stream()
                .map { fieldError -> "${fieldError.field}: ${fieldError.defaultMessage}"}
                .collect(Collectors.toList())

        val apiErrorInfo = ApiErrorInfo(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.reasonPhrase,
            "fields validation error",
            request.requestURI,
            errorList,
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorInfo)
        }

       @ExceptionHandler(IncompatiblePistolCaliberException::class)
       fun handleIncompatiblePistolCaliberException(
           exception: IncompatiblePistolCaliberException,
           request: HttpServletRequest
       ): ResponseEntity<ApiErrorInfo> {

           val apiErrorInfo = ApiErrorInfo(
               HttpStatus.BAD_REQUEST.value(),
               HttpStatus.BAD_REQUEST.toString(),
               exception.message ?: "unknown error",
               request.requestURI,
               emptyList<String>()
           )

           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorInfo)
       }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exception: RuntimeException, request: HttpServletRequest): ResponseEntity<ApiErrorInfo> {
        val apiError: ApiErrorInfo = ApiErrorInfo(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.reasonPhrase,
            exception.message ?: "unknown error",
            request.requestURI,
            emptyList<String>()
        )
        return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun haldleIllegalArgumentException(exception: IllegalArgumentException, request: HttpServletRequest): ResponseEntity<ApiErrorInfo> {
        val apiError = ApiErrorInfo(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.reasonPhrase,
            exception.message ?: "unknown error",
            request.requestURI,
            emptyList<String>()
        )

        return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception, request: HttpServletRequest): ResponseEntity<ApiErrorInfo> {
        val apiError = ApiErrorInfo(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            exception.message ?: "Server error",
            request.requestURI,
            emptyList<String>()
        )
        exception.printStackTrace()

        return ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}