import javafx.scene.control.ListCell;

/**
 *
 * @author Leon Verhelst
 */
public class CheckBoxCell extends ListCell<Course> {

    public CheckBoxCell() {
    }

    @Override
    protected void updateItem(Course item, boolean empty) {
        //call super (oracle docs say this is uber important)
        super.updateItem(item, empty);

        if (item != null) {
            this.setText(item.getTitle());
        }
                
    }
}
