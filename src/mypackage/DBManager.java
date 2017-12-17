package mypackage;

import mypackage.db.DBException;
import mypackage.db.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String SQL_INSERT_USER = "INSERT INTO users (login, password) VALUES (?, ?);";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users u WHERE u.login = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    private static final String SQL_UPDATE_USER = "UPDATE users SET login = ? WHERE user_id = ?";

    private static DBManager instance;

    public static synchronized DBManager getInstance(){
        if (instance==null){
            instance = new DBManager();
        }
        return instance;
    }

    public DBManager() {
    }

    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/forSql" +
            "?user=testuser&password=testuser&useSSL=false";


    public List<User> getAllUsers() throws SQLException, DBException {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        Statement statement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();

            statement = connection.createStatement();

            rs = statement.executeQuery(SQL_GET_ALL_USERS);
            while (rs.next()){
                User user = extractUser(rs);
                users.add(user);
            }
            return users;
        }catch (SQLException ex){
            throw new DBException("Sorry we don't return lists users", ex);
        }finally {
            close(connection);
        }

    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(CONNECTION_URL);

        return connection;
    }

    public void close(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));

        return user;

    }

    public User findUserByLogin(String login) throws SQLException {
        Connection connection =getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN);

        int l = 1;
        preparedStatement.setString(l++, login);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            return extractUser(rs);
        }
        return null;
    }

    public boolean createUser(User user) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            preparedStatement.setString(k++, user.getLogin());
            preparedStatement.setString(k++, user.getPassword());

            if (preparedStatement.executeUpdate() > 0){
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    user.setUserId(rs.getInt(1));
                    res = true;
                }
            }

        } catch (SQLException e) {
            throw new DBException("Sorry, please try now create user" + user, e);
        }finally {
            close(connection);
        }

        return res;
    }

    public boolean delete(int id) throws DBException {
        boolean res = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Sorry, we don't delete user with id: " + id, e);
        } finally {
            close(connection);
        }

        return res;
    }

    public boolean update(String login, int id) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            preparedStatement.setString(1, login);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Sorry, we don't update user with id: " + id, e);
        } finally {
            close(connection);
        }

        return res;
    }


    public boolean createTwoUser(User user, User user2) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            res = createUser(user) && createUser(user2);
            connection.commit();

        } catch (SQLException e) {
            throw new DBException("Sorry, you been rollback, because arose problem while creating user: " + user
                    + ", user2: " + user2, e);
        }finally {
            close(connection);
        }

        return res;
    }

}
