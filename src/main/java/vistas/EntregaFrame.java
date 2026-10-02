package vistas;

import datos.EntregaDAO;
import datos.PedidoDAO;
import datos.RepartidorDAO;
import models.Entrega;
import models.Pedido;
import models.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EntregaFrame extends JFrame {

    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    private final JComboBox<String> cboPedido = new JComboBox<>();
    private final JComboBox<String> cboRepartidor = new JComboBox<>();

    private final JTextField txtFecha = new JTextField(10);
    private final JTextField txtHora = new JTextField(6);

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new String[]{"ID", "Pedido", "Dirección", "Repartidor", "Fecha", "Hora"}, 0);

    private final JTable tabla = new JTable(modelo);

    public EntregaFrame() {

        super("Gestión de Entregas");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);

        JPanel panelDatos = new JPanel();

        panelDatos.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 5, 15)
        );

        panelDatos.add(new JLabel("Pedido:"));
        panelDatos.add(cboPedido);

        panelDatos.add(Box.createHorizontalStrut(15));

        panelDatos.add(new JLabel("Repartidor:"));
        panelDatos.add(cboRepartidor);

        panelDatos.add(Box.createHorizontalStrut(15));

        panelDatos.add(new JLabel("Fecha:"));
        panelDatos.add(txtFecha);

        panelDatos.add(Box.createHorizontalStrut(15));

        panelDatos.add(new JLabel("Hora:"));
        panelDatos.add(txtHora);

        JPanel panelBotones = new JPanel();

        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnCrear);
        panelBotones.add(Box.createHorizontalStrut(20));
        panelBotones.add(btnActualizar);
        panelBotones.add(Box.createHorizontalStrut(20));
        panelBotones.add(btnEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout());

        panelSuperior.add(panelDatos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnCrear.addActionListener(e -> crear());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        tabla.getSelectionModel().addListSelectionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                txtFecha.setText(modelo.getValueAt(fila, 4).toString());

                txtHora.setText(modelo.getValueAt(fila, 5).toString());

                String idPedido = modelo.getValueAt(fila, 1).toString();

                String repartidor = modelo.getValueAt(fila, 3).toString();

                seleccionarPedido(idPedido);
                seleccionarRepartidor(repartidor);
            }
        });

        cargarPedidos();
        cargarRepartidores();
        cargar();

        txtFecha.setText(LocalDate.now().toString());

        txtHora.setText(LocalTime.now().withSecond(0).withNano(0).toString());
    }

    private void cargarPedidos() {

        try {

            cboPedido.removeAllItems();

            for (Pedido p : pedidoDAO.listar()) {

                cboPedido.addItem(p.getId() + " - " + p.getDireccionEntrega());
            }

        } catch (Exception e) {

            error(e);
        }
    }

    private void cargarRepartidores() {

        try {

            cboRepartidor.removeAllItems();

            for (Repartidor r : repartidorDAO.listar()) {

                cboRepartidor.addItem(
                        r.getId() + " - " + r.getNombre()
                );
            }

        } catch (Exception e) {

            error(e);
        }
    }

    private void cargar() {

        try {

            modelo.setRowCount(0);

            List<Pedido> pedidos = pedidoDAO.listar();
            List<Repartidor> repartidores = repartidorDAO.listar();

            for (Entrega e : entregaDAO.listar()) {

                String direccion = "";
                String nombreRepartidor = "";

                for (Pedido p : pedidos) {

                    if (p.getId() == e.getIdPedido()) {
                        direccion = p.getDireccionEntrega();
                        break;
                    }
                }

                for (Repartidor r : repartidores) {

                    if (r.getId() == e.getIdRepartidor()) {
                        nombreRepartidor = r.getNombre();
                        break;
                    }
                }

                modelo.addRow(new Object[]{e.getId(), e.getIdPedido(), direccion, nombreRepartidor, e.getFecha(), e.getHora()});}

        } catch (Exception e) {

            error(e);
        }
    }

    private void crear() {

        try {

            int idPedido = obtenerId(cboPedido);
            int idRepartidor = obtenerId(cboRepartidor);

            LocalDate fecha = LocalDate.parse(txtFecha.getText());

            LocalTime hora = LocalTime.parse(txtHora.getText());

            entregaDAO.crear(idPedido, idRepartidor, fecha, hora);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega creada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }

    private void actualizar() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega"
            );

            return;
        }

        try {

            int id = Integer.parseInt(
                    modelo.getValueAt(fila, 0).toString()
            );

            int idPedido = obtenerId(cboPedido);
            int idRepartidor = obtenerId(cboRepartidor);

            LocalDate fecha = LocalDate.parse(txtFecha.getText());

            LocalTime hora = LocalTime.parse(txtHora.getText());

            entregaDAO.actualizar(id, idPedido, idRepartidor, fecha, hora);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega actualizada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }

    private void eliminar() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega"
            );

            return;
        }

        try {

            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());

            entregaDAO.eliminar(id);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }

    private int obtenerId(JComboBox<String> combo) {

        String texto = combo.getSelectedItem().toString();

        return Integer.parseInt(texto.split(" - ")[0]);
    }

    private void seleccionarPedido(String id) {

        for (int i = 0; i < cboPedido.getItemCount(); i++) {

            if (cboPedido.getItemAt(i).startsWith(id + " - ")) {

                cboPedido.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarRepartidor(String nombre) {

        for (int i = 0; i < cboRepartidor.getItemCount(); i++) {

            if (cboRepartidor.getItemAt(i).contains(" - " + nombre)) {

                cboRepartidor.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiar() {

        if (cboPedido.getItemCount() > 0) {
            cboPedido.setSelectedIndex(0);
        }

        if (cboRepartidor.getItemCount() > 0) {
            cboRepartidor.setSelectedIndex(0);
        }

        txtFecha.setText(LocalDate.now().toString());

        txtHora.setText(LocalTime.now().withSecond(0).withNano(0).toString());

        tabla.clearSelection();
    }

    private void error(Exception e) {

        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
