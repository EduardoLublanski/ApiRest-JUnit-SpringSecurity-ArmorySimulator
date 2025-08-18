package com.eduardo.webappclass.adapter.controller.exception

data class ApiErrorInfo(
     val status: Int,
     val error: String,
     val messasge: String,
     val path: String,
     val errors: List<String>,
     val timestamp: Long = System.currentTimeMillis()
)

