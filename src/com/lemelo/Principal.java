package com.lemelo;

import com.lemelo.entrada.EntradaGridPane;
import com.lemelo.util.FabricaConexao;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Principal extends Application {
    public static void main(String[] args) throws ClassNotFoundException {
        launch(args);
        Class.forName("org.hsqldb.jdbcDriver");
        new FabricaConexao().createTables();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);

        Tab entradaTab = new Tab();
        entradaTab.setText("Entrada de Valores");
        entradaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        entradaTab.setClosable(false);
        entradaTab.setContent(new EntradaGridPane().executar());
        tabPane.getTabs().add(entradaTab);

        Tab saidaTab = new Tab();
        saidaTab.setText("Saída de Valores");
        saidaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        saidaTab.setContent(null);
        tabPane.getTabs().add(saidaTab);

        Scene scene = new Scene(tabPane, 640, 480, Color.GRAY);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Controle local de gastos");

        //primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
