package com.lemelo.cliente;

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
import java.util.Observable;

public class ClienteNode {
    private TextField nomeTextField;
    private Flag flag;
    private TableView<Cliente> tableView;
    private Integer idEditar;
    private String ultimaEdicaoEditar;
    private TextField contagemTextField;

    public Node executar(Tab clienteResumoTab) throws SQLException {

        clienteResumoTab.setOnSelectionChanged(e->{
            if(clienteResumoTab.isSelected()==true) {
                Platform.runLater(()->nomeTextField.requestFocus());
            }
        });

        GridPane formularioGridPane = geraFormularioGridPane();
        GridPane botoesGridPane = geraBotoesGridPane();
        tableView = new TableView<>();
        TableView<Cliente> clienteTableView = geraClienteTableView();
        GridPane clienteTableViewGridPane = geraClienteTableViewGridPane(clienteTableView);
        GridPane principalGridPane = geraPrincipal(formularioGridPane, botoesGridPane, clienteTableViewGridPane);

        tableView.setOnMousePressed(event->{
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Cliente cliente = tableView.getSelectionModel().getSelectedItem();

                if (cliente != null) {
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);

                    ButtonType editarButtonType = new ButtonType("Editar");
                    ButtonType excluirButtonType = new ButtonType("Excluir");
                    ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert1.setHeaderText("O que deseja fazer com?\n\n" + cliente.toString());
                    alert1.getButtonTypes().setAll(editarButtonType, excluirButtonType, cancelarButtonType);
                    alert1.showAndWait().ifPresent(escolha1->{
                        if (escolha1 == editarButtonType) {
                            flag = Flag.EDITAR;
                            idEditar = cliente.getId();
                            nomeTextField.setText(cliente.getNome());
                        } else if (escolha1 == excluirButtonType) {
                            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);

                            ButtonType simButtonType = new ButtonType("Sim");
                            ButtonType naoButtonType = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert2.setHeaderText("Deseja realmente excluir?\n\n" + cliente.toString());
                            alert2.getButtonTypes().setAll(simButtonType, naoButtonType);
                            alert2.showAndWait().ifPresent(escolha2 ->{
                                ClienteDao clienteDao = new ClienteDao();
                                if (escolha2 == simButtonType) {
                                    clienteDao.apagar(cliente.getId());
                                    try {
                                        geraClienteTableView();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private GridPane geraClienteTableViewGridPane(TableView<Cliente> clienteTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        clienteTableView.setPrefWidth(5000);
        clienteTableView.setPrefHeight(5000);
        gridPane.add(clienteTableView,0,0);

        return gridPane;
    }

    private TableView<Cliente> geraClienteTableView() throws SQLException {
        TableColumn<Cliente, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        ClienteDao clienteDao = new ClienteDao();
        ObservableList<Cliente> list = clienteDao.buscaClientes();

        contagemTextField.setText(""+list.size());

        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(nomeColuna);

        return tableView;
    }

    private GridPane geraBotoesGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(salvarButton,0,1);

        ClienteDao clienteDao = new ClienteDao();

        salvarButton.defaultButtonProperty().bind(salvarButton.focusedProperty());

        salvarButton.setOnAction(event -> {
            String nomeStr = nomeTextField.getText();

            Cliente cliente = new Cliente();
            cliente.setNome(nomeStr);

            if (flag == Flag.EDITAR) {
                clienteDao.update(cliente, idEditar);
                flag = null;
            } else {
                clienteDao.insert(cliente);
                nomeTextField.setText("");
                nomeTextField.requestFocus();
            }
            try {
                geraClienteTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(novoButton,1,1);

        novoButton.setOnAction(event -> {
            nomeTextField.setText("");
            flag = null;
            try {
                geraClienteTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return gridPane;
    }

    private GridPane geraFormularioGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text clienteLabel = new Text("Cliente: ");
        clienteLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(clienteLabel,0,0);
        nomeTextField = new TextField();
        nomeTextField.setPrefWidth(5000);
        gridPane.add(nomeTextField,0,1);

        Text contagemLabel = new Text("Contagem: ");
        contagemLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(contagemLabel,1,0);
        contagemTextField = new TextField();
        gridPane.add(contagemTextField,1,1);

        return gridPane;
    }

    private GridPane geraPrincipal(GridPane formularioGridPane, GridPane botoesGridPane, GridPane clienteTableViewGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(formularioGridPane,0,0);
        gridPane.add(botoesGridPane,0,1);
        gridPane.add(clienteTableViewGridPane,0,2);

        return gridPane;
    }
}
