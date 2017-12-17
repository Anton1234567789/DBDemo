package mypackage;

import mypackage.db.DBException;
import mypackage.db.dao.mysql.MySQLDAOFactory;

import java.sql.SQLException;

public class Runner {

    public static void main(String[] args) throws SQLException, DBException {
//        DBManager dbManager = DBManager.getInstance();
//
//        User user = new User();
//        user.setLogin("client3");
//        user.setPassword("client3");
//
//        System.out.println();
////        dbManager.createUser(user);
//        dbManager.getAllUsers().forEach(System.out::println);
//        System.out.println();
////        System.out.println("before delete user");
////        System.out.println(dbManager.delete(4));
////        System.out.println("after delete user");
////        dbManager.getAllUsers().forEach(System.out::println);
//
//        System.out.println("after update user");
//        System.out.println(dbManager.update("odmen", 3));
//        dbManager.getAllUsers().forEach(System.out::println);

        MySQLDAOFactory mySQLDAOFactory = new MySQLDAOFactory();
        mySQLDAOFactory.getUsersDAO().findAllUsers().forEach(System.out::println);
    }
}
