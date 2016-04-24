package com.twothreeandme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeoutException;

import org.w3c.dom.*;

import javax.xml.xpath.*;
import javax.xml.parsers.*;

import java.io.*;

import org.xml.sax.SAXException;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SuppressWarnings("deprecation")
public class OrderTest {
    
    private WebDriver driver;
    
    private List<Integer> values;
    
    private static double cartPrice=0;
    private static double youSaved=0;
    private static double Total=0;
    
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 10;
    
    private int screenShotCount;
    
    Process BrowserProcess;
    
    File src;
    FileInputStream fis;
    Properties pro;
    ExtentReports extent;
    ExtentTest test;
    
    public static ExtentReports Instance()
    {
ExtentReports extent;
String Path = "./target/ExtentReport.html";
System.out.println(Path);
extent = new ExtentReports(Path, false);
extent.config()
           .documentTitle("23 and Me Automation Report")
           .reportName("Coding Test");

return extent;
 }
    
    public static String CaptureScreen(WebDriver driver, String ImagesPath)
    {
        TakesScreenshot oScn = (TakesScreenshot) driver;
        File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
     File oDest = new File(ImagesPath+".jpg");
     try {
          FileUtils.copyFile(oScnShot, oDest);
     } catch (IOException e) {System.out.println(e.getMessage());}
     return ImagesPath+".jpg";
    }
    
    
    @BeforeTest(groups={"startWebDriver"})
    public void initializeExtentReport() throws Exception{
    	extent = OrderTest.Instance();  		
    }
    
    
    @BeforeTest(groups={"startWebDriver"})
    //@Parameters("browser")
    public void startWebDriver () throws Exception {
        
    	String browser = "FireFox";
        System.out.println("starting Web Driver");
        // start Web Driver
        if(browser.equalsIgnoreCase("FireFox")){
        	 
            //create firefox instance
     
        	 driver = new FirefoxDriver();

     
            }
        
        else if(browser.equalsIgnoreCase("Safari")){
       	 
            //create Safari instance
     
        	
        	 SafariOptions options = new SafariOptions();
        	 options.setUseCleanSession(true);
        	 
        	 driver = new SafariDriver(options);

     
            }
        
      //Check if parameter passed as 'chrome'
        
        else if(browser.equalsIgnoreCase("chrome")){
 
            //set path to chromedriver.exe You may need to download it from http://code.google.com/p/selenium/wiki/ChromeDriver
 
            System.setProperty("webdriver.chrome.driver","./src/test/resources/chromedriver.exe");
 
            //create Chrome instance
 
            driver = new ChromeDriver();
 
        }
        
        else if(browser.equalsIgnoreCase("IE")){
        	 
            //set path to IEDriverServer.exe You may need to download it from http://code.google.com/p/selenium/
 
            System.setProperty("webdriver.ie.driver","./src/test/resources/IEDriverServer.exe");
 
            //create IE instance
 
            driver = new InternetExplorerDriver();
 
        }
 
        
        boolean BrowserStarted = false;
        
        System.out.println("Browser started.");
        
        String baseUrl = "https://store.23andme.com/en-us/cart/";
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
    }
    
    
    @AfterTest(groups={"flows"})
    public void tearDownExtentReports() throws Exception {
    	 extent.endTest(test);
    	  extent.flush();
    	  extent.close();
    }
    
