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
import javafx.stage.Stage;

/**
 *
 * @author lucas
 */
public class Inicio extends Application 
{
    private Caixa caixa;
    
    private Label lbStatus;
    private TextField txStatus;
    private Label lbSaldoIni;
    private TextField txSaldoIni;
    private Label lbSaldoFin;
    private TextField txSaldoFin;
    private Button btAbrir;
    private Button btFechar;
    private Button btLancar;
    private Button btQuitar;
    private Button btReceber;
    private Button btSair;
    private AnchorPane root;
    private Scene scene;
    
    @Override
    public void start(Stage primaryStage) 
    {
        this.caixa = new InicioControl().obterCaixa(1);
        
        lbStatus = new Label("Estado: ");
        lbStatus.setLayoutX(10);
        lbStatus.setLayoutY(10);
        
        txStatus = new TextField(caixa.isStatus() ? "ABERTO" : "FECHADO");
        txStatus.setLayoutX(10);
        txStatus.setLayoutY(26);
        txStatus.setPrefWidth(280);
        txStatus.setDisable(true);
        
        lbSaldoIni = new Label("Saldo Inicial:");
        lbSaldoIni.setLayoutX(10);
        lbSaldoIni.setLayoutY(56);
        
        txSaldoIni = new TextField(""+caixa.getSaldoInicial());
        txSaldoIni.setLayoutX(10);
        txSaldoIni.setLayoutY(72);
        txSaldoIni.setDisable(true);
        txSaldoIni.setPrefWidth(135);
        
        lbSaldoFin = new Label("Saldo Final:");
        lbSaldoFin.setLayoutX(155);
        lbSaldoFin.setLayoutY(56);
        
        txSaldoFin = new TextField(""+caixa.getSaldoFinal());
        txSaldoFin.setLayoutX(155);
        txSaldoFin.setLayoutY(72);
        txSaldoFin.setDisable(true);
        txSaldoFin.setPrefWidth(135);
        
        btAbrir = new Button("ABRIR");
        btAbrir.setLayoutX(10);
        btAbrir.setLayoutY(112);
        btAbrir.setPrefWidth(135);
        btAbrir.setOnAction((ActionEvent event) -> { abrirAction(); });
        
        btFechar = new Button("FECHAR");
        btFechar.setLayoutX(155);
        btFechar.setLayoutY(112);
        btFechar.setPrefWidth(135);
        btFechar.setOnAction((ActionEvent event) -> { fecharAction(); });
        
        btLancar = new Button("LANCAR PAGAR / RECEBER");
        btLancar.setLayoutX(10);
        btLancar.setLayoutY(152);
        btLancar.setPrefWidth(280);
        btLancar.setOnAction((ActionEvent event) -> { lancarAction(primaryStage); });
        
        btQuitar = new Button("QUITAR");
        btQuitar.setLayoutX(10);
        btQuitar.setLayoutY(192);
        btQuitar.setPrefWidth(280);
        btQuitar.setOnAction((ActionEvent event) -> { quitarAction(primaryStage); });
        
        btReceber = new Button("RECEBER");
        btReceber.setLayoutX(10);
        btReceber.setLayoutY(232);
        btReceber.setPrefWidth(280);
        btReceber.setOnAction((ActionEvent event) -> { receberAction(primaryStage); });
        
        btSair = new Button("SAIR");
        btSair.setLayoutX(10);
        btSair.setLayoutY(272);
        btSair.setPrefWidth(280);
        btSair.setOnAction((ActionEvent event) -> { sairAction(); });
        
        root = new AnchorPane();
        root.getChildren().add(lbStatus);
        root.getChildren().add(txStatus);
        root.getChildren().add(lbSaldoIni);
        root.getChildren().add(txSaldoIni);
        root.getChildren().add(lbSaldoFin);
        root.getChildren().add(txSaldoFin);
        root.getChildren().add(btAbrir);
        root.getChildren().add(btFechar);
        root.getChildren().add(btLancar);
        root.getChildren().add(btQuitar);
        root.getChildren().add(btReceber);
        root.getChildren().add(btSair);
        
        scene = new Scene(root, 300, 310);
        
        primaryStage.setTitle("Template Caixa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void abrirAction()
    {
        String retorno = new InicioControl().abreCaixa(caixa);
        
        if (retorno.length() > 0) 
        {
            new Alert(Alert.AlertType.ERROR, retorno, ButtonType.CLOSE).show();
        }
        else
        {
            txStatus.setText("ABERTO");
        }
    }
    
    private void fecharAction()
    {
        String retorno = new InicioControl().fecharCaixa(caixa);
        
        if (retorno.length() > 0) 
        {
            new Alert(Alert.AlertType.ERROR, retorno, ButtonType.CLOSE).show();
        }
        else
        {
            txStatus.setText("FECHADO");
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
