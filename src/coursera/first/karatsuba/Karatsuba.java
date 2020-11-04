package coursera.first.karatsuba;

import java.math.BigInteger;

public final class Karatsuba {

    public BigInteger multiply(final BigInteger x, final BigInteger y) {
        if (x.compareTo(BigInteger.TEN) < 0 || y.compareTo(BigInteger.TEN) < 0) {
            return x.multiply(y);
        }
        final int n = x.compareTo(y) < 0 ? x.toString().length() : y.toString().length();
        final int n2 = n / 2;
        final BigInteger low1 = x.mod(BigInteger.TEN.pow(n2));
        final BigInteger high1 = x.divide(BigInteger.TEN.pow(n2));
        final BigInteger low2 = y.mod(BigInteger.TEN.pow(n2));
        final BigInteger high2 = y.divide(BigInteger.TEN.pow(n2));
        final BigInteger r0 = multiply(low1, low2);
        final BigInteger r1 = multiply(low1.add(high1), low2.add(high2));
        final BigInteger r2 = multiply(high1, high2);
        return r2.multiply(BigInteger.TEN.pow(2 * n2))
                .add(r1.subtract(r0).subtract(r2).multiply(BigInteger.TEN.pow(n2)))
                .add(r0);
    }

}
