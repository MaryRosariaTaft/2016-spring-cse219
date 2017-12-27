package pm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import pm.data.DataManager;
import pm.gui.Workspace;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author Mary R. Taft
 * @version 1.0
 */
public class FileManager implements AppFileComponent {

    /**
     * This method is for saving user work as in a JSON format.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException In case there's an issue writing out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {

        StringWriter sw = new StringWriter();
        DataManager manager = (DataManager)data;
        
        JsonArrayBuilder builder = Json.createArrayBuilder();
        Workspace workspace = manager.getWorkspace();
        Pane p = workspace.getRightPane();
        
        workspace.deselect(); //so that if a shape is selected, its temporary yellow outline isn't stored
        fillArrayWithShapes(p, builder); //see below
        JsonArray shapeArray = builder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("backgroundColor", workspace.getBackgroundColor())
                .add("shapes", shapeArray)
                .build();

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();
        
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();

    }
    
    //creates a JsonObject with the attributes of each shape and adds it
    // to the JsonArrayBuilder, which then gets sent back to the
    // JsonObject in saveData() above, which finally gets output
    // to a file of the user's choice
    public void fillArrayWithShapes(Pane p, JsonArrayBuilder jab){
        for(Node s : p.getChildren()){
            if(s instanceof Rectangle){
                jab.add(rectAttributes((Rectangle)s));
            }else if(s instanceof Ellipse){
                jab.add(ellipseAttributes((Ellipse)s));
            }
        }
    }
    
    private JsonObject rectAttributes(Rectangle r){
        JsonObject attributes = Json.createObjectBuilder()
                .add("type", "r")
                .add("xpos", r.getX() + r.getTranslateX())
                .add("ypos", r.getY() + r.getTranslateY())
                .add("width", r.getWidth())
                .add("height", r.getHeight())
                .add("fill", r.getFill().toString())
                .add("stroke", r.getStroke().toString())
                .add("strokeWidth", r.getStrokeWidth())
                .build();
        return attributes;
    }
    
    private JsonObject ellipseAttributes(Ellipse r){
        JsonObject attributes = Json.createObjectBuilder()
                .add("type", "e")
                .add("xpos", r.getCenterX() + r.getTranslateX())
                .add("ypos", r.getCenterY() + r.getTranslateY())
                .add("radx", r.getRadiusX())
                .add("rady", r.getRadiusY())
                .add("fill", r.getFill().toString())
                .add("stroke", r.getStroke().toString())
                .add("strokeWidth", r.getStrokeWidth())
                .build();
        return attributes;
    }
    
    
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException In case there's an issue reading in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        JsonObject json = loadJSONFile(filePath);
        DataManager manager = (DataManager)data;
        Workspace workspace = manager.getWorkspace();
        Pane p = workspace.getRightPane();
        
        workspace.resetWorkspace(); //so that currently-open data doesn't meld with loaded data
        //set the background color
        workspace.setBackgroundColor(json.getString("backgroundColor"));
        //iterate through shapes' properties, instantiate them, and
        // add them to the right-hand pane of the workspace (via helpers)
        JsonArray shapes = json.getJsonArray("shapes");
        for(int i = 0; i < shapes.size(); i++){
            JsonObject shapesJO = shapes.getJsonObject(i);
            if(shapesJO.getString("type").equals("r")){
                makeRectangle(p, shapesJO);
            }else if(shapesJO.getString("type").equals("e")){
                makeEllipse(p, shapesJO);
            }
        }
    }
    
    private void makeRectangle(Pane p, JsonObject jo){
        Rectangle r = new Rectangle();
        r.setX((double)jo.getInt("xpos"));
        r.setY((double)jo.getInt("ypos"));
        r.setWidth((double)jo.getInt("width"));
        r.setHeight((double)jo.getInt("height"));
        r.setFill(Paint.valueOf(jo.getString("fill")));
        r.setStroke(Paint.valueOf(jo.getString("stroke")));
        r.setStrokeWidth(jo.getInt("strokeWidth"));
        p.getChildren().add(r);
    }

    private void makeEllipse(Pane p, JsonObject jo){
        Ellipse r = new Ellipse();
        r.setCenterX((double)jo.getInt("xpos"));
        r.setCenterY((double)jo.getInt("ypos"));
        r.setRadiusX((double)jo.getInt("radx"));
        r.setRadiusY((double)jo.getInt("rady"));
        r.setFill(Paint.valueOf(jo.getString("fill")));
        r.setStroke(Paint.valueOf(jo.getString("stroke")));
        r.setStrokeWidth(jo.getInt("strokeWidth"));
        p.getChildren().add(r);
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// NOTE THAT THE Web Page Maker APPLICATION MAKES
	// NO USE OF THIS METHOD SINCE IT NEVER IMPORTS
	// EXPORTED WEB PAGES
    }
}
