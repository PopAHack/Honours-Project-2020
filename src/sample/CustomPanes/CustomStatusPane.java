package sample.CustomPanes;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomStatusPane extends VBox {
    // Global Vars:
    Label statusLabel = new Label("Status Pane");
    Label cityNameLabel1 = new Label("Selected Cities Name:");
    TextField cityNameTF2 = new TextField("No city selected");

    VBox nameVBox = new VBox(cityNameLabel1, cityNameTF2);
    Label cityPopLabel1 = new Label("City Population:");
    TextField cityPopTF2 = new TextField("No city selected");

    VBox popVBox = new VBox(cityPopLabel1, cityPopTF2);
    Label minEffDisLabel1 = new Label("Minimum Effective Distance:");
    TextField minEffDisTF2 = new TextField("No city selected");
    VBox minEffDisVBox = new VBox(minEffDisLabel1, minEffDisTF2);

    Label caseIncidenceLabel1 = new Label("Case Incidence Value:");
    TextField caseIncidenceTF2 = new TextField("No city selected");

    VBox caseIncidenceVBox = new VBox(caseIncidenceLabel1, caseIncidenceTF2);

    // Constructor.
    public CustomStatusPane()
    {
        // No city is initially selected at the start of the program, so set to default values.
        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 0 25 5 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");

        statusLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        statusLabel.setPadding(new Insets(0,0,10,0));

        cityNameTF2.setEditable(false);

        nameVBox.setSpacing(4);
        nameVBox.setPadding(new Insets(2,1,2,1));

        cityPopTF2.setEditable(false);

        popVBox.setSpacing(4);
        popVBox.setPadding(new Insets(2,1,2,1));

        minEffDisTF2.setEditable(false);

        minEffDisVBox.setSpacing(4);
        minEffDisVBox.setPadding(new Insets(2,1,2,1));

        caseIncidenceTF2.setEditable(false);

        caseIncidenceVBox.setSpacing(4);
        caseIncidenceVBox.setPadding(new Insets(2,1,2,1));

        this.getChildren().addAll(statusLabel, nameVBox, popVBox, minEffDisVBox, caseIncidenceVBox);
        this.setPrefWidth(300);
    }

    // Setters
    public void setCityName(String value)
    {
        cityNameTF2.setText(value);
    }
    public void setCityPop(String value)
    {
        cityPopTF2.setText(value);
    }
    public void setCityEffDis(String value)
    {
        minEffDisTF2.setText(value);
    }
    public void setCityCaseIncidence(String value)
    {
        caseIncidenceTF2.setText(value);
    }
}
