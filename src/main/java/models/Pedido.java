package models;

public class Pedido implements Comparable<Pedido> {
    private int id;
    private String direccionEntrega;
    private EstadoPedido estado;
    private String tipo;
    private Repartidor repartidor;

    public Pedido(int id, String direccionEntrega, EstadoPedido estado, String tipo) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.estado = estado;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    @Override
    public int compareTo(Pedido otro) {
        return this.id - otro.id;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", estado=" + estado +
                ", tipo='" + tipo + '\'' +
                ", repartidor=" + repartidor +
                '}';
    }
}