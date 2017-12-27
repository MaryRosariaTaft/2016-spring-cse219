/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import java.util.Arrays;
import static jcd.data.AccessType.*;
import static jcd.data.BoxType.ABSTRACT_CLASS;

/**
 *
 * @author Mary Taft
 */
public class MethodRow {
    
    String methodName = "";
    String returnType = "";
    boolean isStatic = false;
    boolean isAbstract = false;
    AccessType access = PACKAGE_PRIVATE;
    ArrayList<String> args = new ArrayList<String>();
    VBoxData data = null;
    
    public MethodRow(VBoxData vboxData){
        //do nothing
        data = vboxData;
    }
    
    public MethodRow(String n, String r, boolean s, boolean ab, AccessType ac){
        methodName = n;
        returnType = r;
        isStatic = s;
        isAbstract = ab;
        access = ac;
    }
    
    public MethodRow(String n, String r, boolean s, boolean ab, AccessType ac, ArrayList<String> ar){
        methodName = n;
        returnType = r;
        isStatic = s;
        isAbstract = ab;
        access = ac;
        args = ar;
    }
    
    public String getMethodName(){
        return methodName;
    }
    
    public void setMethodName(String s){
        methodName = s;
    }
    
    public String getReturnType(){
        return returnType;
    }
    
    public void setReturnType(String s){
        returnType = s;
    }
    
    public boolean isStatic(){
        return isStatic;
    }
    
    public boolean getIsStatic(){
        return isStatic;
    }
    
    public void setStatic(boolean b){
        isStatic = b;
    }
    
    public boolean isAbstract(){
        return isAbstract;
    }
    
    public boolean getIsAbstract(){
        return isAbstract;
    }
    
    public void setAbstract(boolean b){
        isAbstract = b;
        if(b){
            data.setBoxType(ABSTRACT_CLASS);
        }
    }
    
    public AccessType getAccess(){
        return access;
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
    }
    
    public String getArgsString(){
        String ans = "";
        int i = 1;
        for (String arg : args) {
            ans += "arg" + i + " : " + arg + ", ";
            i++;
        }
        //comma delimiting
        if(args.size()>0){
            ans = ans.substring(0, ans.length()-2);
        }
        return ans;
    }

    public ArrayList<String> getArgs(){
        return args;
    }
    
    public void setArgs(String argsString){
        String[] tmp = argsString.split("\\s*,\\s*");
        ArrayList<String> alsoTmp = new ArrayList<String>();
        for(String s : tmp){
            alsoTmp.add(s);
        }
        args = alsoTmp;
    }
    
    public MethodRow addArg(String arg){
        args.add(arg);
        return this;
    }
    
    public MethodRow removeArg(String arg){
        args.remove(arg);
        return this;
    }
    
    @Override
    public String toString(){
        String ans = "";
        if(isAbstract) ans += "{abstract}";
        else if(access != null) switch(access){
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
        ans += methodName + "(";
        int i = 1;
        for (String arg : args) {
            ans += "arg" + i + " : " + arg + ", ";
            i++;
        }
        //comma delimiting
        if(args.size()>0){
            ans = ans.substring(0, ans.length()-2);
        }
        ans += ") ";
        if(!returnType.equals("")){ //accounts for constructors
            ans += ": " + returnType + " ";
        }
        return ans;
    }
    
}
