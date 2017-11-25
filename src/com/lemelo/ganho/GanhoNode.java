package com.lemelo.ganho;

import com.lemelo.cliente.Cliente;
import com.lemelo.cliente.ClienteDao;
import com.lemelo.util.Flag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GanhoNode {
    private DatePicker dataDatePicker;
    private TableView<Ganho> tableView;
    private Flag flag;
    private ComboBox<Cliente> clienteComboBox;
    private ComboBox quantidadeComboBox;
    private String numeroDigitado;
    private ComboBox statusComboBox;
    private TextField valorTextField;
    private TextField ganhoBuscarPorClienteTextField;
    private TableView<Ganho> tableViewDevedor;

    public Node executar(Tab ganhoTab) throws ParseException, SQLException {

        GridPane formularioGridPane = geraFormularioGridPane();

        GridPane botoesGridPane = geraBotoesGridPane();

        tableView = new TableView<>();
        TableView<Ganho> ganhoTableView = geraGanhoTableView();
        GridPane ganhoTableViewGridPane = geraGanhoTableViewGridPane(ganhoTableView);

        ganhoTab.setOnSelectionChanged(e->{
            if(ganhoTab.isSelected()==true) {
                ClienteDao clienteDao = new ClienteDao();
                ObservableList<Cliente> clienteList = null;
                try {
                    clienteList = clienteDao.buscaClientes();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                ObservableList<Cliente> finalClienteList = clienteList;
                Platform.runLater(()->clienteComboBox.setItems(finalClienteList));
            }
        });

        tableViewDevedor = new TableView<>();
        TableView<Ganho> devedorTableView = geraDevedorTableView();
        GridPane devedorTableViewGridPane = geraDevedorTableViewGridPane(devedorTableView);

        GridPane principalGridPane = geraPrincipalGridPane(formularioGridPane, botoesGridPane, ganhoTableViewGridPane,devedorTableViewGridPane);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0,2,0,2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private GridPane geraDevedorTableViewGridPane(TableView<Ganho> devedorTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        devedorTableView.setPrefWidth(5000);
        gridPane.add(devedorTableView,0,0);

        return gridPane;
    }

    private TableView<Ganho> geraDevedorTableView() throws SQLException {
        TableColumn<Ganho, String> dataColuna = new TableColumn<>("Data");
        dataColuna.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Ganho, String> diaSemanaColuna = new TableColumn<>("Dia da Semana");
        diaSemanaColuna.setMinWidth(100);
        diaSemanaColuna.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));

        TableColumn<Ganho, String> clienteColuna = new TableColumn<>("Cliente");
        clienteColuna.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        TableColumn<Ganho, String> statusColuna = new TableColumn<>("Status");
        statusColuna.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Ganho, String> sessaoColuna = new TableColumn<>("Sessão");
        sessaoColuna.setCellValueFactory(new PropertyValueFactory<>("sessao"));

        TableColumn<Ganho, String> qtdeColuna = new TableColumn<>("Qtde");
        qtdeColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Ganho, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        GanhoDao ganhoDao = new GanhoDao();

        ObservableList<Ganho> list1 = ganhoDao.buscaDevedor();

        tableViewDevedor.getColumns().clear();
        tableViewDevedor.setItems(list1);
        tableViewDevedor.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);

        return tableViewDevedor;
    }

    private GridPane geraFormularioGridPane() throws ParseException, SQLException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataLabel = new Text("Data: ");
        dataLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(dataLabel,0,0);
        dataDatePicker = new DatePicker();
        //SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //dataDatePicker.setText(sdf1.format(Calendar.getInstance().getTime()));
        gridPane.add(dataDatePicker,0,1);

        Text clienteLabel = new Text("Cliente: ");
        clienteLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(clienteLabel,1,0);
        clienteComboBox = new ComboBox();
        ClienteDao clienteDao = new ClienteDao();
        ObservableList<Cliente> clienteList = clienteDao.buscaClientes();
        clienteComboBox.setItems(clienteList);
        gridPane.add(clienteComboBox,1,1);

        Text statusLabel = new Text("Status: ");
        statusLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(statusLabel,2,0);
        statusComboBox = new ComboBox();
        ObservableList<String> statusList = FXCollections.observableArrayList("pagou","deve");
        statusComboBox.setItems(statusList);
        gridPane.add(statusComboBox,2,1);

        Text quantidadeLabel = new Text("Qtde: ");
        quantidadeLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(quantidadeLabel,3,0);
        quantidadeComboBox = new ComboBox();
        quantidadeComboBox.setMaxWidth(50);
        ObservableList<String> quantidadeList = FXCollections.observableArrayList("1","2","3","4","5","6","7","8");
        quantidadeComboBox.setItems(quantidadeList);
        gridPane.add(quantidadeComboBox,3,1);

        Text valorLabel = new Text("Valor: ");
        valorLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorLabel, 4, 0);
        valorTextField = new TextField();
        valorTextField.setMaxWidth(80);
        gridPane.add(valorTextField, 4, 1);
        valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0")));

        //Posiciona o cursor no fim da linha
        valorTextField.setOnMousePressed(event -> {
            Platform.runLater(()->valorTextField.positionCaret(valorTextField.getText().length()));
        });

        //Formatação em moeda corrente
        valorTextField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            String numeroAntigoStr = valorTextField.getText();
            if(e.getCode().isDigitKey()) {
                if(numeroDigitado==null) {
                    numeroDigitado = e.getText();
                } else {
                    numeroDigitado += e.getText();
                }
                Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(numeroDigitado).divide(new BigDecimal("100")))));
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if(numeroDigitado==null){
                    Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0"))));
                } else {
                    numeroDigitado = removeUltimoDigito(numeroDigitado);
                    if(numeroDigitado.equals("") || numeroDigitado == null) {
                        Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0"))));
                    } else {
                        Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(numeroDigitado).divide(new BigDecimal("100")))));
                    }
                }
            }
            else {
                //System.out.println(e);
                Platform.runLater(()->valorTextField.setText(numeroAntigoStr));
            }

            //Coloca o cursor no fim da string
            Platform.runLater(()->valorTextField.positionCaret(valorTextField.getText().length()));
        });

        return gridPane;
    }

    private String removeUltimoDigito(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
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

        GanhoDao ganhoDao = new GanhoDao();

        salvarButton.setOnAction(event -> {

            //SimpleDateFormat sdf0 = new SimpleDateFormat("dd/MM/yyyy");
            String dataStr = dataDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            //Pega dia da semana por extenso
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            Date dataUtil = null;
            try {
                dataUtil = sdf1.parse(dataStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
            String diaSemanaStr = sdf2.format(dataUtil);

            String clienteStr = clienteComboBox.getValue().toString();

            String statusStr = statusComboBox.getValue().toString();

            String quantidadeStr = quantidadeComboBox.getValue().toString();

            String valorStr = valorTextField.getText();

            Ganho ganho = new Ganho();
            ganho.setData(dataStr);
            ganho.setDiaSemana(diaSemanaStr);
            ganho.setCliente(clienteStr);
            ganho.setStatus(statusStr);
            ganho.setQuantidade(quantidadeStr);
            ganho.setValor(valorStr);

            if(flag == Flag.EDITAR) {
                //TODO
            } {
                try {
                    ganhoDao.insert(ganho);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                limpaFormulario();
                Platform.runLater(()->{
                    try {
                        geraGanhoTableView();
                        geraDevedorTableView();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(novoButton,1,1);



        return gridPane;
    }

    private void limpaFormulario() {
        //SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //dataTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
        clienteComboBox.setValue(null);
    }

    private TableView<Ganho> geraGanhoTableView() throws SQLException {
        TableColumn<Ganho, String> dataColuna = new TableColumn<>("Data");
        dataColuna.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Ganho, String> diaSemanaColuna = new TableColumn<>("Dia da Semana");
        diaSemanaColuna.setMinWidth(100);
        diaSemanaColuna.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));

        TableColumn<Ganho, String> clienteColuna = new TableColumn<>("Cliente");
        clienteColuna.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        TableColumn<Ganho, String> statusColuna = new TableColumn<>("Status");
        statusColuna.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Ganho, String> sessaoColuna = new TableColumn<>("Sessão");
        sessaoColuna.setCellValueFactory(new PropertyValueFactory<>("sessao"));

        TableColumn<Ganho, String> qtdeColuna = new TableColumn<>("Qtde");
        qtdeColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Ganho, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        GanhoDao ganhoDao = new GanhoDao();

        ObservableList<Ganho> list1 = ganhoDao.buscaPorMesAno();

        tableView.getColumns().clear();
        tableView.setItems(list1);
        tableView.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);

        Platform.runLater(()->ganhoBuscarPorClienteTextField.textProperty().addListener((observable, oldValue, newValue)->{
            ObservableList<Ganho> list2 = null;
            try {
                list2 = ganhoDao.buscaPorCliente(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list2);
            tableView.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);
        }));

        return tableView;
    }

    private GridPane geraGanhoTableViewGridPane(TableView<Ganho> ganhoTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text ganhoBuscarPorClienteLabel = new Text("Buscar ganhos por cliente: ");
        ganhoBuscarPorClienteLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(ganhoBuscarPorClienteLabel,0,0);
        ganhoBuscarPorClienteTextField = new TextField();
        gridPane.add(ganhoBuscarPorClienteTextField,0,1);

        ganhoTableView.setPrefWidth(5000);
        gridPane.add(ganhoTableView,0,3);

        return gridPane;
    }

    private GridPane geraPrincipalGridPane(GridPane formularioGridPane, GridPane botoesGridPane, GridPane ganhoTableViewGridPane, GridPane devedorTableViewGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(formularioGridPane, 0,0);
        gridPane.add(botoesGridPane,0,1);
        gridPane.add(ganhoTableViewGridPane,0,2);
        gridPane.add(devedorTableViewGridPane,0,3);

        return gridPane;
    }


}
