/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author Mary Taft
 */
public class ParentRow extends HBox {
    
    private Label parentLabel = new Label("Parent: ");
    private ComboBox parentComboBox = new ComboBox();
    
    public ParentRow(){
        getChildren().add(parentLabel);
        getChildren().add(parentComboBox);
    }
    
}
