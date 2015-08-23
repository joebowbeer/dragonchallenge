package joebowbeer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Random;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DragonTest {

  @Test
  public void solvesEmpty() {
    assertArrayEquals(new int[0], Dragon.solve(new int[0]));
  }

  @Test
  public void solvesOne() {
    assertArrayEquals(new int[]{0}, Dragon.solve(new int[]{1}));
  }

  @Test
  public void solvesSample() {
    assertArrayEquals(new int[]{0, 5, 9}, Dragon.solve(new int[]{5, 6, 0, 4, 2, 4, 1, 0, 0, 4}));
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
  public void handlesManyElements() {
    int canyonLength = 1000000;
    int longestFlight = 50;
    int dragonCount = 5000;
    for (int trial = 0; trial < 10; trial++) {
      int[] traversal = Dragon.solve(randomCanyon(canyonLength, longestFlight, dragonCount));
      System.out.printf("Trail %d: traversal length %d%n", trial, traversal.length);
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
    if (dragonCount >= canyonLength){
      throw new IllegalArgumentException();
    }
    int[] canyon = new int[canyonLength];
    Random random = new Random();
    // The canyon starts with the clan of dragons on the left.
    // Remaining elements are initialized with random lengths.
    for (int index = dragonCount; index < canyonLength; index++) {
      canyon[index] = 1 + random.nextInt(longestFlight);
    }
    // Disperse dragons into canyon by swapping with randomly chosen elements
    for (int dragon = 0; dragon < dragonCount; dragon++) {
      int dragonIndex = dragonCount + random.nextInt(canyonLength - dragonCount);
      canyon[dragon] = canyon[dragonIndex];
      canyon[dragonIndex] = 0;
    }
    return canyon;
  }
}
