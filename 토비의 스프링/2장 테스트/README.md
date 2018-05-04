# 테스트
스프링이 개발자에게 제공하는 가장 중요한 가치는 토비의 스프링에 의하면 __객체지향__ 과 __테스트__ 라고 한다. 스프링의 핵심인 IoC와 DI는 오브젝트의 설계와 생성, 관계, 사용에 관한 기술이다. 스프링은 IoC/DI를 이용해 객체지향 프로그래밍 언어의 근본과 가치를 개발자가 손쉽게 적용하고 사용할 수 있게 도와주는 기술이다. 그리고 이를 개발하는데 필요한 도구가 __객체지향__ 기술이고 또 하나의 도구는 __테스트__ 이다.    
    
테스트는 스프링을 학습하는데 있어 가장 효과적인 방법의 하나이다. 테스트의 작성은 스프링의 다양한 기술을 활용하는 방법을 이해하고 검증하고, 실전에 적용하는 방법을 익히는 데 효과적으로 사용될 수 있다. 


## Step 1
#### 테스트 유용성   
기존에 짯던 코드를 살펴보자.
~~~
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
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + " 조회 성공");
	}
}
~~~
위의 UserDaoTest 코드를 살펴보면 다음과 같이 정리할 수 있다.
* 자바에서 가장 손쉽게 실행 가능한 main() 메소드를 이용한다.
* 테스트할 대상인 __UserDao의 오브젝트__ 를 가져와 메소드를 호출한다.
* 테스트에 사용할 입력 값 User 오브젝트를 직접 코드에서 만들어 넣어준다.
* 테스트의 결과를 콘솔로 출력해준다.
* 각 단계의 작업이 에러 없이 끝나면 콘솔에 성공 메시지를 출력한다.

위의 정리된 내용에서 main() 스레드, 메인 메소드를 통해서 테스트를 수행했다는 점과 테스트할 대상인 UserDao를 직접 호출해서 사용한다는 점이다.   

#### 웹을 통한 DAO 테스트 방법의 문제점
웹 개발에서 테스트를 진행한다면 Service Layer - MVC Presentation Layer 까지 포함한 모든 입출력 기능을 만든 이후 실행을 한다고 가정하자. 이러한 실행 속에서 에러가 발생하면 이후 어느 지점에서 에러가 발생하였는지 캐치해내기가 매우 번거롭다. DB 연결방법, DAO 코드 문제, JDBC API 의 잘못된 호출 등 수많은 상황을 고려할 수 있다. 여기서 효율적인 테스트 방법을 알아보아야 한다.

#### 작은 단위의 테스트
테스트하고자 하는 대상이 명확하다면, 그 대상에만 집중해서 테스트를 실시할 수 있도록 하여야 한다. 한번에 너무 많은 것을 몰아서 테스트하면 테스트 수행 과정이 복잡해지는 것은 물론이고 문제점이 발생한 경우 정확한 원인을 찾기 어렵다. 테스트의 관심이 다르다면 테스트할 대상을 분리하고 집중해서 접근해야 한다. __단위의 개념은__ 딱 정해진 것은 아니며 사용자 관리 기능을 통틀어서 지칭할 수도 하나의 작은 메소드를 지칭할 수도 있다. __하나의 관심에 집중해서 효율적으로 테스트할 만한 범위의 단위__ 라고 보는 것이 올바르다.   
   
#### UserDaoTest 의 문제점
* 수동 확인 작업의 번거로움   
   
사람이 직접 콘솔에 나오는 값을 확인하고 비교하기 때문에 완전히 자동화되었다고 보기 어렵다.
* 실행 작업의 번거로움   
   
main() 를 매번 실행하는 것은 번거롭다. 좀 더 체계적으로 테스트를 실행하고 그 결과를 확인하는 방법이 필요하다.

## Step 2
## UserDaoTest 개선
## 테스트가 이끄는 개발
테스트할 코드도 안 만들어놓고 테스트 코드부터 만드는 것은 좀 이상할지 모르지만 이러한 개발 방법을 적극적으로 권장하고 실제로 사용되고 있다. 
