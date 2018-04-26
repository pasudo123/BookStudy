# 오브젝트와 의존관계
 
## Step 1
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

## Step 2
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

## Step 3
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

## Step 4
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

## Step 5
#### 인터페이스의 도입
인터페이스를 도입함으로써, 클래스가 특정 클래스에 종속되는 관계를 종속적이지 않도록 즉, 결합도를 느슨하게 낮출 수 있다. 이러한 과정을 통해서 해당 인터페이스 구현체를 이용함으로써 특정 클래스의 메소드에 대한 기능을 명확히 알 필요가 없다.
~~~
import java.sql.Connection;
import java.sql.SQLException;

/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
 * 
 * [ 인터페이스의 도입  ]
 * 클래스가 하나의 클래스에 종속되는 문제를 해결하기 위해서 등장
 * 클래스끼리의 결합도를 낮추기 위함이며 더불어서 해당 클래스가 
 * 특정 인터페이스의 구현체를 이용함으로써 사용 클래스에 메소드 
 * 기능을 알지 않아도 된다는 것
 * 
 * 단지 인터페이스를 통해서 필요한 기능을 사용하면 되는 것이다.
 * 
 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
public interface ConnectionMaker {
	public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
~~~

#### 인터페이스를 상속받는 각각의 클래스
인터페이스를 상속받는 클래스들은 자신이 맡은 역할에 대한 기능을 명세하고 있다.
~~~
public class DConnectionMaker implements ConnectionMaker{

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
public class NConnectionMaker implements ConnectionMaker{

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
~~~

#### 해당 인터페이스를 호출하는 클래스
~~~
public class UserDao {
	
	ConnectionMaker connectionMaker;
	
	public UserDao(){
		connectionMaker = new DConnectionMaker();
	}
	
	// 이하 내용 생략
	
}
~~~

## Step 6
#### 관계설정 책임의 분리
Step5 에서의 UserDao 클래스를 살펴보면 UserDao 클래스는 여전히 구체적인 클래스를 알고있어야만 인터페이스 구현체를 만들 수 있다. 이 때문에 인터페이스를 이용한 분리에도 불구하고 UserDao 변경 없이는 DB커넥션 기능의 확장이 자유롭지 못하다. UserDao 내부에 있는 커넥션 기능이 따로 분리되어야 한다. 기능의 확장이 자유롭지 못하다. UserDao와 UserDao 가 사용할 ConnectionMaker의 특정 구현 클래스 사이의 관계를 설정해주는 것에 관한 관심사이며 이 관심사를 담은 코드를 UserDao에서 분리하지 않으면 UserDao는 결코 독립적으로 확장 가능한 클래스가 될 수 없다.  

따라서 UserDao 와 ConnectionMaker 인터페이스를 연결해주기 위한 하나의 클래스를 만들어준다. UserDaoTest 클래스이다. UserDao의 클라이언트에서 UserDao를 사용하기 이전에, 먼저 UserDao가 어떤 ConnectionMaker의 구현 클래스를 사용할 지 결정하도록 하는 클래스인 것이다. __오브젝트와 오브젝트 사이의 관계를 설정해주는 것__ 클래스와 클래스 간의 관계가 아니다.  

#### UserDao 클라이언트
~~~
public class UserDao {
	
	ConnectionMaker connectionMaker;
	
	public UserDao(ConnectionMaker connectionMaker){
		this.connectionMaker = connectionMaker;
	}
	
	// 이하 내용 생략
	
}
~~~
  
#### UserDaoTest 클래스
~~~
import java.sql.SQLException;

public class UserDaoTest {
	public static void main(String[]args) throws ClassNotFoundException, SQLException{
		
		/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * 
		 * UserDao 가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
		 * 
		 * UserDaoTest 는 런타임 의존관계를 설정하는 책임을 담당한다. 따라서 특정 
		 * ConnectionMaker 구현 클래스의 오브젝트를 만들고, UserDao 생성자 파라미터에 
		 * 넣어 두 개의 오브젝트를 연결해준다. 그리고 자기 책임이던 UserDao에 대한 테스트 작업을 실시한다.
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
		
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		UserDao userDao = new UserDao(connectionMaker);
	}
}
~~~

## Step 7 : 제어의 역전(IoC)
#### 제어의 역전
앞선 UserDaoTest 클래스에는 기존에 UserDao 가 직접 담당하던 기능, 즉 어떤 ConnectionMaker 구현 클래스를 사용할지를 결정하는 기능을 맡게되었다. 이부분에서 UserDao가 ConnectionMaker 인터페이스를 구현한 특정 클래스로부터 완벽하게 독립할 수 있도록 UserDao 클라이언트인 UserDaoTest가 그 수고를 담당하게 되었다. 하지만 __UserDaoTest__ 는 진짜 __TEST 를 위한 클래스__ 이기 때문에 다시 관심사를 분리해줄 필요가 있다.  
    
분리될 기능은 UserDao 와 ConnectionMaker 구현 클래스의 오브젝트를 만드는 것과, 그렇게 만들어진 두 개의 오브젝트가 연결되어 사용될 수 있도록 관계를 맺어주는 것이다.  
#### 오브젝트 팩토리 : 팩토리
~~~
public class DaoFactory {
	public UserDao userDao(){
		
		/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * 
		 * [팩토리 메소드] 는 UserDao 타입의 오브젝트를 
		 * (1) 어떻게 만들고
		 * (2) 어떻게 준비시킬지
		 * 결정한다.
		 * 
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
		
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao userDao = new UserDao(connectionMaker);
		
		return userDao;
	}
}
~~~

#### 팩토리 메소드를 사용하는 UserDaoTest
~~~
public class UserDaoTest {
	public static void main(String[]args) throws ClassNotFoundException, SQLException{
		
		/**
		 * 팩토리를 사용하도록 만듦
		 * **/
		
		UserDao dao = new DaoFactory().userDao();
	}
}
~~~

__DaoFactory__ 를 사용함으로써 얻는 이점은 아래와 같다.   
* __애플리케이션 컴포넌트 역할을 하는 오브젝트__ & __애플리케이션 구조를 결정하는 오브젝트__ 를 분리

#### 오브젝트 팩토리의 활용
Dao 클래스에 따른 코드의 반복
~~~
public class DaoFactory {
	
