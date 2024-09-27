package com.example;

public class Cuenta {
    private boolean estado;
    private long nro_cuenta;
    private double saldo;
    private String banco;

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public long getNro_cuenta() {
        return nro_cuenta;
    }

    public void setNro_cuenta(long nro_cuenta) {
        this.nro_cuenta = nro_cuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}
