/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import static jcd.data.AccessType.*;
import static jcd.data.BoxType.*;
import static jcd.data.ConnectorType.AGGREGATION;
import static jcd.data.ConnectorType.ASSOCIATION;
import static jcd.data.ConnectorType.IMPLEMENTATION;
import jcd.gui.Workspace;

/**
 *
 * @author Mary Taft
 */
public class VBoxData {
    
    String className;
    String packageName;
    String parentName = ""; //DEPRECATED
    ArrayList<String> parentNames;
    ArrayList<VariableRow> variables;
    ArrayList<MethodRow> methods;
    
    BoxType type;
    boolean isDisplayed;
    DraggableVBox vbox = null;
    
    public VBoxData(BoxType type, boolean isDisplayed){
        className = "";
        packageName = "";
        parentNames = new ArrayList<String>();
        variables = new ArrayList<VariableRow>();
        methods = new ArrayList<MethodRow>();
        this.type = type;
        this.isDisplayed = isDisplayed; //set to false if a user doesn't want to display box
        if(isDisplayed){
            vbox = new DraggableVBox(this);
        }
        
        //todo: REMOVE; tmp:
        /*
        VariableRow tmpv = new VariableRow("tmpName", "tmpType", true, PACKAGE_PRIVATE);
        variables.add(tmpv);
        MethodRow tmpm = new MethodRow("name", "type", false, true, PACKAGE_PRIVATE);
        methods.add(tmpm);
        tmpm.addArg("dummyType");
        */
    }
    
    public String getClassName(){
        return className;
    }
    
    public void setClassName(String s){
        className = s;
    }
    
    public String getPackageName(){
        return packageName;
    }
    
    public void setPackageName(String s){
        packageName = s;
    }
    
    public String getParentName(){ //DEPRECATED
        return parentName;
    }
    
    public void setParentName(String s){ //DEPRECATED
        parentName = s;
    }
    
    public ArrayList<String> getParentNames(){
        return parentNames;
    }
    
    public void setParentNames(ArrayList<String> p, Workspace w, DataManager dm){
        parentNames = p;
        addCorrespondingClasses(p, w, dm);
        //System.out.println(p);
    }
    
    public void addCorrespondingClasses(ArrayList<String> p, Workspace w, DataManager dm){
        for(String s : p){
            VBoxData classData = new VBoxData(API_CLASS, true);
            //init the new class's basic information
            classData.setClassName(s);
            dm.initNewDataWithoutSelecting(classData);
            
            Connector c = new Connector(this, classData, IMPLEMENTATION);
            dm.initNewConnector(c);
        }
        //System.out.println(p);
    }
    
    public String getParentNamesString(){
        String ans = "";
        for(String s : parentNames){
            ans += s + ", ";
        }
        if(ans.length()>0){
            return ans.substring(0, ans.length()-2);
        }else{
            return "(none)";
        }
    }
    
    public void addParentName(String s){
        parentNames.add(s);
    }
    
    public void removeParentName(String s){
        parentNames.remove(s);
    }
            
    public ArrayList<VariableRow> getVariables(){
        return variables;
    }
    
    public void addVariable(VariableRow vr){
        //todo: check that no variable has the same name
        variables.add(vr);
    }
    
    public void addVariable(VariableRow vr, Workspace w, DataManager dm){
        //todo: check that no variable has the same name
        variables.add(vr);
        addCorrespondingClasses(vr, w, dm);
        
        //System.out.println("new: " + vr);
        //System.out.println("total: " + variables);
    }
    
    public void addCorrespondingClasses(VariableRow vr, Workspace workspace, DataManager dataManager){
        //if(!ArrayList.contains(class-to-be-added-based-on-type)){
        VBoxData classData = new VBoxData(API_CLASS, true);
        //init the new class's basic information
        classData.setClassName(vr.getType());
        dataManager.initNewDataWithoutSelecting(classData);
        
        Connector c = new Connector(this, classData, AGGREGATION);
        dataManager.initNewConnector(c);
        //System.out.println(dataManager.getConnectors());
        /*
        else{
        don't add
        }
        */
    }
    
    public void removeVariable(int index){
        if(index < variables.size()){
            variables.remove(index);
        }
    }
    
    public void removeVariable(VariableRow vr){
        variables.remove(vr);
    }
    
    public ArrayList<MethodRow> getMethods(){
        return methods;
    }
    
    public void addMethod(MethodRow mr){
        methods.add(mr);
        if(mr.isAbstract()){
            type = ABSTRACT_CLASS;
        }
    }
    
    public void addMethod(MethodRow mr, Workspace w, DataManager dm){
        //todo: check that no method has the same signature (name & args)
        methods.add(mr);
        if(mr.isAbstract()){
            type = ABSTRACT_CLASS;
        }
        addCorrespondingClasses(mr, w, dm);
    }
    
    public void addCorrespondingClasses(MethodRow mr, Workspace workspace, DataManager dataManager){
        //if(!ArrayList.contains(class-to-be-added-based-on-type)){
        VBoxData classData = new VBoxData(API_CLASS, true);
        //init the new class's basic information
        classData.setClassName(mr.getReturnType());
        dataManager.initNewDataWithoutSelecting(classData);
        
        Connector c = new Connector(this, classData, ASSOCIATION);
        dataManager.initNewConnector(c);
        /*
        else{
        don't add
        }
        */
        for(String s : mr.getArgs()){
            VBoxData v = new VBoxData(API_CLASS, true);
            v.setClassName(s);
            dataManager.initNewDataWithoutSelecting(v);
            
            Connector d = new Connector(this, v, ASSOCIATION);
            dataManager.initNewConnector(d);
        }
        //System.out.println(dataManager.getConnectors());
    }

    public void removeMethod(int index){
        if(index < methods.size()){
            methods.remove(index);
        }
    }
    
    public void removeMethod(MethodRow mr){
        methods.remove(mr);
        //todo: if all abstract rows are gone, is no longer an abstract class
    }
    
    public ObservableList getVariablesObservableList(){
        return FXCollections.observableArrayList(variables);
    }
    
    public ObservableList getMethodsObservableList(){
        return FXCollections.observableArrayList(methods);
    }
    
    public BoxType getBoxType(){
        return type;
    }
    
    public void setBoxType(BoxType t){
        type = t;
    }
    
    public boolean isDisplayed(){
        return isDisplayed;
    }
    
    public void setDisplayed(boolean b){
        isDisplayed = b;
    }
    
    public DraggableVBox getVBox(){
        return vbox;
    }
    
    public String displayData(){
        String ans = "";
        ans += displayClassName();
        ans += displayVariables();
        ans += displayMethods();
        return ans;
    }
    
    public String displayClassName(){
        String ans = "";
        if(type==ABSTRACT_CLASS){
            ans += "{abstract}\n";
        }
        if(type==INTERFACE){
            ans += "<<interface>>\n";
        }
        ans += className;
        return ans;
    }
    
    public String displayVariables(){
        String ans = "\n";
        for(VariableRow vr : variables){
            ans += vr.toString() + "\n";
        }
        return ans.substring(0, ans.length()-1); //todo: remove extra newline
    }
    
    public String displayMethods(){
        String ans = "\n";
        for(MethodRow mr : methods){
            ans += mr.toString() + "\n";
        }
        return ans.substring(0, ans.length()-1); //todo: remove extra newline
    }
       
    @Override
    public String toString(){
        return className;
    }
    
}
