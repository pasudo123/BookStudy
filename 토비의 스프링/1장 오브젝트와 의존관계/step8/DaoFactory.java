package tobi_spring.chapter1.step8;

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
