package edu.tacoma.uw.udeal;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.ItemDisplay;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the ItemDisplay class.
 *
 * @version 1.0
 * @author TCSS 450 Team 8
 */
public class ItemDisplayTest {

    /** The item display used for testing. */
    ItemDisplay testItemDisplay;

    /**
     * Sets up the ItemDisplay object.
     */
    @Before
    public void setUp() {
        List<ItemDisplay> temp = new ArrayList<>();
        testItemDisplay = new ItemDisplay(999999, 10000000, "abcdefghijklmnop", "Tesla Model X", "98042",
                "A brand new 2020 Tesla Model X", "Cars and Trucks", 89999.99, true,
                "2020-12-12T12:00:00.000Z", 7895, new MainActivity.SimpleItemRecyclerViewAdapter(new MainActivity(), temp),
                "mzloria", true, 9999, true);
    }

    /**
     * Tests to ensure that the ItemDisplay object was successfully created using the constructor.
     */
    @Test
    public void testItemDisplayConstructor() {
        assertNotNull(testItemDisplay);

        List<ItemDisplay> myList = new ArrayList<>();
        ItemDisplay myItemDisplay = new ItemDisplay(123, 123, "thisisatest", "Honda", "98058",
                "Car", "Cars and Trucks", 9.99, true,
                "2019-11-12T12:00:00.000Z", 123, new MainActivity.SimpleItemRecyclerViewAdapter(new MainActivity(), myList),
                "dungthai", true, 123, true);

        assertNotNull(myItemDisplay);

    }

    /**
     * Tests getting the item id.
     */
    @Test
    public void testGetItemID() {
        assertEquals(testItemDisplay.getMyItemID(), 999999);
    }

    /**
     * Tests setting the item ID.
     */
    @Test
    public void testSetItemID() {
        testItemDisplay.setMyItemID(200000);
        assertEquals(testItemDisplay.getMyItemID(), 200000);

        testItemDisplay.setMyItemID(123);
        assertEquals(testItemDisplay.getMyItemID(), 123);

        testItemDisplay.setMyItemID(456);
        assertEquals(testItemDisplay.getMyItemID(), 456);

        testItemDisplay.setMyItemID(789);
        assertEquals(testItemDisplay.getMyItemID(), 789);
    }

    /**
     * Tests getting the member ID.
     */
    @Test
    public void testGetMyMemberID() {
        assertEquals(testItemDisplay.getMyMemberID(), 10000000);
    }

    /**
     * Tests setting the member ID.
     */
    @Test
    public void testSetMyMemberID() {
        testItemDisplay.setMyMemberID(45000);
        assertEquals(testItemDisplay.getMyMemberID(), 45000);

        testItemDisplay.setMyMemberID(342);
        assertEquals(testItemDisplay.getMyMemberID(), 342);

        testItemDisplay.setMyMemberID(3451);
        assertEquals(testItemDisplay.getMyMemberID(), 3451);
    }

    /**
     * Tests getting the object url.
     */
    @Test
    public void testGetMyURL() {
        assertEquals(testItemDisplay.getMyURL(),  "https://udeal-app-services-backend.herokuapp.com/download?myfilename=abcdefghijklmnop");
    }

    /**
     * Tests setting the object url.
     */
    @Test
    public void testSetMyURL() {
        testItemDisplay.setMyURL("thisisatestfilename");
        assertEquals(testItemDisplay.getMyURL(), "https://udeal-app-services-backend.herokuapp.com/download?myfilename=thisisatestfilename");

        testItemDisplay.setMyURL("12345");
        assertEquals(testItemDisplay.getMyURL(), "https://udeal-app-services-backend.herokuapp.com/download?myfilename=12345");

        testItemDisplay.setMyURL("file");
        assertEquals(testItemDisplay.getMyURL(), "https://udeal-app-services-backend.herokuapp.com/download?myfilename=file");

        testItemDisplay.setMyURL("carimage");
        assertEquals(testItemDisplay.getMyURL(), "https://udeal-app-services-backend.herokuapp.com/download?myfilename=carimage");

    }

    /**
     * Tests getting the title of item.
     */
    @Test
    public void testGetMyTitle() {
        assertEquals(testItemDisplay.getMyTitle(),  "Tesla Model X");
    }

    /**
     * Tests setting title of the item.
     */
    @Test
    public void testSetMyTitle() {
        testItemDisplay.setMyTitle("Honda Civic");
        assertEquals(testItemDisplay.getMyTitle(),  "Honda Civic");

        testItemDisplay.setMyTitle("Toyota Corolla");
        assertEquals(testItemDisplay.getMyTitle(),  "Toyota Corolla");

        testItemDisplay.setMyTitle("Calculator");
        assertEquals(testItemDisplay.getMyTitle(),  "Calculator");
    }

    /**
     * Tests getting the location of the item.
     */
    @Test
    public void testGetMyLocation() {
        assertEquals(testItemDisplay.getMyLocation(), "98042");
    }

    /**
     * Tests resetting the bitmap images.
     */
    @Test
    public void testResetBitmaps() {
        testItemDisplay.resetBitmaps();
        assertNull(testItemDisplay.getMyBitmap());
    }

    /**
     * Tests setting the location of the item display.
     */
    @Test
    public void testSetMyLocation() {
        testItemDisplay.setMyLocation("98058");
        assertEquals(testItemDisplay.getMyLocation(),  "98058");

        testItemDisplay.setMyLocation("12354");
        assertEquals(testItemDisplay.getMyLocation(),  "12354");

        testItemDisplay.setMyLocation("80985");
        assertEquals(testItemDisplay.getMyLocation(),  "80985");
    }

