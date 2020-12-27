package net.axay.blueutils.maths;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @deprecated Use kotlin ranges instead.
 */
@Deprecated
public class RandomUtils {

    public static int randomIntInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}