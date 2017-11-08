package com.lemelo.entrada;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EntradaGridPane {

    public Node executar() {
        GridPane gridPane = new GridPane();
        //gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataHoraLabel = new Text("Dt/Hr: ");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        TextField dataHoraTextField = new TextField();
        dataHoraTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
        //dataHoraTextField.setEditable(false);
        dataHoraTextField.setFocusTraversable(false);
        dataHoraLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(dataHoraLabel,0,0);
        gridPane.add(dataHoraTextField,1,0);

        Text descricaoLabel = new Text("Descrição: ");
        TextField descricaoTextField = new TextField();
        descricaoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(descricaoLabel,0,1);
        gridPane.add(descricaoTextField,1,1);

        Text valorLabel = new Text("Valor: ");
        TextField valorTextField = new TextField();
        valorLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorLabel,0,2);
        gridPane.add(valorTextField,1,2);

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(salvarButton,0,3);

        TableView tableView = new TableView();

        salvarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String dataHoraStr = dataHoraTextField.getText();
                String descricaoStr = descricaoTextField.getText();
                String valorStr = valorTextField.getText();

                Entrada entrada = new Entrada();
                entrada.setDataHora(dataHoraStr);
                entrada.setDescricao(descricaoStr);
                entrada.setValor(valorStr);

                EntradaDao entradaDao = new EntradaDao();
                try {
                    entradaDao.insert(entrada);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    imprimir(tableView);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox vBox = new VBox(20);
        vBox.setPadding(new Insets(25,25,25,25));
        vBox.getChildren().addAll(gridPane, tableView);

        return vBox;
    }

    private void imprimir(TableView tableView) throws SQLException {
        EntradaDao entradaDao = new EntradaDao();
        ObservableList<Entrada> list = entradaDao.listAll();
        tableView.setItems(list);
    }
}
