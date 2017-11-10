package com.lemelo.saida;

import com.lemelo.util.Flag;
import javafx.application.Platform;
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

public class SaidaNode {

    private Flag flag;
    private String ultimaEdicaoEditar;
    private Integer idEditar;

    public Node executar(Tab saidaTab) throws SQLException {

        Text tituloLabel = new Text("\nSaída de Valores");
        tituloLabel.setStyle("-fx-font: normal bold 17px 'verdana' ");

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

        saidaTab.setOnSelectionChanged(e->{
            if (saidaTab.isSelected()) {
                Platform.runLater(descricaoTextField::requestFocus);
            }
        });

        Text valorLabel = new Text("Valor: ");
        TextField valorTextField = new TextField();
        valorLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorLabel, 0, 2);
        gridPane.add(valorTextField, 1, 2);

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(salvarButton, 1, 3);

        Text avisoLabel = new Text("Dê dois cliques na linha da tabela para Editar ou Excluir um registro.");
        avisoLabel.setStyle("-fx-font: normal 15px 'verdana' ");

        TableView<Saida> tableView = new TableView<>();

        TableColumn<Saida, String> dataHoraColuna = new TableColumn<>("Dt./Hr.");
        dataHoraColuna.setMinWidth(100);
        dataHoraColuna.setMaxWidth(100);
        dataHoraColuna.setCellValueFactory(new PropertyValueFactory<>("dataHora"));

        TableColumn<Saida, String> descricaoColuna = new TableColumn<>("Descrição");
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Saida, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        TableColumn<Saida, String> ultimaEdicaoColuna = new TableColumn<>("Última edição");
        ultimaEdicaoColuna.setMinWidth(100);
        ultimaEdicaoColuna.setCellValueFactory(new PropertyValueFactory<>("ultimaEdicao"));

        SaidaDao saidaDao = new SaidaDao();

        saidaTableView(saidaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);

        salvarButton.defaultButtonProperty().bind(salvarButton.focusedProperty());

        salvarButton.setOnAction(event -> {
            String dataHoraStr = dataHoraTextField.getText();
            String descricaoStr = descricaoTextField.getText();
            String valorStr = valorTextField.getText();

            Saida saida = new Saida();
            saida.setDataHora(dataHoraStr);
            saida.setDescricao(descricaoStr);
            saida.setValor(valorStr);
            saida.setUltimaEdicao("-");

            try {
                if (flag == Flag.EDITAR) {
                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    ultimaEdicaoEditar = sdf3.format(Calendar.getInstance().getTime());
                    saidaDao.update(saida, ultimaEdicaoEditar, idEditar);
                    flag = null;
                } else {
                    saidaDao.insert(saida);
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                dataHoraTextField.setText(sdf2.format(Calendar.getInstance().getTime()));

                descricaoTextField.setText("");
                descricaoTextField.requestFocus();

                valorTextField.setText("");

                saidaTableView(saidaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {

                Saida saida = tableView.getSelectionModel().getSelectedItem();

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);

                ButtonType editarButtonType = new ButtonType("Editar");
                ButtonType excluirButtonType = new ButtonType("Excluir");
                ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert1.setHeaderText("O que deseja fazer com?\n\n" + saida.toString());
                alert1.getButtonTypes().setAll(editarButtonType, excluirButtonType, cancelarButtonType);
                alert1.showAndWait().ifPresent(escolha1 -> {
                    if (escolha1 == editarButtonType) {
                        flag = Flag.EDITAR;
                        idEditar = saida.getId();
                        dataHoraTextField.setText(saida.getDataHora());
                        descricaoTextField.setText(saida.getDescricao());
                        valorTextField.setText(saida.getValor());
                        ultimaEdicaoEditar = saida.getUltimaEdicao();
                    } else if (escolha1 == excluirButtonType) {
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);

                        ButtonType simButtonType = new ButtonType("Sim");
                        ButtonType naoButtonType = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert2.setHeaderText("Deseja realmente excluir?\n\n" + saida.toString());
                        alert2.getButtonTypes().setAll(simButtonType, naoButtonType);
                        alert2.showAndWait().ifPresent(escolha2 -> {
                            if (escolha2 == simButtonType) {
                                saidaDao.apagar(saida.getId());
                                try {
                                    saidaTableView(saidaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
        
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(tituloLabel, gridPane, avisoLabel, tableView);

        return vBox;
    }

    private void saidaTableView(SaidaDao saidaDao, TableView<Saida> tableView, TableColumn dataHoraColuna, TableColumn descricaoColuna, TableColumn valorColuna, TableColumn ultimaEdicaoColuna) throws SQLException {
        ObservableList<Saida> list = saidaDao.listAll();
        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
    }
}