	/**ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 * 
	 * Dao 클래스에 따른 코드의 반복
	 * 
	 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ**/
	
	public UserDao userDao(){
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao userDao = new UserDao(connectionMaker);
		
		return userDao;
	}
	
	public AccountDao accountDao(){
		ConnectionMaker connectionMaker = new DConnectionMaker();
		AccountDao accountDao = new AccountDao(connectionMaker);
		
		return accountDao;
	}
	
	public MessageDao messageDao(){
		ConnectionMaker connectionMaker = new DConnectionMaker();
		MessageDao messageDao = new MessageDao(connectionMaker);
		
		return messageDao;
	}
}
~~~
   
__해결책__ 은 Connection Maker 를 생성하는 메소드를 분리시킨다.
~~~
public class DaoFactory {
	public UserDao userDao(){
		UserDao userDao = new UserDao(connectionMaker());
		
		return userDao;
	}
	
	// 이하 생략
	
	/**
	 * 메소드만 분리
	 * **/
	
	private ConnectionMaker connectionMaker(){
		return new DConnectionMaker();
	}
}
~~~

## Step 8 : 스프링IoC
#### DaoFactory 수정
DaoFactory를 스프링의 빈 팩토리, 즉 애플리케이션 컨텍스트가 사용할 수 있도록 애노테이션을 달아준다.
* 스프링이 빈 팩토리를 위한 오브젝트 설정을 담당하는 클래스라고 인식할 수 있도록 @Configuration 애노테이션 추가
* 오브젝트를 만들어주는 메소드에는 @Bean 애노테이션 추가
~~~
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration	// 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정 정보
public class DaoFactory {
	
	@Bean // 오브젝트 생성을 담당하는 IoC용 메소드
	public UserDao userDao(){
		UserDao userDao = new UserDao(connectionMaker());
		
		return userDao;
	}

	@Bean
	private ConnectionMaker connectionMaker(){
		return new DConnectionMaker();
	}
}
~~~
   
#### UserDaoTest 수정
getBean() 메소드의 파라미터 __첫번째 인자__ 와 __두번째 인자__ 설명. 
* "userDao" 는 ApplicationContext에 등록된 빈의 이름이다. DaoFactory에서 __@Bean이라는 애노테이션을 userDao 라는 이름의 메소드에 붙였기 때문에__ userDao라는 이름의 빈을 가져오는 것은 DaoFactory에 userDao() 메소드를 호출해서 그 결과를 가져오는 것이다.
* getBean() 메소드는 기본적으로 Object 타입을 리턴하게 되어 있다. 따라서 매번 리턴되는 오브젝트를 캐스팅해줘야 하는 부담이 존재.따라서 두번째 파라미터에 해당 __리턴타입__ 을 넣어줌으로써, 지저분한 코딩을 하지 않아도 된다.

~~~
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
	public static void main(String[]args) throws ClassNotFoundException, SQLException{
		
		/**
		 * Dao 설정정보를 사용하는 애플리케이션 컨텍스트 생성하며,
		 * @Configuration 애노테이션이 붙은 자바 코드의 설정 정보를 사용하기 위해 "AnnotationConfigApplicationContext"를 이용
		 * **/
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
	}
}
~~~

#### IoC vs Spring IoC 비교
* 오브젝트 팩토리에 대응되는 것이 스프링의 애플리케이션 컨텍스트
* 스프링에서는 이 애플리케이션 컨텍스트를 IoC 컨텍스트 / 스프링 컨테이너 / 빈 팩토리 라고 지칭한다.
#### 애플리케이션 컨텍스트를 사용했을 때 얻을 수 있는 장점
* __클라이언트는 구체적인 팩토리 클래스를 알 필요가 없다.__ 그리고 필요할 때마다 팩토리 오브젝트를 생성해야 하는 번거로움에서 벗어날 수 있다. 일관된 방식으로 원하는 오브젝트를 가져올 수 있는 장점이 존재하며 XML 처럼 단순한 방법을 사용해 애플리케이션 컨텍스트가 사용할 IoC 설정정보를 만들 수 있다.
* __애플리케이션 컨텍스트는 종합 IoC 서비스를 제공__ 오브젝트가 만들어지는 방식과 시점 그리고 전략을 다르게 가져갈 수 있고 이에 부가적으로 자동생성, 오브젝트에 대한 후처리, 정보와 조합, 설정방식의 다변화, 인터셉팅 등 오브젝트를 효과적으로 활용할 수 있는 다양한 기능을 제공
* __애플리케이션 컨텍스트는 빈을 검색하는 다양한 방법을 제공__ 애플리케이션 컨텍스트의 getBean()메소드는 빈의 이름을 이용해 빈을 찾을 수 있게 도와준다. 타입만으로 빈을 검색하거나 특별한 애노테이션 설정이 되어있는 빈을 찾을 수 있다.
