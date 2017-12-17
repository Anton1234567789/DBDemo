package mypackage.db.dao;

import mypackage.db.DBException;
import mypackage.db.entity.User;

import java.util.List;

public interface UserDAO{
    User findUserById(Integer userId) throws DBException;
    List<User> findAllUsers()throws DBException;
    User findUserByLogin(String login)throws DBException;
    boolean createUser(User user)throws DBException;
    boolean deleteUser(int userId)throws DBException;
    boolean updateUser(String login, int userId)throws DBException;
//    boolean createTwoUser(User user, User user2)throws DBException;

}
