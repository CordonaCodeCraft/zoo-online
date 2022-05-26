package tech.cordona.zooonline.security.dto

import tech.cordona.zooonline.domain.user.entity.Authority

data class JwtTokenInfo(val id: String, val email: String, val authority: Authority)