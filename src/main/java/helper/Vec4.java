package helper;

public class Vec4 {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    public int getXAsInt() {
        return (int) x;
    }

    public int getYAsInt() {
        return (int) y;
    }

    public int getZAsInt() {
        return (int) z;
    }

    public int getWAsInt() {
        return (int) w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setW(float w) {
        this.w = w;
    }
}
