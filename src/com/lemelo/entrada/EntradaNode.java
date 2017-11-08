package com.lemelo.entrada;

import com.lemelo.util.Flag;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EntradaNode {
    private Integer idEditar;
    private String ultimaEdicaoEditar;
    private Flag flag;

    public Node executar() throws SQLException {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataHoraLabel = new Text("Dt/Hr: ");
        TextField dataHoraTextField = new TextField();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        dataHoraTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
        dataHoraTextField.setFocusTraversable(false);
        dataHoraLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(dataHoraLabel, 0, 0);
        gridPane.add(dataHoraTextField, 1, 0);

        Text descricaoLabel = new Text("Descrição: ");
        TextField descricaoTextField = new TextField();
        descricaoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(descricaoLabel, 0, 1);
        gridPane.add(descricaoTextField, 1, 1);

        Text valorLabel = new Text("Valor: ");
        TextField valorTextField = new TextField();
        valorLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorLabel, 0, 2);
        gridPane.add(valorTextField, 1, 2);

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(salvarButton, 1, 3);

        TableView<Entrada> tableView = new TableView<>();

        TableColumn<Entrada, String> dataHoraColuna = new TableColumn<>("Dt./Hr.");
        dataHoraColuna.setMinWidth(100);
        dataHoraColuna.setMaxWidth(100);
        dataHoraColuna.setCellValueFactory(new PropertyValueFactory<>("dataHora"));

        TableColumn<Entrada, String> descricaoColuna = new TableColumn<>("Descrição");
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Entrada, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        TableColumn<Entrada, String> ultimaEdicaoColuna = new TableColumn<>("Última edição");
        ultimaEdicaoColuna.setCellValueFactory(new PropertyValueFactory<>("ultimaEdicao"));

        EntradaDao entradaDao = new EntradaDao();

        entradaTableView(entradaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);

        salvarButton.setOnAction(event -> {
            String dataHoraStr = dataHoraTextField.getText();
            String descricaoStr = descricaoTextField.getText();
            String valorStr = valorTextField.getText();

            Entrada entrada = new Entrada();
            entrada.setDataHora(dataHoraStr);
            entrada.setDescricao(descricaoStr);
            entrada.setValor(valorStr);
            entrada.setUltimaEdicao("-");

            try {
                if (flag == Flag.EDITAR) {
                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    ultimaEdicaoEditar = sdf3.format(Calendar.getInstance().getTime());
                    entradaDao.update(entrada, idEditar, ultimaEdicaoEditar);
                    flag = null;
                } else {
                    entradaDao.insert(entrada);
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                dataHoraTextField.setText(sdf2.format(Calendar.getInstance().getTime()));

                descricaoTextField.setText("");

                valorTextField.setText("");

                entradaTableView(entradaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {

                Entrada entrada = tableView.getSelectionModel().getSelectedItem();

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);

                ButtonType editarButtonType = new ButtonType("Editar");
                ButtonType excluirButtonType = new ButtonType("Excluir");
                ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert1.setHeaderText("O que deseja fazer com?\n\n" + entrada.toString());
                alert1.getButtonTypes().setAll(editarButtonType, excluirButtonType, cancelarButtonType);
                alert1.showAndWait().ifPresent(escolha1 -> {
                    if (escolha1 == editarButtonType) {
                        flag = Flag.EDITAR;
                        idEditar = entrada.getId();
                        dataHoraTextField.setText(entrada.getDataHora());
                        descricaoTextField.setText(entrada.getDescricao());
                        valorTextField.setText(entrada.getValor());
                        ultimaEdicaoEditar = entrada.getUltimaEdicao();
                    } else if (escolha1 == excluirButtonType) {
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);

                        ButtonType simButtonType = new ButtonType("Sim");
                        ButtonType naoButtonType = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert2.setHeaderText("Deseja realmente excluir?\n\n" + entrada.toString());
                        alert2.getButtonTypes().setAll(simButtonType, naoButtonType);
                        alert2.showAndWait().ifPresent(escolha2 -> {
                            if (escolha2 == simButtonType) {
                                entradaDao.apagar(entrada.getId());
                                try {
                                    entradaTableView(entradaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });

        Text avisoLabel = new Text("Dê dois cliques na linha da tabela para Editar ou Excluir um registro.");
        avisoLabel.setStyle("-fx-font: normal 15px 'verdana' ");

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(gridPane, avisoLabel, tableView);

        return vBox;
    }

    private void entradaTableView(EntradaDao entradaDao, TableView<Entrada> tableView, TableColumn dataHoraColuna, TableColumn descricaoColuna, TableColumn valorColuna, TableColumn ultimaEdicaoColuna) throws SQLException {
        ObservableList<Entrada> list = entradaDao.listAll();
        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
    }
}
