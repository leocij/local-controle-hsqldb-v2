package com.lemelo;

import com.lemelo.entrada.EntradaNode;
import com.lemelo.saida.SaidaNode;
import com.lemelo.util.FabricaConexao;
import com.lemelo.util.SaldoResumoNode;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Principal extends Application {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        new FabricaConexao().createTables();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);

        Tab entradaTab = new Tab();
        entradaTab.setText("Entrada de Valores");
        entradaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        entradaTab.setClosable(false);
        entradaTab.setContent(new EntradaNode().executar(entradaTab));
        tabPane.getTabs().add(entradaTab);

        Tab saidaTab = new Tab();
        saidaTab.setText("Saída de Valores");
        saidaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        saidaTab.setClosable(false);
        saidaTab.setContent(new SaidaNode().executar(saidaTab));
        tabPane.getTabs().add(saidaTab);

        Tab parcelamentoTab = new Tab();
        parcelamentoTab.setText("Parcelamento");
        parcelamentoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        parcelamentoTab.setClosable(false);
        parcelamentoTab.setContent(null);
        tabPane.getTabs().add(parcelamentoTab);

        Tab fixaTab = new Tab();
        fixaTab.setText("Fixa");
        fixaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        fixaTab.setClosable(false);
        fixaTab.setContent(null);
        tabPane.getTabs().add(fixaTab);

        Tab ganhoTab = new Tab();
        ganhoTab.setText("Ganhos");
        ganhoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        ganhoTab.setClosable(false);
        ganhoTab.setContent(null);
        tabPane.getTabs().add(ganhoTab);

        Tab saldoResumoTab = new Tab();
        saldoResumoTab.setText("Saldo e Resumo");
        saldoResumoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        saldoResumoTab.setClosable(false);
        saldoResumoTab.setContent(new SaldoResumoNode().executar(saldoResumoTab));
        tabPane.getTabs().add(saldoResumoTab);

        Integer WIDTH_TAM = 763;
        Integer HEIGHT_TAM = 680;
        Scene scene = new Scene(tabPane, WIDTH_TAM, HEIGHT_TAM, Color.GRAY);
        primaryStage.setMinWidth(WIDTH_TAM);
        primaryStage.setMinHeight(HEIGHT_TAM);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Controle local de gastos");

        //primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
