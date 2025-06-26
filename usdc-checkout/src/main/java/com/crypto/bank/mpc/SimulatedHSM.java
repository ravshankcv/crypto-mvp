package com.crypto.bank.mpc;

import java.math.BigInteger;

public class SimulatedHSM {
    private final BigInteger storedKey;

    public SimulatedHSM(BigInteger privateKey) {
        this.storedKey = privateKey;
    }

    public String signTransaction(String message, BigInteger keyAttempt) {
        if (keyAttempt.equals(storedKey)) {
            return "signed(" + message + ")";
        } else {
            return "unauthorized";
        }
    }
}