    /**
     * Tests getting the description of the item display.
     */
    @Test
    public void testGetMyDescription() {
        assertEquals(testItemDisplay.getMyDescription(), "A brand new 2020 Tesla Model X");
    }

    /**
     * Tests setting the description of the item display.
     */
    @Test
    public void testSetMyDescription() {
        testItemDisplay.setMyDescription("A used old 1995 honda civic");
        assertEquals(testItemDisplay.getMyDescription(), "A used old 1995 honda civic");

        testItemDisplay.setMyDescription("A new box");
        assertEquals(testItemDisplay.getMyDescription(), "A new box");

        testItemDisplay.setMyDescription("Used desk and table");
        assertEquals(testItemDisplay.getMyDescription(), "Used desk and table");
    }

    /**
     * Tests getting the category.
     */
    @Test
    public void testGetMyCategory() {
        assertEquals(testItemDisplay.getMyCategory(), "Cars and Trucks");
    }

    /**
     * Tests setting the category.
     */
    @Test
    public void testSetMyCategory() {
        testItemDisplay.setMyCategory("Furniture");
        assertEquals(testItemDisplay.getMyCategory(), "Furniture");

        testItemDisplay.setMyCategory("General");
        assertEquals(testItemDisplay.getMyCategory(), "General");

        testItemDisplay.setMyCategory("Clothing");
        assertEquals(testItemDisplay.getMyCategory(), "Clothing");
    }

    /**
     * Tests getting the price of item.
     */
    @Test
    public void testGetMyPrice() {
        assertEquals(testItemDisplay.getMyPrice(), 89999.99);
    }

    /**
     * Tests setting the price of item.
     */
    @Test
    public void testSetMyPrice() {
        testItemDisplay.setMyPrice(19.99);
        assertEquals(testItemDisplay.getMyPrice(), 19.99);

        testItemDisplay.setMyPrice(79999.99);
        assertEquals(testItemDisplay.getMyPrice(), 79999.99);

        testItemDisplay.setMyPrice(5.00);
        assertEquals(testItemDisplay.getMyPrice(), 5.00);
    }

    /**
     * Tests getting the listed boolean.
     */
    @Test
    public void getMyListed() {
        assertTrue(testItemDisplay.getMyListed());
    }

    /**
     * Tests setting the listed boolean.
     */
    @Test
    public void testSetMyListed() {
        testItemDisplay.setMyListed(false);
        assertFalse(testItemDisplay.getMyListed());

        testItemDisplay.setMyListed(true);
        assertTrue(testItemDisplay.getMyListed());
    }

    /**
     * Tests getting the date item was posted.
     */
    @Test
    public void testGetMyDatedPosted() {
        assertEquals(testItemDisplay.getMyDatePosted(), "Dec 12, 2020 05:00 AM");
    }

    /**
     * Tests setting the date item was posted.
     */
    @Test
    public void testSetMyDatePosted() {
        testItemDisplay.setMyDatePosted("2019-12-13T13:00:00.000Z");
        assertEquals(testItemDisplay.getMyDatePosted(), "Dec 13, 2019 06:00 AM");

        testItemDisplay.setMyDatePosted("2005-07-13T13:00:00.000Z");
        assertEquals(testItemDisplay.getMyDatePosted(), "Jul 13, 2005 06:00 AM");

        testItemDisplay.setMyDatePosted("2020-06-03T15:00:00.000Z");
        assertEquals(testItemDisplay.getMyDatePosted(), "Jun 03, 2020 08:00 AM");
    }

    /**
     * Tests getting the item bitmap.
     */
    @Test
    public void testGetMyBitmap() {
        assertNull(testItemDisplay.getMyBitmap());
    }

    /**
     * Tests getting the username.
     */
    @Test
    public void testGetMyUsername() {
        assertEquals(testItemDisplay.getMyUsername(), "mzloria");
    }

    /**
     * Tests setting the username.
     */
    @Test
    public void testSetMyUsername() {
        testItemDisplay.setMyUsername("dungthai");
        assertEquals(testItemDisplay.getMyUsername(), "dungthai");

        testItemDisplay.setMyUsername("test");
        assertEquals(testItemDisplay.getMyUsername(), "test");

        testItemDisplay.setMyUsername("uwtacoma");
        assertEquals(testItemDisplay.getMyUsername(), "uwtacoma");
    }

    /**
     * Tests getting the liker id of the item.
     */
    @Test
    public void testGetMyLikerID() {
        assertEquals(testItemDisplay.getMyLikerID(), 9999);
    }

    /**
     * Tests setting the liker id of the item.
     */
    @Test
    public void testSetMyLikerID() {
        testItemDisplay.setMyLikerID(123);
        assertEquals(testItemDisplay.getMyLikerID(), 123);

        testItemDisplay.setMyLikerID(34235);
        assertEquals(testItemDisplay.getMyLikerID(), 34235);

        testItemDisplay.setMyLikerID(123567);
        assertEquals(testItemDisplay.getMyLikerID(), 123567);
    }

    /**
     * Tests getting the liked boolean value.
     */
    @Test
    public void testGetMyLiked() {
        assertTrue(testItemDisplay.getmyLiked());
    }

    /**
     * Tests setting the liked boolean value.
     */
    @Test
    public void testSetMyLiked() {
        testItemDisplay.setMyLiked(false);
        assertFalse(testItemDisplay.getmyLiked());

        testItemDisplay.setMyLiked(true);
        assertTrue(testItemDisplay.getmyLiked());
    }
}
