package com.crypto.bank.mpc;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class SecretShare {
    private static final SecureRandom random = new SecureRandom();

    public static Map<Integer, BigInteger> splitSecret(BigInteger secret, int n, int k, BigInteger prime) {
        BigInteger[] coeffs = new BigInteger[k - 1];
        for (int i = 0; i < k - 1; i++) {
            coeffs[i] = new BigInteger(prime.bitLength(), random).mod(prime);
        }

        Map<Integer, BigInteger> shares = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            BigInteger x = BigInteger.valueOf(i);
            BigInteger y = secret;
            for (int j = 0; j < k - 1; j++) {
                y = y.add(coeffs[j].multiply(x.pow(j + 1))).mod(prime);
            }
            shares.put(i, y);
        }
        return shares;
    }

    public static BigInteger reconstructSecret(Map<Integer, BigInteger> shares, BigInteger prime) {
        BigInteger secret = BigInteger.ZERO;

        for (Map.Entry<Integer, BigInteger> entryI : shares.entrySet()) {
            int xi = entryI.getKey();
            BigInteger yi = entryI.getValue();
            BigInteger li = BigInteger.ONE;

            for (Map.Entry<Integer, BigInteger> entryJ : shares.entrySet()) {
                int xj = entryJ.getKey();
                if (xi != xj) {
                    li = li.multiply(BigInteger.valueOf(xj).negate())
                            .multiply(BigInteger.valueOf(xi - xj).modInverse(prime))
                            .mod(prime);
                }
            }
            secret = secret.add(yi.multiply(li)).mod(prime);
        }
        return secret;
    }
}
