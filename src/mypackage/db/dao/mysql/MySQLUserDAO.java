package mypackage.db.dao.mysql;

import mypackage.db.DBException;
import mypackage.db.Constants;
import mypackage.db.dao.DAOFactory;
import mypackage.db.dao.UserDAO;
import mypackage.db.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDAO implements UserDAO {
    @Override
    public User findUserById(Integer userId) {
        return null;
    }

    @Override
    public List<User> findAllUsers() throws DBException {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        Statement statement= null;
        ResultSet rs= null;
        try {
            connection = DAOFactory.getConnection();

            statement = connection.createStatement();

            rs = statement.executeQuery(Constants.SQL_GET_ALL_USERS);
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
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));

        return user;

    }
    private void close(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public User findUserByLogin(String login) {
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DAOFactory.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_FIND_USER_BY_LOGIN);
            int l = 1;
            preparedStatement.setString(l++, login);
            rs = preparedStatement.executeQuery();

            if (rs.next()){
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createUser(User user) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet rs= null;
        try {
            connection = DAOFactory.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
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

    @Override
    public boolean deleteUser(int userId) throws DBException {
        boolean res = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DAOFactory.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_DELETE_USER);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Sorry, we don't delete user with id: " + userId, e);
        } finally {
            close(connection);
        }

        return res;
    }

    @Override
    public boolean updateUser(String login, int userId) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DAOFactory.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_UPDATE_USER);
            preparedStatement.setString(1, login);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Sorry, we don't update user with id: " + userId, e);
        } finally {
            close(connection);
        }

        return res;
    }
}
