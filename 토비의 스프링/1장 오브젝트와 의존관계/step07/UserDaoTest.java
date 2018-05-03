package tobi_spring.chapter1.step7;

import java.sql.SQLException;

public class UserDaoTest {
	public static void main(String[]args) throws ClassNotFoundException, SQLException{
		
		/**
		 * 팩토리를 사용하도록 만듦
		 * **/
		
		UserDao dao = new DaoFactory().userDao();
	}
}
