package net.axay.blueutils.sql;

import org.jetbrains.annotations.NotNull;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Deprecated
public class MariaDBJava implements Closeable, AutoCloseable {

    private MariaDbPoolDataSource pool;

    private final DatabaseLoginInformation databaseLoginInformation;

    public MariaDBJava(@NotNull DatabaseLoginInformation databaseLoginInformation) {
        this.databaseLoginInformation = databaseLoginInformation;
    }

    /**
     * This method opens a new "connection" to the database.
     */
    public void connect() {

        try {

            pool = new MariaDbPoolDataSource();

            pool.setUser(databaseLoginInformation.getUser());
            pool.setServerName(databaseLoginInformation.getHost());
            pool.setPort(databaseLoginInformation.getPort());
            pool.setPassword(databaseLoginInformation.getPassword());
            pool.setDatabaseName(databaseLoginInformation.getDatabase());

            pool.setMaxPoolSize(32);
            pool.setMinPoolSize(8);

            System.out.println("Successfully set up connection pool to " + databaseLoginInformation.getHost() + ":" + databaseLoginInformation.getPort());

        } catch (SQLException e) {
            System.out.println("Failed to set up connection pool to " + databaseLoginInformation.getHost() + ":" + databaseLoginInformation.getPort());
            e.printStackTrace();
        }

    }

    public MariaDbPoolDataSource getPool() {
        return pool;
    }

    /**
     * Closes the connection pool.
     * This method should be invoked.
     */
    @Override
    public void close() {
        if (pool != null) pool.close();
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
    @NotNull
    public ResultSet executePreparedStatement(String sql, Object... parameters)
            throws SQLException, IllegalStateException, IllegalArgumentException {

        if (pool == null) throw new IllegalStateException("Cannot execute prepared statement. Call connect() first!");

        try (
                Connection connection = getPool().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            int i = 1;
            for (Object parameter : parameters) {
                try {
                    preparedStatement.setObject(i, parameter);
                } catch (Exception exc) {
                    throw new IllegalArgumentException("An object of the type " + parameter.getClass().getName() + " cannot be set as a parameter of the prepared statement!");
                }
                i++;
            }

            return preparedStatement.executeQuery();

        }

    }

    /**
     * @return This will return true if the connection is still open.
     * This method won't validate the connection.
     * For that see {@link #validate(Connection, int)}
     */
    public static boolean isConnected(Connection connection) {

        try {

            if (connection == null) {
                return false;
            } else {
                return !connection.isClosed();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * This method will validate the connection with
     * the given timeout amount.
     * @param timeout the amount of time in seconds the connection
     *                has time to validate itself
     * @return This will return true if the connection is still open.
     */
    public static boolean validate(Connection connection, int timeout) {

        try {

            if (!isConnected(connection)) {
                return false;
            } else {
                return connection.isValid(timeout);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

}