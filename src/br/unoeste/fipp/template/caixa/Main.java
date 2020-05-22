package br.unoeste.fipp.template.caixa;

import br.unoeste.fipp.template.caixa.view.Inicio;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Lucas
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        new Inicio().start(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
