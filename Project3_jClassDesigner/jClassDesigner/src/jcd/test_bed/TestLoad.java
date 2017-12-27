/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import java.util.ArrayList;
import jcd.data.Connector;
import jcd.data.DataManager;
import jcd.data.VBoxData;
import jcd.file.FileManager;

/**
 *
 * @author Mary Taft
 */
public class TestLoad {
    private static final DataManager dataManager = new DataManager();
    private static final FileManager fileManager = new FileManager();
    
    public static DataManager getDataManager(){
        return dataManager;
    }
        
    public static void main(String[] args){
        try{
            fileManager.loadData(dataManager, "./work/DesignSaveTest.jcd");
        }catch(IOException ioe){
            //DO NOTHING (todo: should open a dialog...)
            //System.out.println("problem saving :( "); //tmp
        }
        
        ArrayList<VBoxData> dataItems = dataManager.getDataItems();
        System.out.println("Data Items: " + dataItems + "\n");
        for(VBoxData v : dataItems){
            System.out.println(v.displayData() + "\n>parent: " + v.getParentName() + 
                    "\n>package: " + v.getPackageName() + "\n");
        }

        System.out.println("--------\n");
        
        ArrayList<Connector> connectors = dataManager.getConnectors();
        System.out.println("Connectors: " + connectors + "\n");
        //insert for-loop
        for(Connector c : connectors){
            System.out.println(c);
        }

    }

}
