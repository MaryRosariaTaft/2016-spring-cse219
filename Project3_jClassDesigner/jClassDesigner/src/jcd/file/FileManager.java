package jcd.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.data.AccessType;
import static jcd.data.AccessType.PACKAGE_PRIVATE;
import jcd.data.BoxType;
import static jcd.data.BoxType.*;
import jcd.data.Connector;
import jcd.data.ConnectorType;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import jcd.data.DataManager;
//import jcd.data.DraggableEllipse;
import jcd.data.DraggableVBox;
import jcd.data.Draggable;
import static jcd.data.Draggable.RECTANGLE;
import jcd.data.MethodRow;
import jcd.data.VBoxData;
import jcd.data.VariableRow;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author Mary R. Taft
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    // TODO/NOTE: should use following format (taken from Pose Maker) for my code
    /*
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_SHAPES = "shapes";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    */
    
    static final String JSON_CLASSINTERFACE_OBJECTS = "classes_and_interfaces";
    static final String USON_CONNECTOR_OBJECTS = "connectors";
    
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	DataManager dataManager = (DataManager)data;
	
	// NOW CREATE THE JSON OBJCTS TO SAVE
	JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();
        JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
        
	//ObservableList<Node> vboxes = dataManager.getVBoxes();
        ArrayList<VBoxData> dataItems = dataManager.getDataItems();
        ArrayList<Connector> connectors = dataManager.getConnectors();
        
        for(VBoxData v : dataItems){
            String className = v.getClassName();
            String packageName = v.getPackageName();
            String parentName = v.getParentName(); //TODOTODOTODO: ArrayList of parents
            ArrayList<VariableRow> variables = v.getVariables();
            ArrayList<MethodRow> methods = v.getMethods();
            BoxType bType = v.getBoxType();
            boolean isDisplayed = v.isDisplayed();
            double x = isDisplayed ? v.getVBox().getX() : 0;
            double y = isDisplayed ? v.getVBox().getY() : 0;
            double width = isDisplayed ? v.getVBox().getBoxWidth() : 0;
            double height = isDisplayed ? v.getVBox().getBoxHeight() : 0;

            JsonObject dataJson = Json.createObjectBuilder()
                    .add("class_name", className)
                    .add("package_name", packageName)
                    .add("parent_name", parentName)
                    .add("variables", makeJsonVariablesArray(variables))
                    .add("methods", makeJsonMethodsArray(methods))
                    .add("type", bType.name().toLowerCase())
                    .add("is_displayed", isDisplayed)
                    .add("x", x)
                    .add("y", y)
                    .add("width", width)
                    .add("height", height)
                    .build();
            
            arrayBuilder1.add(dataJson);
        }
        JsonArray dataArray = arrayBuilder1.build();
        
        for(Connector c : connectors){
            String parent = c.getParent().getClassName();
            String child = c.getChild().getClassName();
            String cType = c.getConnectorType().name().toLowerCase();
            
            JsonObject connectorJson = Json.createObjectBuilder()
                    .add("parent", parent)
                    .add("child", child)
                    .add("type", cType)
                    .build();
            
            arrayBuilder2.add(connectorJson);
        }
        JsonArray connectorArray = arrayBuilder2.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                //.add(JSON_SHAPES, shapesArray)
                .add("classes_and_interfaces", dataArray)
                .add("connectors", connectorArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    public JsonArray makeJsonVariablesArray(ArrayList<VariableRow> variables){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for(VariableRow v : variables){
            String name = v.getName();
            String type = v.getType();
            boolean isStatic = v.isStatic();
            AccessType access = v.getAccess();
    
            JsonObject dataJson = Json.createObjectBuilder()
                    .add("name", name)
                    .add("type", type)
                    .add("is_static", isStatic)
                    .add("access_type", access.name().toLowerCase())
                    .build();
            arrayBuilder.add(dataJson);
        }
        
        return arrayBuilder.build();
    }

    public JsonArray makeJsonMethodsArray(ArrayList<MethodRow> methods){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for(MethodRow m : methods){
            String methodName = m.getMethodName();
            String returnType = m.getReturnType();
            boolean isStatic = m.isStatic();
            boolean isAbstract = m.isAbstract();
            AccessType access = m.getAccess(); 
            ArrayList<String> args = m.getArgs();
   
            JsonObject dataJson = Json.createObjectBuilder()
                    .add("method_name", methodName)
                    .add("return_type", returnType)
                    .add("is_static", isStatic)
                    .add("is_abstract", isAbstract)
                    .add("access_type", access.name().toLowerCase())
                    .add("args", makeJsonArgsArray(args))
                    .build();
            arrayBuilder.add(dataJson);
        }

        return arrayBuilder.build();
    }
    
    public JsonArray makeJsonArgsArray(ArrayList<String> args){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(String a : args){
            arrayBuilder.add(a);
        }
        return arrayBuilder.build();
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
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonArray dataArray = json.getJsonArray("classes_and_interfaces");
	for(int i=0; i<dataArray.size(); i++){
            JsonObject jsonDataItem = dataArray.getJsonObject(i);
            VBoxData dataItem = loadDataItem(jsonDataItem);
            dataManager.initNewData(dataItem);
        }
        JsonArray connectorArray = json.getJsonArray("connectors");
        for(int i=0; i<connectorArray.size(); i++){
            JsonObject jsonConnector = connectorArray.getJsonObject(i);
            Connector connector = loadConnector(jsonConnector, dataManager);
            dataManager.initNewConnector(connector);
        }
        
        //System.out.println(dataManager.getDataItems());
        //System.out.println(dataManager.getConnectors());
        
    }
    
/*
    public void loadDataNOGUI(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	//dataManager.reset(); //todo: add back once loading is integrated into the UI
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonArray dataArray = json.getJsonArray("classes_and_interfaces");
	for(int i=0; i<dataArray.size(); i++){
            JsonObject jsonDataItem = dataArray.getJsonObject(i);
            VBoxData dataItem = loadDataItem(jsonDataItem);
            dataManager.initNewData(dataItem);
        }
        JsonArray connectorArray = json.getJsonArray("connectors");
        for(int i=0; i<connectorArray.size(); i++){
            JsonObject jsonConnector = connectorArray.getJsonObject(i);
            Connector connector = loadConnector(jsonConnector, dataManager);
            dataManager.initNewConnector(connector);
        }
        
        //System.out.println(dataManager.getDataItems());
        //System.out.println(dataManager.getConnectors());
        
    }
*/

    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private VBoxData loadDataItem(JsonObject jsonDataItem) {

        //EXTRACT INFORMATION FROM THE JSON FILE
        String className = jsonDataItem.getString("class_name");
        String packageName = jsonDataItem.getString("package_name");
        String parentName = jsonDataItem.getString("parent_name");
        ArrayList<VariableRow> variables = loadVariables(jsonDataItem.getJsonArray("variables"));
        ArrayList<MethodRow> methods = loadMethods(jsonDataItem.getJsonArray("methods"));
        BoxType type = BoxType.valueOf(jsonDataItem.getString("type").toUpperCase());
        boolean isDisplayed = jsonDataItem.getBoolean("is_displayed");
        
	// BUILD AND SET DATA OF THE VBoxData OBJECT
	VBoxData dataItem = new VBoxData(type, isDisplayed);
        dataItem.setClassName(className);
        dataItem.setPackageName(packageName);
        dataItem.setParentName(parentName);
        for(VariableRow v : variables){
            dataItem.addVariable(v); //todo: api_class integration...
        }
        for(MethodRow m : methods){
            dataItem.addMethod(m);
        }
        dataItem.setBoxType(type); //todo: why was this commented out earlier?
        if(isDisplayed){
            dataItem.getVBox().setX((double)jsonDataItem.getInt("x"));
            dataItem.getVBox().setY((double)jsonDataItem.getInt("y"));
            dataItem.getVBox().setBoxWidth((double)jsonDataItem.getInt("width"));
            dataItem.getVBox().setBoxHeight((double)jsonDataItem.getInt("height"));
        }
        
	// ALL DONE, RETURN IT
	return dataItem;
    }

    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    private ArrayList<VariableRow> loadVariables(JsonArray jsonVariableArray){
        ArrayList<VariableRow> variableArray = new ArrayList<VariableRow>();
        for(int i=0; i<jsonVariableArray.size(); i++){
            JsonObject jsonVariable = jsonVariableArray.getJsonObject(i);
            VariableRow vr = loadVariableRow(jsonVariable);
            variableArray.add(vr);
        }
        return variableArray;
    }
    
    private VariableRow loadVariableRow(JsonObject jsonVariable){
        String name = jsonVariable.getString("name");
        String type = jsonVariable.getString("type");
        boolean isStatic = jsonVariable.getBoolean("is_static");
        AccessType access = AccessType.valueOf(jsonVariable.getString("access_type").toUpperCase());

        VariableRow vr = new VariableRow(name, type, isStatic, access);
        return vr;
    }
    
    private ArrayList<MethodRow> loadMethods(JsonArray jsonMethodArray){
        ArrayList<MethodRow> methodArray = new ArrayList<MethodRow>();
        for(int i=0; i<jsonMethodArray.size(); i++){
            JsonObject jsonMethod = jsonMethodArray.getJsonObject(i);
            MethodRow mr = loadMethodRow(jsonMethod);
            methodArray.add(mr);
        }
        return methodArray;
    }
    
    private MethodRow loadMethodRow(JsonObject jsonMethod){
        String methodName = jsonMethod.getString("method_name");
        String returnType = jsonMethod.getString("return_type");
        boolean isStatic = jsonMethod.getBoolean("is_static");
        boolean isAbstract = jsonMethod.getBoolean("is_abstract");
        AccessType access = AccessType.valueOf(jsonMethod.getString("access_type").toUpperCase());
        ArrayList<String> args = loadArgs(jsonMethod.getJsonArray("args"));
        
        MethodRow mr = new MethodRow(methodName, returnType, isStatic, isAbstract, access, args);
        return mr;
    }
    
    private ArrayList<String> loadArgs(JsonArray jsonStringArray){
        ArrayList<String> argsArray = new ArrayList<String>();
        for(int i=0; i<jsonStringArray.size(); i++){
            argsArray.add(jsonStringArray.getString(i));
        }
        return argsArray;
    }
    
    private Connector loadConnector(JsonObject jsonConnector, DataManager dataManager){
        VBoxData parent = getDataItem(jsonConnector.getString("parent"), dataManager);
        VBoxData child = getDataItem(jsonConnector.getString("child"), dataManager);
        ConnectorType type = ConnectorType.valueOf(jsonConnector.getString("type").toUpperCase());
        
        Connector c = new Connector(parent, child, type);
        return c;
    }
    
    private VBoxData getDataItem(String name, DataManager dataManager){
        ArrayList<VBoxData> dataItems = dataManager.getDataItems();
        for(VBoxData v : dataItems){
            if(v.getClassName().equals(name)){
                return v;
            }
        }
        return null;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Java skeleton file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
//        System.out.println("in exportData(); filePath = " + filePath);

	// GET THE DATA
	DataManager dataManager = (DataManager)data;
	
        ArrayList<VBoxData> dataItems = dataManager.getDataItems();
        for(VBoxData d : dataItems){
            if(d.getBoxType() != API_CLASS){
                //generate file in appropriate folder
                PrintWriter pw;
                if(d.getPackageName().equals("default") || d.getPackageName().equals("")){
                    pw = new PrintWriter(filePath + "/" + d.getClassName() + ".java");
                }else{
                    pw = new PrintWriter(filePath + "/" + getPath(d.getPackageName(), filePath) + d.getClassName() + ".java");
                }
                pw.write(generateFileContents(d));
                pw.close();
            }
        }
    }
    
    private String getPath(String s, String filePath){
        String path = s.replace('.', '/');
        new File(filePath + "/" + path).mkdirs();
        return path + "/";
    }
    
    public String generateFileContents(VBoxData d){
        String ans = "";
        //package
        if(!d.getPackageName().equals("default") && !d.getPackageName().equals("")){
            ans += "package " + d.getPackageName() + "\n\n";
        }else{
            ans += "//no package declared; the unnamed package is implicit\n\n";
        }
        //imports
        ans += generateImportStatements(d);
        //access (always public)
        ans += "public ";
        //class type (abstract class, class, or interface)
        if(d.getBoxType()==ABSTRACT_CLASS){
            ans += "abstract class ";
        }else if(d.getBoxType()==CLASS){
            ans += "class ";
        }else if(d.getBoxType()==INTERFACE){
            ans += "interface ";
        }
        //and class name
        ans += d.getClassName();
        //extends... / implements...
        if(!d.getParentName().equals("Object")){
            ans += " extends " + d.getParentName(); //todo: INCORRECT, if parent is interface then *implement*
        }
        ans += "{\n\n";
        //globals
        for(VariableRow v : d.getVariables()){
            ans += "\t";
            if(v.getAccess() != PACKAGE_PRIVATE){
                ans += v.getAccess().name().toLowerCase() + " ";
            }
            if(v.isStatic()){
                ans += "static ";
            }
            ans += v.getType() + " ";
            ans += v.getName() + " = ";
            ans += dummyVal(v.getType()) + ";\n";
        }
        ans += "\n";
        //methods with dummy return values
        for(MethodRow m : d.getMethods()){
            ans += "\t";
            if(m.isAbstract()){
                ans += "abstract ";
            }else{
                if(m.getAccess() != PACKAGE_PRIVATE){
                    ans += m.getAccess().name().toLowerCase() + " ";
                }
            }
            if(m.isStatic()){
                ans += "static ";
            }
            ans += m.getReturnType() + " ";
            ans += m.getMethodName() + "(";
            int i = 1;
            for(String arg : m.getArgs()){
                ans += arg + " " + "arg" + i + ", ";
                i++;
            }
            if(m.getArgs().size()>0){
                ans = ans.substring(0, ans.length()-2);
            }
            ans += ")";
            if(m.isAbstract() || d.getBoxType()==INTERFACE){
                ans += ";\n\n";
            }else{
                ans += "{\n";
                if(!m.getReturnType().equals("")){
                    ans += "\t\treturn " + dummyVal(m.getReturnType()) + ";";
                }
                ans += "\n\t}\n\n";
            }
        }
        ans += "}\n";
        return ans;
    }
    
    public String generateImportStatements(VBoxData d){
        //NOTE: don't import primitives, String, or wrapper classes (e.g., Double, Boolean)
        //DO import: (a) all global types & (b) all method parameter types
        return "//importing is currently nonfunctional\n\n";
    }
    
    public String dummyVal(String s){
        if(s.equals("int") || s.equals("short") || s.equals("long") || s.equals("float") || s.equals("double")){
            return "0";
        }
        if(s.equals("char")){
            return "''";
        }
        if(s.equals("boolean")){
            return "false";
        }
        if(s.equals("void")){
            return "";
        }
        return "null"; //todo: handle byte primitives
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// NOTE THAT THE jClassDesigner APPLICATION MAKES
	// NO USE OF THIS METHOD SINCE IT NEVER IMPORTS
	// EXPORTED JAVA SKELETON FILES
    }
}
