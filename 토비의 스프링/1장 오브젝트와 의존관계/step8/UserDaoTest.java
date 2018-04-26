package tobi_spring.chapter1.step8;

import java.sql.SQLException;

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
