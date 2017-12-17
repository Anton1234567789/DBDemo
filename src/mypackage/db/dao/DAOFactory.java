package mypackage.db.dao;

import mypackage.db.Constants;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAOFactory implements Serializable {

    private static DAOFactory instance = null;

    public static synchronized DAOFactory getInstance(){
        if (instance == null){
            try {
                Class.forName(Constants.DRIVER_CLASS_NAME);
                Class claz = Class.forName(Constants.DAO_FACTORY_CLASS_NAME);
                instance = (DAOFactory) claz.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public abstract UserDAO getUsersDAO();

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(Constants.CONNECTION_URL);

        return connection;
    }

}