    @AfterTest(groups={"flows"})
    public void tearDownWebDriver() throws Exception {
       // javax.swing.JOptionPane.showMessageDialog(null, "Testing complete. Press Ok to generate Report");
        driver.quit();
    }
    
 
    private boolean isNumeric (String s) {
        char c = s.toCharArray()[0];
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }
    
    
   // @Test(groups={"flows"},priority=1)
    public void addItemToCart (String name) throws Exception {
        
       // int kitCount=5;
    	double price=0,totalPrice=0;
        System.out.println("starting addKitToCart");
        test = extent.startTest("Add Kit " + name, "Verify Cusomer is able to add Kits to cart");

        
        
       
        List<WebElement> headerText = driver.findElements(By.tagName("h2"));
        java.util.Iterator<WebElement> i = headerText.iterator();
        if(headerText.size()>0)
    	{
    	while(i.hasNext())
    	{
    		WebElement text = i.next();
    		if(text.isDisplayed() && text.getText().contains("no items"))
    		{
    			clickLinkText("Add a kit.").click();
    	        Thread.sleep(5000);
    	        List<WebElement> inputFields = driver.findElements(By.tagName("input").className("js-kit-name"));
                i = inputFields.iterator();
           	System.out.println("Cart Items : " + inputFields.size());
           	if(inputFields.size()>0)
           	{
           		//test = extent.startTest("Add Kit Member 0", "Verify Cusomer is able to add Kits to cart");
              inputFields.get(inputFields.size()-1).sendKeys("Member 0");
              //extent.endTest(test); 
              List<WebElement> priceValue  = driver.findElements(By.tagName("span").className("price"));
              System.out.println("Price test : " + priceValue.get(0).getText().substring(1,priceValue.get(0).getText().length()));
              cartPrice = Double.parseDouble(priceValue.get(0).getText().substring(1,priceValue.get(0).getText().length()));
             
              Thread.sleep(2000);
           	}
           	System.out.println("Total sum is : " + price);
    	        break;
    		}
    	}
    	}
        
        
        	
            	List<WebElement> buttons = driver.findElements(By.tagName("img").className("js-add-kit"));
                 i = buttons.iterator();
            	
            	if(buttons.size()>0)
            	{
            	while(i.hasNext())
            	{
            		WebElement row = i.next();
            		System.out.println("src is : " + row.getAttribute("src"));
            		if(row.getAttribute("src").contains("plus"))
            		{
            			
            			 row.click();
            	         driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            	         Thread.sleep(3000);
            	         break;
            			
            		}
            	}
            	}
            	
            	List<WebElement> inputFields = driver.findElements(By.tagName("input").className("js-kit-name"));
                i = inputFields.iterator();
           	System.out.println("Cart Size : " + inputFields.size());
           	if(inputFields.size()>0)
           	{
              inputFields.get(inputFields.size()-1).sendKeys(name);
              Thread.sleep(2000);
           	}	
            	
            List<WebElement> discountValue  = driver.findElements(By.tagName("div").className("discount"));
            i = discountValue.iterator();
            System.out.println("Discount Size : " + discountValue.size());
            while(i.hasNext())
            {
          	  WebElement discount = i.next();
          	  if(discount.getText().contains("off"))
          	  {
          		  price = price + Double.parseDouble(discount.getText().substring(discount.getText().length()-6,discount.getText().length()));
          	  }
            }
            
            cartPrice = price + 199;
            youSaved = Total-cartPrice;
     
            List<WebElement> cartQty  = driver.findElements(By.tagName("span").className("kit-quantity"));
            System.out.println("Cart Qty: " + cartQty.get(0).getText());
            if(Integer.valueOf(cartQty.get(0).getText())!=inputFields.size())
            {
            	 test.log(LogStatus.FAIL, "Cart Qty doesnt Match" );	
            	 test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./cartQtyFail")));
            	 takeScreenShot("./target/cartQtyFail");     	 
            }
        System.out.println("Total price after calculation : " + cartPrice);
        System.out.println("You Saved : " + youSaved);
        
        test.log(LogStatus.INFO, "Sum of Total items in Cart (includes 10% discount for additional kits :" + cartPrice );
        test.log(LogStatus.INFO, "You Saved : 10% discount for additional kits :" + youSaved );
    
        test.log(LogStatus.PASS, "Kits Added" );
        test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./Addkit" + name)));
        
        Thread.sleep(500);
        
        
        takeScreenShot("./target/Addkit" +name);
        
        extent.endTest(test);
     
    }
    
