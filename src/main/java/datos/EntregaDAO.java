package datos;

import conexion.ConexionBD;
import models.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public EntregaDAO() {
        try (
                Connection c = ConexionBD.obtenerConexion();
                Statement st = c.createStatement()
        ) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS entregas (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        id_pedido INT,
                        id_repartidor INT,
                        fecha DATE,
                        hora TIME,
                        FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
                        FOREIGN KEY (id_repartidor) REFERENCES repartidores(id)
                    )
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void crear(
            int idPedido,
            int idRepartidor,
            java.time.LocalDate fecha,
            java.time.LocalTime hora
    ) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO entregas " +
                                "(id_pedido, id_repartidor, fecha, hora) " +
                                "VALUES (?, ?, ?, ?)"
                )
        ) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idRepartidor);
            ps.setDate(3, java.sql.Date.valueOf(fecha));
            ps.setTime(4, java.sql.Time.valueOf(hora));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Entrega> listar() throws Exception {

        List<Entrega> entregas = new ArrayList<>();

        try (
                Connection c = ConexionBD.obtenerConexion();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM entregas")
        ) {
            while (rs.next()) {

                entregas.add(
                        new Entrega(
                                rs.getInt("id"),
                                rs.getInt("id_pedido"),
                                rs.getInt("id_repartidor"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getTime("hora").toLocalTime()
                        )
                );
            }
        }

        return entregas;
    }

    public void actualizar(
            int id,
            int idPedido,
            int idRepartidor,
            java.time.LocalDate fecha,
            java.time.LocalTime hora
    ) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE entregas " +
                                "SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? " +
                                "WHERE id = ?"
                )
        ) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idRepartidor);
            ps.setDate(3, java.sql.Date.valueOf(fecha));
            ps.setTime(4, java.sql.Time.valueOf(hora));
            ps.setInt(5, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminar(int id) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM entregas WHERE id = ?"
                )
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
