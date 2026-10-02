package datos;

import conexion.ConexionBD;
import models.EstadoPedido;
import models.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public PedidoDAO() {
        try (
                Connection c = ConexionBD.obtenerConexion();
                Statement st = c.createStatement()
        ) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS pedidos (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        direccion VARCHAR(100) NOT NULL,
                        tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
                        estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO')
                    )
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void crear(String direccion, String tipo, EstadoPedido estado) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO pedidos(direccion, tipo, estado) VALUES(?, ?, ?)"
                )
        ) {
            ps.setString(1, direccion);
            ps.setString(2, tipo);
            ps.setString(3, estado.toString());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> listar() throws Exception {

        List<Pedido> pedidos = new ArrayList<>();

        try (
                Connection c = ConexionBD.obtenerConexion();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM pedidos")
        ) {
            while (rs.next()) {

                pedidos.add(
                        new Pedido(
                                rs.getInt("id"),
                                rs.getString("direccion"),
                                EstadoPedido.valueOf(rs.getString("estado")),
                                rs.getString("tipo")
                        )
                );
            }
        }

        return pedidos;
    }

    public void actualizar(
            int id,
            String direccion,
            String tipo,
            EstadoPedido estado
    ) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?"
                )
        ) {
            ps.setString(1, direccion);
            ps.setString(2, tipo);
            ps.setString(3, estado.toString());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminar(int id) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM pedidos WHERE id = ?"
                )
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
