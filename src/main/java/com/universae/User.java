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
            System.out.println("usuario registrado bien " + username);
            return true;
        } catch (SQLException e) {
            System.out.println("error al registrar " + e.getMessage());
            return false;
        }
    }

    public static int login(String username, String password) {
        String sql = "SELECT id, password FROM users WHERE username = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    int userId = rs.getInt("id");
                    System.out.println("el login fue exitoso " + userId);
                    return userId;
                } else {
                    System.out.println("contraseña incorrecta " + username);
                    return -1;
                }
            } else {
                System.out.println("user no encontrado " + username);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("eerror en login " + e.getMessage());
            return -1;
        }
    }
}