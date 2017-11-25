package com.lemelo.cliente;

import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDao {
    public ObservableList<Cliente> buscaClientes() throws SQLException {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        String clienteSqlSelect = "select * from cliente order by nome";
        ResultSet resultSet = new FabricaConexao().getResultSet(clienteSqlSelect);
        while (resultSet.next()){
            Cliente cliente = new Cliente();
            cliente.setId(resultSet.getInt("id"));
            cliente.setNome(resultSet.getString("nome"));
            clientes.add(cliente);
        }
        resultSet.close();
        return clientes;
    }

    public void insert(Cliente cliente) {
        String nomeStr = cliente.getNome();

        String clienteSqlInsert = "insert into cliente (nome) values ('"+nomeStr+"')";
        new FabricaConexao().insert(clienteSqlInsert);
    }
}
