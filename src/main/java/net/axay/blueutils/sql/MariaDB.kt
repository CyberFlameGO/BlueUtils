package net.axay.blueutils.sql

import org.mariadb.jdbc.MariaDbPoolDataSource
import java.sql.ResultSet
import java.sql.SQLException

class MariaDB(private val databaseLoginInformation: DatabaseLoginInformation) : AutoCloseable {

    private var pool: MariaDbPoolDataSource? = null

    fun connect() {
        try {

            MariaDbPoolDataSource().let {

                it.user = databaseLoginInformation.user
                it.serverName = databaseLoginInformation.host
                it.port = databaseLoginInformation.port
                it.setPassword(databaseLoginInformation.password)
                it.databaseName = databaseLoginInformation.database
                it.maxPoolSize = 32
                it.minPoolSize = 8

                pool = it

            }

            println("Successfully set up connection pool to " + databaseLoginInformation.host + ":" + databaseLoginInformation.port)

        } catch (e: SQLException) {
            println("Failed to set up connection pool to " + databaseLoginInformation.host + ":" + databaseLoginInformation.port)
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
     * @throws SQLException if the statement could not be executed
     * @throws IllegalArgumentException if the type of a parameter
     * cannot be used in a sql statement
     * @throws IllegalStateException if the pool is null
     */
    fun executePreparedStatement(sql: String, vararg parameters: Any): ResultSet {

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

        throw IllegalStateException("Cannot execute prepared statement. Call connect() first!")

    }

}