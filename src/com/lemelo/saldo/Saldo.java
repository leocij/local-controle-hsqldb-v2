package com.lemelo.saldo;

public class Saldo {
    private Integer id;
    private String data;
    //private String sobrouMesPassado;
    private String saldo;
    //private String dataUltimaConsultaSaldo;

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

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }


    @Override
    public String toString() {
        return "Saldo{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", saldo='" + saldo + '\'' +
                '}';
    }
}
