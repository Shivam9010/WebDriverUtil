package webdriverUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.io.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtil {
	private WebDriver driver;
	private ExtentTest etest;
	private ExtentReports extReport;
	private Properties prop;

	/*
	 * create Constractor
	 */

	public WebDriverUtil(String fileName, String Testid) {
		DateFormat df = new SimpleDateFormat("dd-mm-yyyy-hh-mm-ss");
		String dformet = df.format(new Date());
		ExtentSparkReporter exs = new ExtentSparkReporter(fileName + dformet + ".html");
		extReport = new ExtentReports();
		extReport.attachReporter(exs);
		etest = extReport.createTest(Testid);
	}
	public WebDriverUtil() {
		
	}
	/*
	 * if we want to creatReport then use this method
	 */

	public ExtentReports generateReports(String Testid, String fileName) {
		DateFormat df = new SimpleDateFormat("dd-mm-yyyy-hh-mm-ss");
		String dformet = df.format(new Date());
		ExtentSparkReporter exs = new ExtentSparkReporter(fileName + dformet + ".html");
		ExtentReports r = new ExtentReports();
		r = new ExtentReports();
		r.attachReporter(exs);
		etest = r.createTest(Testid);
		return r;
	}

	public ExtentReports createReport(String fileName) {
		DateFormat df = new SimpleDateFormat("dd-mm-yyyy-hh-mm-ss");
		String dformet = df.format(new Date());
		ExtentSparkReporter exs = new ExtentSparkReporter(fileName + dformet + ".html");
		extReport = new ExtentReports();
		extReport.attachReporter(exs);
		return extReport;
	}

	public void testCaseId(String Testid) {

		etest = extReport.createTest(Testid);
	}
	
	/*
	 * flush Reports
	 */

	public void flush() {
		extReport.flush();
	}
	
	/*
	 * Read from Config File Data
	 */
	
	public void readConfiguration() {
		   prop = new Properties();
		try {
			InputStream is = new FileInputStream("config.properties");
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getConfigValue(String propertyKey) {
		if (prop == null) {
			readConfiguration();
		}
		return prop.getProperty(propertyKey);
	}
	
	

	/*
	 * get WebDriver;
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/*
	 * set WebDriver;
	 */
	public WebDriver setDriver(WebDriver driver) {
		this.driver = driver;
		return driver;
	}

	/*
	 * //// Launch Browser ////
	 */

	public void LaunchBrowser(String Browser, String Url) {
		try {
			if (Browser.equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions crom=	new ChromeOptions();
				crom.addArguments("--remote-allow-origins=*");
				driver = new ChromeDriver(crom);
				etest.log(Status.PASS, "Browser launch successfully");
			} else if (Browser.equalsIgnoreCase("Firefox")) {
				WebDriverManager.edgedriver().setup();
				driver = new FirefoxDriver();
				etest.log(Status.PASS, "Browser launch successfully");

			} else if (Browser.equalsIgnoreCase("edge")) {
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				etest.log(Status.PASS, "Browser launch successfully");

			} else if (Browser.equalsIgnoreCase("Safari")) {
				WebDriverManager.safaridriver().setup();
				driver = new SafariDriver();
				etest.log(Status.PASS, "Browser launch successfully");

			} else {
				etest.log(Status.INFO, "Browser launch successfully");
			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
		try {
			driver.get(Url);
			etest.log(Status.PASS, "This Url " + Url + " successfully");
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
			takeScreenShot();

		}

	}

	/*
	 * //// HIt Url ////
	 */

	public void getUrl(String Url) {
		driver.get(Url);
	}

	/*
	 * check Element Displayed and Enabled
	 */

	public boolean checkElement(String elementname, WebElement we) {
		boolean status = false;

		if (we.isDisplayed() == true) {
			etest.log(Status.PASS, elementname + " is  Displayed");
			if (we.isEnabled() == true) {
				etest.log(Status.PASS, elementname + " is  Enabled");
				status = true;
			} else {
				etest.log(Status.FAIL, elementname + " is not  Enabled");
			}
		} else {
			etest.log(Status.FAIL, elementname + " is not Displayed");
			takeScreenShot();
		}
		return status;
	}

	public boolean checkElements(String elementname, List<WebElement> we1) {
		boolean status = false;
		for (int i = 0; i < we1.size(); i++) {
			WebElement we = we1.get(i);
			if (we.isDisplayed() == true) {
				etest.log(Status.PASS, elementname + " is  Displayed");
				if (we.isEnabled() == true) {
					etest.log(Status.PASS, elementname + " is  Enabled");
					status = true;
				} else {
					etest.log(Status.FAIL, elementname + " is not  Enabled");
				}
			} else {
				etest.log(Status.FAIL, elementname + " is not Displayed");
				takeScreenShot();
			}
		}
		return status;
	}

	/*
	 * 
	 * //// @enterTextboxValue --- Enter Value ////
	 * 
	 */

	public void enterTextboxValue(WebElement we, String elementname, String dataValue) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				we.clear();
				we.sendKeys(dataValue);
				etest.log(Status.PASS, elementname + " is Entered  Successfully  ");
			} else {
				etest.log(Status.FAIL, elementname + " is  not Entered Successfully ");

			}
		} catch (Exception e) {
			e.printStackTrace();
			takeScreenShot();
		}
	}

	/*
	 * WebElement , Actions , JavaScript - EnterValue
	 */
	public void enterTextboxValueAcJS(WebElement we, String elementname, String dataValue) {
		try {

			boolean st = checkElement(elementname, we);
			try {
				if (st == true) {
					we.sendKeys(dataValue);
				}
			} catch (Exception e) {
				Actions ac = ActionsObj();
				if (st == true) {
					ac.sendKeys(we, dataValue).build().perform();
				}
			} catch (Throwable e) {
				JavascriptExecutor js = javaScriptObj();
				if (st == true) {
					js.executeScript("arguments [0].value('" + dataValue + "');", we);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			takeScreenShot();
		}
	}

	/*
	 * //// Click ON Element ////
	 */

	public void click(WebElement we, String elementname) {

		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				we.click();
				etest.log(Status.PASS, "  clicked On "+elementname );

			} else {
				etest.log(Status.FAIL,  " is not clicked On "+elementname );

			}
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
			takeScreenShot();
		}
	}

	/*
	 * WebElement , Actions , JavaScript - Click
	 */

	public void clickAcJs(WebElement we, String elementname) {

		boolean st = checkElement(elementname, we);
		try {
			if (st == true) {
				we.click();
			}
		} catch (Exception e) {
			Actions ac = ActionsObj();
			if (st == true) {
				ac.click(we).build().perform();
				e.printStackTrace();
			}
		} catch (Throwable e) {
			JavascriptExecutor js = javaScriptObj();
			if (st == true) {
				js.executeScript("arguments [0].click();", we);
				e.printStackTrace();
			}
		}
	}

	public void jsclick(WebElement we, String elementname) {

		boolean st = checkElement(elementname, we);
		if (st == true) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments [0].click();", we);
			etest.log(Status.PASS, "click on date");

		} else {
			etest.log(Status.FAIL, "fail");
		}
	}

	/*
	 * /////////////// Select Class method ///////////////
	 */

	public void selectByValue(WebElement we, String elementname, String ValueAttributeSelect) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.selectByValue(ValueAttributeSelect);
				etest.log(Status.INFO, elementname + " Select Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not Select Value in" + elementname + " dropDown ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
	}
	/*
	 * //// SelectByVisiual in dropdown ////
	 */

	public void selectByVisiual(WebElement we, String elementname, String VisiualText) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.selectByVisibleText(VisiualText);
				etest.log(Status.INFO, elementname + " Select Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not Select Value in" + elementname + " dropDown ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
	}
	/*
	 * //// SelectByIndex in dropdown ////
	 */

	public void selectByIndex(WebElement we, String elementname, int index) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.selectByIndex(index);
				etest.log(Status.INFO, elementname + " Select Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not Select Value in" + elementname + " dropDown ");
			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
		}
	}

	/*
	 * //////////////////////// Deselect all method /////////////////////
	 */
	public void deselectByIndex(WebElement we, String elementname, int index) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.deselectByIndex(index);
				etest.log(Status.INFO, elementname + " DeSelect Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not Deselect Value in" + elementname + " dropDown ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
		}
	}

	/*
	 * /////// DeselectByVisiual in dropdown /////
	 */

	public void deselectByVisiual(WebElement we, String elementname, String VisiualText) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.deselectByVisibleText(VisiualText);
				etest.log(Status.INFO, elementname + " DeSelect Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not Deselect Value in" + elementname + " dropDown ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
		}
	}
	/*
	 * Check multipal Dropdown
	 */

	public boolean isMultipal(WebElement we, String elementname) {
		boolean multipal = false;
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				multipal = selectobj.isMultiple();
				if (multipal == true) {
					etest.log(Status.INFO, elementname + " dropDown is multipal selected");
				} else {
					etest.log(Status.INFO, elementname + "is not multipal selected");
				}
			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
		return multipal;
	}

	/*
	 * ///// DeselectByValue in ////////
	 */

	public void deselectByValue(WebElement we, String elementname, String ValueAttributeSelect) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.deselectByValue(ValueAttributeSelect);
				etest.log(Status.INFO, elementname + " DeSelect Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not Deselect Value in" + elementname + " dropDown ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
	}

	/*
	 * Deselect All in multpal dropdown
	 */

	public void deselectAll(WebElement we, String elementname) {

		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				selectobj.deselectAll();
				etest.log(Status.INFO, elementname + " DeSelectAll Value in dropDown successfully");
			} else {
				etest.log(Status.INFO, "Not DeselectAll Value in" + elementname + " dropDown ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
		}
	}
	/*
	 * Get First Selected option //
	 */

	public WebElement getFirstSelectedOption(WebElement we, String elementname) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				String getFirstOption = selectobj.getFirstSelectedOption().getText();
				etest.log(Status.INFO,
						"getFirstSelectedOption Value in " + elementname + " Dropdown is " + getFirstOption);
				return we;
			} else {
				etest.log(Status.INFO, " Not getFirstSelectedOption Value in " + elementname + " Dropdown  ");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
		return null;
	}

	/*
	 * Get All Selected Options
	 */
	public List<WebElement> getAllSelectedOptions(WebElement we, String elementname) {
		List<WebElement> listObj;
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				listObj = selectobj.getAllSelectedOptions();
				etest.log(Status.INFO, "getAllSelectedOptions in " + elementname + " dropDown ");
				return listObj;
			} else {
				etest.log(Status.INFO, "NOT getAllSelectedOptions " + elementname + "dropDown");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
		}
		return null;
	}

	/* //// CountDropdown Item //// */

	public int CountDropdownOp(WebElement we, String elementname) {
		int optionSize = 0;
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Select selectobj = new Select(we);
				List<WebElement> we1 = selectobj.getOptions();
				optionSize = we1.size();
				etest.log(Status.INFO, "Count DropDown Option");
				return optionSize;
			}
		} catch (Exception e) {
			takeScreenShot();
		}

		return optionSize;
	}

	///////////////////////////////////////////////////////////////////////////

	/*
	 * ////// Get InnerText //////
	 */

	public String getInnerText(WebElement we, String elementname) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				String innertext = we.getText();
				etest.log(Status.INFO, elementname + "  get innerText successfully");
				return innertext;
			}
		} catch (Exception e) {
			takeScreenShot();
		}
		return "";
	}

	/*
	 * Validate text to Varify Actual Expected
	 */

	public void validateText(WebElement we, String elementname, String expected) {
		String actual = we.getText();
		checkActualExpected(elementname, actual, expected);
	}

	///////// Actual Expected ////////

	public void checkActualExpected(String elementname, String actual, String expected) {
//		System.out.println(actual+" = "+expected);

		if (actual.equalsIgnoreCase(expected)) {
			etest.log(Status.PASS, elementname + " match with actual and Expected ");
		} else {
			
			etest.log(Status.FAIL, elementname + " Not match with actual and Expected ");
		}
	}

	/*
	 * Get Innertext Multpal Element then use
	 */

	public void validatemultipalInnerText(List<String> list, String elementname, List<WebElement> we1) {

		try {
			boolean st = checkElements(elementname, we1);
			if (st == true) {
				for (int i = 0; i < we1.size(); i++) {
					WebElement we = we1.get(i);
					String actual = we.getText();
					String expected = list.get(i);
					if (actual.equalsIgnoreCase(expected)) {
						etest.log(Status.PASS, actual + " match with " + expected);
					} else {
						etest.log(Status.FAIL, actual + " not match with " + expected);

					}
				}
			} else {
				etest.log(Status.INFO, elementname + " is not");
			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}

	}

	/*
	 * Click on multipal Element
	 */
//	public void clickmultipalElemement(String elementname, WebElement  we) {
//		WebElement we;
//		
//		try {
//			List<WebElement> we1 = getWebElements(locatorType, locatorValue);
//			boolean st = checkElement(elementname,we);
//			if (st == true) {
//				for (int i = 0; i < we1.size(); i++) {
//					we = we1.get(i);
//					 we.click();;
//					etest.log(Status.INFO,we.getText()+ elementname + " is  Clicked");
//			}
//			} else {
//				etest.log(Status.INFO, elementname + " not clicked");
//			}
//		} catch (Exception e) {
//			etest.log(Status.INFO, e);
//			takeScreenShot();
//		}
//	}
	/*
	 * if We want to take ScreenShot any where then use it
	 */

	public void takeScreenShot() {
		TakesScreenshot take = (TakesScreenshot) driver;
		File from = take.getScreenshotAs(OutputType.FILE);
		File to = new File("takes/" + ".png");
		try {
			Files.copy(from, to);
		} catch (IOException e) {

			e.printStackTrace();
			takeScreenShot();

		}

	}
	

	/*
	 * if we want get SizeofElement then use it
	 */

	public void validateSizeofElement(WebElement we, String elementname, double expectedHeighValue,
			double expecteWidghValue) {

		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Dimension size = we.getSize();
				double actualheighValue = size.height;
				double actualwidghValue = size.width;
				if (actualheighValue == expectedHeighValue) {
					etest.log(Status.PASS, elementname + "Heigh is same as Extepted");
				} else {
					etest.log(Status.FAIL, elementname + "Heigh not  same as  Extepted");
				}
				if (actualwidghValue == expecteWidghValue) {
					etest.log(Status.PASS, elementname + "Widgh is same as  Extepted");
				} else {
					etest.log(Status.FAIL, elementname + "Widgh not  same as  Extepted");
				}
			}
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
			takeScreenShot();
		}
	}

	/*
	 * if we want to get Location then it use
	 */
	public void validateLocationofElement(WebElement we, String elementname, double ExceptedXValue,
			double ExceptedYValue) {

		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {

				Point lo = we.getLocation();
				double X = lo.getX();
				double Y = lo.getY();
				if (X == ExceptedXValue) {
					etest.log(Status.PASS, elementname + "X  is same as Extepted");
				} else {
					etest.log(Status.FAIL, elementname + "X not same as  Extepted");
				}
				if (Y == ExceptedYValue) {
					etest.log(Status.PASS, elementname + "Y is same as  Extepted");
				} else {
					etest.log(Status.FAIL, elementname + "Y is not same as  Extepted");
				}
			}
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
			takeScreenShot();
		}
	}

	/* * if we want to Verify Attribute Value then use it */

	public void ValidateAttribu(String Attributename, String elementname, String expected, WebElement we) {

		boolean st = checkElement(elementname, we);
		try {
			if (st == true) {
				String actual = we.getAttribute(Attributename);
				if (actual.equalsIgnoreCase(expected)) {
					etest.log(Status.PASS, elementname + "Attribute Value Same as Expected");
				} else {
					etest.log(Status.PASS, elementname + "Attribute Value not Same as Expected");
				}
			}
		} catch (Exception e) {
			etest.log(Status.FAIL, elementname + e);
		}
	}

	/*
	 * Check Enabled Element
	 */
	public void validateElementIsEnabled(String elementname, WebElement we) {

		boolean status = we.isEnabled();
		if (status == true) {
			etest.log(Status.PASS, elementname + "Is Enabled");
		} else {
			etest.log(Status.FAIL, elementname + "Is not Enabled");
		}
	}

	public void validateElementIsDisabled(String elementname, WebElement we) {
		 try {
		boolean status = we.isDisplayed();
		if (status == true) {
			etest.log(Status.PASS, elementname + "Is Disabled");
		} else {
			etest.log(Status.FAIL, elementname + "Is not Disabled");
		}}catch (Exception e) {
			etest.log(Status.PASS, elementname + e);
			takeScreenShot();
		}
	}
	/*
	 * /// check Visible Element
	 */

	public void validateElementIsVisible(String elementname, WebElement we) {
     try {
		boolean status = we.isDisplayed();
		if (status == true) {
			etest.log(Status.PASS, elementname + "Is visible");
		} else {
			etest.log(Status.PASS, elementname + "Is invisible");
		}}catch (Exception e) {
			etest.log(Status.PASS, elementname + e);
			takeScreenShot();
		}
	}
	/*
	 * ////// get Title of Page /////
	 */

	public void getTitle(String actual) {

		try {
			String Expected = driver.getTitle();
			if (Expected.equalsIgnoreCase(actual)) {
				etest.log(Status.INFO, Expected + "Expected Title match with actual Title" + actual);
			} else {
				etest.log(Status.INFO, Expected + " Not match  Expected  Title with actual Title" + actual);
			}
		} catch (Exception e) {
			etest.log(Status.INFO, "title is not present");
			takeScreenShot();
		}

	}

	/* if we want to close Browser then use it */

	public void closeBrowser() {
		driver.close();
		etest.log(Status.INFO, "Browser is closed");
	}

	/*
	 * Check CheckBOx is Seleced or not
	 */

	public void getCheckBoxStatus(String elementname, WebElement we) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				boolean Ex = we.isSelected();
				if (Ex == true) {
					etest.log(Status.INFO, elementname + " is Selected");
				} else {
					etest.log(Status.INFO, elementname + " is not Selected");
				}
			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
		    takeScreenShot();
		}
	}

	/* /////// implicitlyWait //////// */

	public void implicityWait(int second) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(second));
	}

	/* /// Visuality Wait /// */

	public void VisualityWait(WebElement we, int second) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOf(we));
	}

	////////////////////////////////// Actions Class /////////////////////////

	/* //// MouseOver ///// */

	public void mouseOver(String elementname, WebElement we) {
		try {

		boolean st = checkElement(elementname, we);
		if (st == true) {
			Actions ac = new Actions(driver);
			ac.moveToElement(we).build().perform();
			etest.log(Status.INFO, "MouseOver on " + elementname);
		}
		}catch(Exception e) {
			etest.log(Status.FAIL, e);
			takeScreenShot();
		}

	}

	public void ActionEnterValue(String elementname, WebElement we, String dataValue) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Actions ac = new Actions(driver);
				ac.sendKeys(we, dataValue);
				etest.log(Status.PASS, "Enter Value successfully in" + elementname);
			}
		} catch (Exception e) {
			takeScreenShot();
		}
	}

	///////////////////////////////// POPS Handal ////////////////////////////

	/* if we want to Accept Aleart then use */

	public void popupAccept() {
		try {
			driver.switchTo().alert().accept();
			etest.log(Status.PASS, "Aleart is Accepted");
		} catch (Exception e) {
			etest.log(Status.FAIL, "Aleart is not Accepted");
			takeScreenShot();
		}
	}

	/* if we want to Dismiss then use it */

	public void popupdismiss() {
		try {
			driver.switchTo().alert().dismiss();
			etest.log(Status.INFO, "dismiss is Accepted");
		} catch (Exception e) {
			etest.log(Status.INFO, "dismiss is not Accepted");
			takeScreenShot();
		}
	}

	/* sendValue in pops then use it */

	public void popupsend(String datavalue) {
		try {
			driver.switchTo().alert().sendKeys(datavalue);
			etest.log(Status.INFO, "is a send Value in pops");

		} catch (Exception e) {
			etest.log(Status.INFO, "not a send Value in pops");
			takeScreenShot();
		}
	}

	/* get text */

	public String popupgettext() {
		try {
			String text = driver.switchTo().alert().getText();
			etest.log(Status.INFO, " Get Value in pops");
			return text;
		} catch (Exception e) {
			etest.log(Status.FAIL, "not a Get Value in pops");
			takeScreenShot();
		}
		return "";
	}
	
	public void ValidatePopuptext(String exepected) {
		try {
			String popuptext = popupgettext();
			System.out.println(popuptext);
			if (popuptext.contains(exepected)) {
				etest.log(Status.PASS, exepected + " match with " + popuptext);
			} else {
				etest.log(Status.FAIL, exepected + " is not match with " + popuptext);
			}
		} catch (Exception e) {
			takeScreenShot();
		}
	}
	/*
	 * get Text in Aleart Box
	 */

	public void getAleartText(String Ac, String elementname) {
		try {
			String Ex = driver.switchTo().alert().getText();
			if (Ex.equalsIgnoreCase(Ac)) {
				etest.log(Status.INFO, elementname + "same as Ex");
			} else {
				etest.log(Status.INFO, elementname + "same as not");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	//////////////////////////////// Frame Handal /////////////////////////

	/* Handal Frame */

	public void Frame(int index) {
		try {
			driver.switchTo().frame(index);
			etest.log(Status.INFO, "frame is Handle ");
		} catch (Exception e) {
			etest.log(Status.INFO, "frame is not Handle");
			takeScreenShot();
		}
	}

	public void Frame(WebElement we) {
		try {
			driver.switchTo().frame(we);
			etest.log(Status.INFO, "frame is Handle ");
		} catch (Exception e) {
			etest.log(Status.INFO, "frame is not Handle");
			takeScreenShot();
		}
	}

	public void Frame(String id) {
		try {
			driver.switchTo().frame(id);
			etest.log(Status.INFO, "frame is Handle ");
		} catch (Exception e) {
			etest.log(Status.INFO, "frame is not Handle");
			takeScreenShot();
		}
	}

	/* switch to parent frame */

	public void ParentFrame() {
		try {
			driver.switchTo().parentFrame();
			etest.log(Status.INFO, "switch to parent frame");
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}
	}

	/* Go out from Frame */

	public void FrameOut() {
		try {
			driver.switchTo().defaultContent();
			etest.log(Status.INFO, "go out side frame");
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}

	}

	///////////////////////////////// WindowHandal /////////////////////////////

	/* chose Window */

	public void SwitchWindowTitle(String dataValue) {
		try {
			Set<String> Handal = driver.getWindowHandles();
			for (String str : Handal) {
				driver.switchTo().window(str);
				String title = driver.getTitle();
				System.err.println(title);
				if (title.equalsIgnoreCase(dataValue)) {

					break;
				}
			}
		} catch (Exception e) {
			takeScreenShot();
		}
	}

	public void SwitchWindowUrl(String dataValue) {
		String Url = "";
		try {
			Set<String> Handal = driver.getWindowHandles();
			for (String str : Handal) {
				driver.switchTo().window(str);
				Url = driver.getCurrentUrl();
//				System.out.println(Url);
				if (Url.equalsIgnoreCase(dataValue)) {
					
					break;
				}
			}
			etest.log(Status.INFO, "switch to window" + Url);
		} catch (Exception e) {
			takeScreenShot();
		}
		
	}
	///////////////////////////////// Maximize////////////////////////////////////

	/* Maximize */

	public void maximize() {
		driver.manage().window().maximize();
		etest.log(Status.INFO, "page is maximize");
	}

	/* Minimize */

	public void minimize() {
		driver.manage().window().minimize();
		etest.log(Status.INFO, "Page is minimize");
	}

	///////////////////////////////// Manimize ////////////////////////////////////

	///////////////////

	// Close Window //

	public void close() {
		driver.close();
		etest.log(Status.INFO, "Browser is close");
	}

	// Quit Window //

	public void quit() {
		driver.quit();
		etest.log(Status.INFO, "all Browser is close");
	}

	//////////////////////////////////// Navigate
	//////////////////////////////////// ////////////////////////////////////

	/* Refresh */

	public void refresh() {
		driver.navigate().refresh();
		etest.log(Status.INFO, "Page is Refresh");
	}

	/* Back */

	public void back() {
		driver.navigate().back();
		etest.log(Status.INFO, "Page is Back");
	}

	/* forword */

	public void forword() {
		driver.navigate().forward();
		etest.log(Status.INFO, "Page is forword");
	}

	/*
	 * //////// Scroll down in page ///////////
	 */

	public void Scroll(String elementname, WebElement we) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				JavascriptExecutor js = javaScriptObj();
				js.executeScript("arguments [0] scrollIntoView", we);
				etest.log(Status.INFO, elementname + "is Scroll");
			} else {
				etest.log(Status.INFO, elementname + "is not Scroll");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);

		}
	}

	/*
	 * Object of JavaScript
	 */
	public JavascriptExecutor javaScriptObj() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js;
	}

	/*
	 * Click on Element useing JavaScript
	 */

	public void ClickByJs(String elementname, WebElement we) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments [0] click();", we);
				etest.log(Status.INFO, elementname + "is clicked");
			} else {
				etest.log(Status.INFO, elementname + "is not clicked");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);

		}
	}

	/*
	 * Enter Value by useing JavaScript
	 */

	public void EnterValueByJs(String elementname, WebElement we) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				JavascriptExecutor js = javaScriptObj();
				js.executeScript("arguments [0] value();", we);
				etest.log(Status.INFO, elementname + " Entered");
			} else {
				etest.log(Status.INFO, elementname + "is not Entered");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);

		}
	}

	public Actions ActionsObj() {
		Actions ac = new Actions(driver);
		return ac;
	}

	/*
	 * login useing by Keyboard function
	 */

	public void tabtoLogin(String UserName, String UserPassword) {
		try {
			Actions ac = ActionsObj();
			ac.sendKeys(UserName, Keys.TAB, UserPassword, Keys.TAB.TAB.ENTER).build().perform();
			etest.log(Status.INFO, "login is pass");
		} catch (Exception e) {
			etest.log(Status.INFO, "Login  is fail");

		}
	}

	/*
	 * Right click on Element
	 */

	public void rightClick(String elementname, WebElement we) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				Actions ac = ActionsObj();
				ac.contextClick(we).perform();
				;
				etest.log(Status.INFO, elementname + "Right click succesfully");
			} else {
				etest.log(Status.INFO, elementname + "Right is not click succesfully");

			}
		} catch (Exception e) {
			etest.log(Status.INFO, elementname + e);
            takeScreenShot();
		}
	}

	///////// match one by one Element in list

	public String varifyColorofElement(String elementname, WebElement we, String dataValue) {

		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				String color = we.getCssValue(dataValue);
				String Actual = Color.fromString(dataValue).asHex();
				etest.log(Status.INFO, elementname + "Color is pass");
				return Actual;
			} else {
				etest.log(Status.INFO, elementname + "Color is fail");
			}
		} catch (Exception e) {
			etest.log(Status.INFO, e);
			takeScreenShot();
		}

		return "";
	}
	/*
	 * Upload file type must be file;
	 */

	public void UploadFile(String elementname, String filePath, WebElement we) {
		try {

			boolean st = checkElement(elementname, we);
			if (st == true) {
				we.sendKeys(filePath);
				etest.log(Status.INFO, "file is upload");
			} else {
				etest.log(Status.INFO, elementname + "filePath is not corect");
			}
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
			takeScreenShot();
		}

	}
	/*
	 * get current Date and Time
	 */

	public String getCurrentDateTime(String formet) {
		DateFormat df = new SimpleDateFormat(formet);
		String dformet = df.format(new Date());
		return dformet;

	}

	/*
	 * Handal Calendar
	 */
	public String[] getCalendar(String formetDate, String split, int nofDate) {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, nofDate);
		Date d = c.getTime();
		DateFormat df = new SimpleDateFormat(formetDate);
		String date = df.format(d);
		String[] arrdate = date.split(split);
		return arrdate;

	}

	/*
	 * Handal Table
	 */
	public String[] handalTable(String a) {

		String[] b = a.split("/");
		return b;
	}

	public void checkElementPresentinTable(WebElement rowwe, WebElement columwe, String locatorTypeCell,
			WebElement cellwe, String expected) {

		List<WebElement> row = (List<WebElement>) rowwe;
		List<WebElement> colum = (List<WebElement>) columwe;

		for (int i = 1; i < row.size(); i++) {
			for (int j = 1; j <= colum.size(); j++) {
				WebElement cell = cellwe;
				String actual = cell.getText();
//				String actual = driver.findElement(By.xpath("//table[@class='lvt small']//tbody//tr[" + (i) + "]//td[" + j + "]")).getText();
				if (actual.equalsIgnoreCase(expected)) {
					System.out.println(i + " : " + j);

				} else {
					System.err.println(actual);
					System.out.println(i + " : " + j);
				}
			}
		}
	}

	public boolean checkElementPresentinTable(String expected) {
		boolean status = false;
		List<WebElement> row = driver.findElements(By.xpath("//table[@class='lvt small']//tbody//tr"));
		List<WebElement> colum = driver
				.findElements(By.xpath("//table[@class='lvt small']//tbody//tr//td[@class='lvtCol']"));

		for (int i = 1; i < row.size(); i++) {
			for (int j = 1; j <= colum.size(); j++) {
				String actual = driver
						.findElement(By.xpath("//table[@class='lvt small']//tbody//tr[" + (i + 1) + "]//td[" + j + "]"))
						.getText();
				if (actual.equalsIgnoreCase(expected)) {
					status = true;
					return status;
				}
			}
		}
		return status;
	}

	public boolean ElementPresentinTable(String expected, WebElement we1) {
		boolean status = false;
		List<WebElement> we = (List<WebElement>) we1;
		for (int i = 1; i < we.size(); i++) {
			WebElement ele = we.get(i);
			String actual = ele.getText();
			if (actual.equalsIgnoreCase(expected)) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public void whereElementPresent_Row_Colum(String expected) {
		String test;
		List<WebElement> row = driver.findElements(By.xpath("//table[@class='lvt small']//tbody//tr"));
		List<WebElement> colum = driver
				.findElements(By.xpath("//table[@class='lvt small']//tbody//tr//td[@class='lvtCol']"));

		for (int i = 1; i < row.size(); i++) {
			for (int j = 1; j <= colum.size(); j++) {
				String actual = driver
						.findElement(By.xpath("//table[@class='lvt small']//tbody//tr[" + i + "]//td[" + j + "]"))
						.getText();
				if (actual.equalsIgnoreCase(expected)) {
					System.out.println(i + ":" + j);
				}
			}
		}

	}

	/*
	 * Scroll by Actions
	 */

	public void actionScroll(WebElement we, String elementname) {

		boolean status = checkElement(elementname, we);
		try {
			if (status == true) {
				Actions ac = ActionsObj();
				ac.scrollToElement(we).build().perform();
				etest.log(Status.PASS, "Scroll on Element");
			} else {
				etest.log(Status.FAIL, " not Scroll");

			}
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
		}
	}

	public Actions dragandDrop(WebElement we, WebElement we1, String elementname) {

		boolean status = checkElement(elementname, we);
		boolean status2 = checkElement(elementname, we);
		Actions ac = ActionsObj();
		;
		try {
			if (status & status2 == true) {

				ac.dragAndDrop(we, we1).build().perform();
				;
				etest.log(Status.PASS, " Drag and drop on Element");
			} else {
				etest.log(Status.PASS, " Drag and drop not Element");

			}
		} catch (Exception e) {
			etest.log(Status.FAIL, e);
		}
		return ac;
	}

}