/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import jcd.data.DataManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mary Taft
 */
public class SaveAndLoadJUnitTest {
    
    public SaveAndLoadJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("* SaveAndLoadJUnitTest: @BeforeClass method");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("* SaveAndLoadJUnitTest: @AfterClass method");
    }

    /**
     * Test of main method, of class TestSave.
     */
    @Test
    public void testMain() {
        System.out.println("* SaveJUnitTest: testing whether DataManagers have same values");
        String[] args = null;
        TestSave.main(args);
        DataManager savedData = TestSave.getDataManager();
        //        System.out.println("dataManager: " + TestSave.getDataManager().getDataItems());
        TestLoad.main(args);
        DataManager loadedData = TestLoad.getDataManager();
        //        System.out.println("dataManager: " + TestLoad.getDataManager().getDataItems());
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(savedData.getDataItems().get(3).getClassName(), loadedData.getDataItems().get(3).getClassName());
        assertEquals(savedData.getDataItems().get(3).getPackageName(), loadedData.getDataItems().get(3).getPackageName());
        assertEquals(savedData.getDataItems().get(5).getClassName(), loadedData.getDataItems().get(5).getClassName());
        assertEquals(savedData.getDataItems().get(0).getVariables().get(0).getAccess(), loadedData.getDataItems().get(0).getVariables().get(0).getAccess());
        assertEquals(savedData.getDataItems().get(0).getMethods().get(5).getArgs().get(0), loadedData.getDataItems().get(0).getMethods().get(5).getArgs().get(0));
        assertEquals(savedData.getDataItems().get(0).getMethods().get(5).getReturnType(), loadedData.getDataItems().get(0).getMethods().get(5).getReturnType());
        assertEquals(savedData.getDataItems().get(1).getParentName(), loadedData.getDataItems().get(1).getParentName());
        assertEquals(savedData.getConnectors().get(0).getChild().getClassName(), loadedData.getConnectors().get(0).getChild().getClassName());
        assertEquals(savedData.getConnectors().get(0).getParent().getClassName(), loadedData.getConnectors().get(0).getParent().getClassName());
        assertEquals(savedData.getConnectors().get(7).getConnectorType(), loadedData.getConnectors().get(7).getConnectorType());
    }
    
}
