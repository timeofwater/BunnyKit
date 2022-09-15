package BitmapPiecer;

/**
 * @author Shang Zemo on 2022/7/14
 */
public class V3 {

    int x;
    int y;
    int z;
    String name;

    V3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    float distance(V3 p2) {
        return (float) Math.sqrt(Math.pow(x - p2.x, 2) + Math.pow(y - p2.y, 2) + Math.pow(z - p2.z, 2));
    }

    @Override
    public String toString() {
        return "v3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }

}
