package com.lemelo.entrada;

import com.lemelo.saldo.SaldoLogica;
import com.lemelo.saldo.SaldoResumoNode;
import com.lemelo.util.Flag;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EntradaNode {
    private Integer idEditar;
    private String ultimaEdicaoEditar;
    private Flag flag;

    String numeroDigitado;

    public Node executar(Tab entradaTab) throws SQLException {

        Text tituloLabel = new Text("\nEntrada de Valores");
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
        gridPane.add(dataHoraTextField, 0, 1);

        Text descricaoLabel = new Text("Descrição: ");
        TextField descricaoTextField = new TextField();
        descricaoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        descricaoTextField.setPrefWidth(5000);
        gridPane.add(descricaoLabel, 0, 2);
        gridPane.add(descricaoTextField, 0, 3);

        entradaTab.setOnSelectionChanged(e->{
            if (entradaTab.isSelected() == true) {
                Platform.runLater(()->descricaoTextField.requestFocus());
            }
        });

        Text valorLabel = new Text("Valor: ");
        TextField valorTextField = new TextField();
        valorLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorLabel, 0, 4);
        gridPane.add(valorTextField, 0, 5);
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


        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-font: normal bold 15px 'verdana' ");

        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");

        GridPane botoesGridPane = new GridPane();
        botoesGridPane.setPadding(new Insets(5, 2, 0, 2));
        botoesGridPane.setVgap(5);
        botoesGridPane.setHgap(5);
        botoesGridPane.setAlignment(Pos.TOP_LEFT);

        botoesGridPane.add(salvarButton, 0, 0);
        botoesGridPane.add(novoButton, 1, 0);

        novoButton.setOnAction(event -> {
            limpaFormulario(dataHoraTextField, descricaoTextField, valorTextField);
        });

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
        ultimaEdicaoColuna.setMinWidth(100);
        ultimaEdicaoColuna.setCellValueFactory(new PropertyValueFactory<>("ultimaEdicao"));

        EntradaDao entradaDao = new EntradaDao();

        entradaTableView(entradaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);

        salvarButton.defaultButtonProperty().bind(salvarButton.focusedProperty());

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
                    //Atualiza total da entrada


                    SaldoLogica saldoLogica = new SaldoLogica();
                    saldoLogica.calcularEntrada(dataHoraStr, valorStr);

                    SaldoResumoNode saldoResumoNode = new SaldoResumoNode();
                    String mesAno = dataHoraStr.substring(3,10);
                    saldoResumoNode.atualizaTotalEntrada(mesAno);

                    entradaDao.insert(entrada);
                }

                limpaFormulario(dataHoraTextField, descricaoTextField, valorTextField);

                entradaTableView(entradaDao, tableView, dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {

                Entrada entrada = tableView.getSelectionModel().getSelectedItem();

                if (entrada != null) {
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

                            String valorStr = entrada.getValor();
                            try {
                                String valorNf = NumberFormat.getCurrencyInstance().parse(valorStr).toString();
                                valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(valorNf)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

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
            }
        });

        Text avisoLabel = new Text("Dê dois cliques na linha da tabela para Editar ou Excluir um registro.");
        avisoLabel.setStyle("-fx-font: normal 15px 'verdana' ");

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(tituloLabel, gridPane, botoesGridPane, avisoLabel, tableView);

        return vBox;
    }

    private void limpaFormulario(TextField dataHoraTextField, TextField descricaoTextField, TextField valorTextField) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        dataHoraTextField.setText(sdf2.format(Calendar.getInstance().getTime()));

        descricaoTextField.setText("");
        descricaoTextField.requestFocus();

        valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0")));

        numeroDigitado = "";
    }

    private String removeUltimoDigito(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void entradaTableView(EntradaDao entradaDao, TableView<Entrada> tableView, TableColumn dataHoraColuna, TableColumn descricaoColuna, TableColumn valorColuna, TableColumn ultimaEdicaoColuna) throws SQLException {
        ObservableList<Entrada> list = entradaDao.listAll();
        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
    }
}
