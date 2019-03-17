package com.example.demo

import org.springframework.jdbc.core.JdbcTemplate

/*
// なぜかTが解決できない
inline fun <reified T> JdbcTemplate.queryForObject(sql: String): T {
    return queryForObject(sql. T::class.java)
}
*/

