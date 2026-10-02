package vistas;

import datos.RepartidorDAO;
import models.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RepartidorFrame extends JFrame {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    private final JTextField txtNombre = new JTextField(15);

    private final DefaultTableModel modelo =
            new DefaultTableModel(new String[]{"ID", "Nombre"}, 0);

    private final JTable tabla = new JTable(modelo);

    public RepartidorFrame() {

        super("CRUD de Repartidores");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

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

                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            }
        });

        cargar();
    }

    private void cargar() {

        try {

            modelo.setRowCount(0);

            for (Repartidor r : repartidorDAO.listar()) {

                modelo.addRow(new Object[]{r.getId(), r.getNombre()});
            }

        } catch (Exception e) {

            error(e);
        }
    }

    private void crear() {

        try {

            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar un nombre",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            repartidorDAO.crear(nombre);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor creado exitosamente",
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
                    "Debe seleccionar un repartidor"
            );

            return;
        }

        try {

            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar un nombre",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());

            repartidorDAO.actualizar(id, nombre);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado exitosamente",
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
                    "Seleccione un repartidor de la tabla",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar este repartidor?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            repartidorDAO.eliminar(id);

            limpiar();
            cargar();

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            error(e);
        }
    }

    private void limpiar() {

        txtNombre.setText("");
        tabla.clearSelection();
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
