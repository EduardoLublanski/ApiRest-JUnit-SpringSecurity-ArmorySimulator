package com.eduardo.webappclass.adapter.controller.dto

import com.eduardo.webappclass.domain.util.Role
import com.fasterxml.jackson.annotation.JsonProperty

data class RoleDto(@field: JsonProperty("role") val value: Role)
