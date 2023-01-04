package automator;

import java.io.IOException;
import java.util.List;

import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.sun.jna.platform.win32.WinDef.POINT;

import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.Element;
import mmarquee.automation.UIAutomation;
import mmarquee.automation.controls.MainMenu;
import mmarquee.automation.controls.MenuItem;
import mmarquee.automation.controls.Window;
import mmarquee.uiautomation.TreeScope;

public class Driver{

	UIAutomation automation;
	App application;
	public static Window window;
	Element element;
	private Helper helper = new Helper();
	private int waitTime = 5;

	public Driver()
	{
		automation = UIAutomation.getInstance();
	}

	public void reportSetup()
	{
		helper.ReportConfig();
	}

	public void reportFlush()
	{
		helper.reportFlush();
	}

	private ControlType getControlType(String type)
	{
		ControlType controlType = null;
		switch (type) {
		case "Button":
			controlType = ControlType.Button;
			break;
		case "RadioButton":
			controlType = ControlType.RadioButton;
			break;
		case "CheckBox":
			controlType = ControlType.CheckBox;
			break;
		case "MenuItem":
			controlType = ControlType.MenuItem;
			break;
		case "ComboBox":
			controlType = ControlType.ComboBox;
			break;
		case "ListItem":
			controlType = ControlType.ListItem;
			break;
		case "Text":
			controlType = ControlType.Text;
			break;
		case "Edit":
			controlType = ControlType.Edit;
			break;
		case "ToolBar":
			controlType = ControlType.ToolBar;
			break;
		case "List":
			controlType = ControlType.List;
			break;
		case "Menu":
			controlType = ControlType.Menu;
			break;
		case "HyperLink":
			controlType = ControlType.Hyperlink;
			break;
		case "Window":
			controlType = ControlType.Window;
			break;

		default:
			break;
		}
		return controlType;
	}

	public void assertExist(final String controlType, final String name, final String testCaseName)
	{
		boolean status = isExitsts(controlType, name);
		helper.testCaseStatus(status, testCaseName);
	}

	public void assertNotExist(final String controlType, final String name, final String testCaseName)
	{
		boolean status = isVanished(controlType, name);
		helper.testCaseStatus(status, testCaseName);
	}

	public void launchApplication(String applicationPath)
	{
		application = new App(applicationPath);
		application.open();
		application.focus();
	}

	public boolean isApplicationRunning(String applicationPath)
	{
		application = new App(applicationPath);
		return application.isRunning();
	}

	public void closeApplication(String applicationPath)
	{
		application = new App(applicationPath);
		application.close();
	}

	public boolean isExitsts(final String controlType, final String name)
	{
		boolean status = false;
		try {
			status = automation.isExists(getControlType(controlType), name, TreeScope.DESCENDANTS, waitTime);
		} catch (AutomationException e) {
		}
		return status;
	}

	public boolean isVanished(final String controlType, final String name)
	{
		boolean status = false;
		try {
			status = automation.isExists(getControlType(controlType), name, TreeScope.DESCENDANTS, waitTime);
		} catch (AutomationException e) {
		}
		return !status;
	}

