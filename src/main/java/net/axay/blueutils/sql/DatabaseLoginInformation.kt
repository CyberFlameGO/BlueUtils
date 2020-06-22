package net.axay.blueutils.sql

data class DatabaseLoginInformation(
        val host: String,
        val port: Int,
        val database: String,
        val user: String,
        val password: String
)