## 템플릿 / 콜백 메소드 패턴
기존에 아래와 같은 메소드들이 존재

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
