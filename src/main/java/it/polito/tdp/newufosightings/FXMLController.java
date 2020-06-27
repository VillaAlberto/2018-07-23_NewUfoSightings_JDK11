package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	try {
			int anno = Integer.parseInt(txtAnno.getText());
			String shape= cmbBoxForma.getValue();
			model.creaGrafo(anno, shape);
			txtResult.setText("Grafo creato\n");
			for (State s: model.listaVertici())
			{
				txtResult.appendText(String.format("%s :%d\n", s, model.pesoAdiacenti(s)));
			}
		} catch (Exception e) {
			txtResult.appendText("Errore");
		}
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	try {
			int anno= Integer.parseInt(txtAnno.getText());
			if (anno>2014&&anno<1910)
			{
				txtResult.setText("Inserisci anno tra 1910 e 2014");
				return;
			}
			List<String> ls= model.getAllShapes(anno);
			ls.remove(0);
			cmbBoxForma.getItems().setAll(ls);
			cmbBoxForma.setValue(ls.get(0));
			
		} 
    	catch (NumberFormatException e) {
			txtResult.setText("Non hai introdotto un numero");
		}
    	catch (Exception e) {
    		txtResult.setText("Errore!");
		}
    }

    @FXML
    void doSimula(ActionEvent event) {

    	try {
			int t= Integer.parseInt(txtT1.getText());
			if (t<0||t>365)
			{
				txtResult.setText("Introduci un numero t tra 0 e 365");
				return;
			}
			
			int alfa= Integer.parseInt(txtAlfa.getText());
			if (alfa<0||alfa>100)
			{
				txtResult.setText("Introduci un numero alfa tra 0 e 100");
				return;
			}
			
			int anno= Integer.parseInt(txtAnno.getText());
			if (anno<1910||alfa>2014)
			{
				txtResult.setText("Introduci un anno tra 1910 e 2014");
				return;
			}
			
			String shape= cmbBoxForma.getValue();
			
			Map<String,Double> mappa= model.simula(t, alfa, anno, shape);
			txtResult.setText("Simulazione\n");
			for (String s: mappa.keySet()) {
				txtResult.appendText(String.format("%s :%.1f\n", s,mappa.get(s)));
			}
			
		} 
    	catch (NumberFormatException e) {
			txtResult.setText("Non hai introdotto un numero");
		}
    	catch (Exception e) {
			txtResult.setText("Errore!");
		}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
