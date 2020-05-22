package br.unoeste.fipp.template.caixa.view;

import br.unoeste.fipp.template.caixa.control.LancarContasControl;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LancarPagarReceber extends Application
{  
    public LancarPagarReceber()
    {
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        Label lbData = new Label("Data: ");
        lbData.setFont(Font.font("System", 11));
        lbData.setLayoutX(10);
        lbData.setLayoutY(12);
        
        TextField txData = new TextField(LocalDate.now().toString());
        txData.setLayoutX(60);
        txData.setLayoutY(8);
        txData.setDisable(true);
        
        Label lbTipo = new Label("Tipo: ");
        lbTipo.setFont(Font.font("System", 11));
        lbTipo.setLayoutX(10);
        lbTipo.setLayoutY(50);
        
        RadioButton rbPag = new RadioButton("Pagar");
        rbPag.setFont(Font.font("System", 11));
        rbPag.setLayoutX(60);
        rbPag.setLayoutY(50);
        
        RadioButton rbRec = new RadioButton("Receber");
        rbRec.setFont(Font.font("System", 11));
        rbRec.setLayoutX(165);
        rbRec.setLayoutY(50);
        
        ToggleGroup rg = new ToggleGroup();
        rbPag.setToggleGroup(rg);
        rbRec.setToggleGroup(rg);
        rbPag.setSelected(true);
        
        Label lbConta = new Label("Conta: ");
        lbConta.setFont(Font.font("System", 11));
        lbConta.setLayoutX(10);
        lbConta.setLayoutY(92);
        
        TextField txConta = new TextField();
        txConta.setLayoutX(60);
        txConta.setLayoutY(88);
        txConta.setPrefWidth(230);
        
        Label lbValor = new Label("Valor R$:");
        lbValor.setFont(Font.font("System", 11));
        lbValor.setLayoutX(10);
        lbValor.setLayoutY(132);
        
        TextField txValor = new TextField();
        txValor.setLayoutX(60);
        txValor.setLayoutY(128);
        
        Label lbVenc = new Label("Vencimento:");
        lbVenc.setFont(Font.font("System", 11));
        lbVenc.setLayoutX(10);
        lbVenc.setLayoutY(172);
        
        DatePicker dtVenc = new DatePicker(LocalDate.now());
        dtVenc.setLayoutX(80);
        dtVenc.setLayoutY(168);
        
        String lancamentos = new LancarContasControl().obterLancamentos();
        TextArea txArea = new TextArea(lancamentos);
        txArea.setLayoutX(10);
        txArea.setLayoutY(260);
        txArea.setPrefWidth(280);
        txArea.setPrefHeight(200);
        
        Button btConfirmar = new Button("CONFIRMAR");
        btConfirmar.setLayoutX(10);
        btConfirmar.setLayoutY(220);
        btConfirmar.setPrefWidth(280);
        btConfirmar.setOnAction((ActionEvent event) -> { confirmar(rbPag, txValor, txConta, dtVenc, txArea, primaryStage); });
        
        Button btVoltar = new Button("VOLTAR");
        btVoltar.setLayoutX(10);
        btVoltar.setLayoutY(470);
        btVoltar.setPrefWidth(280);
        btVoltar.setOnAction((ActionEvent event) -> { voltar(rbPag, txValor, txConta, dtVenc, primaryStage); });
        
        AnchorPane root = new AnchorPane();
        root.getChildren().add(lbData);
        root.getChildren().add(txData);
        root.getChildren().add(lbTipo);
        root.getChildren().add(rbPag);
        root.getChildren().add(rbRec);
        root.getChildren().add(lbValor);
        root.getChildren().add(txValor);
        root.getChildren().add(lbConta);
        root.getChildren().add(txConta);
        root.getChildren().add(lbVenc);
        root.getChildren().add(dtVenc);
        root.getChildren().add(txArea);
        root.getChildren().add(btConfirmar);
        root.getChildren().add(btVoltar);
        
        Scene scene = new Scene(root, 300, 515);
        
        primaryStage.setTitle("Lançar Pagar / Receber");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void confirmar(RadioButton rbPag, TextField txValor, TextField txConta, DatePicker dtVenc, TextArea txArea, Stage primaryStage)
    {
        int tipo = rbPag.isSelected() ? 1 : 2;
        String svalor = txValor.getText();
        String conta = txConta.getText();
        LocalDate venc = dtVenc.getValue();

        double valor = Double.parseDouble(svalor);

        String mensagem = "";
        
        mensagem = new LancarContasControl().confirmar(tipo, valor, conta, venc);

        if (mensagem.length() > 0)
        {
            new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.CLOSE).show();
        }
        else
        {
            rbPag.setSelected(true);
            txValor.setText("");
            txConta.setText("");
            dtVenc.setValue(LocalDate.now());

            txArea.setText(new LancarContasControl().obterLancamentos());
        }
    }
    
    private void voltar(RadioButton rbPag, TextField txValor, TextField txConta, DatePicker dtVenc, Stage primaryStage) {
        rbPag.setSelected(true);
        txValor.setText("");
        txConta.setText("");
        dtVenc.setValue(LocalDate.now());

        new Inicio().start(primaryStage);
    }
}
