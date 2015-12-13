/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci_events;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author avina
 */
public class EmailTest {
    
    public EmailTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendEmail method, of class Email.
     */
    @Test
    public void testSendEmail() {
        System.out.println(" Testing Email");
        Email instance = new Email();
        instance.addRecipients("avinanick@gmail.com");
        instance.setMessage("This is a test");
        instance.sendEmail();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addRecipients method, of class Email.
     */
//    @Test
//    public void testAddRecipients() {
//        System.out.println("addRecipients");
//        String[] recipients = null;
//        Email instance = new Email();
//        instance.addRecipients(recipients);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of setMessage method, of class Email.
     */
//    @Test
//    public void testSetMessage() {
//        System.out.println("setMessage");
//        String message = "";
//        Email instance = new Email();
//        instance.setMessage(message);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
