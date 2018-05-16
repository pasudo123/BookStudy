# 서비스 추상화
지금까지 만든 DAO 트랜잭션을 적용하여 스프링이 어떻게 성격이 비슷한 여러 종류의 기술을 추상화하고 이를 일관된 방법으로 사용할 수 있도록 지원하는지 알아보자.

## 사용자 레벨 관리 기능 추가
지금까지 만들었던 UserDao 를 다수의 회원이 가입할 수 있는 인터넷 서비스의 사용자 관리 모듈에 적용한다고 생각한다면, 사용자 관리 기능에는 단지 정보를 넣고 검색하는 것외에도 정기적으로 사용자의 활동내역을 참고해서 레벨을 조정해주는 기능이 필요하다.
* 사용자 레벨은 BASIC, SILVER, GOLD 세 가지 중 하나
* 사용자가 처음 가입하면 BASIC 레벨이 되며, 이후 활동에 따라서 한 단계씩 업그레이드될 수 있다.
* 가입 후 50회 이상 로그인을 하면 BASIC에서 SILVER 레벨이 된다.
* 사용자 레벨의 변경 작업은 일정한 주기를 가지고 일괄적으로 진행된다. 변경 작업 전에는 조건을 충족하더라도 레벨의 변경이 일어나지 않는다.

#### 필드 추가
__정수형 상수값으로 정의한 사용자 레벨__
~~~
class User {
	private static final int BASIC = 1;
	private static final int SILVER = 2;
	private static final int GOLD = 3;
	
	int level;
	
	public void setLevel(int level){
		this.level = level;
	}
}
~~~  
  
__사용자 레벨 상수 값을 이용한 코드__
BASIC, SILVER, GOLD 처럼 의미 있는 상수를 정의해놨기 때문에 아래와 같이 리턴되는 숫자값을 사용하면 된다.
~~~
if(user1.getLevel() == User.BASIC){
	user1.setLevel(User.SILVER);
}
~~~
여기서 __문제__는 level 타입이 int 이기 때문에 다음처럼 다른 종류의 정보를 넣는 실수를해도 __컴파일러가 체크하지 못한다는 점__이다. 우연히 getSum() 메소드가 1, 2, 3,과 같이 값을 돌려주면 기능은 문제없이 돌아가겠지만 사실 레벨은 엉뚱하게 바뀌는 심각한 버그가 만들어진다.
~~~
user1.setLevel(other.getSum()); // 다른 int 형 값 반환

user1.setLevel(1000); // 레벨의 범위보다 큰 숫자
~~~
따라서, 숫자타입을 직접 사용하는 것보단 __enum 을 통해 관리__ 하는 것이 안전하다.
~~~
public enum Level {
	BASIC(1), SIVER(2), GOLD(3);

	private final int value;

	Level(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public static Level valueOf(int value){
        switch(value){
        case 1:return BASIC;
        case 2:return SIVER;
        case 3:return GOLD;
        default:throw new AssertionError("Unknown value : " + value);
        }
	}
}
~~~
위와 같이 관리하게 된다면, Level enum은 내부에는 DB에 저장할 int 타입의 값을 갖고 있지만, 겉으로는 Level 타입의 오브젝트이기 때문에 안전하게 사용할 수 있다. user1.setLevel(1000)과 같은 코드는 컴파일러가 타입이 일치하지 않는다고 에러를 알려주기 때문이다.

