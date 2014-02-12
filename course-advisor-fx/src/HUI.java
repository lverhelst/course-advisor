
/**
 *
 * @author Leon Verhelst
 */
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HUI extends Application implements Initializable {

    private static final ObservableList<Course> _courses = FXCollections.observableArrayList();

    @FXML
    private ListView<Course> _listview;

    @Override
    public void start(Stage stage) throws Exception {

        String filename = "GUI.fxml";
        try {
            Pane page = (Pane) FXMLLoader.load(HUI.class.getResource(filename));
            Scene scene = new Scene(page);
            stage.setScene(scene);
           
            stage.setTitle("Course Advisor");

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         CourseList cl = new CourseList();
        if (!cl.loadCourseList()) {
            System.out.println("Could not load courses.");
        }
        _listview.setCellFactory(new Callback<ListView<Course>, ListCell<Course>>() {
            @Override
            public ListCell<Course> call(ListView<Course> list) {
                return new CheckBoxCell();
            }
        });
        _listview.setItems(_courses);
        
        _courses.addAll(Arrays.asList(cl.getCourses()));
    }

   /* @FXML
    void initialize() {
        assert _listview != null : "fx:id=\"_listview\" was not injected";
        
    }
    */

}
