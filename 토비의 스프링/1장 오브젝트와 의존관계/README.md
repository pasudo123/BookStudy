# 오브젝트와 의존관계
 
## Step1
#### 사용자 정보를 DB에 넣고 관리할 수 있는 DAO 클래스.
~~~
package tobi_spring.chapter1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
	
	/**
	 * [ JDBC 의 일반적인 작업순서   ]
	 * 
	 * (1) DB 연결을 위한 Connection 을 가져온다.
	 * (2) SQL을 담은 Statement 또는 PreparedStatement 를 만든다.
	 * (3) 만들어진 Statement 를 실행한다.
	 * (4) 조회의 경우 SQL 쿼리의 실행결과를 ResultSet으로 받아서 정보를 저장할 오브젝트에 옮겨준다.
	 * (5) 작업 중에 생성된 Connection, Statemen, ResultSet 같은 리소스는 작업을 마친 후 받드시 닫는다.
	 * (6) JDBC API 가 만들어낸 예외 Exception 을 잡아서 처리하거나 혹은 메소드에 throws 선언해서 예외가 발생하면 메소드 밖으로 던지게 한다.
	 * 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * **/
	
	public void add(User user) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/daum","root","rootpass");
		
		PreparedStatement ps = c.prepareStatement("Insert INTO users_tb(id, name, password) VALUES(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/daum","root","rootpass");
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM users_tb WHERE id = ?");
		ps.setString(1,  id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}
}

~~~

## Step2
#### 중복된 코드에 대한 메소드 추출
~~~
private Connection getConnection() throws ClassNotFoundException, SQLException{
	
	/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 * 
	 * 중복된 코드를 독립적인 메소드로 만들어서 관심사를 분리
	 * 따라서 DB 연결이 필요하면 getConnection()메소드를 이용할 수 있도록 설정
	 * 이를 [ 메소드 추출 ] 이라고 부른다.
	 * 
	 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
	Class.forName("com.mysql.jdbc.Driver");
	Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/daum","root","rootpass");

	return c;
}
~~~

## Step3
#### 템플릿 메소드 패턴
슈퍼클래스에서 기본적인 로직의 흐름을 작성하고, 이후에 추상메소드 혹은 오버라이딩 가능한 메소드들은 서브클래스에서 담당하는 기법을 __템플릿 메소드 패턴__ 이라고 부른다.  

__추상 클래스 UserDao__
~~~
public abstract class UserDao {
	
	/**
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * **/
	
	/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 * 구현코드는 제거되고 추상메소드로 변경되었음
	 * 메소드에 대한 구현은 서브클래스에서 담당할 수 있도록 설정
	 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
~~~
__UserDao 를 상속받는 두 개의 클래스__
~~~
public class NUserDao extends UserDao{

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * 
		 * 	  [N] 사 DB Connection 생성 코드
		 * 
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
		return null;
	}

}

ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

public class DUserDao extends UserDao{

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * 
		 * 	  [D] 사 DB Connection 생성 코드
		 * 
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
		return null;
	}

}
~~~

## Step4
#### DAO 확장 및 클래스 분리
클래스를 분리시키고 아예 하나의 인스턴스로써 DB Connection 객체를 관리하기 위함
~~~
package tobi_spring.chapter1.step4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
	
	/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 * 
	 * 	상속관계가 아닌 아예 독립적인 클래스로 분리시키기 위함
	 * 
	 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
	private SimpleConnectionMaker simpleConnectionMaker;
	
	public UserDao(){
		/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * 
		 * 상태를 관리하는 것이 아니기 때문에, 한 번만 인스턴스를 만들어놓고, 
		 * 인스턴스를 변수에 저장하여 메소드해서 사용할 수 있도록 하기 위함
		 * 
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
		simpleConnectionMaker = new SimpleConnectionMaker();
	}
	
	public void add(User user) throws SQLException, ClassNotFoundException{
		Connection c = simpleConnectionMaker.makeNewConnection();
		
		// 이하 내용 생략
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		Connection c = simpleConnectionMaker.makeNewConnection();
		
		// 이하 내용 생략
	}
}
~~~
#### DB Connection 기능을 독립시킨 SimpleConnectionMaker 클래스
아예 클래스를 분리시켜버렸다.
~~~
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
~~~
