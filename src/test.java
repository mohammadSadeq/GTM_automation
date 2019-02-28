

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
//import net.lightbody.bmp.proxy.ProxyServer;

public class test {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "C:\\SelAssets\\Drivers\\chromeDriver.exe");
		System.setProperty("webdriver.firefox.driver", "C:\\SelAssets\\Drivers\\firefoxDriver.exe");

		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.setTrustAllServers(true);
		proxy.setHarCaptureTypes(CaptureType.RESPONSE_HEADERS);
		proxy.start();
		System.out.println("BrowserMob Proxy running on port: " + proxy.getPort());

		Proxy sproxy = ClientUtil.createSeleniumProxy(proxy);
		ChromeOptions options = new ChromeOptions();
		String hostIp = Inet4Address.getLocalHost().getHostAddress();
		sproxy.setHttpProxy(hostIp + ":" + proxy.getPort());
		sproxy.setSslProxy(hostIp + ":" + proxy.getPort());
		options.setCapability(CapabilityType.PROXY, sproxy);

		WebDriver driver = new ChromeDriver(options);
		proxy.newHar();
		driver.get("https://www.frontgate.com/outdoor-furniture/");
		Thread.sleep(5000);

		net.lightbody.bmp.core.har.Har har = proxy.getHar();
		File harFile = new File("C:\\temp\\testHAR22");
		har.writeTo(harFile);
		System.out.print("Har DONE ");
	
		proxy.stop();
		driver.quit();
		driver.close();
	}

}
