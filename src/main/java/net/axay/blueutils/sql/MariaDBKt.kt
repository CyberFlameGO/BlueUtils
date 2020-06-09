package net.axay.blueutils.sql

import org.mariadb.jdbc.MariaDbPoolDataSource
import java.sql.ResultSet
import java.sql.SQLException

class MariaDBKt(private val sqlLoginInformation: SQLLoginInformation) : AutoCloseable {

    private var pool: MariaDbPoolDataSource? = null

    fun connect() {
        try {

            MariaDbPoolDataSource().let {

                it.user = sqlLoginInformation.user
                it.serverName = sqlLoginInformation.host
                it.port = sqlLoginInformation.port
                it.setPassword(sqlLoginInformation.password)
                it.databaseName = sqlLoginInformation.database
                it.maxPoolSize = 32
                it.minPoolSize = 8

                pool = it

            }

            println("Successfully set up connection pool to " + sqlLoginInformation.host + ":" + sqlLoginInformation.port)

        } catch (e: SQLException) {
            println("Failed to set up connection pool to " + sqlLoginInformation.host + ":" + sqlLoginInformation.port)
            e.printStackTrace()
        }
    }

    /**
     * Closes the connection pool.
     * This method should be invoked.
     */
    override fun close() {
        pool?.close()
    }

    /**
     * @param sql the sql statement which should be executed
     * @param parameters the parameters for the prepared statement
     * (in the correct order)
     * @return the result of the sql statement
     * or null if the pool is null
     * @throws SQLException if the statement could not be executed
     */
    fun executePreparedStatement(sql: String, vararg parameters: Any): ResultSet? {

        pool?.let { pool ->

            pool.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->

                for ((index: Int, parameter: Any) in parameters.withIndex()) {
                    try {
                        preparedStatement.setObject(index + 1, parameter)
                    } catch (exc: Exception) {
                        throw IllegalArgumentException("An object of the type ${parameter::class.simpleName} cannot be set as a parameter of the prepared statement!")
                    }
                }

                return preparedStatement.executeQuery()

            } }

        }

        return null

    }

}