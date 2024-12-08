package com.cnpm.bikerentalapp.config.security

object URL {
    val ADMIN: Array<String> = arrayOf("/api/bike/add", "/api/user/**",
        "/api/bike/delete/**", "/api/bike/update", "/api/station/add",
        "/api/station/delete", "/api/station/update",
        )
}