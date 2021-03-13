package mck.mvnmon.util;

/**
 * An algorithm for measuring the difference between two character sequences.
 *
 * <p>This is the number of changes needed to change one sequence into another, where each change is
 * a single character modification (deletion, insertion or substitution).
 *
 * <p>This code has been adapted from Apache Commons Lang 3.3.
 *
 * @since 1.0
 */
public enum LevenshteinDistance {
  INSTANCE;

  /**
   * Find the Levenshtein distance between two Strings.
   *
   * <p>A higher score indicates a greater distance.
   *
   * <p>The previous implementation of the Levenshtein distance algorithm was from <a
   * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
   *
   * <p>Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError which
   * can occur when my Java implementation is used with very large strings.<br>
   * This implementation of the Levenshtein distance algorithm is from <a
   * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a>
   *
   * <pre>
   * distance.apply(null, *)             = IllegalArgumentException
   * distance.apply(*, null)             = IllegalArgumentException
   * distance.apply("","")               = 0
   * distance.apply("","a")              = 1
   * distance.apply("aaapppp", "")       = 7
   * distance.apply("frog", "fog")       = 1
   * distance.apply("fly", "ant")        = 3
   * distance.apply("elephant", "hippo") = 7
   * distance.apply("hippo", "elephant") = 7
   * distance.apply("hippo", "zzzzzzzz") = 8
   * distance.apply("hello", "hallo")    = 1
   * </pre>
   *
   * @param left the first string, must not be null
   * @param right the second string, must not be null
   * @return result distance, or -1
   * @throws IllegalArgumentException if either String input {@code null}
   */
  public static int apply(CharSequence left, CharSequence right) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("CharSequences must not be null");
    }

    /*
      This implementation use two variable to record the previous cost counts,
      So this implementation use less memory than previous impl.
    */

    int n = left.length(); // length of left
    int m = right.length(); // length of right

    if (n == 0) {
      return m;
    }
    if (m == 0) {
      return n;
    }

    if (n > m) {
      // swap the input strings to consume less memory
      final CharSequence tmp = left;
      left = right;
      right = tmp;
      n = m;
      m = right.length();
    }

    final int[] p = new int[n + 1];

    // indexes into strings left and right
    int i; // iterates through left
    int j; // iterates through right
    int upperLeft;
    int upper;

    char rightJ; // jth character of right
    int cost; // cost

    for (i = 0; i <= n; i++) {
      p[i] = i;
    }

    for (j = 1; j <= m; j++) {
      upperLeft = p[0];
      rightJ = right.charAt(j - 1);
      p[0] = j;

      for (i = 1; i <= n; i++) {
        upper = p[i];
        cost = left.charAt(i - 1) == rightJ ? 0 : 1;
        // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
        p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upperLeft + cost);
        upperLeft = upper;
      }
    }

    return p[n];
  }
}
