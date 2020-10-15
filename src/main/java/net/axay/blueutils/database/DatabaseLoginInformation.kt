package net.axay.blueutils.database

data class DatabaseLoginInformation(
        val host: String,
        val port: Int,
        val database: String,
        val user: String,
        val password: String
) {
    companion object {
        val NOTSET_DEFAULT get() = DatabaseLoginInformation("notset", 12345, "notset", "notset", "notset")
    }
}