package com.universae;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public static boolean register(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashed);
            pstmt.executeUpdate();
            System.out.println("Usuario registrado: " + username + ", hash: " + hashed);
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public static boolean login(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                boolean isValid = BCrypt.checkpw(password, hashed);
                System.out.println("Login attempt - Username: " + username + ", Password: " + password);
                System.out.println("Stored hash: " + hashed);
                System.out.println("Login result: " + (isValid ? "success" : "failure"));
                return isValid;
            } else {
                System.out.println("Usuario no encontrado: " + username);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
            return false;
        }
    }
}