    public void validateImages() throws Exception
    {
    	 System.out.println("starting ValidateImages");
         test = extent.startTest("verifyImages", "Verify the Page Images in 23andMe page");
         
         List<WebElement> images = driver.findElements(By.tagName("img"));
         java.util.Iterator<WebElement> i = images.iterator();
         while(i.hasNext())
         {
        	 WebElement link = i.next(); 
        	 try
        	 {
        		 if(link!=null && !link.getText().equals(""))
        		 {
              if(!link.getAttribute("src").contains("javascript"))
              {
        	  test.log(LogStatus.INFO, "Image : " + link.getAttribute("name") + " Dest URL : " + link.getAttribute("src") + " returned " + checkBrokenLink(new URL(link.getAttribute("src"))));
        	  System.out.println("Link : " + link.getText() + " Dest URL : " + link.getAttribute("src"));
        	  System.out.println("Image link returned " + checkBrokenLink(new URL(link.getAttribute("src"))));
              }
        		 }
             }catch(Exception e)
        	 {
           	  test.log(LogStatus.FAIL, "Dead Link Image : " + link.getAttribute("name") + " Dest URL : " + link.getAttribute("src"));
              e.printStackTrace();
        	 }
         }
         test.log(LogStatus.PASS, "Image Links verified" );
         test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./ImageVerify")));
         
         
         takeScreenShot("./target/ImageVerify");
         
         extent.endTest(test);
         
    }   
    
    public void validateLinks() throws Exception
    {
    	javax.swing.JOptionPane.showMessageDialog(null, "Links Validation in progress. Press Ok to Continue");
    	 System.out.println("starting ValidateLinks");
    	 boolean flag=true;
         test = extent.startTest("verifyLinks", "Verify the Page links in 23andMe page");
         
         
         List<WebElement> links = driver.findElements(By.tagName("a"));
         java.util.Iterator<WebElement> i = links.iterator();
         while(i.hasNext())
         {
        	 WebElement link = i.next(); 
        	 if(link!=null && link.getAttribute("href")!=null && !link.getText().equals("") && !link.getText().isEmpty())
        	 {
        		 try
        		 {
        			 if(!link.getAttribute("href").contains("javascript"))
        			 {
        				 System.out.println("Link : " + link.getText() + " Dest URL : " + link.getAttribute("href"));
                 	 System.out.println(" URL returned " + checkBrokenLink(new URL(link.getAttribute("href"))));

        	       test.log(LogStatus.INFO, "Link : " + link.getText() + " Dest URL : " + link.getAttribute("src") + " returned " + checkBrokenLink(new URL(link.getAttribute("href"))));
        			 }
        		  }catch(Exception e)
        		 {
                	 e.printStackTrace();
        		 }
        	 }
        	
        } 
         
         
         test.log(LogStatus.PASS, "Page Links verified" );
         test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./PageLinks")));
         
         
         takeScreenShot("./target/PageLinks");
         
         extent.endTest(test);
         
    }
    
