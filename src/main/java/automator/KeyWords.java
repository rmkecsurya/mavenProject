package automator;

import org.sikuli.hotkey.Keys;
import org.sikuli.script.Screen;

import mmarquee.automation.Element;

public class KeyWords {

	Driver driver = new Driver();
	Element element;

	public void launchApp(String path)
	{
		driver.launchApplication(path);
	}

	public void closeApp(String path)
	{
		driver.closeApplication(path);
	}

	public void focus(String windowTitle)
	{
		driver.focus(windowTitle);
	}
	
	public void focusWindow(String applicationPath, String windowTitle)
	{
		driver.focusWindow(applicationPath, windowTitle);
	}

	public void clickButton(String controlName)
	{
		element = driver.findButton(controlName);
		element.click();
	}

	public void clickMenuItem(String parent, String controlName)
	{
		element = driver.findMenuItem(controlName);
		try {
			element.click();
		}
		catch (Exception e) {
			driver.getMenuItem(parent, controlName).click();
		}
	}

	public void clickMenuItem(String controlName)
	{
		element = driver.findMenuItem(controlName);
		element.click();
	}


	public void clickComboBox(String controlName)
	{
		element = driver.findComboBox(controlName);
		element.click();
	}

	public void clickListItem(String controlName)
	{
		element = driver.findlistItem(controlName);
		element.click();
	}

	public void clickRadioButton(String controlName)
	{
		element = driver.findRadioButton(controlName);
		element.click();
	}
	
	public void clickEnterText(String controlName)
	{
		element = driver.findEdit(controlName);
		element.click();
	}

	public void enterText(String controlName, String text)
	{
		element = driver.findEdit(controlName);
		element.type(text);
	}
	
	public void clickCheckBox(String controlName, String state)
	{
		element = driver.findCheckBox(controlName);
		element.toggle(state);
	}

	public void clickHyperlink(String controlName)
	{
		element = driver.findHyperLink(controlName);
		element.click();
	}

	public void clickTabItem(String controlName)
	{
		element = driver.findTabItem(controlName);
		element.click();
	}

	public void enterTextExcel(String cellId, String text)
	{
		element = driver.findCell(cellId);
		element.click();
		element.type(text);
	}

	public void maximize()
	{
		driver.maximize();
	}

	public void minimize()
	{
		driver.minimize();
	}

	public void startCapture(String fileName)
	{
		driver.startCapture(fileName);
	}

	public void saveCapture()
	{
		driver.saveCapture();
	}

	public void compareGPS(String baseFile, String currentFile)
	{
		driver.compareGPS(baseFile, currentFile);
	}


	public void assertExist(final String controlType, final String name, final String testCaseName)
	{
		driver.assertExist(controlType, name, testCaseName);
	}

	public void assertNotExist(final String controlType, final String name, final String testCaseName)
	{
		driver.assertNotExist(controlType, name, testCaseName);
	}

	public void reportSetup()
	{
		driver.reportSetup();
	}

	public void reportClose()
	{
		driver.reportFlush();
	}

	public void logTestCase(Boolean status, String testCaseName )
	{
		driver.logTestCase(status, testCaseName);
	}

	public void shortcut(String specialKey, String key)
	{
		Screen screen = new Screen();
		String spclKey = null;

		switch (specialKey) {
		case "Ctrl":
			spclKey = Keys.CTRL;
			break;
		case "Alt":
			spclKey = Keys.ALT;
			break;
		case "Shift":
			spclKey = Keys.SHIFT;
			break;
		default:
			break;
		}
		screen.type(key, spclKey);
	}


}
