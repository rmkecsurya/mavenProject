package automator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExcelExecutor {
	
	// Paste Excel File path in place of Testcases.xls 
	// Note :- Excel file format should be 97
		
		static String testSheetPath = "D:\\Selenium Workspace\\PD Workspace\\UI_Automation\\GeneratedExcel\\TestCase.xls";
//				"D:\\Selenium Workspace\\PD Workspace\\UI_Automation\\TestCases.xls";

		public void runReflectionMethod(String strClassName, String strMethodName,
				Object... inputArgs) {

			Class<?> params[] = new Class[inputArgs.length];

			for (int i = 0; i < inputArgs.length; i++) {
				if (inputArgs[i] instanceof String) {
					params[i] = String.class;
				}
			}
			try {
				Class<?> cls = Class.forName(strClassName);
				Object _instance = cls.getDeclaredConstructor().newInstance();
				Method myMethod = cls.getDeclaredMethod(strMethodName, params);
				myMethod.invoke(_instance, inputArgs);

			} catch (ClassNotFoundException e) {
				System.err.format(strClassName + ":- Class not found%n");
			} catch (IllegalArgumentException e) {
				System.err
				.format("Method invoked with wrong number of arguments%n");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				System.err.format("In Class " + strClassName + "::" + strMethodName
						+ ":- method does not exists%n");
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				System.err.format("Exception thrown by an invoked method%n");
			} catch (IllegalAccessException e) {
				System.err
				.format("Can not access a member of class with modifiers private%n");
				e.printStackTrace();
			} catch (InstantiationException e) {
				System.err
				.format("Object cannot be instantiated for the specified class using the newInstance method%n");
			}
		}


		public static void main(String[] args) {
			
			ExcelExecutor exeKey = new ExcelExecutor();

			ReadExcel excelIndexSheet = new ReadExcel();
			excelIndexSheet.openSheet(testSheetPath, "");
			ReadExcel excelSheet = new ReadExcel();

			int indexCount=excelIndexSheet.getRowCount();
			String sSheetName;

			for (int rowIndex = 1; rowIndex < indexCount; rowIndex++) {

				if (!excelIndexSheet.getValueFromCell(0, rowIndex).equals("") & !excelIndexSheet.getValueFromCell(0, rowIndex).equals("null")) 
				{
					sSheetName = excelIndexSheet.getValueFromCell(0, rowIndex).toString();

					excelSheet.openSheet(testSheetPath, sSheetName);  

					for (int row = 1; row < excelSheet.getRowCount(); row++) {
						List<Object> myParamList = new ArrayList<Object>();
						String methodName = excelSheet.getValueFromCell(0, row);
						for (int col = 1; col < excelSheet.getColumnCount(); col++) {
							if (!excelSheet.getValueFromCell(col, row).isEmpty()
									& !excelSheet.getValueFromCell(col, row).equals("null")) {
								myParamList.add(excelSheet.getValueFromCell(col, row));
							}
						}


						Object[] paramListObject = new String[myParamList.size()];
						paramListObject = myParamList.toArray(paramListObject);

						exeKey.runReflectionMethod("automator.KeyWords",methodName, paramListObject);

					}
				}
			}
		}
}
