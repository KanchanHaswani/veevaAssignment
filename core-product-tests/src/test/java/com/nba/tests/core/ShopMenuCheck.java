package com.nba.tests.core;

import com.nba.framework.driver.DriverManager;
import com.nba.tests.core.pages.WarriorsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ShopMenuCheck {

    @Test
    public void checkShopMenu() {
        WarriorsPage page = new WarriorsPage();
        DriverManager manager = new DriverManager();
        
        try {
            // Navigate to Warriors page
            System.out.println("Navigating to Warriors page...");
            page.navigateToWarriorsPage();
            
            // Wait for page to load
            Thread.sleep(3000);
            
            // Check if Shop menu is visible
            boolean shopMenuVisible = page.isElementVisible(page.SHOP_MENU);
            System.out.println("Shop menu visible: " + shopMenuVisible);
            
            // Get current URL
            System.out.println("Current URL: " + page.getCurrentUrl());
            
            // Print page title
            System.out.println("Page title: " + page.getPageTitle());
            
            // Try to find the Shop menu element
            try {
                WebDriver driver = DriverManager.getDriver();
                WebElement shopElement = driver.findElement(page.SHOP_MENU);
                System.out.println("Shop element found!");
                System.out.println("Text: " + shopElement.getText());
                System.out.println("Href: " + shopElement.getAttribute("href"));
                System.out.println("Visible: " + shopElement.isDisplayed());
                System.out.println("Enabled: " + shopElement.isEnabled());
            } catch (Exception e) {
                System.out.println("Shop element not found: " + e.getMessage());
            }
            
            // Print all links on the page containing "shop"
            System.out.println("\nSearching for all links containing 'shop'...");
            WebDriver driver = DriverManager.getDriver();
            java.util.List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            int shopLinks = 0;
            for (WebElement link : allLinks) {
                try {
                    String href = link.getAttribute("href");
                    String text = link.getText();
                    if (href != null && href.toLowerCase().contains("shop")) {
                        System.out.println("Found shop link: text='" + text + "', href='" + href + "'");
                        shopLinks++;
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
            System.out.println("Total shop links found: " + shopLinks);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(10000); // Keep browser open for inspection
            } catch (Exception e) {
                // Ignore
            }
        }
    }
}
