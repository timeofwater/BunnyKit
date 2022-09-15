package BunnyBot;

import java.awt.*;

/**
 * |  gap time(ms)  |start|  drag time(ms)  |end|
 * ----------------------------------------------
 *
 * @author Shang Zemo on 2022/6/24
 */
public class StandardOperation {

    private final long gapTime;
    private final Point start;
    private final long dragTime;
    private final Point end;
    private final int button;

    StandardOperation(long gapTime, int x1, int y1, long dragTime, int x2, int y2, int button) {
        this(gapTime, new Point(x1, y1), dragTime, new Point(x2, y2), button);
    }

    StandardOperation(long gapTime, Point start, long dragTime, Point end, int button) {
        this.gapTime = gapTime;
        this.button = button;
        this.start = start;
        this.dragTime = dragTime;
        this.end = end;
    }

    public long getGapTime() {
        return gapTime;
    }

    public Point getStart() {
        return start;
    }

    public long getDragTime() {
        return dragTime;
    }

    public Point getEnd() {
        return end;
    }

    public int getButton() {
        return button;
    }

    @Override
    public String toString() {
        return "StandardOperation{" +
                "gapTime=" + gapTime +
                ", start=" + start +
                ", dragTime=" + dragTime +
                ", end=" + end +
                ", button=" + button +
                '}';
    }
}
