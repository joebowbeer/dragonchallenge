package joebowbeer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Random;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DragonTest {

  @Test
  public void solvesEmpty() {
    int[] canyon = new int[0];
    int[] solution = new int[0];
    checkSolution(canyon, solution);
    assertArrayEquals(solution, Dragon.solve(canyon));
  }

  @Test
  public void solvesOne() {
    int[] canyon = new int[]{1};
    int[] solution = new int[]{0};
    checkSolution(canyon, solution);
    assertArrayEquals(solution, Dragon.solve(canyon));
  }

  @Test
  public void solvesSample() {
    int[] canyon = new int[]{5, 6, 0, 4, 2, 4, 1, 0, 0, 4};
    int[] solution = new int[]{0, 5, 9};
    checkSolution(canyon, solution);
    assertArrayEquals(solution, Dragon.solve(canyon));
  }

  @Test
  public void failsDragon() {
    assertArrayEquals(new int[0], Dragon.solve(new int[]{0}));
  }

  @Test
  public void parsesEmpty() {
    assertArrayEquals(new int[0], Dragon.parse(inputStream()));
  }

  @Test
  public void parsesOne() {
    assertArrayEquals(new int[]{1}, Dragon.parse(inputStream(1)));
  }

  @Test
  public void parsesSampleInput() {
    assertArrayEquals(new int[]{5, 6, 0, 4, 2, 4, 1, 0, 0, 4},
        Dragon.parse(inputStream(5, 6, 0, 4, 2, 4, 1, 0, 0, 4)));
  }

  @Test
  public void formatsEmpty() {
    assertEquals("failure", Dragon.format());
  }

  @Test
  public void formatsZero() {
    assertEquals("0, out", Dragon.format(0));
  }

  @Test
  public void formatsSampleSolution() {
    assertEquals("0, 5, 9, out", Dragon.format(0, 5, 9));
  }

  @Test
  public void failsEmptyInput() throws IOException {
    assertEquals("failure", solutionOf());
  }

  @Test
  public void solvesOneInput() throws IOException {
    assertEquals("0, out", solutionOf(1));
  }

  @Test
  public void solvesSampleInput() throws IOException {
    assertEquals("0, 5, 9, out", solutionOf(5, 6, 0, 4, 2, 4, 1, 0, 0, 4));
  }

  @Test
  public void failsNegativeInput() throws IOException {
    assertEquals("failure", solutionOf(-1));
  }

  @Test
  public void solvesRandomCanyons() {
    int canyonLength = 1000000;
    int longestFlight = 50;
    int dragonCount = 10000;
    for (int trial = 0; trial < 16; trial++) {
      int[] canyon = randomCanyon(canyonLength, longestFlight, dragonCount);
      int[] traversal = Dragon.solve(canyon);
      System.out.printf("Trial %2d: traversal length %d%n", trial, traversal.length);
      checkSolution(canyon, traversal);
    }
  }

  private static InputStream inputStream(int... values) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos)) {
      for (int i : values) {
        ps.println(i);
      }
    }
    return new ByteArrayInputStream(baos.toByteArray());
  }

  private static String solutionOf(int... values) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos)) {
      Dragon.solve(inputStream(values), ps);
    }
    return new BufferedReader(new StringReader(baos.toString())).readLine();
  }

  private static int[] randomCanyon(
      int canyonLength, int longestFlight, int dragonCount) {
    if (dragonCount >= canyonLength) {
      throw new IllegalArgumentException();
    }
    int[] canyon = new int[canyonLength];
    Random random = new Random();
    // The canyon starts with the clan of dragons on the right, with remaining
    // elements initialized to random lengths.
    for (int index = canyonLength - dragonCount; --index >= 0;) {
      canyon[index] = 1 + random.nextInt(longestFlight);
    }
    // Shuffle the array; except first element, for no dragon should be there.
    for (int index = 1; index < canyonLength; index++) {
      int swapIndex = index + random.nextInt(canyonLength - index);
      if (swapIndex != index) {
        int swapValue = canyon[swapIndex];
        canyon[swapIndex] = canyon[index];
        canyon[index] = swapValue;
      }
    }
    return canyon;
  }

  /**
   * Validates traversal.
   */
  private static void checkSolution(int[] canyon, int[] traversal) {
    if (traversal.length > 0) {
      assertEquals(0, traversal[0]);
      int lastIndex = Arrays.stream(traversal).reduce((prevIndex, index) -> {
        assertTrue(prevIndex + canyon[prevIndex] >= index);
        return index;
      }).getAsInt();
      assertTrue(lastIndex + canyon[lastIndex] >= canyon.length);
    }
  }
}
