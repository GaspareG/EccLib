import java.math.BigInteger;
import java.util.Random;

public class ECC {

    // Codifica di Koblitz del messaggio m sulla curva prima P
    public static Point KoblitzEnc(PrimeCurve P, BigInteger m) {
        Integer h = P.getH();
        BigInteger x = m.multiply(BigInteger.valueOf(h)); // Calcolo x = m*h
        for (int i = 0; i < h; i++) {
            // Calcolo il punto ad ascissa x, se è valido lo restituisco
            Point p = P.getPoint(x);
            if (p.isValid()) return p;
            // Altrimenti incremento x e ripeto
            x = x.add(BigInteger.ONE);
        }
        // Se non ho trovato nessun punto, ne restituisco uno invalido
        return Point.INVALID;
    }

    // Decodifica di Koblitz del punto p sulla curva prima P
    public static BigInteger KoblitzDec(PrimeCurve P, Point p) {
        Integer h = P.getH();
        BigInteger x = p.getX().divide(BigInteger.valueOf(h));
        return x;
    }

// Cifratura del messaggio m sulla curva prima P
// Con l'uso del punto base B e la chiave pubblica Pd
public static Point[] ElGamalEnc(PrimeCurve P, BigInteger m, Point B, Point Pd) {
    Point pm = ECC.KoblitzEnc(P, m);
    BigInteger r = ECC.randomBigInteger();
    Point V = P.mul(B, r);
    Point rPd = P.mul(Pd, r);
    Point W = P.sum(pm, rPd);
    return new Point[]{V, W};
}

// Decodifica del messaggio (V, W) sulla curva prima P
public static BigInteger ElGamalDec(PrimeCurve P, Point B, Point[] VW, BigInteger nD) {
    Point V = VW[0];
    Point W = VW[1];
    Point nDV = P.mul(V, nD);
    Point Pm = P.sub(W, nDV);
    BigInteger m = ECC.KoblitzDec(P, Pm);
    return m;
}

    // Prima fase dell'algoritmo ECDH, genera chiave privata nX e e chiave pubblica Px
    public static ECDHKey ECDHPhase1(PrimeCurve P, Point B, BigInteger n) {
        BigInteger nX = ECC.randomBigInteger().abs().mod(n);
        Point pX = P.mul(B, nX);
        ECDHKey key = new ECDHKey(nX, pX);
        return key;
    }

    // Seconda fase dell'algoritmo, genera il punto S in comune ai due utenti
    public static Point ECDHPhase2(PrimeCurve P, ECDHKey priv, Point pub) {
        BigInteger nX = priv.getPrivateKey();
        Point S = P.mul(pub, nX);
        return S;
    }

    // Firma digitale del messaggio m
    public static BigInteger[] ECDSASign(PrimeCurve P, Point B, BigInteger n, BigInteger m, ECDHKey key) {
        Point Q;
        BigInteger k, r, s;
        do {
            do {
                k = ECC.randomBigInteger().mod(n.subtract(BigInteger.valueOf(1))).add(BigInteger.ONE);
                Q = P.mul(B, k);
                r = Q.getX().mod(n);
            }
            while (r.compareTo(BigInteger.ZERO) == 0);
            BigInteger priv = key.getPrivateKey();
            BigInteger kinv = k.modInverse(n);
            s = r.multiply(priv).add(m).multiply(kinv).mod(n);
        }
        while (s.compareTo(BigInteger.ZERO) == 0);
        return new BigInteger[]{r, s};
    }

    public static Boolean ECDSAVerify(PrimeCurve P, Point B, BigInteger n, BigInteger[] sign, BigInteger m, ECDHKey key) {
        BigInteger r = sign[0];
        BigInteger s = sign[1];
        if (r.compareTo(BigInteger.ONE) < 0) return false;
        if (s.compareTo(BigInteger.ONE) < 0) return false;
        if (r.compareTo(n) >= 0) return false;
        if (s.compareTo(n) >= 0) return false;
        BigInteger w = s.modInverse(n);
        BigInteger u1 = m.multiply(w).mod(n);
        BigInteger u2 = r.multiply(w).mod(n);
        Point Pd = key.getPublicKey();
        Point Q = P.sum(P.mul(B, u1), P.mul(Pd, u2));
        if (Q.isInfinity()) return false;
        BigInteger v = Q.getX().mod(n);
        if (v.compareTo(r) != 0) return false;
        return true;
    }

    public static BigInteger randomBigInteger() {
        BigInteger n = new BigInteger("12345678987654321");
        Random rnd = new Random();
        int bitLength = n.bitLength();
        BigInteger ret;
        do {
            ret = new BigInteger(bitLength, rnd);
        } while (ret.compareTo(n) > 0);
        return ret;
    }
}
