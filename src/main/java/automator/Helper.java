package automator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.sikuli.script.Screen;
import org.w3c.dom.Document;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;  

public class Helper{


	//	-------------------------------------------------------------------------------------------------------------------------------

	public static ExtentReports extentReporter;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest logger;
	private File screenShotFolder = new File("screenshots");
	private File ReportsFolder = new File("Reports");
	private File outputFolder = new File("GPSFiles");
	private File eventScreenShotFolder = new File("EventScreenShot");
	private String botXslPath = "Resources\\XSL-File\\BotFormatter.xsl";
	private String xslPath = "Resources\\XSL-File\\Formatter.xsl";
	private String currentPath = "GPSFiles\\";
	private String EpiplexRepoPath = "C:\\Users\\Public\\Documents\\Epiplex500\\Repository\\Capture\\";




	public void ReportConfig()
	{
		folderSetUp();
		htmlReporter = new ExtentHtmlReporter(ReportsFolder+"\\EPIPLEX500 - HOME Automation Report.html");
		extentReporter = new ExtentReports();
		extentReporter.attachReporter(htmlReporter);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setDocumentTitle("EPIPLEX500 - HOME Automation Report");
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.config().setTheme(Theme.STANDARD);
		extentReporter.setSystemInfo("Organization", "Epiance Software");
		extentReporter.setSystemInfo("Build NO", "Version 1.0");
		extentReporter.setSystemInfo("Automation Tester", "Dhanush");
	}


	public void reportFlush()
	{
		extentReporter.flush();
	}

