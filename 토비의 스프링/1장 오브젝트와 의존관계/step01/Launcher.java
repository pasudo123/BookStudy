package tobi_spring.chapter1.step1;

import java.sql.SQLException;

public class Launcher {
	
	/**
	 * main() 메소드를 만들고 
	 * UserDAO 오브젝트를 생성해서, add() 와 get() 메소드를 검증
	 * 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * **/
	
	public static void main(String[]args) throws ClassNotFoundException, SQLException{
		UserDao dao = new UserDao();
		
		User user = new User();
		user.setId("pasudo");
		user.setName("파스도");
		user.setPassword("pasudopass");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
	}
}
