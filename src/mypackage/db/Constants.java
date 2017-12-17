package mypackage.db;

public final class Constants {

    public static final String SQL_INSERT_USER = "INSERT INTO users (login, password) VALUES (?, ?);";
    public static final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    public static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users u WHERE u.login = ?";
    public static final String SQL_DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    public static final String SQL_UPDATE_USER = "UPDATE users SET login = ? WHERE user_id = ?";

    public static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    public static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/forSql" +
            "?user=testuser&password=testuser&useSSL=false";
    public static final String DAO_FACTORY_CLASS_NAME = "mypackage.MySQLDAOFactory";

}
