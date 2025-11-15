package com.hospital.dao;

import com.hospital.model.AppointmentRequest;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRequestDAO {
    public AppointmentRequestDAO() {
        // No per-instance persistent Connection - obtain per-operation
    }

    public int addAppointmentRequest(AppointmentRequest request) {
        String sql = "INSERT INTO appointment_requests (patient_id, specialization, requested_date, status, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getPatientId());
            stmt.setString(2, request.getSpecialization());
            stmt.setDate(3, new java.sql.Date(request.getRequestedDate().getTime()));
            stmt.setString(4, request.getStatus());
            stmt.setString(5, request.getDescription() != null ? request.getDescription() : "");
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public AppointmentRequest getRequestById(int requestId) {
        String sql = "SELECT ar.*, p.name as patient_name FROM appointment_requests ar " +
                     "JOIN patients p ON ar.patient_id = p.patient_id WHERE ar.request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AppointmentRequest request = new AppointmentRequest(
                        rs.getInt("request_id"),
                        rs.getInt("patient_id"),
                        rs.getString("specialization"),
                        rs.getDate("requested_date"),
                        rs.getString("status")
                    );
                    request.setPatientName(rs.getString("patient_name"));
                    request.setDescription(rs.getString("description"));
                    return request;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AppointmentRequest> getAllPendingRequests() {
        List<AppointmentRequest> requests = new ArrayList<>();
        String sql = "SELECT ar.*, p.name as patient_name FROM appointment_requests ar " +
                     "JOIN patients p ON ar.patient_id = p.patient_id WHERE ar.status = 'PENDING' ORDER BY ar.requested_date";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AppointmentRequest request = new AppointmentRequest(
                    rs.getInt("request_id"),
                    rs.getInt("patient_id"),
                    rs.getString("specialization"),
                    rs.getDate("requested_date"),
                    rs.getString("status")
                );
                request.setPatientName(rs.getString("patient_name"));
                request.setDescription(rs.getString("description"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<AppointmentRequest> getRequestsByPatientId(int patientId) {
        List<AppointmentRequest> requests = new ArrayList<>();
        String sql = "SELECT ar.*, p.name as patient_name FROM appointment_requests ar " +
                     "JOIN patients p ON ar.patient_id = p.patient_id WHERE ar.patient_id = ? ORDER BY ar.requested_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AppointmentRequest request = new AppointmentRequest(
                        rs.getInt("request_id"),
                        rs.getInt("patient_id"),
                        rs.getString("specialization"),
                        rs.getDate("requested_date"),
                        rs.getString("status")
                    );
                    request.setPatientName(rs.getString("patient_name"));
                    request.setDescription(rs.getString("description"));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public boolean updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE appointment_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

