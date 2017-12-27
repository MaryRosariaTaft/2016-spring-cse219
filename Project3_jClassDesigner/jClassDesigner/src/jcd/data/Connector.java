/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import static jcd.data.ConnectorType.*;

/**
 *
 * @author Mary Taft
 */
public class Connector {
    
    VBoxData parent;
    VBoxData child;
    ConnectorType type;
    
    Line line; //graphical portion
    
    public Connector(VBoxData parent, VBoxData child, ConnectorType type){
        this.parent = parent;
        this.child = child;
        this.type = type;
        line = new Line();
        if(type == AGGREGATION){
            line.setStroke(Color.web("ff0000"));
        }else if(type == ASSOCIATION){
            line.setStroke(Color.web("00ff00"));
        }else{
            line.setStroke(Color.web("0000bb"));
        }
        updateEndPoints();
//        line.setStartX(child.getVBox().getX());
//        line.setStartY(child.getVBox().getY());
//        line.setEndX(parent.getVBox().getX());
//        line.setEndY(parent.getVBox().getY());
//        line.startXProperty().bind(child.getVBox().getXDoubleProperty());
//        line.startYProperty().bind(child.getVBox().getYDoubleProperty());
//        line.endXProperty().bind(parent.getVBox().getXDoubleProperty());
//        line.endYProperty().bind(parent.getVBox().getYDoubleProperty());
    }
    
    public void updateEndPoints(){
        line.setStartX(child.getVBox().getX());
        line.setStartY(child.getVBox().getY());
        line.setEndX(parent.getVBox().getX());
        line.setEndY(parent.getVBox().getY());
    }
    
    public VBoxData getParent(){
        return parent;
    }
    
    public VBoxData getChild(){
        return child;
    }
    
    public ConnectorType getConnectorType(){
        return type;
    }
    
    public Line getLine(){
        return line;
    }
    
    @Override
    public String toString(){
        return parent.toString() + " <-- " + child.toString();
    }
    
}
