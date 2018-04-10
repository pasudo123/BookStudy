package template_callback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
//	public Integer calcSum(String filePath) throws IOException{
//		
//		BufferedReader br = null;
//		
//		try{
//			br = new BufferedReader(new FileReader(filePath));
//			Integer sum = 0;
//			String line = null;
//			
//			while((line = br.readLine()) != null){
//				sum += Integer.parseInt(line);
//			}
//
//			return sum;
//		}
//		catch(IOException e){
//			System.out.println(e.getMessage());
//			throw e;
//		}
//		finally{
//			if(br != null){
//				try{
//					br.close();
//				}
//				catch(IOException e){
//					System.out.println(e.getMessage());
//				}
//			}
//		}
//	}
	
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
}
