package automator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class ReadExcel {


	Sheet shSheet;
	Workbook wbWorkbook;

	public void openSheet(String filePath, String sSheetName) {
		FileInputStream fs;
		try {
			fs = new FileInputStream(filePath);

			wbWorkbook = Workbook.getWorkbook(fs);

			if(sSheetName.equals("") | sSheetName.equals("null"))
				shSheet = wbWorkbook.getSheet("Index");
			else
				shSheet = wbWorkbook.getSheet(sSheetName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}



	public String getValueFromCell(int iColNumber, int iRowNumber) {
		return shSheet.getCell(iColNumber, iRowNumber).getContents();
	}

	public int getRowCount() {
		return shSheet.getRows();
	}

	public int getColumnCount() {
		return shSheet.getColumns();
	}

}