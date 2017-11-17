package com.lemelo.saldo;

public class Saldo {
    private Integer id;
    private String data;
    private String totalEntrada;
    private String totalSaida;
    private String saldo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTotalEntrada() {
        return totalEntrada;
    }

    public void setTotalEntrada(String totalEntrada) {
        this.totalEntrada = totalEntrada;
    }

    public String getTotalSaida() {
        return totalSaida;
    }

    public void setTotalSaida(String totalSaida) {
        this.totalSaida = totalSaida;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Data: " + data +
                "Total da Entrada: " + totalEntrada +
                "Total da Saida: " + totalSaida +
                "Saldo: " + saldo;
    }
}
