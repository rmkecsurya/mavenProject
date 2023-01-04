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

public class GPS_Executor {


	static Driver driver = new Driver();
	static Helper help = new Helper();

	public static void main(String[] args) throws IOException {

		String baseFile = "noteword";
		String gpsName = "notewordTest";
		driver.reportSetup();
		driver.startCapture(gpsName);

		gpsRunner(baseFile);
		driver.compareGPS(baseFile, gpsName);
		driver.reportFlush();
	}

	public static void gpsRunner(String fileName)
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
						driver.focusWindow(applicationPath,windowTitle);
					}

					mmarquee.automation.Element element = null;
					switch (role) {
					case "push button":
					{

						element = driver.findButton(controlName);
						element.click();
						break;
					}
					case "menu item":
					{
						element = driver.findMenuItem(controlName);
						try {
							element.click();
						}
						catch (Exception e) {
							driver.getMenuItem(parent, controlName).click();
						}
						break;
					}
					case "combo box":
					{
						element = driver.findComboBox(controlName);
						element.click();
						break;
					}
					case "list item":
					{
						element = driver.findlistItem(controlName);
						element.click();
						break;
					}
					case "radio button":
					{
						element = driver.findRadioButton(controlName);
						element.click();
						break;
					}
					case "editable text":
					{
						element = driver.findEdit(controlName);
						if(event.equals("Click"))
							element.click();
						else if(event.equals("TYPETEXT"))
							element.type(controlData);
						break;
					}
					case "check box":
					{
						element = driver.findCheckBox(controlName);
						element.toggle(controlData);
						break;
					}
					case "link":
					{
						element = driver.findHyperLink(controlName);
						element.click();
						break;
					}
					case "page tab":
					{
						element = driver.findTabItem(controlName);
						element.click();
						break;
					}
					case "cell":
					{
						element = driver.findCell(cellId);
						if(event.equals("Click"))
							element.click();
						else if(event.equals("TYPETEXT"))
							element.type(controlData);
						break;
					}
					default:
						break;
					}

				}
			}
			driver.saveCapture();
			for(String application :applications)
			{
				driver.closeApplication(application);
			}
		}   
		catch (Exception e)   
		{  
			e.printStackTrace();  
		}  
	}  


	private static void waitTime(int millisecond) {

		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}