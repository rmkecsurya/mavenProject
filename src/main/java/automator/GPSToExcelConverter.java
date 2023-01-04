package automator;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GPSToExcelConverter {


	static Driver driver = new Driver();
	static Helper help = new Helper();

	public static void main(String[] args) throws IOException {

		String GPS_FileName = "chrome";
		String excelFileName = "TestCase";
		String sheetName = "chromeTest";
		File generatedExcel = new File("GeneratedExcel");
		if(!generatedExcel.exists())
		{
			generatedExcel.mkdir();
		}
		help.createExcel(excelFileName,sheetName);
		ConvertToExcel(GPS_FileName);
	}

	public static void ConvertToExcel(String fileName)
	{
		try   
		{  
			String sourcePath = new Helper().transformBotGPS(fileName);
			File file = new File(sourcePath);  
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize(); 

			NodeList nodeList = doc.getElementsByTagName("AutomationDetails");  

			HashSet<String> applications = new HashSet<>();

			for (int itr = 0; itr < nodeList.getLength(); itr++)   
			{  
				Node node = nodeList.item(itr);  
				if (node.getNodeType() == Node.ELEMENT_NODE)   
				{  
					LinkedList<String> data = new LinkedList<>();

					Element eElement = (Element) node; 

					String windowTitle = eElement.getAttribute("WindowTitle");

					String sentence = eElement.getAttribute("StepSentence");
					System.out.println(sentence);

					String path = eElement.getAttribute("ApplicationPath");
					String exeName = eElement.getAttribute("ExeName");
					path = path.replace("\\", "\\\\");
					String applicationPath = path+"\\\\"+exeName;

					String url = eElement.getAttribute("Url");

					String event = eElement.getAttribute("Event");

					String controlName = eElement.getAttribute("ControlName");

					String role = eElement.getAttribute("Role");

					String parent = eElement.getAttribute("Parent");

					String controlData = eElement.getAttribute("ControlData");

					String cellId = eElement.getAttribute("ID");


					if(event.equals("WindowActivate"))
					{
						applications.add(applicationPath);
						data.add("focusWindow");
						data.add(applicationPath);
						data.add(windowTitle);
						help.writeExcel(itr+1, data);
					}

					switch (role) {
					case "push button":
					{
						data.add("clickButton");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "menu item":
					{
						data.add("clickMenuItem");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "combo box":
					{
						data.add("clickComboBox");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "list item":
					{
						data.add("clickListItem");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "radio button":
					{
						data.add("clickRadioButton");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "editable text":
					{
						if(event.equals("Click"))
						{
							data.add("clickEnterText");
							data.add(controlName);
							help.writeExcel(itr+1, data);
						}
						else if(event.equals("TYPETEXT"))
						{
							data.add("enterText");
							data.add(controlName);
							data.add(controlData);
							help.writeExcel(itr+1, data);
						}
						break;
					}
					case "check box":
					{
						data.add("clickCheckBox");
						data.add(controlName);
						data.add(controlData);
						help.writeExcel(itr+1, data);
						break;
					}
					case "link":
					{
						data.add("clickHyperlink");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "page tab":
					{
						data.add("clickTabItem");
						data.add(controlName);
						help.writeExcel(itr+1, data);
						break;
					}
					case "cell":
					{
						data.add("enterTextExcel");
						data.add(cellId);
						data.add(controlData);
						help.writeExcel(itr+1, data);
						break;
					}
					default:
						break;
					}

				}
			}
			int itr = nodeList.getLength()-1;
			for(String application :applications)
			{
				LinkedList<String> data = new LinkedList<>();
				data.add("closeApp");
				data.add(application);
				help.writeExcel(itr+1, data);
				itr++;
			}
		}   
		catch (Exception e)   
		{  
			e.printStackTrace();  
		}  
	}  
}