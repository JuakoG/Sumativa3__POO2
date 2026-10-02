package datos;

import conexion.ConexionBD;
import models.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public RepartidorDAO() {
        try (
                Connection c = ConexionBD.obtenerConexion();
                Statement st = c.createStatement()
        ) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS repartidores (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL
                    )
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void crear(String nombre) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO repartidores(nombre) VALUES(?)"
                )
        ) {
            ps.setString(1, nombre);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Repartidor> listar() throws Exception {

        List<Repartidor> repartidores = new ArrayList<>();

        try (
                Connection c = ConexionBD.obtenerConexion();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM repartidores")
        ) {
            while (rs.next()) {

                repartidores.add(
                        new Repartidor(
                                rs.getInt("id"),
                                rs.getString("nombre")
                        )
                );
            }
        }

        return repartidores;
    }

    public void actualizar(int id, String nombre) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE repartidores SET nombre = ? WHERE id = ?"
                )
        ) {
            ps.setString(1, nombre);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminar(int id) throws SQLException {

        try (
                Connection c = ConexionBD.obtenerConexion();
                PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM repartidores WHERE id = ?"
                )
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
