package coursera.first.karatsuba;

import java.math.BigInteger;

public final class RecursiveMultiplication {

    public static void main(final String[] args) {
        final String[] num1 = "3141592653589793238462643383279502884197169399375105820974944592".split("");
        final String[] num2 = "2718281828459045235360287471352662497757247093699959574966967627".split("");
        final short[] arr1 = new short[num1.length];
        final short[] arr2 = new short[num2.length];
        for (int i = 0; i < num1.length; i++) {
            arr1[i] = Short.parseShort(num1[i]);
            arr2[i] = Short.parseShort(num2[i]);
        }
        final RecursiveMultiplication algorithm = new RecursiveMultiplication();
        final String result = algorithm.multiply(arr1, arr2);
        System.out.println(result);
        final KaratsubaAlgorithm karatsubaAlgorithm = new KaratsubaAlgorithm();
        final BigInteger karatsubaResult = karatsubaAlgorithm
                .multiply(
                        new BigInteger("3141592653589793238462643383279502884197169399375105820974944592"),
                        new BigInteger("2718281828459045235360287471352662497757247093699959574966967627")
                );
        System.out.println(karatsubaResult.toString());
    }

    public String multiply(final short[] num1, final short[] num2) {
        final int length = num1.length + num2.length;
        final short[] res = new short[length];
        for (int i = num2.length - 1, j = length - num1.length; i >= 0; i--, j--) {
            short addition = multiply(res, j, num1, 0, num2[i]);
            while (addition > 0) {
                final short local = (short) (res[i] + addition);
                res[i] = (short) (local % 10);
                addition = (short) (local / 10);
            }
        }
        return toNum(res);
    }

    public short multiply(final short[] res, final int i, final short[] num1, final int j, final int d) {
        int addition = 0;
        if (j < num1.length - 1) {
            addition = multiply(res, i + 1, num1, j + 1, d);
        }
        final short local = (short) (res[i] + num1[j] * d + addition);
        res[i] = (short) (local % 10);
        return (short) (local / 10);
    }

    private String toNum(final short[] num) {
        int i = 0;
        while (i < num.length && num[i] == 0) {
            i++;
        }
        final StringBuilder sb = new StringBuilder();
        while (i < num.length) {
            sb.append(num[i++]);
        }
        return sb.toString();
    }

    public static final class KaratsubaAlgorithm {

        //O(n^log2(3))
        public BigInteger multiply(final BigInteger x, final BigInteger y) {
            if (x.compareTo(BigInteger.TEN) < 1 || y.compareTo(BigInteger.TEN) < 1) {
                return x.multiply(y);
            }
            BigInteger max = x.max(y);
            BigInteger tmp = max;
            int i = 0;
            while (tmp.compareTo(BigInteger.ZERO) > 0) {
                tmp = tmp.divide(BigInteger.TEN);
                i++;
            }
            BigInteger div = BigInteger.ONE;
            if (i % 2 > 0) {
                max = max.multiply(BigInteger.TEN);
                div = BigInteger.TEN;
                i++;
            }
            BigInteger min = x.min(y);
            tmp = min;
            while (tmp.compareTo(BigInteger.ZERO) > 0) {
                tmp = tmp.divide(BigInteger.TEN);
                i--;
            }
            if (i % 2 > 0) {
                div = div.multiply(BigInteger.TEN);
            }
            while (i > 0) {
                min = min.multiply(BigInteger.TEN);
                i--;
            }
            return doMultiply(max, min).divide(div);
        }

        public BigInteger doMultiply(final BigInteger x, final BigInteger y) {
            if (x.compareTo(BigInteger.TEN) < 1 || y.compareTo(BigInteger.TEN) < 1) {
                return x.multiply(y);
            }
            BigInteger tmp = x;
            int i = 0;
            while (tmp.compareTo(BigInteger.ZERO) > 0) {
                tmp = tmp.divide(BigInteger.TEN);
                i++;
            }
            i /= 2;
            final BigInteger extent = BigInteger.TEN.pow(i);
            final BigInteger[] xHalves = new BigInteger[] {(x.divide(extent)), x.mod(extent)};
            final BigInteger[] yHalves = new BigInteger[] {(y.divide(extent)), y.mod(extent)};
            final BigInteger step1 = doMultiply(xHalves[0], yHalves[0]);
            final BigInteger step2 = doMultiply(xHalves[1], yHalves[1]);
            final BigInteger step3 = doMultiply(xHalves[0].add(xHalves[1]), yHalves[0].add(yHalves[1])).subtract(step2).subtract(step1);
            return extent.pow(2).multiply(step1).add(extent.multiply(step3)).add(step2);
        }

    }

}
