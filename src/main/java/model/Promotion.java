package model;

public class Promotion {
    private static boolean active = false;

    public static void setActive(boolean arg) {
        active = arg;
    }

    public static boolean isActive() {
        return active;
    }
}
