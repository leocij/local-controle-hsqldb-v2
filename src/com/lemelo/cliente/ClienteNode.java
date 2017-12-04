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
        gridPane.add(clienteTableView,0,0);

        return gridPane;
    }

    private TableView<Cliente> geraClienteTableView() throws SQLException {
        TableColumn<Cliente, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        ClienteDao clienteDao = new ClienteDao();
        ObservableList<Cliente> list = clienteDao.buscaClientes();

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

        salvarButton.setOnAction(event -> {
            String nomeStr = nomeTextField.getText();

            Cliente cliente = new Cliente();
            cliente.setNome(nomeStr);

            if (flag == Flag.EDITAR) {
                //TODO
            } else {
                clienteDao.insert(cliente);
                nomeTextField.setText("");
                nomeTextField.requestFocus();
                try {
                    geraClienteTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(novoButton,1,1);

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
