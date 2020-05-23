package br.unoeste.fipp.template.caixa.view;

import br.unoeste.fipp.template.caixa.control.ReceberControl;
import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Receber extends Application
{    
    private Caixa caixa;
    private Recebimento recebimento;
    
    private Label lbPesq;
    private TextField txPesq;
    private Button btBuscar;
    private Label lbData;
    private TextField txData;
    private Label lbValor;
    private TextField txValor;
    private Label lbDesc;
    private TextField txDesc;
    private Button btConfirmar;
    private Button btVoltar;
    private AnchorPane root;
    private Scene scene;
    
    public Receber(Caixa caixa) {
        this.caixa = caixa;
    }
    
    @Override
    public void start(Stage primaryStage) {
        lbPesq = new Label("Conta: ");
        lbPesq.setLayoutX(10);
        lbPesq.setLayoutY(12);
        
        txPesq = new TextField();
        txPesq.setLayoutX(60);
        txPesq.setLayoutY(8);
        
        btBuscar = new Button("BUSCAR");
        btBuscar.setLayoutX(220);
        btBuscar.setLayoutY(8);
        btBuscar.setPrefWidth(70);
        btBuscar.setOnAction((ActionEvent event) -> { obter(txPesq); });
        
        lbData = new Label("Data: ");
        lbData.setLayoutX(10);
        lbData.setLayoutY(50);
        
        txData = new TextField();
        txData.setLayoutX(60);
        txData.setLayoutY(48);
        txData.setDisable(true);
        
        lbValor = new Label("Valor R$:");
        lbValor.setLayoutX(10);
        lbValor.setLayoutY(92);
        
        txValor = new TextField();
        txValor.setLayoutX(60);
        txValor.setLayoutY(88);
        
        lbDesc = new Label("Desc.: ");
        lbDesc.setLayoutX(10);
        lbDesc.setLayoutY(132);
        
        txDesc = new TextField();
        txDesc.setLayoutX(60);
        txDesc.setLayoutY(128);
        txDesc.setPrefWidth(230);
        
        btConfirmar = new Button("RECEBER");
        btConfirmar.setLayoutX(10);
        btConfirmar.setLayoutY(180);
        btConfirmar.setPrefWidth(280);
        btConfirmar.setOnAction((ActionEvent event) -> { confirmar(); });
        
        btVoltar = new Button("VOLTAR");
        btVoltar.setLayoutX(10);
        btVoltar.setLayoutY(220);
        btVoltar.setPrefWidth(280);
        btVoltar.setOnAction((ActionEvent event) -> { voltar(primaryStage); });
        
        root = new AnchorPane();
        root.getChildren().add(lbPesq);
        root.getChildren().add(txPesq);
        root.getChildren().add(btBuscar);
        root.getChildren().add(lbData);
        root.getChildren().add(txData);
        root.getChildren().add(lbValor);
        root.getChildren().add(txValor);
        root.getChildren().add(lbDesc);
        root.getChildren().add(txDesc);
        root.getChildren().add(btConfirmar);
        root.getChildren().add(btVoltar);
        
        scene = new Scene(root, 300, 260);
        
        primaryStage.setTitle("Receber contas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void obter(TextField txPesq) {
        String pesq = txPesq.getText();
        if (!pesq.isEmpty()) {
            int id = Integer.parseInt(pesq);
            recebimento = new ReceberControl(null).obter(id);
            txData.setText(recebimento.getDataConta().toString());
            txDesc.setText(recebimento.getConta());
            txValor.setText(recebimento.getValor().toString());
            
            if (recebimento.getDataRecebimento() != null) {
                btConfirmar.setDisable(true);
                new Alert(Alert.AlertType.INFORMATION, "Conta já recebida!", ButtonType.CLOSE).show();
            } else {
                btConfirmar.setDisable(false);
            }
        } 
    }
    
    private void voltar(Stage primaryStage) {
        new Inicio().start(primaryStage);
    }
    
    private void confirmar()
    {
        String mensagem;
        try {
            mensagem = new ReceberControl(caixa).confirmar(recebimento.getId());
            //String mensagem = "";
            if (mensagem.length() > 0) {
                new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.CLOSE).show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Conta recebida!", ButtonType.CLOSE).show();
                btConfirmar.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Receber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
