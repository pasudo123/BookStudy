package tobi_spring.chapter1.step8;

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
