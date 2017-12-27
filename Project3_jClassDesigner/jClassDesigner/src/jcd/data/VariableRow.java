/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import static jcd.data.AccessType.*;

/**
 *
 * @author Mary Taft
 */
public class VariableRow {
    
    String name = "";
    String type = "";
    boolean isStatic = false;
    AccessType access = PACKAGE_PRIVATE;
    
    public VariableRow(){
        //do nothing
    }
    
    public VariableRow(String n, String t, boolean s, AccessType a){
        name = n;
        type = t;
        isStatic = s;
        access = a;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public boolean isStatic(){
        return isStatic;
    }
    
    public boolean getIsStatic(){
        return isStatic;
    }
    
    public AccessType getAccess(){
        return access;
    }
    
    public void setName(String name){
        this.name = name;
        //System.out.println(name);
    }
    
    public void setType(String type){
        this.type = type;
        //System.out.println(this.type);
    }
    
    public void setStatic(boolean b){
        isStatic = b;
        //System.out.println(isStatic);
    }
    
    public void setAccess(String access){
        if(access.contains("Public")){
            this.access = PUBLIC;
        }else if(access.contains("Package")){
            this.access = PACKAGE_PRIVATE;
        }else if(access.contains("Private")){
            this.access = PRIVATE;
        }else if(access.contains("Protected")){
            this.access = PROTECTED;
        }
        //System.out.println(this.access.toString());
    }
    
    @Override
    public String toString(){
        String ans = "";
        if(access != null) switch(access){
            case PUBLIC:
                ans += "+";
                break;
            case PRIVATE:
                ans += "-";
                break;
            case PROTECTED:
                ans += "#";
                break;
            case PACKAGE_PRIVATE:
                ans += "~";
                break;
            default:
                break;
        }
        if(isStatic) ans += "$";
        ans += name + " : ";
        ans += type;
        return ans;
    }
    
}
