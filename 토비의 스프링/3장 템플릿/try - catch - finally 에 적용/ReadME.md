## 템플릿 / 콜백 메소드 패턴
기존에 아래와 같은 메소드들이 존재,   
템플릿 / 콜백 메소드 패턴을 설명하기 위한 예시이다.

## Step1
#### 단순한 덧셈 메소드  
try-catch 문의 반복
~~~
public Integer calcSum(String filePath) throws IOException{
	
	BufferedReader br = null;
	
	try{
		br = new BufferedReader(new FileReader(filePath));
		Integer sum = 0;
		String line = null;
		
		while((line = br.readLine()) != null){
			sum += Integer.parseInt(line);
		}

		return sum;
	}
	catch(IOException e){
		System.out.println(e.getMessage());
		throw e;
	}
	finally{
		if(br != null){
			try{
				br.close();
			}
			catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
}
~~~

## Step2
#### 콜백 인터페이스
~~~
import java.io.BufferedReader;
import java.io.IOException;

// BufferedReader 를 전달받은 콜백 인터페이스
public interface BufferedReaderCallback {
	Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
~~~

#### 템플릿 메소드
~~~
// 인터페이스 파라미터
public Integer fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException{
	
	BufferedReader br = null;
	
	try{
		br = new BufferedReader(new FileReader(filePath));
		
		/**
		 * BufferedReader 를 만들어서 넘겨주는 것과 그 외의 작업들은 해당 [템플릿] 에서 진행
		 * 준비된 BufferedReader를 이용해 작업을 수행하는 부분은 [콜백] 을 호출해서 처리.
		 * **/
		
		// 콜백 메소드 호출, 콜백의 작업결과를 반환받는다.
		int ret = callback.doSomethingWithReader(br);	
		return ret;
	}// try()
	
	catch(IOException e){
		System.out.println(e.getMessage());
		throw e;
	}// catch()
	
	finally{
		if(br != null){
			try{
				br.close();
			}
			catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}// finally()
}
~~~

#### 덧셈 메소드
~~~
// 덧셈 테스트 메소드
public Integer calcSum(String filePath) throws IOException{
	// 익명 내부 클래스를 해당 파라미터로 전달
	BufferedReaderCallback sumCallback = 
		new BufferedReaderCallback(){
			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
				Integer sum = 0;
				String line = null;
				
				while((line = br.readLine()) != null){
					sum += Integer.valueOf(line);
				}
				
				return sum;
			}
	};
	
	return fileReadTemplate(filePath, sumCallback);
}
~~~
 
#### 곱셈 메소드
~~~
// 곱셈 콜백 메소드
public Integer calcMultiply(String filePath) throws IOException{
	BufferedReaderCallback multiplyCallback = 
		new BufferedReaderCallback(){
			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException {
				Integer multiply = 1;
				String line = null;
				
				while((line = br.readLine()) != null){
					multiply *= Integer.valueOf(line);
				}
				
				return multiply;
			}
		};
		
	return fileReadTemplate(filePath, multiplyCallback);
}
~~~

## Step3
#### 라인 콜백 인터페이스
~~~
public interface LineCallback {
	/**
	 * 템플릿과 콜백을 찾아내는 경우에는 변하는 코드의 경계를 찾고,
	 * 그 경계를 사이에 두고 주고받는 일정한 정보가 있는지 확인해야 한다. 
	 * **/
	
	/**
	 * 결과적으로 템플릿에 포함되는 작업흐름은 더 많아지고 콜백은 더 단순해진다.
	 * **/
	Integer doSomethingWithLine(String line, Integer value);
}
~~~

#### 콜백 메소드 이용 (덧셈)
~~~
public Integer calcSum(String filePath) throws IOException{
	LineCallback sumCallback = new LineCallback(){
		@Override
		public Integer doSomethingWithLine(String line, Integer value) {
			return value + Integer.valueOf(line);
		}
	};
	return lineReadTemplate(filePath, sumCallback, 0);
}
~~~

#### 콜백 메소드 이용 (곱셈)
~~~
public Integer calcMul(String filePath) throws IOException{
	LineCallback mulCallback = new LineCallback(){
		public Integer doSomethingWithLine(String line, Integer value){
			return value * Integer.valueOf(line);
		}
	};
	
	return lineReadTemplate(filePath, mulCallback, 1);
}
~~~

## 정리
* JDBC와 같은 예외가 발생할 가능성이 있으며 공유 리소스의 반환이 필요한 코드는 반드시 try/catch/finally 블록으로 관리해야 한다.
* 일정한 작업 흐름이 반복되면서 그 중 일부 기능만 바뀌는 코드가 존재한다면 __전략패턴__ 을 적용한다. __바뀌지 않는 부분은 컨텍스트__ 로, __바뀌는 부분은 전략__ 으로 만들고 인터페이스를 통해 전략을 변경할 수 있도록 한다.
* 같은 애플리케이션 안에서 여러가지 종류의 전략을 다이내믹하게 구성하고 사용해야 한다면 __컨텍스트를 이용하는 클라이언트 메소드에서 직접 전략을 정의하고 제공하도록__ 만든다.
* 클라이언트 메소드 안에 익명 내부 클래스를 사용해서 전략 오브젝트를 구현하면 코드도 간결해지고 메소드의 정보를 직접 사용할 수 있어서 편리하다.
* 컨텍스트가 하나 이상의 클라이언트 오브젝트에서 사용된다면 클래스로 분리해서 공유하도록 만든다.
* 단일 전략 메소드를 갖는 전략 패턴이면서 익명 내부 클래스를 사용해서 매번 전략을 새로 만들어 사용하고, 컨텍스트 호출과 동시에 전략 DI를 수행하는 방식을 __템플릿/콜백 패턴__ 이라고 한다.
* 콜백의 코드에도 일정한 패턴이 반복된다면 콜백을 템플릿에 넣고 재활용하는 것이 편리하다.
* 템플릿과 콜백의 타입이 다양하게 바뀔 수 있다면 제네릭을 이용한다.
__* 템플릿/콜백을 설계할 때에는 템플릿과 콜백 사이에 주고받는 정보에 관심을 둬야 한다.__
