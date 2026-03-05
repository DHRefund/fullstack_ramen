package com.sapporo.ramenbooking.util;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class AdminCredentials {

    // Hardcode admin accounts (username -> password)
    @Getter
    private static final Map<String, String> ADMIN_ACCOUNTS = Map.of(
            "admin", "admin123",
            "manager", "manager123",
            "staff", "staff123");

    @Getter
    private static final Map<String, String> ADMIN_ROLES = Map.of(
            "admin", "ADMIN",
            "manager", "MANAGER",
            "staff", "STAFF");

    @Getter
    private static final Map<String, String> ADMIN_NAMES = Map.of(
            "admin", "Quản Trị Viên",
            "manager", "Quản Lý",
            "staff", "Nhân Viên");

    public static boolean validate(String username, String password) {
        String storedPassword = ADMIN_ACCOUNTS.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public static String getRole(String username) {
        return ADMIN_ROLES.getOrDefault(username, "STAFF");
    }

    public static String getFullName(String username) {
        return ADMIN_NAMES.getOrDefault(username, "Admin");
    }
}