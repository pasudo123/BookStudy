package template_callback;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CalSumTest {
	Calculator calculator;
	String numFilePath;
	
	@Before 
	public void setUp(){
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("numbers.txt").getPath();
	}
	
	@Test
	public void sumOfNumbers() throws IOException{
		assertEquals((int)calculator.calcSum(this.numFilePath), (10));
	}
	
	@Test
	public void mulOfNumbers() throws IOException{
		assertEquals((int)calculator.calcMultiply(this.numFilePath), (24));
	}
}
