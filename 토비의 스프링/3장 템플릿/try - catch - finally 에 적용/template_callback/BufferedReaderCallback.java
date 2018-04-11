package template_callback;

import java.io.BufferedReader;
import java.io.IOException;

// BufferedReader 를 전달받은 콜백 인터페이스
public interface BufferedReaderCallback {
	
	Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
