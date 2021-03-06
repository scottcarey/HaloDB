package com.oath.halodb.histo;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.junit.ArrayAsserts;

public class EstimatedHistogramTest {

    @Test
    public void testGetBuckets() {
        EstimatedHistogram estimatedHistogram = new EstimatedHistogram(
                new long[]{2, 4, 0}, new long[]{1, 2, 6, 0});
        ArrayAsserts.assertArrayEquals(new long[]{1, 2, 6, 0},
                estimatedHistogram.getBuckets(true));
        ArrayAsserts.assertArrayEquals(new long[]{0, 0, 0, 0},
                estimatedHistogram.getBuckets(false));
    }

    @Test
    public void testMin() {
        EstimatedHistogram estimatedHistogram = new EstimatedHistogram();
        Assert.assertEquals(estimatedHistogram.min(), 0l);

        estimatedHistogram.add(4l);
        estimatedHistogram.add(2l);
        estimatedHistogram.add(3l);
        Assert.assertEquals(estimatedHistogram.min(), 2l);
    }

    @Test
    public void testMax() {
        EstimatedHistogram estimatedHistogram1 =
                new EstimatedHistogram(new long[]{2, 4}, new long[]{1, 2, 6});

        Assert.assertEquals(estimatedHistogram1.max(), 9223372036854775807L);

        EstimatedHistogram estimatedHistogram2 = new EstimatedHistogram();
        Assert.assertEquals(estimatedHistogram2.max(), 0l);

        estimatedHistogram2.add(2l);
        estimatedHistogram2.add(4l);
        estimatedHistogram2.add(3l);

        Assert.assertEquals(estimatedHistogram2.max(), 4l);
    }

    @Test
    public void testPercentile() {
        long[] offsets = new long[]{2, 1, 3, 5, 4};
        long[] bucketData = new long[]{1, 2, 3, 4, 5, 0};

        EstimatedHistogram estimatedHistogram =
                new EstimatedHistogram(offsets, bucketData);

        Assert.assertEquals(estimatedHistogram.percentile(0.5), 5l);
        Assert.assertEquals(estimatedHistogram.percentile(1.0), 4l);
        Assert.assertEquals(estimatedHistogram.percentile(0.0), 0l);
    }

    @Test
    public void testMean() {
        long[] offsets = new long[]{2, 1, 3, 5, 4};
        long[] bucketData = new long[]{1, 2, 3, 4, 5, 0};

        EstimatedHistogram estimatedHistogram =
                new EstimatedHistogram(offsets, bucketData);
        Assert.assertEquals(estimatedHistogram.mean(), 4l);
    }

    @Test
    public void testIsOverflowed() {
        EstimatedHistogram estimatedHistogram1 =
                new EstimatedHistogram(new long[]{2, 1}, new long[]{1, 2, 0});
        estimatedHistogram1.add(1l);

        Assert.assertFalse(estimatedHistogram1.isOverflowed());

        EstimatedHistogram estimatedHistogram2 =
                new EstimatedHistogram(new long[]{2}, new long[]{1, 3});

        Assert.assertTrue(estimatedHistogram2.isOverflowed());
    }

    @Test
    public void testToString() {
        long[] offsets = new long[]{0, 1};
        long[] bucketData = new long[]{1, 2, 1};

        EstimatedHistogram estimatedHistogram =
                new EstimatedHistogram(offsets, bucketData);

        Assert.assertEquals(estimatedHistogram.toString(),
                "[-Inf..0]: 1\n   [1..1]: 2\n [2..Inf]: 1\n");
        Assert.assertEquals(new EstimatedHistogram(1).toString(), "");
    }

    @Test
    public void testEquals() {
        long[] offsets = new long[]{2, 1};
        long[] bucketData = new long[]{1, 2, 0};

        EstimatedHistogram estimatedHistogram =
                new EstimatedHistogram(offsets, bucketData);

        Assert.assertFalse(estimatedHistogram.equals(""));

        Assert.assertTrue(estimatedHistogram.equals(estimatedHistogram));
        Assert.assertTrue(estimatedHistogram.equals(
                new EstimatedHistogram(offsets, bucketData)));
    }
}
