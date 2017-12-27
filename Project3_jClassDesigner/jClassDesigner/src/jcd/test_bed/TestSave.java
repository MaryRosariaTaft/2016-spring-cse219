/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import java.util.ArrayList;
import static jcd.data.AccessType.*;
import static jcd.data.BoxType.*;
import jcd.data.BoxType;
import jcd.data.Connector;
import static jcd.data.ConnectorType.*;
import jcd.data.DataManager;
import jcd.data.MethodRow;
import jcd.data.VBoxData;
import jcd.data.VariableRow;
import jcd.file.FileManager;

/**
 *
 * @author Mary Taft
 */
public class TestSave {
    
    private static final DataManager dataManager = new DataManager();
    private static final FileManager fileManager = new FileManager();
    
    public static DataManager getDataManager(){
        return dataManager;
    }
        
    public static void main(String[] args){
        /*
        //CLASSES
        VBoxData ThreadExample = new VBoxData();
        VBoxData CounterTask = new VBoxData();
        VBoxData DateTask = new VBoxData();
        VBoxData PauseHandler = new VBoxData();
        VBoxData StartHandler = new VBoxData();
        makeThreadExample(ThreadExample);
        makeCounterTask(CounterTask);
        makeDateTask(DateTask);
        makePauseHandler(PauseHandler);
        makeStartHandler(StartHandler);
        
        //ABSTRACT CLASS / INTERFACE
        VBoxData Application = new VBoxData();
        VBoxData EventHandler = new VBoxData();
        makeApplication(Application);
        makeEventHandler(EventHandler);
        
        //API CLASSES
        VBoxData Task = new VBoxData();
        VBoxData Date = new VBoxData();
        VBoxData Platform = new VBoxData();
        VBoxData Stage = new VBoxData();
        VBoxData BorderPane= new VBoxData();
        VBoxData FlowPane = new VBoxData();
        VBoxData Button = new VBoxData();
        VBoxData ScrollPane = new VBoxData();
        VBoxData TextArea = new VBoxData();
        VBoxData Thread = new VBoxData();
        makeTask(Task);
        makeDate(Date);
        makePlatform(Platform);
        makeStage(Stage);
        makeBorderPane(BorderPane);
        makeFlowPane(FlowPane);
        makeButton(Button);
        makeScrollPane(ScrollPane);
        makeTextArea(TextArea);
        makeThread(Thread);
        
        //CONNECTORS
        dataManager.initNewConnector(new Connector(Task, CounterTask, INHERITANCE));
        dataManager.initNewConnector(new Connector(Task, DateTask, INHERITANCE));
        dataManager.initNewConnector(new Connector(Platform, CounterTask, ASSOCIATION));
        dataManager.initNewConnector(new Connector(Platform, DateTask, ASSOCIATION));
        dataManager.initNewConnector(new Connector(DateTask, Date, AGGREGATION));
        dataManager.initNewConnector(new Connector(EventHandler, PauseHandler, IMPLEMENTATION));
        dataManager.initNewConnector(new Connector(EventHandler, StartHandler, IMPLEMENTATION));
        dataManager.initNewConnector(new Connector(Application, ThreadExample, INHERITANCE));
        dataManager.initNewConnector(new Connector(ThreadExample, CounterTask, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, DateTask, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, PauseHandler, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, StartHandler, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, Stage, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, BorderPane, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, FlowPane, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, Button, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, ScrollPane, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, TextArea, AGGREGATION));
        dataManager.initNewConnector(new Connector(ThreadExample, Thread, AGGREGATION));

        //System.out.println(ThreadExample.getVariables());
        //System.out.println(ThreadExample.getMethods());
        //System.out.println(dataManager.getDataItems());
        //System.out.println(dataManager.getConnectors());
        
        try{
            fileManager.saveData(dataManager, "./work/DesignSaveTest.jcd");
        }catch(IOException ioe){
            //DO NOTHING (todo: should open a dialog...)
            //System.out.println("problem saving :( "); //tmp
        }
        */
    }
    /*
    private static void makeThreadExample(VBoxData data){
        data.setClassName("ThreadExample");
        data.setPackageName("default");
        data.setParentName("Application");
        ArrayList<VariableRow> vars = new ArrayList<VariableRow>();
        vars.add(new VariableRow("START_TEXT", "String", true, PUBLIC));
        vars.add(new VariableRow("PAUSE_TEXT", "String", true, PUBLIC));
        vars.add(new VariableRow("window", "Stage", false, PRIVATE));
        vars.add(new VariableRow("appPane", "BorderPane", false, PRIVATE));
        vars.add(new VariableRow("topPane", "FlowPane", false, PRIVATE));
        vars.add(new VariableRow("startButton", "Button", false, PRIVATE));
        vars.add(new VariableRow("pauseButton", "Button", false, PRIVATE));
        vars.add(new VariableRow("scrollPane", "ScrollPane", false, PRIVATE));
        vars.add(new VariableRow("textArea", "TextArea", false, PRIVATE));
        vars.add(new VariableRow("dateThread", "Thread", false, PRIVATE));
        vars.add(new VariableRow("dateTask", "Task", false, PRIVATE));
        vars.add(new VariableRow("counterThread", "Thread", false, PRIVATE));
        vars.add(new VariableRow("counterTask", "Task", false, PRIVATE));
        vars.add(new VariableRow("work", "boolean", false, PRIVATE));
        for(VariableRow v : vars){
            data.addVariable(v);
        }
        ArrayList<MethodRow> mtds = new ArrayList<MethodRow>();
        mtds.add(new MethodRow("start", "void", false, false, PUBLIC).addArg("Stage"));
        mtds.add(new MethodRow("startWork", "void", false, false, PUBLIC));
        mtds.add(new MethodRow("pauseWork", "void", false, false, PUBLIC));
        mtds.add(new MethodRow("doWork", "boolean", false, false, PUBLIC));
        mtds.add(new MethodRow("appendText", "void", false, false, PUBLIC).addArg("String"));
        mtds.add(new MethodRow("sleep", "void", false, false, PUBLIC).addArg("int"));
        mtds.add(new MethodRow("initLayout", "void", false, false, PRIVATE));
        mtds.add(new MethodRow("initHandlers", "void", false, false, PRIVATE));
        mtds.add(new MethodRow("initWindow", "void", false, false, PRIVATE).addArg("Stage"));
        mtds.add(new MethodRow("initThreads", "void", false, false, PRIVATE));
        mtds.add(new MethodRow("main", "void", true, false, PUBLIC).addArg("String[]"));
        for(MethodRow m : mtds){
            data.addMethod(m);
        }
        data.setBoxType(CLASS);
        dataManager.initNewData(data);
    }

    private static void makeCounterTask(VBoxData data){
        data.setClassName("CounterTask");
        data.setPackageName("default");
        data.setParentName("Task");
        ArrayList<VariableRow> vars = new ArrayList<VariableRow>();
        vars.add(new VariableRow("app", "ThreadExample", false, PRIVATE));
        vars.add(new VariableRow("counter", "int", false, PRIVATE));
        for(VariableRow v : vars){
            data.addVariable(v);
        }
        ArrayList<MethodRow> mtds = new ArrayList<MethodRow>();
        mtds.add(new MethodRow("CounterTask", "", false, false, PUBLIC).addArg("ThreadExample"));
        mtds.add(new MethodRow("call", "void", false, false, PROTECTED));
        for(MethodRow m : mtds){
            data.addMethod(m);
        }
        data.setBoxType(CLASS);
        dataManager.initNewData(data);
    }
        
    private static void makeDateTask(VBoxData data){
        data.setClassName("DateTask");
        data.setPackageName("default");
        data.setParentName("Task");
        ArrayList<VariableRow> vars = new ArrayList<VariableRow>();
        vars.add(new VariableRow("app", "ThreadExample", false, PRIVATE));
        vars.add(new VariableRow("now", "Date", false, PRIVATE));
        for(VariableRow v : vars){
            data.addVariable(v);
        }
        ArrayList<MethodRow> mtds = new ArrayList<MethodRow>();
        mtds.add(new MethodRow("DateTask", "", false, false, PUBLIC).addArg("ThreadExample"));
        mtds.add(new MethodRow("call", "void", false, false, PROTECTED));
        for(MethodRow m : mtds){
            data.addMethod(m);
        }
        data.setBoxType(CLASS);
        dataManager.initNewData(data);
    }
        
    private static void makePauseHandler(VBoxData data){
        data.setClassName("PauseHandler");
        data.setPackageName("default");
        data.setParentName("EventHandler");
        ArrayList<VariableRow> vars = new ArrayList<VariableRow>();
        vars.add(new VariableRow("app", "ThreadExample", false, PRIVATE));
        for(VariableRow v : vars){
            data.addVariable(v);
        }
        ArrayList<MethodRow> mtds = new ArrayList<MethodRow>();
        mtds.add(new MethodRow("PauseHandler", "", false, false, PUBLIC).addArg("ThreadExample"));
        mtds.add(new MethodRow("handle", "void", false, false, PUBLIC).addArg("Event"));
        for(MethodRow m : mtds){
            data.addMethod(m);
        }
        data.setBoxType(CLASS);
        dataManager.initNewData(data);
    }
        
    private static void makeStartHandler(VBoxData data){
        data.setClassName("StartHandler");
        data.setPackageName("default");
        data.setParentName("EventHandler");
        ArrayList<VariableRow> vars = new ArrayList<VariableRow>();
        vars.add(new VariableRow("app", "ThreadExample", false, PRIVATE));
        for(VariableRow v : vars){
            data.addVariable(v);
        }
        ArrayList<MethodRow> mtds = new ArrayList<MethodRow>();
        mtds.add(new MethodRow("StartHandler", "", false, false, PUBLIC).addArg("ThreadExample"));
        mtds.add(new MethodRow("handle", "void", false, false, PUBLIC).addArg("Event"));
        for(MethodRow m : mtds){
            data.addMethod(m);
        }
        data.setBoxType(CLASS);
        dataManager.initNewData(data);
    }
    */
    
