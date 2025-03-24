package com.habit.app.service;



public class UserRegisterRestriction {
    // Username restrictions
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 16;
    private static final String USERNAME_PATTERN = "^\\w{3,16}$";

    // Password restrictions
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 64;
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,64}$";

    // Email restrictions
    private static final String EMAIL_PATTERN = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+$";

    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        return username.length() >= USERNAME_MIN_LENGTH && username.length() <= USERNAME_MAX_LENGTH
                && username.matches(USERNAME_PATTERN);
    }

    public static boolean isPasswordMatch(String password, String repeatPassword)
    {
        if(password.equals(repeatPassword))
            return true;
        else return false;
    }
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= PASSWORD_MIN_LENGTH && password.length() <= PASSWORD_MAX_LENGTH
                && password.matches(PASSWORD_PATTERN);
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }
}
