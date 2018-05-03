package tobi_spring.chapter1.step9;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoConnectionCouningTest {
	public static void main(String[]args){
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		/** Dao 사용 코드 **/
		// -- Code
		
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());
	}
}
