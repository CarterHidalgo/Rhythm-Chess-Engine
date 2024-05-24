package helper;

public class Vec2 {
    private float x;
    private float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getXAsInt() {
        return (int) x;
    }

    public int getYAsInt() {
        return (int) y;
    }

    public byte getXAsByte() {
        return (byte) x;
    }

    public byte getYAsByte() {
        return (byte) y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "x: " + x + "\ny: " + y;
    }
}
