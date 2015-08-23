package joebowbeer;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Solves the Dragon challenge.
 */
public class Dragon {

  private static final String OUT = "out";
  private static final String FAILURE = "failure";

  private Dragon() { }

  /**
   * Reads input from System.in and writes solution to System.out.
   *
   * @param args the unused command line arguments
   */
  public static void main(String[] args) {
    solve(System.in, System.out);
  }

  /**
   * Reads input from given input stream and writes solution to given print stream.
   *
   * @param args the unused command line arguments
   */
  static void solve(InputStream in, PrintStream out) {
    try {
      out.println(format(solve(parse(in))));
    } catch (RuntimeException ex) {
//      ex.printStackTrace();
      out.println(FAILURE);
    }
  }

  /**
   * Returns array of numbers read from the given input stream.
   */
  static int[] parse(InputStream in) {
    IntStream.Builder builder = IntStream.builder();
    Scanner scanner = new Scanner(in);
    while (scanner.hasNextInt()) {
      int value = scanner.nextInt();
      if (value < 0) {
        throw new IllegalArgumentException();
      }
      builder.add(value);
    }
    return builder.build().toArray();
  }

  /**
   * Returns string representation for the given solution.
   */
  static String format(int... indices) {
    if (indices.length == 0) {
      return FAILURE;
    }
    return Arrays.stream(indices)
        .mapToObj(i -> String.valueOf(i))
        .collect(Collectors.joining(", ", "", ", " + OUT));
  }

  /**
   * Represents list of indices in a traversal.
   * Each node is linked to the previously visited node.
   */
  private static class Node {

    final int index;
    final Node prev;

    Node(int index, Node prev) {
      this.index = index;
      this.prev = prev;
    }
  }

  /**
   * Searches for a shortest solution.
   *
   * @param canyon array of positive numbers representing dragons and maximum flight lengths
   * @return shortest solution found, or an empty array if no solution found
   */
  static int[] solve(int[] canyon) {
    Queue<Node> queue = new ArrayDeque<>();
    boolean[] visited = new boolean[canyon.length];
    if (canyon.length > 0) {
      // visit first element
      queue.add(new Node(0, null));
      visited[0] = true;
    }
    // Breadth-first search to find a traveral with the smallest number of flights.
    for (int flights = 1; !queue.isEmpty(); flights++) {
      for (int traversals = queue.size(); --traversals >= 0;) {
        Node traversal = queue.remove();
        int lastIndex = traversal.index;
        int longestFlight = canyon[lastIndex];
        // If longestFlight is 0, a dragon be there and the loop below does nothing.
        for (int flight = longestFlight; flight > 0; --flight) {
          int nextIndex = lastIndex + flight;
          if (nextIndex >= canyon.length) {
            // Canyon traversed! Return array of indices.
            int[] result = new int[flights];
            Node node = traversal;
            for (int index = flights; --index >= 0; node = node.prev) {
              result[index] = node.index;
            }
            assert node == null;
            return result;
          }
          // If we jumped to new element, enqueue new traversal for future exploration.
          if (!visited[nextIndex]) {
            queue.add(new Node(nextIndex, traversal));
            visited[nextIndex] = true;
          }
        }
      }
    }
    // no solution
    return new int[0];
  }
}