    private static void makeApplication(VBoxData data){
        data.setClassName("Application");
        data.setPackageName("default");
        data.setParentName("Object");
        data.addMethod(new MethodRow("start", "void", false, true, PUBLIC).addArg("Stage"));
        data.setBoxType(ABSTRACT_CLASS);
        dataManager.initNewData(data);
    }

    private static void makeEventHandler(VBoxData data){
        data.setClassName("EventHandler");
        data.setPackageName("default");
        data.setParentName("Object");
        data.addMethod(new MethodRow("handle", "void", false, false, PUBLIC).addArg("Event"));
        data.setBoxType(INTERFACE);
        dataManager.initNewData(data);
    }

    private static void makeTask(VBoxData data){
        data.setClassName("Task");
        data.setPackageName(""); //Package.getPackage("Task").toString());
        data.setParentName(""); //todo: should fix
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeDate(VBoxData data){
        data.setClassName("Date");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makePlatform(VBoxData data){
        data.setClassName("Platform");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }

    private static void makeStage(VBoxData data){
        data.setClassName("Stage");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeBorderPane(VBoxData data){
        data.setClassName("BorderPane");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeFlowPane(VBoxData data){
        data.setClassName("FlowPane");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeButton(VBoxData data){
        data.setClassName("Button");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeScrollPane(VBoxData data){
        data.setClassName("ScrollPane");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeTextArea(VBoxData data){
        data.setClassName("TextArea");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
    private static void makeThread(VBoxData data){
        data.setClassName("Thread");
        data.setPackageName("");
        data.setParentName("");
        data.setBoxType(API_CLASS);
        dataManager.initNewData(data);
    }
    
}
