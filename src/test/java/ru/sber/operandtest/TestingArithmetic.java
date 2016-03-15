package ru.sber.operandtest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

@RunWith(Parameterized.class)
public class TestingArithmetic {

	public TestingArithmetic(String operand1, String operand2,
			String OPERATION, String result) {
		this.OPERAND1 = Integer.parseInt(operand1);
		this.OPERAND2 = Integer.parseInt(operand2);
		this.OPERATION = OPERATION;
		this.RESULT = Double.parseDouble(result);
	}

	private final int OPERAND1;
	private final int OPERAND2;
	private final String OPERATION;
	private final double RESULT;

	@Parameters
	public static Collection stringsFromFile() throws IOException {
		return getStringsFromFile("src/test/resources/file.csv");
	}

	public static Collection<String[]> getStringsFromFile(String fileName)
			throws IOException {
		List<String[]> records = new ArrayList<String[]>();
		String record;
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		while ((record = file.readLine()) != null) {
			String fields[] = record.split(";");
			records.add(fields);
		}
		file.close();
		return records;
	}

	@Title("Testing string from file")
	@Test
	public void checkOperation() throws Exception {
		getString(OPERAND1, OPERAND2, RESULT, OPERATION);
		double trueResult = getResult(OPERAND1, OPERAND2);
		validate(trueResult, RESULT);
	}

	@Step("Checking string {0}{3}{1}={2}")
	public void getString(int operand1, int operand2, double result,
			String OPERATION) {
		// empty. For Allure report.
	}

	@Step("Calculation result...")
	public double getResult(int operand1, int operand2) {

		double result;
		if (OPERATION.equals("+")) {
			result = operand1 + operand2;
		} else if (OPERATION.equals("-")) {
			result = operand1 - operand2;
		} else if (OPERATION.equals("/")) {
			assertFalse("Division by zero!", operand2 == 0);
			double doubleOperand1 = operand1;
			double doubleOperand2 = operand2;
			result = doubleOperand1 / doubleOperand2;
		} else if (OPERATION.equals("*")) {
			result = operand1 * operand2;
		} else {
			fail("Invalid arithmetic operation: " + OPERATION);
			result = 0;
		}

		return result;
	}

	@Step("Expected result = {0} compare with result from file = {1}")
	public void validate(double trueResult, double result) {
		assertTrue("Сравнение прошло некорректно: " + trueResult
				+ " не соответсвует " + result,
				(Double.compare(trueResult, result) == 0));
	}
}