    public void clickContinue()
    {
    	List<WebElement> links = driver.findElements(By.tagName("input"));
        java.util.Iterator<WebElement> i = links.iterator();
        while(i.hasNext())
        {
       	 WebElement link = i.next();
       	 if(link.getAttribute("type").equals("submit"))
       	 {
       		 link.click();
       		 try {
       			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       		 break;
       	 }
        }
    }
    
    public void clickButton(String contains,String value)
    {
    	List<WebElement> links = driver.findElements(By.tagName("input"));
        java.util.Iterator<WebElement> i = links.iterator();
        while(i.hasNext())
        {
       	 WebElement link = i.next();
       	 if(link.getAttribute("type").equals(contains) && link.getAttribute("value").equals(value) )
       	 {
       		 link.click();
       		 try {
       			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       		 break;
       	 }
        }
    }
    
    public void addressFieldValidation()
    {
    	System.out.println("Starting Field Validation");
    	 test = extent.startTest("Address Page - Field Validation", "Verify whether field validation messages are displayed in 23andMe Address page");
    	  
    	 clickContinue();
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<WebElement> errorFields = driver.findElements(By.tagName("span").className("error"));
        System.out.println(errorFields.size());
       
        if(errorFields.size()>14 ||errorFields.size()<0 )
        {
        	test.log(LogStatus.FAIL, "Fields missing validation message");
        }
       
        
        try {
			takeScreenShot("./target/FieldValidation");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./FieldValidation")));
        test.log(LogStatus.PASS, "Address field inline messages validated");
        try {
			takeScreenShot("./target/FieldValidation");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        extent.endTest(test);
    }
    
    public void fillAddress(String firstname,String lastname,String AddressLine1, String AptAddressLine2,String city,String state,String zip,String country,String email, String phone) throws Exception
    {
    	Thread.sleep(2000);
    	 System.out.println("starting type Address");
         test = extent.startTest("Auto type Address", "Verify the customer is able to fill address in 23andMe page");
         
         List<WebElement> textFields = driver.findElements(By.tagName("input"));
        // System.out.println(textFields.size());
         //java.util.Iterator<WebElement> i = textFields.iterator();
         driver.findElement(By.id("id_first_name")).sendKeys(firstname);
         driver.findElement(By.id("id_last_name")).sendKeys(firstname);
         driver.findElement(By.id("id_address")).sendKeys(AddressLine1);
         driver.findElement(By.id("id_address2")).sendKeys(AptAddressLine2);
         driver.findElement(By.id("id_city")).sendKeys(city);
         driver.findElement(By.id("id_postal_code")).sendKeys(zip);
         driver.findElement(By.id("id_email")).sendKeys(email);
         driver.findElement(By.id("id_int_phone")).sendKeys(phone);
         Select select = new Select(waitForWebElement("state"));
        // select.deselectAll();
         select.selectByVisibleText(state);
         select = new Select(waitForWebElement("shipping_method"));
        // select.deselectAll();
         select.selectByIndex(0);
         test.log(LogStatus.PASS, "Address Information Filled");
         test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./TypedInfo")));
         takeScreenShot("./target/TypedInfo");
         extent.endTest(test);
    }
    
    public void addressVerification() throws Exception
    {
    	//Thread.sleep(2000);
    	 System.out.println("starting AddressVerification");
         test = extent.startTest("verifyAddress", "Verify the system is able to verify address in 23andMe page");
         
         List<WebElement> headerText = driver.findElements(By.tagName("section").className("verification-summary"));
         java.util.Iterator<WebElement> i = headerText.iterator();
         if(headerText.size()>0)
     	{
     	while(i.hasNext())
     	{
     		WebElement text = i.next();
     		if(text.isDisplayed() && text.getText().contains("We can't ship to the address as entered "))
     		{
     			
     			List<WebElement> buttons = driver.findElements(By.tagName("a").linkText("edit shipping address"));
     	         java.util.Iterator<WebElement> j = buttons.iterator();
     		 	while(j.hasNext())
     		 	{
     		 		WebElement button = j.next();
     		 		if(button.getAttribute("href").equals("/en-us/shipping/"))
     		 		{
     		 			button.click();
     		 			Thread.sleep(2000);
     		 			if(driver.getCurrentUrl().contains("shipping"))
     		 			{
     		 		System.out.println("Reached Edit Address Page");
     		 			}
     		 			break;
     		 		}
     		 	}
     		   test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./AddressVerify")));
     	         takeScreenShot("./target/AddressVerify");
     		 	
     		}
     		else if(text.isDisplayed() && text.getText().contains("unverified address"))
     		{
     		   test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./AddressVerify")));
     	         takeScreenShot("./target/AddressVerify");
     			clickButton("submit","ship to verified address");
     		 
     		 	if(driver.getCurrentUrl().contains("payment"))
     		 			{
     		 		System.out.println("Reached Payment Page");
     		 			}
     		}
     		
     	 }
     	}
        
         test.log(LogStatus.PASS, "Address Information Verified");	
         
         test.log(LogStatus.INFO, test.addScreenCapture(OrderTest.CaptureScreen(driver, "./PaymentPage")));
         takeScreenShot("./target/PaymentPage");
      
         extent.endTest(test);
    }
    
   
    @Test(groups={"flows"},priority=1)
   // @Parameters("kitNumber")
    public void firstFlow () throws Exception {
    	int kitNumber = 5;
    	Total = kitNumber * 199;
    	validateLinks();
    	for(int i=1;i<kitNumber;i++)
    	addItemToCart("Member" + i); 
    	validateImages();
    	clickContinue();
    	addressFieldValidation();
        fillAddress("John","Doe","4116 Providence Rd","Apt J","Charlotte","North Carolina","28211","United States","gbo@gmail.com","7049871234");
        clickContinue();
        addressVerification();
    }
    
   
    
    private WebElement findElementByLocation (int x, int y) throws Exception {
        
        List<WebElement> allElements = driver.findElements(By.tagName("a"));
        
        Point desiredLocation = new Point(x, y);
        
        for (WebElement we : allElements) {
            System.out.println("text: " + we.getText() + ", tagName: " + we.getTagName() + ", displayed: " + we.isDisplayed() + ", location: " + we.getLocation());
            Point buttonLocation = we.getLocation();
            if (buttonLocation.equals(desiredLocation)) {
                return we;
            }
        }
        
        return null;
    }
    
       
    
    WebElement findElementByXPathAndNameValuePair (String xpath, String attributeName, String attributeValue) throws Exception {
        
        WebElement anElement = null;
        String foundValue = "";
        
        long startTime = System.currentTimeMillis();
        
        do {
            try {
                anElement = driver.findElement(By.xpath(xpath));
                foundValue = anElement.getAttribute(attributeName);
                System.out.println(foundValue);
            }
            catch (NoSuchElementException nsee) {
              
                Thread.sleep(100);
                long endTime = System.currentTimeMillis();
                long deltaTime = endTime - startTime;
                if (deltaTime > 10000) {
                    throw nsee;
                }
            }
            
        }
        while (!foundValue.equals(attributeValue));
        
        return anElement;
    }
    
    
    public static String checkBrokenLink(URL url) throws Exception
    
	{
 
 
		String respCode = "";
 
		HttpURLConnection link = (HttpURLConnection) url.openConnection();
 
		try
 
		{
 
		    link.connect();
 
		     respCode = link.getResponseMessage();	        
 
		    link.disconnect();
 
		    return respCode;
 
		}
 
		catch(Exception e)
 
		{
 
			return e.getMessage();
 
		}  				
 
	}
    
    
    void takeScreenShot () throws Exception {
        
        //if (true) return;
        
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile,new File("./target/screenShot." + screenShotCount + ".png" ));
        screenShotCount++;
    }
    
    
    void takeScreenShot (String s) throws Exception {
        
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile,new File( s + ".jpg" ));

    }
    
    
    void clickButtonNameAndIndex (String s, Integer i) throws Exception {
        
        try {
            List<WebElement> buttons = driver.findElements(By.name(s));
            
            for (WebElement button : buttons) {
                System.out.println("text " + button.getText());
                System.out.println("location " + button.getLocation());
                System.out.println("size " + button.getSize());
                //System.out.println(button);
            }
            
            WebElement aButton = buttons.get(i);
            
            aButton.click();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    void clickButtonName (String s) throws Exception {
        
        try {
            WebElement button = driver.findElement(By.name(s));
            //System.out.println(button);
            //javax.swing.JOptionPane.showMessageDialog(null, "check button data");
            button.click();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 private WebElement waitForWebElementByID (String elementName, Integer waitTime, int maxCount) throws Exception {
        
        System.out.print("Waiting for " + elementName);
        //Thread.sleep(waitTime);
        
        WebElement anElement = null;
        //String foundValue = "";
        
        long startTime = System.currentTimeMillis();
        boolean found = false;
        
        int count = 0;
        
        do {
            ++count;
            try {
                
                List<WebElement> theElements = driver.findElements(By.id(elementName));
                
                
                for (WebElement we : theElements) {
                    if (we.isDisplayed()) {
                        found = true;
                        anElement = we;
                    }
                }

                
                
            }
            catch (NoSuchElementException nsee) {
                System.out.print(".");
                Thread.sleep(waitTime);
                
            }
            catch (Exception nsee) {
                                nsee.printStackTrace();
               
                Thread.sleep(waitTime);
                long endTime = System.currentTimeMillis();
                long deltaTime = endTime - startTime;
                if (deltaTime > 10000) {
                    throw nsee;
                }
            }
            
        }
        while (!found && count < maxCount);
        
        System.out.println("");
        
        return anElement;
    } 
    
    private WebElement waitForWebElement (String elementName, Integer waitTime, int maxCount) throws Exception {
        
        System.out.print("Waiting for " + elementName);
        
        
        WebElement anElement = null;
  
        
        long startTime = System.currentTimeMillis();
        boolean found = false;
        
        int count = 0;
        
        do {
            ++count;
            try {
                
               
                List<WebElement> theElements = driver.findElements(By.name(elementName));
                
                
                for (WebElement we : theElements) {
                    if (we.isDisplayed()) {
                        found = true;
                        anElement = we;
                    }
                }

                
                
            }
            catch (NoSuchElementException nsee) {
                System.out.print(".");
                Thread.sleep(waitTime);
               
            }
            catch (Exception nsee) {
                nsee.printStackTrace();
                Thread.sleep(waitTime);
                long endTime = System.currentTimeMillis();
                long deltaTime = endTime - startTime;
                if (deltaTime > 10000) {
                    throw nsee;
                }
            }
            
        }
        while (!found && count < maxCount);
        
        System.out.println("");
        
        return anElement;
    }
    
    private WebElement waitForWebElement (String elementName, Integer waitTime) throws Exception {
        return waitForWebElement(elementName,waitTime, 50);
    }
    
   
 private WebElement clickLinkText (final String elementName)  throws Exception {
        
	 WebElement result = null;   
     List<WebElement> links = driver.findElements(By.tagName("a"));   
	 java.util.Iterator<WebElement> i = links.iterator();
     while(i.hasNext())
	 {
		 WebElement sublink = i.next();
		 System.out.println("Elements are : " + sublink.getText());
		 if(sublink.getText().equals(elementName))
		 {
			 
			 result = sublink;
			 break;
		 }			 
	 }
	 return result;
    }
    
 private WebElement waitForWebElementByID (final String elementName)  throws Exception {
     
     return waitForWebElementByID(elementName, 50, 50);
 }
 
    private WebElement waitForWebElement (final String elementName)  throws Exception {
        
        return waitForWebElement(elementName, 50, 50);
    }
  
    
    private WebElement waitForWebElementByXPath (final String elementName)  throws Exception {
        
        return waitForWebElementByXPath(elementName, 100);
    }
    
    private WebElement waitForWebElementByXPath (final String elementName, Integer waitTime) throws InterruptedException {
        
        System.out.println("Waiting for XPath: " + elementName);
        Thread.sleep(100);
        
        
        return (new WebDriverWait(driver, waitTime)).until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply (WebDriver d) {
                //System.out.print(".");
                return d.findElement(By.xpath(elementName));
            }
        });
        
        
    }
        
       
    
    
    boolean webElementIsPresent (String elementName) {
        
        try {
            driver.findElement(By.name(elementName));
        }
        catch (NoSuchElementException nsee) {
            // not there...
            return false;
        }
        return true;
    }
    
    void clickButtonByPath (String s) throws Exception {
        
        WebElement button = driver.findElement(By.xpath(s));
        button.click();
    }   
   
}

