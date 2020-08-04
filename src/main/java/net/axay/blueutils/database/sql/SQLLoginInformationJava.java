package net.axay.blueutils.database.sql;

@Deprecated
public class SQLLoginInformationJava {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public SQLLoginInformationJava(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public SQLLoginInformationJava() {
        this.host = "host";
        this.port = 3306;
        this.database = "database";
        this.user = "user";
        this.password = "password";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
