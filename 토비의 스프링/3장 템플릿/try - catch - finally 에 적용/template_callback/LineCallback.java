package template_callback;

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
