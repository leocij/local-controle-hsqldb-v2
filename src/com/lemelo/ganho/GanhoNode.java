package com.lemelo.ganho;

import com.lemelo.cliente.Cliente;
import com.lemelo.cliente.ClienteDao;
import com.lemelo.entrada.Entrada;
import com.lemelo.entrada.EntradaDao;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GanhoNode {
    private DatePicker dataDatePicker;
    private TableView<Ganho> tableView;
    private Flag flag;
    private ComboBox<Cliente> clienteComboBox;
    private ComboBox<String> quantidadeComboBox;
    private String numeroDigitado;
    private ComboBox<String> statusComboBox;
    private TextField valorTextField;
    private TextField ganhoBuscarPorClienteTextField;
    private TableView<Ganho> tableViewDevedor;
    private Integer idEditar;
    private String sessaoEditar;
    private TableView<Ganho> tableViewGanhador;
    private TextField ganhoBuscarPorQuantidadeDeMesesTextField;
    private ComboBox<String> ganhoBuscarPorQuantidadeDeMesesComboBox;
    private Button listarTudoButton;
    private ComboBox<String> buscarPorStatusComboBox;
    private ComboBox<Cliente> buscarPorClienteComboBox;

    public Node executar(Tab ganhoTab) throws SQLException {

        GridPane formularioGridPane = geraFormularioGridPane();

        GridPane botoesGridPane = geraBotoesGridPane();

        GridPane buscasGridPane = geraBuscasGridPane();

        tableView = new TableView<>();
        TableView<Ganho> ganhoTableView = geraGanhoTableView();
        GridPane ganhoTableViewGridPane = geraGanhoTableViewGridPane(ganhoTableView);

        manipulaTableView(tableView);

        ganhoTab.setOnSelectionChanged(e->{
            if(ganhoTab.isSelected()) {
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


        GridPane principalGridPane = geraPrincipalGridPane(formularioGridPane, botoesGridPane, buscasGridPane, ganhoTableViewGridPane);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0,2,0,2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private void manipulaTableView(TableView<Ganho> tableView) {
        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Ganho ganho = tableView.getSelectionModel().getSelectedItem();
                if (ganho != null) {
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    ButtonType pagarButtonType = new ButtonType("Pagar");
                    ButtonType editarButtonType = new ButtonType("Editar");
                    ButtonType excluirButtonType = new ButtonType("Excluir");
                    ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert1.setHeaderText("O que deseja fazer com?\n\n" + ganho.toString());
                    alert1.getButtonTypes().setAll(pagarButtonType, editarButtonType, excluirButtonType, cancelarButtonType);

                    alert1.showAndWait().ifPresent(escolha1->{
                        if (escolha1 == pagarButtonType) {
                            GanhoDao ganhoDao = new GanhoDao();
                            ganhoDao.update(ganho);
                            try {
                                geraGanhoTableView();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (escolha1 == editarButtonType) {
                            flag = Flag.EDITAR;
                            idEditar = ganho.getId();

                            String dataStr = ganho.getData();
                            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate localDate = LocalDate.parse(dataStr,dtf1);
                            dataDatePicker.setValue(localDate);

                            //String diaSemanaStr = ganho.getDiaSemana();

                            ObservableList<Cliente> clienteList = FXCollections.observableArrayList();
                            Cliente cliente = new Cliente();
                            cliente.setNome(ganho.getCliente());
                            clienteList.add(cliente);
                            clienteComboBox.setItems(clienteList);
                            clienteComboBox.getSelectionModel().select(0);

                            String statusStr = ganho.getStatus();
                            statusComboBox.setValue(statusStr);

                            sessaoEditar = ganho.getSessao();

                            String quantidadeStr = ganho.getQuantidade();
                            quantidadeComboBox.setValue(quantidadeStr);

                            valorTextField.setText(ganho.getValor());
                        } else if (escolha1 == excluirButtonType) {
                            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);

                            ButtonType simButtonType = new ButtonType("Sim");
                            ButtonType naoButtonType = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert2.setHeaderText("Deseja realmente excluir?\n\n" + ganho.toString());
                            alert2.getButtonTypes().setAll(simButtonType, naoButtonType);
                            alert2.showAndWait().ifPresent(escolha2 -> {
                                if (escolha2 == simButtonType) {
                                    GanhoDao ganhoDao = new GanhoDao();
                                    ganhoDao.apagar(ganho.getId());
                                    try {
                                        geraGanhoTableView();
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
    }

    private void manipulaTableViewDevedor(TableView<Ganho> tableViewDevedor) {
        tableViewDevedor.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Ganho ganho = tableViewDevedor.getSelectionModel().getSelectedItem();
                if (ganho != null) {
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    ButtonType pagarButtonType = new ButtonType("Pagar");
                    ButtonType editarButtonType = new ButtonType("Editar");
                    ButtonType excluirButtonType = new ButtonType("Excluir");
                    ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert1.setHeaderText("O que deseja fazer com?\n\n" + ganho.toString());
                    alert1.getButtonTypes().setAll(pagarButtonType, editarButtonType, excluirButtonType, cancelarButtonType);
                    alert1.showAndWait().ifPresent(escolha1->{
                        if (escolha1 == pagarButtonType) {
                            GanhoDao ganhoDao = new GanhoDao();
                            ganhoDao.update(ganho);
                            try {
                                geraDevedorTableView();
                                geraGanhadorTableView();
                                geraGanhoTableView();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (escolha1 == editarButtonType) {
                            flag = Flag.EDITAR;
                            idEditar = ganho.getId();

                            String dataStr = ganho.getData();
                            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate localDate = LocalDate.parse(dataStr,dtf1);
                            dataDatePicker = new DatePicker();
                            dataDatePicker.setValue(localDate);

                            //String diaSemanaStr = ganho.getDiaSemana();

                            ObservableList<Cliente> clienteList = FXCollections.observableArrayList();
                            Cliente cliente = new Cliente();
                            cliente.setNome(ganho.getCliente());
                            clienteList.add(cliente);
                            clienteComboBox.setItems(clienteList);
                            clienteComboBox.getSelectionModel().select(0);

                            String statusStr = ganho.getStatus();
                            statusComboBox.setValue(statusStr);

                            //sessaoEditar = ganho.getSessao();

                            String quantidadeStr = ganho.getQuantidade();
                            quantidadeComboBox.setValue(quantidadeStr);

                            valorTextField.setText(ganho.getValor());
                        } else if (escolha1 == excluirButtonType) {
                            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);

                            ButtonType simButtonType = new ButtonType("Sim");
                            ButtonType naoButtonType = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert2.setHeaderText("Deseja realmente excluir?\n\n" + ganho.toString());
                            alert2.getButtonTypes().setAll(simButtonType, naoButtonType);
                            alert2.showAndWait().ifPresent(escolha2 -> {
                                if (escolha2 == simButtonType) {
                                    GanhoDao ganhoDao = new GanhoDao();
                                    ganhoDao.apagar(ganho.getId());
                                    try {
                                        geraDevedorTableView();
                                        geraGanhoTableView();
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
    }

    private GridPane geraGanhadorTableViewGridPane(TableView<Ganho> ganhadorTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        ganhadorTableView.setPrefWidth(5000);
        ganhadorTableView.setPrefHeight(2000);
        gridPane.add(ganhadorTableView,0,0);

        return gridPane;
    }

    private TableView<Ganho> geraGanhadorTableView() throws SQLException {
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

        ObservableList<Ganho> list1 = ganhoDao.buscaPagador();

        tableViewGanhador.getColumns().clear();
        tableViewGanhador.setItems(list1);
        tableViewGanhador.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);

        return tableViewGanhador;
    }

    private GridPane geraDevedorTableViewGridPane(TableView<Ganho> devedorTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        devedorTableView.setPrefWidth(5000);
        devedorTableView.setPrefHeight(2000);
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

    private GridPane geraFormularioGridPane() throws SQLException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataLabel = new Text("Data: ");
        dataLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(dataLabel,0,0);
        dataDatePicker = new DatePicker(LocalDate.now());
        gridPane.add(dataDatePicker,0,1);

        Text clienteLabel = new Text("Cliente: ");
        clienteLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(clienteLabel,1,0);
        clienteComboBox = new ComboBox<>();
        ClienteDao clienteDao = new ClienteDao();
        ObservableList<Cliente> clienteList = clienteDao.buscaClientes();
        clienteComboBox.setItems(clienteList);
        gridPane.add(clienteComboBox,1,1);

        Text statusLabel = new Text("Status: ");
        statusLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(statusLabel,2,0);
        statusComboBox = new ComboBox<>();
        ObservableList<String> statusList = FXCollections.observableArrayList("pagou","deve");
        statusComboBox.setItems(statusList);
        gridPane.add(statusComboBox,2,1);

        Text quantidadeLabel = new Text("Qtde: ");
        quantidadeLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(quantidadeLabel,3,0);
        quantidadeComboBox = new ComboBox<>();
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
        valorTextField.setOnMousePressed(event -> Platform.runLater(()->valorTextField.positionCaret(valorTextField.getText().length())));

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
                    if(numeroDigitado.equals("")) {
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
        EntradaDao entradaDao = new EntradaDao();

        salvarButton.defaultButtonProperty().bind(salvarButton.focusedProperty());

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

            String statusStr = statusComboBox.getValue();

            String quantidadeStr = quantidadeComboBox.getValue();

            String valorStr = valorTextField.getText();

            Ganho ganho = new Ganho();
            ganho.setData(dataStr);
            ganho.setDiaSemana(diaSemanaStr);
            ganho.setCliente(clienteStr);
            ganho.setStatus(statusStr);
            ganho.setQuantidade(quantidadeStr);
            ganho.setValor(valorStr);

            Entrada entrada;

            if (statusStr.equals("pagou")) {

                entrada = new Entrada();

                SimpleDateFormat dataHoraSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String dataHoraStr = dataHoraSdf.format(Calendar.getInstance().getTime());
                entrada.setDataHora(dataHoraStr);

                entrada.setDescricao(clienteStr);

                BigDecimal quantidadeBdc = new BigDecimal(quantidadeStr);
                if (valorStr.equals("")) {
                    valorStr = "R$ 0,00";
                }
                String valorNf = "";
                try {
                    valorNf = NumberFormat.getCurrencyInstance().parse(valorStr).toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BigDecimal valorBdc = new BigDecimal(valorNf);

                BigDecimal totalValorBdc = valorBdc.multiply(quantidadeBdc);

                String totalValorStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalValorBdc);

                entrada.setValor(totalValorStr);

                entrada.setUltimaEdicao("-");

                try {
                    entradaDao.insert(entrada);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(flag == Flag.EDITAR) {
                ganhoDao.updateEditar(ganho,idEditar);
                flag = null;
            } else {
                try {
                    ganhoDao.insert(ganho);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            limpaFormulario();
            try {
                atualizaGanhoBuscarPorQuantidadeDeMeses();
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            Platform.runLater(()->{
//                try {
//                    geraGanhoTableView();
//                    geraDevedorTableView();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            });
        });



        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(novoButton,1,1);

        novoButton.setOnAction(event -> {
            dataDatePicker = new DatePicker(LocalDate.now());

            clienteComboBox.setItems(null);
            ClienteDao clienteDao = new ClienteDao();
            ObservableList<Cliente> clienteList = null;
            try {
                clienteList = clienteDao.buscaClientes();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            clienteComboBox.setItems(clienteList);

            statusComboBox.setItems(null);
            ObservableList<String> statusList = FXCollections.observableArrayList("pagou","deve");
            statusComboBox.setItems(statusList);

            quantidadeComboBox.setItems(null);
            quantidadeComboBox.setMaxWidth(50);
            ObservableList<String> quantidadeList = FXCollections.observableArrayList("1","2","3","4","5","6","7","8");
            quantidadeComboBox.setItems(quantidadeList);

            valorTextField.setText("");
            valorTextField.setMaxWidth(80);
            valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0")));

            numeroDigitado = "";

            flag = null;
        });

        listarTudoButton = new Button("Listar Tudo");
        listarTudoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(listarTudoButton,2,1);

        return gridPane;
    }

    private void atualizaGanhoBuscarPorQuantidadeDeMeses() throws SQLException {
        ganhoBuscarPorQuantidadeDeMesesComboBox.setItems(null);
        GanhoDao ganhoDao = new GanhoDao();
        Integer quantidadeMesesInt = ganhoDao.buscarQuantidadeMeses();
        ObservableList<String> listString = FXCollections.observableArrayList();
        for (int i=0; i<quantidadeMesesInt; i++) {
            listString.add(""+(i+1));
        }
        ganhoBuscarPorQuantidadeDeMesesComboBox.setItems(listString);
    }

    private void limpaFormulario() {
        //dataDatePicker = new DatePicker(LocalDate.now());
        numeroDigitado = "";
    }

    private GridPane geraBuscasGridPane() throws SQLException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text buscarPorLabel = new Text("Buscar por: ");
        buscarPorLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(buscarPorLabel,0,0);

        Text buscarPorClienteLabel = new Text("Cliente");
        buscarPorClienteLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(buscarPorClienteLabel,0,1);
        buscarPorClienteComboBox = new ComboBox<>();
        ClienteDao clienteDao = new ClienteDao();
        ObservableList<Cliente> clienteList = clienteDao.buscaClientes();
        buscarPorClienteComboBox.setItems(clienteList);
        gridPane.add(buscarPorClienteComboBox,0,2);

        Text buscarPorStatusLabel = new Text("Status");
        buscarPorStatusLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(buscarPorStatusLabel,1,1);
        buscarPorStatusComboBox = new ComboBox<>();
        ObservableList<String> pagouDeveList = FXCollections.observableArrayList("pagou","deve");
        buscarPorStatusComboBox.setItems(pagouDeveList);
        gridPane.add(buscarPorStatusComboBox,1,2);

        return gridPane;
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

        ObservableList<Ganho> list1 = ganhoDao.buscaDevedor();

        tableView.getColumns().clear();
        tableView.setItems(list1);
        tableView.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);

        Platform.runLater(()->buscarPorClienteComboBox.valueProperty().addListener((observable, oldValue, newValue)->{
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

        Platform.runLater(()-> buscarPorStatusComboBox.valueProperty().addListener((observable, oldValue, newValue)->{
            ObservableList<Ganho> list3 = null;
            try {
                if (newValue.equals("pagou")) {
                    list3 = ganhoDao.buscaPagador();
                } else if (newValue.equals("deve")) {
                    list3 = ganhoDao.buscaDevedor();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list3);
            tableView.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);
        }));

        listarTudoButton.setOnAction(event -> {
            ObservableList<Ganho> list3 = null;
            try {
                list3 = ganhoDao.listarTudo();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list3);
            tableView.getColumns().addAll(dataColuna,diaSemanaColuna,clienteColuna,statusColuna,sessaoColuna,qtdeColuna,valorColuna);
        });

        return tableView;
    }

    private GridPane geraGanhoTableViewGridPane(TableView<Ganho> ganhoTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        ganhoTableView.setPrefWidth(5000);
        ganhoTableView.setPrefHeight(2000);
        gridPane.add(ganhoTableView,0,0);

        return gridPane;
    }

    private GridPane geraPrincipalGridPane(GridPane formularioGridPane, GridPane botoesGridPane, GridPane buscasGridPane, GridPane ganhoTableViewGridPane/*, GridPane ganhadorTableViewGridPane, GridPane devedorTableViewGridPane*/) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(formularioGridPane, 0,0);
        gridPane.add(botoesGridPane,0,1);
        gridPane.add(buscasGridPane,0,2);
        gridPane.add(ganhoTableViewGridPane,0,3);
//        gridPane.add(ganhadorTableViewGridPane,0,3);
//        gridPane.add(devedorTableViewGridPane,0,4);

        return gridPane;
    }


}
