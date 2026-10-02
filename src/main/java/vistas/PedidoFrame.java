package vistas;

import datos.PedidoDAO;
import models.EstadoPedido;
import models.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PedidoFrame extends JFrame {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    private final JTextField txtDireccion = new JTextField(10);

    private final JComboBox<String> cboxTipo = new JComboBox<>(
            new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"}
    );

    private final JComboBox<EstadoPedido> cboxEstado =  new JComboBox<>(EstadoPedido.values());

    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Dirección", "Tipo", "Estado"}, 0);

    private final JTable tabla = new JTable(modelo);

    public PedidoFrame() {

        super("CRUD de Pedidos");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);

        panel.add(new JLabel("Tipo:"));
        panel.add(cboxTipo);

        panel.add(new JLabel("Estado:"));
        panel.add(cboxEstado);

        JButton btnAgregar = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panel.add(btnAgregar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> crear());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        tabla.getSelectionModel().addListSelectionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                txtDireccion.setText(modelo.getValueAt(fila, 1).toString());

                cboxTipo.setSelectedItem(modelo.getValueAt(fila, 2).toString());

                cboxEstado.setSelectedItem(EstadoPedido.valueOf(modelo.getValueAt(fila, 3).toString())
                );
            }
        });

        cargar();
    }

    private void cargar() {

        try {
            modelo.setRowCount(0);

            for (Pedido p : pedidoDAO.listar()) {
                modelo.addRow(new Object[]{p.getId(), p.getDireccionEntrega(), p.getTipo(), p.getEstado()});
            }

        } catch (Exception e) {

            error(e);
        }
    }

    private void crear() {

        try {

            String direccion = txtDireccion.getText().trim();

            if (direccion.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar una dirección",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            pedidoDAO.crear(direccion, cboxTipo.getSelectedItem().toString(), (EstadoPedido) cboxEstado.getSelectedItem());

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido creado exitosamente",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }


    private void actualizar() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        try {

            String direccion = txtDireccion.getText().trim();

            if (direccion.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar una dirección",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int id = Integer.parseInt(
                    modelo.getValueAt(fila, 0).toString()
            );

            pedidoDAO.actualizar(id, direccion, cboxTipo.getSelectedItem().toString(), (EstadoPedido) cboxEstado.getSelectedItem());

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado exitosamente",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }


    private void eliminar() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        try {

            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            pedidoDAO.eliminar(id);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado exitosamente",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }

    private void limpiar() {

        txtDireccion.setText("");
        cboxTipo.setSelectedIndex(0);
        cboxEstado.setSelectedIndex(0);
    }

    private void error(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
