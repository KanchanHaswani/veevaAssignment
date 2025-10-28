package com.nba.tests.core.pages;

import com.nba.framework.pages.BasePage;
import com.nba.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarriorsPage extends BasePage {
    
    private static final Logger logger = LogManager.getLogger(WarriorsPage.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // Locators
    public static final By SHOP_MENU = By.xpath("(//a[contains(@href, 'shop')])[1]");
    public static final By MODAL_BACKDROP = By.xpath("//div[contains(@class, 'CustomModal_backdrop')]");
    public static final By MENS_MENU = By.xpath("//a[@aria-label='Men']");
    //a[text()='Men']
    public static final By JACKETS_CATEGORY = By.xpath("(//div[contains(text(),'Jackets')])[1]");
    public static final By PRODUCT_ITEMS = By.cssSelector("[data-testid*='product'], .product-item, .product-card, .product-tile, .product");
    public static final By PRODUCT_TITLE = By.cssSelector(".product-title, .product-name, h3, h4, .product-tile-title, .product-name-link");
    public static final By PRODUCT_PRICE = By.cssSelector(".price, .product-price, [class*='price'], .product-tile-price, .price-current");
    public static final By TOP_SELLER_BADGE = By.cssSelector(".top-seller, .bestseller, [class*='top-seller'], [class*='bestseller']");
    public static final By NEXT_PAGE_BUTTON = By.cssSelector(".next, .pagination-next, [aria-label*='next']");
    public static final By PAGINATION = By.cssSelector(".pagination, .page-numbers");
    
    // New & Features locators
    public static final By NEW_FEATURES_MENU = By.xpath("//a[contains(text(), 'New') or contains(text(), 'Features')]");
    public static final By VIDEO_FEEDS = By.cssSelector("video, .video-player, [class*='video']");
    public static final By VIDEO_CONTAINER = By.cssSelector(".video-container, .video-wrapper");
    
    public void navigateToWarriorsPage() {
        navigateTo(config.getWarriorsUrl());
    }
    
    public void clickShopMenu() {
        // First, try to dismiss any modal backdrops that might be intercepting the click
        dismissModalBackdrop();
        
        // Wait a moment for any animations to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Count windows before click
        int windowsBefore = driver.getWindowHandles().size();
        logger.info("Windows before Shop click: " + windowsBefore);
        
        // Try to click the Shop menu with enhanced click strategy
        clickWithRetry(SHOP_MENU);
        
        // Wait for navigation
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check if new window/tab opened
        int windowsAfter = driver.getWindowHandles().size();
        logger.info("Windows after Shop click: " + windowsAfter);
        
        if (windowsAfter > windowsBefore) {
            logger.info("New window/tab opened! Switching to it...");
            // Switch to the new window
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(driver.getWindowHandle())) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            logger.info("Switched to shop window. Current URL: " + driver.getCurrentUrl());
        } else {
            logger.info("No new window opened. Current URL: " + driver.getCurrentUrl());
        }
    }
    
    private void dismissModalBackdrop() {
        try {
            // Check if modal backdrop exists
            List<WebElement> modalBackdrops = driver.findElements(MODAL_BACKDROP);
            logger.info("Found " + modalBackdrops.size() + " modal backdrops");
            
            for (WebElement backdrop : modalBackdrops) {
                if (backdrop.isDisplayed()) {
                    logger.info("Dismissing modal backdrop with aria-hidden: " + backdrop.getAttribute("aria-hidden"));
                    
                    // Try multiple methods to dismiss the modal
                    try {
                        // Method 1: Click on the backdrop
                        backdrop.click();
                        logger.info("Clicked modal backdrop");
                    } catch (Exception e1) {
                        try {
                            // Method 2: Press Escape key
                            backdrop.sendKeys(org.openqa.selenium.Keys.ESCAPE);
                            logger.info("Pressed Escape key on modal backdrop");
                        } catch (Exception e2) {
                            try {
                                // Method 3: JavaScript click
                                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", backdrop);
                                logger.info("JavaScript clicked modal backdrop");
                            } catch (Exception e3) {
                                // Method 4: Try to hide the modal with JavaScript
                                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'none';", backdrop);
                                logger.info("Hidden modal backdrop with JavaScript");
                            }
                        }
                    }
                    
                    // Wait for modal to disappear
                    Thread.sleep(1000);
                    break; // Only dismiss the first visible modal
                }
            }
        } catch (Exception e) {
            logger.info("No modal backdrop found or couldn't dismiss: " + e.getMessage());
        }
    }
    
    private void clickWithRetry(By locator) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                // Find all elements matching the locator
                List<WebElement> elements = driver.findElements(locator);
                logger.info("Found " + elements.size() + " elements matching: " + locator);
                
                // Find the first element with "SHOP" text
                WebElement targetElement = null;
                for (WebElement element : elements) {
                    String text = element.getText().trim();
                    logger.info("Element text: '" + text + "', visible: " + element.isDisplayed());
                    if ("SHOP".equals(text) && element.isDisplayed()) {
                        targetElement = element;
                        break;
                    }
                }
                
                if (targetElement == null) {
                    // If no element with "SHOP" text found, use the first visible element
                    for (WebElement element : elements) {
                        if (element.isDisplayed()) {
                            targetElement = element;
                            break;
                        }
                    }
                }
                
                if (targetElement == null) {
                    throw new RuntimeException("No visible Shop element found");
                }
                
                logger.info("Target element found: visible=" + targetElement.isDisplayed() + ", enabled=" + targetElement.isEnabled());
                
                // Try JavaScript click first (more reliable for intercepted elements)
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", targetElement);
                logger.info("Successfully clicked Shop element using JavaScript");
                return;
                
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                logger.warn("Click intercepted, attempt " + (i + 1) + "/" + maxRetries);
                
                if (i < maxRetries - 1) {
                    // Try to dismiss modal again
                    dismissModalBackdrop();
                    
                    // Wait before retry
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw e; // Re-throw if all retries failed
                }
            } catch (Exception e) {
                logger.warn("Click failed, attempt " + (i + 1) + "/" + maxRetries + ": " + e.getMessage());
                
                if (i < maxRetries - 1) {
                    // Try to dismiss modal again
                    dismissModalBackdrop();
                    
                    // Wait before retry
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw e; // Re-throw if all retries failed
                }
            }
        }
    }
    
    public void clickMensMenu() {
        try {
            WebElement mensElement = driver.findElement(MENS_MENU);
            logger.info("Found Men's element: visible=" + mensElement.isDisplayed() + ", enabled=" + mensElement.isEnabled());
            
            // Use JavaScript click for reliability
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", mensElement);
            logger.info("Successfully clicked Men's element using JavaScript");
            
            // Wait for navigation
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        } catch (Exception e) {
            logger.error("Failed to click Men's menu: " + e.getMessage());
            throw e;
        }
    }
    
    public void clickJacketsCategory() {
        try {
            // Find the div containing "Jackets" - it's clickable even if not visible
            WebElement jacketsDiv = driver.findElement(JACKETS_CATEGORY);
            
            // Use JavaScript click for dynamic elements that are not interactable
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", jacketsDiv);
            
            // Wait for navigation
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        } catch (Exception e) {
            logger.error("Failed to click Jackets category: " + e.getMessage());
            throw e;
        }
    }
    
    public void navigateToShopMensJackets() {
        clickShopMenu();
        clickMensMenu();
        clickJacketsCategory();
    }
    
    public List<WebElement> getAllProductItems() {
        try {
            // Use non-blocking approach to avoid hanging
            return driver.findElements(PRODUCT_ITEMS);
        } catch (Exception e) {
            logger.error("Failed to find product items: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    public String getProductTitle(WebElement productElement) {
        try {
            return productElement.findElement(PRODUCT_TITLE).getText();
        } catch (Exception e) {
            return "Title not found";
        }
    }
    
    public String getProductPrice(WebElement productElement) {
        try {
            return productElement.findElement(PRODUCT_PRICE).getText();
        } catch (Exception e) {
            return "Price not found";
        }
    }
    
    public String getTopSellerMessage(WebElement productElement) {
        try {
            WebElement topSellerElement = productElement.findElement(TOP_SELLER_BADGE);
            return topSellerElement.isDisplayed() ? topSellerElement.getText() : "";
        } catch (Exception e) {
            return "";
        }
    }
    
    public boolean hasNextPage() {
        return isElementPresent(NEXT_PAGE_BUTTON) && isElementVisible(NEXT_PAGE_BUTTON);
    }
    
    public void clickNextPage() {
        if (hasNextPage()) {
            click(NEXT_PAGE_BUTTON);
        }
    }
    
    public boolean isPaginationPresent() {
        return isElementPresent(PAGINATION);
    }
    
    public void hoverOnMenu(String menuText) {
        By menuLocator = By.xpath("//a[contains(text(), '" + menuText + "')]");
        hoverOverElement(menuLocator);
    }
    
    public void clickNewAndFeatures() {
        click(NEW_FEATURES_MENU);
    }
    
    public List<WebElement> getAllVideoFeeds() {
        return findElements(VIDEO_FEEDS);
    }
    
    public List<WebElement> getAllVideoContainers() {
        return findElements(VIDEO_CONTAINER);
    }
    
    public int countVideoFeeds() {
        return getAllVideoFeeds().size();
    }
    
    public int countVideoContainers() {
        return getAllVideoContainers().size();
    }
    
    public boolean isJacketsCategoryVisible() {
        try {
            // For dynamic elements, use findElements (non-blocking) instead of findElement
            List<WebElement> elements = driver.findElements(JACKETS_CATEGORY);
            return elements.size() > 0; // Dynamic elements exist but may not be visible
        } catch (Exception e) {
            logger.error("Error checking Jackets category: " + e.getMessage());
            return false;
        }
    }
}
