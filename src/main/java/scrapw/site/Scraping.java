package scrapw.site;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Scraping {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//System.out.println(System.getProperty("user.dir"));
		//String pwd = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		
		System.setProperty("webdriver.firefox.driver", "geckodriver");
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.managed_default_content_settings.images", 2); 

        ChromeOptions chrome_options =new ChromeOptions();
        chrome_options.setExperimentalOption("prefs", prefs);
        DesiredCapabilities chromeCaps = DesiredCapabilities.chrome();
        chromeCaps.setCapability(ChromeOptions.CAPABILITY, chrome_options);

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("permissions.default.image", 2);
        WebDriver driver = new FirefoxDriver(options);

		//WebDriver driver = new ChromeDriver(chromeCaps);


		//driver = new ChromeDriver(chromeCaps);
		//WebDriver driver = new FirefoxDriver();
		//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		//driver.get("https://www.footlocker.com/_-_/keyword-air+jordan+shoes?Rpp=180&Ns=P_NewArrivalDateEpoch%7C1&cm_SORT=New%20Arrivals");
		//https://www.champssports.com/_-_/keyword-air+jordan+shoes?cm_PAGE=180&Rpp=180&Ns=P_NewArrivalDateEpoch%7C1
		//List<WebElement> shoes = driver.findElements(By.cssSelector("#endeca_search_results > ul > li > span"));
		//WebElement shoe = driver.findElement(By.cssSelector("#endeca_search_results > ul > li:nth-child(1) > span"));
		
		//Actions action = new Actions(driver);
		//action.moveToElement(driver.findElement(By.cssSelector("#endeca_search_results > ul:nth-child(1) > li:nth-child(1) > span:nth-child(1) > a:nth-child(2) > img:nth-child(1)"))).perform();
		/*action.moveToElement(driver.findElement(By.cssSelector("#endeca_search_results > ul:nth-child(1) > li:nth-child(2) > span:nth-child(1) > a:nth-child(2) > img:nth-child(1)")))
				.click(driver.findElement(By.cssSelector("#endeca_search_results > ul:nth-child(1) > li:nth-child(2) > span:nth-child(1) > a:nth-child(1)")))
				.build().perform();
		action.release();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector("#quickview_bottomRight > a:nth-child(1)")).click();
		action.release();*/
		
		
		HashMap<Integer,String>window=new HashMap<Integer,String>();
		
		
		int i=0;
		ArrayList<String>links=new ArrayList<String>();
		String url="https://www.champssports.com/_-_/keyword-air+jordan+shoes?cm_PAGE=180&Rpp=180&Ns=P_NewArrivalDateEpoch%7C1";
		driver.get(url);
		
		List <WebElement> select=driver.findElements(By.cssSelector("#sortVal > option"));

		ArrayList<String> urllist=new ArrayList<String>();
		for(WebElement option : select) {
			
			url="https://www.champssports.com";
			url+=option.getAttribute("value");
			url+="&cm_PAGE=180";
			System.out.println("next Url:"+url);
			urllist.add(url);
		}
		for(String next_url:urllist) {
		
				while(true) {
					driver.get(next_url);
					List<WebElement> shoes=driver.findElements(By.cssSelector("#endeca_search_results > ul > li"));
						
					for(WebElement listEle:shoes) {
						//System.out.println(listEle.getText());
						if(!listEle.getAttribute("class").equals("clearRow")) {
							WebElement showLinkEle=listEle.findElement(By.cssSelector("a"));
							String showLink=showLinkEle.getAttribute("href");
							System.out.println("Link:"+showLinkEle.getAttribute("href"));
							links.add(showLink);
						}
					}
					try {
						
						next_url=driver.findElement(By.cssSelector("#pbContent > div.mainsite_search_adjustments > div > div.endeca_pagination > a.next")).getAttribute("href");
						next_url="https://www.champssports.com"+next_url;
					}catch(Exception e) {
						System.out.println("NExt page not exist");
						e.printStackTrace();
						break;
					}
					
				}
				
				System.out.println("Total Links:"+links.size());
				JsonArray jsondata=new JsonArray();
				
				try(FileWriter fw = new FileWriter("JsonData.txt", true);
					    BufferedWriter bw = new BufferedWriter(fw);
					    PrintWriter out = new PrintWriter(bw))
				{
					for(String link:links) {
						i++;
						JsonObject temp=new JsonObject();
						
						System.out.println("link--->:"+link);
						driver.get(link);
						temp.addProperty("url", link);
						String title=driver.findElement(By.cssSelector("#product_form > div.pdp_wrapper > div.top_wrapper > div.product_content > span.right_column > div.product_info > div.title > h1")).getText();
						System.out.println("Title:"+title);
						temp.addProperty("title", title);
						List<WebElement>pricelist=driver.findElements(By.cssSelector("#product_form > div.pdp_wrapper > div.top_wrapper > div.product_content > span.right_column > div.product_info > div.product_price > div"));
						String price="";
						for(WebElement divprice:pricelist) {
							if(divprice.getAttribute("class").equals("sales")) {
								price=driver.findElement(By.cssSelector("#product_form > div.pdp_wrapper > div.top_wrapper > div.product_content > span.right_column > div.product_info > div.product_price > div > span.value")).getText();
							}else if(divprice.getAttribute("class").equals("old")){
								
								if(price.length()==0) {
									System.out.println("Price Skiped as old");
									price=driver.findElement(By.cssSelector("#product_form > div.pdp_wrapper > div.top_wrapper > div.product_content > span.right_column > div.product_info > div.product_price > div > span.value")).getText();
								}
							}else if(divprice.getAttribute("class").equals("regular_price")){
								if(price.length()==0) {
									price=driver.findElement(By.cssSelector("#product_form > div.pdp_wrapper > div.top_wrapper > div.product_content > span.right_column > div.product_info > div.product_price > div > span.value")).getText();
								}
							}
						}
						System.out.println("Price:"+price);
						temp.addProperty("price", price);
						ArrayList<String> sizelist=new ArrayList<String>();
						driver.findElement(By.cssSelector("#pdp_size_select")).click();
						List <WebElement> sizes=driver.findElements(By.cssSelector("#product_form > div.pdp_wrapper > div.top_wrapper > div.product_content > span.right_column > div.product_info > div.add_section > div.product_sizes_content > span > a"));
						JsonArray tempsizearray=new JsonArray();
						
						for(WebElement size:sizes) {
							if(size.getAttribute("class").toString().equals("disabled button")) {
								System.out.println("Skip Size");
							}else {
								sizelist.add(size.getText());
								System.out.println("Size in stock Class::"+size.getAttribute("class").toString());
								System.out.println("Size in stock:"+size.getText());
								JsonObject tempob=new JsonObject();
								tempob.addProperty("size",size.getText() );
								tempsizearray.add(tempob);
							}
						}
						
						ArrayList<String>imagelinks=new ArrayList<String>();
						List<WebElement>images=driver.findElements(By.cssSelector("#product_styles > div.slide_content > ul > li.slideitem0.selected > span"));
						JsonArray tempimagearray=new JsonArray();
						
						for(WebElement image:images) {
							imagelinks.add(image.findElement(By.cssSelector("img")).getAttribute("src"));
							System.out.println("Image:"+image.findElement(By.cssSelector("img")).getAttribute("src"));
							JsonObject tempob=new JsonObject();
							tempob.addProperty("size",image.findElement(By.cssSelector("img")).getAttribute("src"));
							tempimagearray.add(tempob);
						
						}
						
						//Thread.sleep(2000);
								
						temp.add("size", tempsizearray);
						temp.add("image", tempimagearray);
						System.out.println("Data: "+i+" : "+temp.toString());
						out.println(temp.toString());
						jsondata.add(temp);
					}
					
					
				} catch (IOException e) {
					    //exception handling left as an exercise for the reader
					e.printStackTrace();
				}
		//Thread.sleep(5000);
				
				
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsondata.toString());
		String prettyJsonString = gson.toJson(je);

		//System.err.println(prettyJsonString);

		}	
	}

}
