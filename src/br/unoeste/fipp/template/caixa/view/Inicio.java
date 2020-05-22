package br.unoeste.fipp.template.caixa.view;

import br.unoeste.fipp.template.caixa.control.InicioControl;
import br.unoeste.fipp.template.caixa.model.Caixa;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author lucas
 */
public class Inicio extends Application 
{
    private Caixa caixa;
    
    @Override
    public void start(Stage primaryStage) 
    {
        this.caixa = new InicioControl().obterCaixa(1);
        
        Label lbStatus = new Label("Estado: ");
        lbStatus.setFont(Font.font("System", 11));
        lbStatus.setLayoutX(10);
        lbStatus.setLayoutY(10);
        
        TextField txStatus = new TextField(caixa.isStatus() ? "ABERTO" : "FECHADO");
        txStatus.setLayoutX(10);
        txStatus.setLayoutY(26);
        txStatus.setPrefWidth(280);
        txStatus.setDisable(true);
        
        Button btAbrir = new Button("ABRIR");
        btAbrir.setLayoutX(10);
        btAbrir.setLayoutY(100);
        btAbrir.setPrefWidth(135);
        btAbrir.setOnAction((ActionEvent event) -> { abrirAction(txStatus); });
        
        Button btFechar = new Button("FECHAR");
        btFechar.setLayoutX(150);
        btFechar.setLayoutY(100);
        btFechar.setPrefWidth(135);
        btFechar.setOnAction((ActionEvent event) -> { fecharAction(txStatus); });
        
        Button btLancar = new Button("LANCAR PAGAR / RECEBER");
        btLancar.setLayoutX(10);
        btLancar.setLayoutY(140);
        btLancar.setPrefWidth(280);
        btLancar.setOnAction((ActionEvent event) -> { lancarAction(primaryStage); });
        
        Button btQuitar = new Button("QUITAR");
        btQuitar.setLayoutX(10);
        btQuitar.setLayoutY(180);
        btQuitar.setPrefWidth(280);
        btQuitar.setOnAction((ActionEvent event) -> { quitarAction(primaryStage); });
        
        Button btReceber = new Button("RECEBER");
        btReceber.setLayoutX(10);
        btReceber.setLayoutY(220);
        btReceber.setPrefWidth(280);
        btReceber.setOnAction((ActionEvent event) -> { receberAction(primaryStage); });
        
        Button btSair = new Button("SAIR");
        btSair.setLayoutX(10);
        btSair.setLayoutY(260);
        btSair.setPrefWidth(280);
        btSair.setOnAction((ActionEvent event) -> { sairAction(); });
        
        AnchorPane root = new AnchorPane();
        root.getChildren().add(lbStatus);
        root.getChildren().add(txStatus);
        root.getChildren().add(btAbrir);
        root.getChildren().add(btFechar);
        root.getChildren().add(btLancar);
        root.getChildren().add(btQuitar);
        root.getChildren().add(btReceber);
        root.getChildren().add(btSair);
        
        Scene scene = new Scene(root, 300, 320);
        
        primaryStage.setTitle("Template Caixa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void abrirAction(TextField txt)
    {
        String retorno = new InicioControl().abreCaixa(caixa);
        
        if (retorno.length() > 0) 
        {
            new Alert(Alert.AlertType.ERROR, retorno, ButtonType.CLOSE).show();
        }
        else
        {
            txt.setText("ABERTO");
        }
    }
    
    private void fecharAction(TextField txt)
    {
        String retorno = new InicioControl().fecharCaixa(caixa);
        
        if (retorno.length() > 0) 
        {
            new Alert(Alert.AlertType.ERROR, retorno, ButtonType.CLOSE).show();
        }
        else
        {
            txt.setText("FECHADO");
        }
    }
    
    private void lancarAction(Stage primaryStage)
    {
        new LancarPagarReceber().start(primaryStage);
    }
    
    private void quitarAction(Stage primaryStage) {
        if (caixa.isStatus()) new Quitacao(this.caixa).start(primaryStage);
        else new Alert(Alert.AlertType.ERROR, "Caixa fechado!", ButtonType.CLOSE).show();
    }
    
    private void receberAction(Stage primaryStage) {
        if (caixa.isStatus()) new Receber(this.caixa).start(primaryStage);
        else new Alert(Alert.AlertType.ERROR, "Caixa fechado!", ButtonType.CLOSE).show();
    }
    
    private void sairAction()
    {
        System.exit(0);
    }
}
