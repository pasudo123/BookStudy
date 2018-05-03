package tobi_spring.chapter1.step7;

public class DaoFactory {
	public UserDao userDao(){
		UserDao userDao = new UserDao(connectionMaker());
		
		return userDao;
	}
	
	// 이하 생력
	
	/**
	 * 메소드만 분리
	 * **/
	
	private ConnectionMaker connectionMaker(){
		return new DConnectionMaker();
	}
}
