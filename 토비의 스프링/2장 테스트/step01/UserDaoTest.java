package tobi_spring.chapter2.step1;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
	public static void main(String[]args) throws ClassNotFoundException, SQLException{
		
		/**
		 * Dao 설정정보를 사용하는 애플리케이션 컨텍스트 생성하며,
		 * @Configuration 애노테이션이 붙은 자바 코드의 설정 정보를 사용하기 위해 "AnnotationConfigApplicationContext"를 이용
		 * **/
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("apple");
		user.setName("apple");
		user.setPassword("applepass");
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 성공");

		User user2 = dao.get(user.getId());
		
		if(!user.getName().equals(user2.getName())){
			System.out.println("테스트 실패 (name)");
		}
		else if(!user.getPassword().equals(user2.getPassword())){
			System.out.println("테스트 실패 (password)");
		}
		else{
			System.out.println("조회 테스트 성공");
		}
	}
}
