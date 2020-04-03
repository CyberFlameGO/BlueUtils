package net.axay.blueutils.sql;

import org.jetbrains.annotations.NotNull;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaDB implements Closeable, AutoCloseable {

    private MariaDbPoolDataSource pool;

    private SQLLoginInformation sqlLoginInformation;

    public MariaDB(SQLLoginInformation sqlLoginInformation) {
        if (sqlLoginInformation != null) {
            this.sqlLoginInformation = sqlLoginInformation;
        } else {
            this.sqlLoginInformation = new SQLLoginInformation();
            NullPointerException nullPointerException = new NullPointerException("The SQL-Login Information is null!");
            nullPointerException.printStackTrace();
        }
    }

    /**
     * This method opens a new "connection" to the database.
     */
    public void connect() {

        try {

            pool = new MariaDbPoolDataSource();

            pool.setUser(sqlLoginInformation.getUser());
            pool.setServerName(sqlLoginInformation.getDatabase());
            pool.setPort(sqlLoginInformation.getPort());
            pool.setPassword(sqlLoginInformation.getPassword());
            pool.setDatabaseName(sqlLoginInformation.getDatabase());

            pool.setMaxPoolSize(32);
            pool.setMinPoolSize(8);

            System.out.println("Successfully setup connection-pool to " + sqlLoginInformation.getHost() + ":" + sqlLoginInformation.getPort());

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to setup connection-pool to " + sqlLoginInformation.getHost() + ":" + sqlLoginInformation.getPort());
        }

    }

    /**
     * @return Returns the SQL pool.
     */
    public MariaDbPoolDataSource getPool() {
        return pool;
    }

    /**
     * Closes the connection pool.
     * This method should be invoked.
     */
    @Override
    public void close() {
        pool.close();
    }

    /**
     * @param sql the sql statement which should be executed; <br>
     *            should be a prepared statement
     * @param parameters the parameters for the prepared statement; <br>
     *                   at the moment this does only support string, int
     * @return the result of the sql statement
     * @throws SQLException if the statement could not be executed
     */
    @NotNull
    public ResultSet executePreparedStatement(String sql, Object... parameters) throws SQLException {

        try (
                Connection connection = getPool().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            int i = 1;
            for (Object parameter : parameters) {

                if (parameter instanceof String) {
                    preparedStatement.setString(i, (String) parameter);
                }
                else if (parameter instanceof Integer) {
                    preparedStatement.setInt(i, (int) parameter);
                }
                else {
                    throw new IllegalArgumentException("The executePreparedStatement() method only allows string and int parameters!");
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