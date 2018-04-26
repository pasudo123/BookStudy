package tobi_spring.chapter1.step6;

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
