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
	
	public Integer lineReadTemplate(String filePath, LineCallback callback, int initVal) throws IOException{
		BufferedReader br = null;
		Integer res = null; 
		
		try{
			
			br = new BufferedReader(new FileReader(filePath));
			res = initVal;
			String line = null;
			
			// 파일의 각 라인을 루프를 돌면서 가져오는 것도 템플릿 담당
			// 각 라인의 내용을 가지고 계산하는 작업만 콜백에게 맡긴다.
			// 콜백이 계산한 값을 저장해두었다가 다음 라인 계산에 다시 사용한다.
			while((line = br.readLine()) != null){
				res = callback.doSomethingWithLine(line, res);
			}
			
			return res;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
		finally{
			if(br != null)
				br.close();
		}
		
		return res;
	}
	
//	// 덧셈 테스트 메소드
//	public Integer calcSum(String filePath) throws IOException{
//		// 익명 내부 클래스를 해당 파라미터로 전달
//		BufferedReaderCallback sumCallback = 
//			new BufferedReaderCallback(){
//				@Override
//				public Integer doSomethingWithReader(BufferedReader br) throws IOException{
//					Integer sum = 0;
//					String line = null;
//					
//					while((line = br.readLine()) != null){
//						sum += Integer.valueOf(line);
//					}
//					
//					return sum;
//				}
//		};
//		
//		return fileReadTemplate(filePath, sumCallback);
//	}
	
	public Integer calcSum(String filePath) throws IOException{
		LineCallback sumCallback = new LineCallback(){
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filePath, sumCallback, 0);
	}
	
//	// 곱셈 콜백 메소드
//	public Integer calcMul(String filePath) throws IOException{
//		BufferedReaderCallback multiplyCallback = 
//			new BufferedReaderCallback(){
//				@Override
//				public Integer doSomethingWithReader(BufferedReader br) throws IOException {
//					Integer multiply = 1;
//					String line = null;
//					
//					while((line = br.readLine()) != null){
//						multiply *= Integer.valueOf(line);
//					}
//					
//					return multiply;
//				}
//			};
//			
//		return fileReadTemplate(filePath, multiplyCallback);
//	}
	
	public Integer calcMul(String filePath) throws IOException{
		LineCallback mulCallback = new LineCallback(){
			public Integer doSomethingWithLine(String line, Integer value){
				return value * Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, mulCallback, 1);
	}
}
