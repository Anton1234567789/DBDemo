package mypackage.db.dao.mysql;

import mypackage.db.dao.DAOFactory;
import mypackage.db.dao.UserDAO;

public class MySQLDAOFactory extends DAOFactory {
    @Override
    public UserDAO getUsersDAO() {
        return new MySQLUserDAO();
    }
}
