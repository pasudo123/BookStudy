package tobi_spring.chapter1.step4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {
	
	// 아예 DB Connection 을 만드는 클래스를 분리하였기 때문에
	// 추상 클래스로 만들 필요가 없다.
	
	public Connection makeNewConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/daum","root","rootpass");
	
		return c;
	}
}
