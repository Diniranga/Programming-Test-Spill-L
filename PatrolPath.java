import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;

public class PatrolPath {

    private static final class Point {
        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        FastInput in = new FastInput();
        int n = in.nextInt();

        Point[] points = new Point[4 * n];
        for (int i = 0; i < n; i++) {
            double x1 = in.nextDouble();
            double y1 = in.nextDouble();
            double x2 = in.nextDouble();
            double y2 = in.nextDouble();

            double xMin = Math.min(x1, x2);
            double xMax = Math.max(x1, x2);
            double yMin = Math.min(y1, y2);
            double yMax = Math.max(y1, y2);

            int base = 4 * i;
            points[base]     = new Point(xMin, yMin);
            points[base + 1] = new Point(xMax, yMin);
            points[base + 2] = new Point(xMax, yMax);
            points[base + 3] = new Point(xMin, yMax);
        }

        Point[] hull = convexHull(points);
        double length = perimeter(hull);

        System.out.printf(Locale.ROOT, "%.6f%n", length);
    }

    private static double cross(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }

    private static double distance(Point p, Point q) {
        double dx = p.x - q.x;
        double dy = p.y - q.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static Point[] convexHull(Point[] points) {
        int n = points.length;
        Arrays.sort(points,
            Comparator.<Point>comparingDouble(p -> p.x)
                      .thenComparingDouble(p -> p.y));

        Point[] hull = new Point[2 * n];
        int k = 0;

        for (int i = 0; i < n; i++) {
            while (k >= 2 && cross(hull[k - 2], hull[k - 1], points[i]) <= 0) {
                k--;
            }
            hull[k++] = points[i];
        }

        int lowerSize = k + 1;
        for (int i = n - 2; i >= 0; i--) {
            while (k >= lowerSize && cross(hull[k - 2], hull[k - 1], points[i]) <= 0) {
                k--;
            }
            hull[k++] = points[i];
        }

        return Arrays.copyOf(hull, k - 1);
    }

    private static double perimeter(Point[] hull) {
        int k = hull.length;
        if (k <= 1) return 0.0;
        if (k == 2) return 2.0 * distance(hull[0], hull[1]);

        double sum = 0.0;
        for (int i = 0; i < k; i++) {
            sum += distance(hull[i], hull[(i + 1) % k]);
        }
        return sum;
    }

    private static final class FastInput {
        private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer tokens;

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        private String next() throws IOException {
            while (tokens == null || !tokens.hasMoreTokens()) {
                tokens = new StringTokenizer(reader.readLine());
            }
            return tokens.nextToken();
        }
    }
}
