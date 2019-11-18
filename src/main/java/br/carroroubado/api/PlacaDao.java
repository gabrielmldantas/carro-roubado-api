package br.carroroubado.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlacaDao {

    public boolean isPlacaRoubada(String placa, String localizacao) throws SQLException, IOException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "select count(*) from placa where placa = ? and localizacao = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, placa);
                ps.setString(2, localizacao);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getLong(1) > 0;
                }
            }
        }
    }

    public void inserir(String placa, String localizacao) throws SQLException, IOException {
        try (Connection connection = DatabaseManager.getConnection()) {
            placa = placa.replace("-", "");
            String sql = "insert into placa(placa, localizacao) values (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, placa);
                ps.setString(2, localizacao);
                ps.executeUpdate();
            }
        }
    }
}
