package sample.CustomPanes;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomAlterPane extends VBox {

    // Global vars
    Label alterLabel = new Label("Alter or Edit Pane");
    Label cityNameLabel1R = new Label("Selected Cities Name:");
    TextField cityNameTF2R = new TextField("No city selected");

    VBox nameRVBox = new VBox(cityNameLabel1R, cityNameTF2R);
    Label setRestrictions1 = new Label("Travel restrictions imposed from target city:");
    TextField setRestrictions2 = new TextField("No city selected"); // Measured in a percentage

    VBox resVBox = new VBox(setRestrictions1, setRestrictions2);

    // Constructor.
    public CustomAlterPane()
    {
        // Alter page.
        // No city is initially selected at the start of the program, so set to default values.
        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 0 5 5 25;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");

        alterLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        alterLabel.setPadding(new Insets(0,0,10,0));

        cityNameTF2R.setEditable(false);
        nameRVBox.setSpacing(4);
        nameRVBox.setPadding(new Insets(2,1,2,1));

        //cityPopTF2.setEditable(true);
        resVBox.setSpacing(4);
        resVBox.setPadding(new Insets(2,1,2,1));

        this.getChildren().addAll(alterLabel, nameRVBox, resVBox);
        this.setPrefWidth(300);
    }

    // Setters.
    public void setAlterCityName(String value)
    {
        cityNameTF2R.setText(value);
    }
    public void setAlterCityRestrictions(String value)
    {
        setRestrictions2.setText(value);
    }
}
