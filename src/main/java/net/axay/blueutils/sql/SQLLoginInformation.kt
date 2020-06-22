package net.axay.blueutils.sql

data class SQLLoginInformation(
        val host: String,
        val port: Int,
        val user: String,
        val password: String,
        val database: String
)