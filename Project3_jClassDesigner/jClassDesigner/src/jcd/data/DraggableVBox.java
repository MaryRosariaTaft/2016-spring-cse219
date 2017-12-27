package jcd.data;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import static jcd.data.BoxType.*;

/**
 * This is a draggable rectangle for our application.
 * 
 * @author McKillaGorilla
 * @author Mary R. Taft
 * @version 1.0
 */

public class DraggableVBox extends VBox implements Draggable {
    VBoxData data;
    
    double startX;
    double startY;
    double startWidth;
    double startHeight;
    
    Text classNameText = new Text("");
    Text variablesText = new Text("");
    Text methodsText = new Text("");
    
    /*
    public DraggableVBox() {
//	setX(0.0);
//	setY(0.0);
        //note: edited default height/width for the sake of testing
//      setWidth(0.0);
//	setHeight(0.0);
	setOpacity(0.5);
	startX = 100.0;
	startY = 100.0;
        getChildren().addAll(classNameText, variablesText, methodsText);
        // TODO: below is tmp. put styling in the css file. & add lines btwn children.
        setStyle("-fx-padding: 15;\n" +
                "    -fx-spacing: 10;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-color: #cd853f;\n"
        );

    }
    */
    
    public DraggableVBox(VBoxData data){
        this.data = data;
	setOpacity(0.9);//0.5);
	startX = 0.0;
	startY = 0.0;
        setX(startX);
        setY(startY);
        if(data.getBoxType() == API_CLASS){
            startWidth = 130.0;
            startHeight = 70.0;
        }else{
            startWidth = 170.0;
            startHeight = 150.0;
        }
        setBoxWidth(startWidth);
        setBoxHeight(startHeight);
        updateText();
        getChildren().addAll(classNameText, variablesText, methodsText);
        // TODO: below is tmp. put styling in the css file. & add lines btwn children.
        setStyle("-fx-padding: 15;\n" +
                "    -fx-spacing: 10;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-color: #cd853f;\n" +
                "    -fx-background-color: #ffffff;\n"
        );
    }
    
    public void updateText(){
        classNameText.setText(data.displayClassName());
        variablesText.setText(data.displayVariables());
        methodsText.setText(data.displayMethods());
    }

    /*
    @Override
    public jClassDesignerState getStartingState() {
	return jClassDesignerState.STARTING_RECTANGLE;
    }
    */
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
//        setTranslateX(x);
//        setTranslateY(y);
//	setX(x);
//	setY(y);
    }
    
    @Override
    public void drag(int x, int y) {
//        System.out.println("successfully reached DraggbaleVBox's drag() method");

        relocate(x, y);
        startX = x;
        startY = y;
    }
    
    public void dragSnapped(int x, int y){
        //TODOTODOTODO; might have to add another param for zoom val
        // (or use a getter from Parent, who knows?)
        relocate(x-x%15, y-y%15);
        startX = x-x%15;
        startY = y-y%15;
    }
    
//    public String cT(double x, double y) {
//	return "(x,y): (" + x + "," + y + ")";
//    }
    
    @Override
    public void size(int x, int y) {
//	double width = x - getX();
//	widthProperty().set(width);
//	double height = y - getY();
//	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
//	xProperty().set(initX);
//	yProperty().set(initY);
//	widthProperty().set(initWidth);
//	heightProperty().set(initHeight);
    }
    
    public VBoxData getData(){
        return data;
    }
    
    @Override
    public double getX() {
        return startX;
    }
    
    public SimpleDoubleProperty getXDoubleProperty(){
        return new SimpleDoubleProperty(startX);
    }

    @Override
    public double getY() {
        return startY;
    }
    
    public SimpleDoubleProperty getYDoubleProperty(){
        return new SimpleDoubleProperty(startY);
    }

    public double getBoxWidth(){
        return startWidth;
    }
    
    public double getBoxHeight(){
        return startHeight;
    }
    
    public void setX(double x){
        startX = x;
        setTranslateX(startX);
    }
    
    public void setY(double y){
        startY = y;
        setTranslateY(startY);
    }
    
    public void setBoxWidth(double w){
        startWidth = w;
        setMinWidth(w);
        setMaxWidth(w);
    }
    
    public void setBoxHeight(double h){
        startHeight = h;
        setMinHeight(h);
        setMaxHeight(h);
    }
    
}