	public Element wait(final ControlType controlType, final String name, int timeInSeconds)
	{
		Element element = null;
		try {
			element = automation.wait(controlType, name, timeInSeconds);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findButton(String name)
	{
		try {
			element = automation.wait(ControlType.Button, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findText(String name)
	{
		try {
			element = automation.wait(ControlType.Text, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findToolBar(String name)
	{
		try {
			element = automation.wait(ControlType.ToolBar, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}


	public Element findRadioButton(String name)
	{
		try {
			element = automation.wait(ControlType.RadioButton, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findCheckBox(String name)
	{
		try {
			element = automation.wait(ControlType.CheckBox, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findEdit(String name)
	{
		try {
			element = automation.wait(ControlType.Edit, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findComboBox(String name)
	{
		try {
			element = automation.wait(ControlType.ComboBox, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findList(String name)
	{
		try {
			element = automation.wait(ControlType.List, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findlistItem(String name)
	{
		try {
			element = automation.wait(ControlType.ListItem, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findHyperLink(String name)
	{
		try {
			element = automation.wait(ControlType.Hyperlink, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findMenu(String name)
	{
		try {
			element = automation.wait(ControlType.Menu, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}



	protected Region getMenuItem(String menuName, String menuItem)
	{
		MainMenu menu = null;
		Region region = null;
		int i = 1;
		while(!window.isMenuEnable(menuName) && i <= 5)
		{
			waitTime(100);
			i++;
		}
		try {
			menu = window.getMenu(menuName);
			List<MenuItem> items = menu.getItems();
			for(MenuItem item : items)
			{
				String text = item.getName();
				if(text.startsWith(menuItem))
				{
					POINT point = item.getClickablePoint();
					region = new Region(point.x, point.y);
					break;
				}
			}
		} catch (AutomationException e1) {
			e1.printStackTrace();
		}
		return region;
	}


	public Element findMenuItem(String name)
	{
		try {
			element = automation.wait(ControlType.MenuItem, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findWindow(String name)
	{
		try {
			element = automation.wait(ControlType.Window, name, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findDocument(String name)
	{
		try {
			element = automation.wait(ControlType.Document, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findTabItem(String name)
	{
		try {
			element = automation.wait(ControlType.TabItem, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public Element findCell(String name)
	{
		try {
			element = automation.wait(ControlType.DataItem, name, waitTime);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
		return element;
	}

	public void focus(String windowTitle)
	{
		App.focus(windowTitle);

		element = findWindow(windowTitle);
		if(element != null)
		{
			try {
				window = automation.getWindow(windowTitle);
				window.focus();
			} catch (AutomationException e) {
			}
		}
		waitTime(100);
	}

	public void focusWindow(String appPath, String windowTitle)
	{
		String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
		if(appPath.equals(chromePath))
		{
			try {
				Runtime.getRuntime().exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe --force-renderer-accessibility");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		application = new App(appPath);
		application.open();
		//		waitTime(1000);
		//		App.focus(windowTitle);
		if(appPath.contains("chrome.exe") | appPath.contains("msedge.exe") | appPath.contains("firefox.exe"))
		{
			element = null;
		}
		else {
			element = findWindow(windowTitle);
		}
		if(element != null)
		{
			try {
				window = automation.getWindow(windowTitle);
				//				window.focus();
			} catch (AutomationException e) {
			}
		}
	}

	public void compareGPS(String base, String current)
	{
		helper.ComapreGPSFiles(base, current);
	}

	public void logTestCase(Boolean status, String testCaseName )
	{
		helper.testCaseStatus(status, testCaseName);
	}

	public void maximize()
	{
		try {
			window.maximize();
			waitTime(200);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
	}
	
	public void minimize()
	{
		try {
			window.minimize();
			waitTime(200);
		} catch (AutomationException e) {
			e.printStackTrace();
		}
	}





	public void startCapture(String fileName)
	{
		String epi = "C:\\Program Files (x86)\\Epiplex500\\epiplex\\BIN\\epiplex.exe";
		focusWindow(epi, "Epiplex500");
		findEdit("Enter a new file name or select from the list").click();
		findEdit("Enter a new file name or select from the list").type(fileName);
		findButton("Start Capture").click();

		boolean fileOverWritePopup = isExitsts("Text","File already exists. Do you want to overwrite?");

		if(fileOverWritePopup)
		{
			findButton("Yes").click();
		}

		findButton("Yes").click();
		boolean status = isExitsts("Button","Capture in progress...");
		while(!status)
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			status = isExitsts("Button","Capture in progress...");
		}
		System.out.println("Capture Started");

	}

	public void saveCapture()
	{
		String epi = "C:\\Program Files (x86)\\Epiplex500\\epiplex\\BIN\\epiplex.exe";
		boolean status = isExitsts("Button","Capture in progress...");
		if(status)
		{
			findButton("Capture in progress...").click();
			Region region = findToolBar("DropDown").getRegion();
			try {
				region.findT("Save").click();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
			status = false;
			while(!status)
			{
				status = isVanished("Window","Saving capture file");
				waitTime(2000);
			}

			findButton("OK").click();
			findButton("No").click();
			closeApplication(epi);
		}
	}






	private void waitTime(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) {
		String notepad = "C:\\Windows\\system32\\notepad.exe";
		Driver driver = null;
		try {
			driver = new Driver();
			//			driver.launchApplication(notepad);
			driver.focusWindow(notepad,"Untitled - Notepad");
			Element element = driver.findButton("Maximize");
			element.click();
			//			driver.findMenuItem("File").click();
			//			driver.findMenuItem("Page Setup...").click();
			driver.findComboBox("Size:").click();
			driver.findlistItem("Statement").click();
			driver.findEdit("Header:").type("Hello");

			driver.findButton("OK").click();
			driver.findButton("Start").hover();
			driver.closeApplication(notepad);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
