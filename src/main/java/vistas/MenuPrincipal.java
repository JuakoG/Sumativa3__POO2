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
import java.util.List;

public class MenuPrincipal extends JFrame {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new String[]{"ID", "Dirección", "Tipo", "Estado", "Repartidor"}, 0);

    private final JTable tabla = new JTable(modelo);

    public MenuPrincipal() {

        super("SpeedFast");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel panelBotones = new JPanel();

        JButton btnPedidos = new JButton("Gestionar Pedidos");
        JButton btnRepartidores = new JButton("Gestionar Repartidores");
        JButton btnEntregas = new JButton("Gestionar Entregas");
        JButton btnActualizar = new JButton("Actualizar");

        panelBotones.add(btnPedidos);
        panelBotones.add(btnRepartidores);
        panelBotones.add(btnEntregas);
        panelBotones.add(btnActualizar);

        add(panelBotones, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnPedidos.addActionListener(e -> {
            new PedidoFrame().setVisible(true);
        });

        btnRepartidores.addActionListener(e -> {
            new RepartidorFrame().setVisible(true);
        });

        btnEntregas.addActionListener(e -> {
            new EntregaFrame().setVisible(true);
        });

        btnActualizar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {

        try {
            modelo.setRowCount(0);

            List<Pedido> pedidos = pedidoDAO.listar();
            List<Entrega> entregas = entregaDAO.listar();
            List<Repartidor> repartidores = repartidorDAO.listar();

            for (Pedido p : pedidos) {

                String nombreRepartidor = "";

                for (Entrega e : entregas) {

                    if (e.getIdPedido() == p.getId()) {

                        for (Repartidor r : repartidores) {

                            if (r.getId() == e.getIdRepartidor()) {

                                nombreRepartidor = r.getNombre();
                                break;
                            }
                        }
                        break;
                    }
                }

                modelo.addRow(new Object[]{p.getId(), p.getDireccionEntrega(), p.getTipo(), p.getEstado(), nombreRepartidor});
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