	private void folderSetUp()
	{

		if(!ReportsFolder.exists())
			ReportsFolder.mkdir();
		if(!eventScreenShotFolder.exists())
			eventScreenShotFolder.mkdir();
		if(!screenShotFolder.exists())
			screenShotFolder.mkdir();

		if(screenShotFolder.exists() && eventScreenShotFolder.exists())
		{
			try {
				FileUtils.cleanDirectory(screenShotFolder);
				FileUtils.cleanDirectory(eventScreenShotFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void testCaseStatus(boolean status, String testName)
	{

		logger = extentReporter.createTest(testName);

		if(status == false)
		{
			String path = captureScreenShot(testName);

			String logCaptureStatus = "<b>"+"Screenshot Captured ";
			String logScreen = "<b>"+"Screenshot :- ";
			String logResult = "<b>"+"TEST CASE : - "+testName.toUpperCase()+" FAILED";
			//			String logException = "<b>"+"EXCEPTION DETAILS : "+exceptionInfo;
			Markup markResult = MarkupHelper.createLabel(logResult, ExtentColor.RED);
			Markup markCapture = MarkupHelper.createLabel(logCaptureStatus, ExtentColor.ORANGE);
			//			Markup markException = MarkupHelper.createLabel(logException, ExtentColor.RED);
			logger.fail(markResult);
			//			logger.info(markException);
			logger.info(markCapture);
			try {
				logger.info(logScreen, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		else
		{
			String logText = "<b>"+"TEST CASE : - "+testName.toUpperCase()+" PASSED";
			Markup markUp = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			logger.pass(markUp);
		}
	}

	private static String captureScreenShot(String screenshotName)
	{
		BufferedImage img = new Screen().capture().getImage();

		String path = "screenshots\\"+screenshotName+".png";
		File outputfile = new File(path);
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputfile.getAbsolutePath();
	}

	public void EventScreenShot(String event)
	{
		BufferedImage img = new Screen().capture().getImage();
		try {
			//String datefld = String.format("%1$tF", new Date()); Gives Date in yyyy-MM-dd
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String datefld = df.format(new Date());
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
			String time = format.format(cal.getTime());

			// now copy the  screenshot to desired location using copyFile method
			ImageIO.write(img, "png", new File("EventScreenShot\\"+datefld+"\\"+event+" - "+time+".png"));  
		} catch (IOException e){
			System.err.println(e.getMessage());
		}
	}

	public String transformBotGPS(String file)
	{ 
		if(!outputFolder.exists())
			outputFolder.mkdir();

		String path = EpiplexRepoPath +file+".gps";
		String newPath = null;
		try {
			Source input = new StreamSource(new FileInputStream(path));
			Source xslt = new StreamSource(new FileInputStream(botXslPath));
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer(xslt);
			newPath = currentPath+file+".xml";
			Result result = new StreamResult(new FileOutputStream(newPath));
			t.transform(input, result);
		}
		catch (Exception e) {
			//			throw new RuntimeException(e);
		}
		return newPath;
	}

	public String transformGPS(String file)
	{ 
		if(!outputFolder.exists())
			outputFolder.mkdir();

		String path = EpiplexRepoPath +file+".gps";
		String newPath = null;
		try {
			Source input = new StreamSource(new FileInputStream(path));
			Source xslt = new StreamSource(new FileInputStream(xslPath));
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer(xslt);
			newPath = currentPath+file+".xml";
			Result result = new StreamResult(new FileOutputStream(newPath));
			t.transform(input, result);
		}
		catch (Exception e) {
			//			throw new RuntimeException(e);
		}
		return newPath;
	}

	@SuppressWarnings("unchecked")
	public boolean ComapreGPSFiles(String baseFile, String currentFile) 
	{
		String testName = "Compare "+baseFile+" with "+currentFile;
		logger = extentReporter.createTest(testName);
		String base = transformGPS(baseFile);
		String current = transformGPS(currentFile);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		Document doc1 = null;
		Document doc2 = null; 
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			doc1 = db.parse(base);
			doc1.normalizeDocument();
			doc2 = db.parse(current);
			doc2.normalizeDocument();
		}
		catch (Exception e) {
		}

		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		Diff diff = new Diff(doc1, doc2);

		DetailedDiff detailedDiff = new DetailedDiff(diff);
		List<Difference> differences = detailedDiff.getAllDifferences();
		int diffs = differences.size();
		if(diffs == 0)
		{
			System.out.println("Both are matching");
			String logText = "<b>"+"TEST CASE : - "+testName.toUpperCase()+" PASSED";
			Markup markUp = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			logger.pass(markUp);
			return true;
		}
		for(Difference difference: differences)
		{
			try {
				String baseFileXpath = difference.getControlNodeDetail().getXpathLocation();
				String baseFileNodeValue = difference.getControlNodeDetail().getNode().getNodeValue();
				String currentFileXpath = difference.getTestNodeDetail().getXpathLocation();
				String currentFileNodeValue = difference.getTestNodeDetail().getNode().getNodeValue();

				if(baseFileNodeValue != null && currentFileNodeValue != null)
				{
					String Xpath = "<b>"+"Xpath :- "+baseFileXpath;
					String baseNodeValue = "<b>"+"Base File Node Value :- "+baseFileNodeValue;
					String currentNodeValue = "<b>"+"Current File Node Value :- "+currentFileNodeValue;
					Markup XpathMark = MarkupHelper.createLabel(Xpath, ExtentColor.ORANGE);
					Markup baseNodeValueMArk = MarkupHelper.createLabel(baseNodeValue, ExtentColor.GREEN);
					Markup currentNodeValueMark = MarkupHelper.createLabel(currentNodeValue, ExtentColor.BLUE);
					logger.info(XpathMark);
					logger.info(baseNodeValueMArk);
					logger.info(currentNodeValueMark);
					System.out.println(baseFileXpath +" ====> "+currentFileXpath );
					System.out.println(baseFileNodeValue +" ====> "+currentFileNodeValue);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("=============================");
		}
		return false;
	}


	String excelPath = null;
	String sheetName = null;
	public void createExcel(String excelFileName, String excelsheetName)
	{
//		XSSFWorkbook wb = new XSSFWorkbook();
//		XSSFSheet sheet = wb.createSheet(excelsheetName);  
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(excelsheetName); 
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		cellStyle.setFont(font);

		Row row = sheet.createRow(0);
		Cell cellTitle = row.createCell(0);

		cellTitle.setCellStyle(cellStyle);
		cellTitle.setCellValue("Actions");

		Cell cellAuthor = row.createCell(1);
		cellAuthor.setCellStyle(cellStyle);
		cellAuthor.setCellValue("Parameter1");

		Cell cellPrice = row.createCell(2);
		cellPrice.setCellStyle(cellStyle);
		cellPrice.setCellValue("Parameter2");
		sheetName = excelsheetName;
		OutputStream fileOut = null;
		excelPath = "GeneratedExcel\\"+excelFileName+".xls";
		try {
			fileOut = new FileOutputStream(excelPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}   
		System.out.println("Excel File has been created successfully.");   
		try {
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}

	public void writeExcel(int rowNo, LinkedList<String> data) 
	{
		InputStream inp = null;
		try {
			inp = new FileInputStream(excelPath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(inp);
		} catch (EncryptedDocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Sheet sheet = wb.getSheet(sheetName);
		Row row = sheet.createRow(rowNo);
		int length = data.size();
		for(int cell = 0; cell < length; cell++)
		{
			row.createCell(cell).setCellValue(data.get(cell));
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(excelPath);
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}


