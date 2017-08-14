import java.math.BigInteger;

public class Main {

    // Numero di test casuali da effettuare
    public static Integer NumberTest = 100;

    // Numero di secondi per ogni benchmark
    public static Integer secondBenchmark = 20;

    public static void main(String[] args) {

        // Definizioni delle 5 diverse curve usate per i test / benchmark
        // Zx: Campo finito
        // Ex: Curva prima
        // Bx: Punto base
        // nx: Ordine del punto
        // kx: Chiave per la cifratura

        // Curva E29(-7,10)
        FinitePrimeField z29 = new FinitePrimeField(29); // Caso P = 5 (mod 8)
        PrimeCurve E29 = new PrimeCurve(z29, -7, 10, 6);
        Point B29 = E29.getPoint(27);
        BigInteger n29 = E29.getOrder(B29);
        ECDHKey k29 = ECC.ECDHPhase1(E29, B29, n29);

        // Curva E31(-7,10)
        FinitePrimeField z31 = new FinitePrimeField(31); // Caso P = 3 (mod 4)
        PrimeCurve E31 = new PrimeCurve(z31, -7, 10, 11);
        Point B31 = E31.getPoint(13);
        BigInteger n31 = E31.getOrder(B31);
        ECDHKey k31 = ECC.ECDHPhase1(E31, B31, n31);

        // Curva E97(-7,10)
        FinitePrimeField z97 = new FinitePrimeField(97); // Caso P = 1 (mod 8)
        PrimeCurve E97 = new PrimeCurve(z29, -7, 10, 6);
        Point B97 = E97.getPoint(13);
        BigInteger n97 = E97.getOrder(B97);
        ECDHKey k97 = ECC.ECDHPhase1(E97, B97, n97);

        // Curva standard P-192
        BigInteger p192 = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        FinitePrimeField z192 = new FinitePrimeField(p192);
        BigInteger b192 = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
        PrimeCurve E192 = new PrimeCurve(z192, BigInteger.valueOf(-3), b192, 256);
        Point B192 = E192.getPoint(new BigInteger("188DA80EB03090F67CBF20EB43A18800F4FF0AFD82FF1012",16));
        BigInteger n192 = new BigInteger("6277101735386680763835789423176059013767194773182842284081");
        ECDHKey k192 = ECC.ECDHPhase1(E192, B192, n192);

        // Curva standard P-521
        BigInteger p521 = new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151");
        FinitePrimeField z521 = new FinitePrimeField(p521);
        BigInteger b521 = new BigInteger("051953eb9618e1c9a1f929a21a0b68540eea2da725b99b315f3b8b489918ef109e156193951ec7e937b1652c0bd3bb1bf073573df883d2c34f1ef451fd46b503f00", 16);
        PrimeCurve E521 = new PrimeCurve(z521, BigInteger.valueOf(-3), b521, 256);
        Point B521 = E521.getPoint(new BigInteger("c6858e06b70404e9cd9e3ecb662395b4429c648139053fb521f828af606b4d3dbaa14b5e77efe75928fe1dc127a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66", 16));
        BigInteger n521 = new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397655394245057746333217197532963996371363321113864768612440380340372808892707005449");
        ECDHKey k521 = ECC.ECDHPhase1(E521, B521, n521);

        // Verifica di correttezza
        System.out.println("======= Test Campo Finito =======");
        System.out.println("- Test Campo Finito z29: " + testPrimeField(z29));
        System.out.println("- Test Campo Finito z31: " + testPrimeField(z31));
        System.out.println("- Test Campo Finito z97: " + testPrimeField(z97));
        System.out.println("- Test Campo Finito z192: " + testPrimeField(z192));
        System.out.println("- Test Campo Finito z521: " + testPrimeField(z521));

        System.out.println("======= Test Curva Prima =======");
        System.out.println("- Test Curva Prima E29: " + testPrimeCurve(E29));
        System.out.println("- Test Curva Prima E31: " + testPrimeCurve(E31));
        System.out.println("- Test Curva Prima E97: " + testPrimeCurve(E97));
        System.out.println("- Test Curva Prima E192: " + testPrimeCurve(E192));
        System.out.println("- Test Curva Prima E521: " + testPrimeCurve(E521));

        System.out.println("======= Test Koblitz =======");
        System.out.println("- Test Koblitz E29: " + testKoblitz(E29));
        System.out.println("- Test Koblitz E31: " + testKoblitz(E31));
        System.out.println("- Test Koblitz E97: " + testKoblitz(E97));
        System.out.println("- Test Koblitz E192: " + testKoblitz(E192));
        System.out.println("- Test Koblitz E521: " + testKoblitz(E521));

        System.out.println("======= Test ElGamal =======");
        System.out.println("- Test ElGamal E29: " + testElGamal(E29, B29, k29));
        System.out.println("- Test ElGamal E31: " + testElGamal(E31, B31, k31));
        System.out.println("- Test ElGamal E97: " + testElGamal(E97, B97, k97));
        System.out.println("- Test ElGamal E192: " + testElGamal(E192, B192, k192));
        System.out.println("- Test ElGamal E521: " + testElGamal(E521, B521, k521));

        System.out.println("======= Test ECDH =======");
        System.out.println("- Test ECDH E29: " + testECDH(E29, B29, n29));
        System.out.println("- Test ECDH E31: " + testECDH(E31, B31, n31));
        System.out.println("- Test ECDH E97: " + testECDH(E97, B97, n97));
        System.out.println("- Test ECDH E192: " + testECDH(E192, B192, n192));
        System.out.println("- Test ECDH E521: " + testECDH(E521, B521, n521));

        System.out.println("======= Test ECDSA =======");
        System.out.println("- Test ECDSA E29: " + testECDSA(E29, B29, n29, k29));
        System.out.println("- Test ECDSA E31: " + testECDSA(E31, B31, n31, k31));
        System.out.println("- Test ECDSA E97: " + testECDSA(E97, B97, n97, k97));
        System.out.println("- Test ECDSA E192: " + testECDSA(E192, B192, n192, k192));
        System.out.println("- Test ECDSA E521: " + testECDSA(E521, B521, n521, k521));

        // Valutazione delle prestazioni
        System.out.println("======= Benchmark Koblitz =======");
        System.out.println("- Benchmark Koblitz E29: " + benchmarkKoblitz(E29) + "op/s");
        System.out.println("- Benchmark Koblitz E31: " + benchmarkKoblitz(E31) + "op/s");
        System.out.println("- Benchmark Koblitz E97: " + benchmarkKoblitz(E97) + "op/s");
        System.out.println("- Benchmark Koblitz E192: " + benchmarkKoblitz(E192) + "op/s");
        System.out.println("- Benchmark Koblitz E521: " + benchmarkKoblitz(E521) + "op/s");

        System.out.println("======= Benchmark ElGamal =======");
        System.out.println("- Benchmark ElGamal E29: " + benchmarkElGamal(E29, B29, k29) + "op/s");
        System.out.println("- Benchmark ElGamal E31: " + benchmarkElGamal(E31, B31, k31) + "op/s");
        System.out.println("- Benchmark ElGamal E97: " + benchmarkElGamal(E97, B97, k97) + "op/s");
        System.out.println("- Benchmark ElGamal E192: " + benchmarkElGamal(E192, B192, k192) + "op/s");
        System.out.println("- Benchmark ElGamal E521: " + benchmarkElGamal(E521, B521, k521) + "op/s");

        System.out.println("======= Benchmark ECDH =======");
        System.out.println("- Benchmark ECDH E29: " + benchmarkECDH(E29, B29, n29) + "op/s");
        System.out.println("- Benchmark ECDH E31: " + benchmarkECDH(E31, B31, n31) + "op/s");
        System.out.println("- Benchmark ECDH E97: " + benchmarkECDH(E97, B97, n97) + "op/s");
        System.out.println("- Benchmark ECDH E192: " + benchmarkECDH(E192, B192, n192) + "op/s");
        System.out.println("- Benchmark ECDH E521: " + benchmarkECDH(E521, B521, n521) + "op/s");

        System.out.println("======= Benchmark ECDSA =======");
        System.out.println("- Benchmark ECDSA E29: " + benchmarkECDSA(E29, B29, n29, k29) + "op/s");
        System.out.println("- Benchmark ECDSA E31: " + benchmarkECDSA(E31, B31, n31, k31) + "op/s");
        System.out.println("- Benchmark ECDSA E97: " + benchmarkECDSA(E97, B97, n97, k97) + "op/s");
        System.out.println("- Benchmark ECDSA E192: " + benchmarkECDSA(E192, B192, n192, k192) + "op/s");
        System.out.println("- Benchmark ECDSA E521: " + benchmarkECDSA(E521, B521, n521, k521) + "op/s");

    }

    private static boolean testPrimeField(FinitePrimeField Z) {
        for (int i = 0; i < Main.NumberTest; i++) {

            // Genero due interi non nulli
            BigInteger x, y;
            do {
                x = Z.mod(ECC.randomBigInteger());
                y = Z.mod(ECC.randomBigInteger());
            }
            while (x.compareTo(BigInteger.ZERO) == 0 || y.compareTo(BigInteger.ZERO) == 0);

            // Chiamo tutte le funzioni implementate
            BigInteger inverse = Z.inverse(x);
            BigInteger opposite = Z.opposite(x);
            BigInteger sqrt = Z.sqrt(x);
            BigInteger add = Z.add(x, y);
            BigInteger sub = Z.sub(x, y);
            BigInteger mul = Z.mul(x, y);
            BigInteger div = Z.div(x, y);

            // Testo le diverse operazioni
            if (inverse != null && Z.mul(x, inverse).compareTo(BigInteger.ONE) != 0) {
                System.out.println("INVERSO[" + x + "] = [" + inverse + "]");
                return false;
            }
            if (Z.add(x, opposite).compareTo(BigInteger.ZERO) != 0) {
                System.out.println("OPPOSTO[" + x + "] = [" + opposite + "]");
                return false;
            }
            if (Z.isResidue(x) && sqrt == null) {
                System.out.println("RESIDUO " + x + " " + Z.isResidue(x));
                return false;
            }
            if (sqrt != null && Z.mul(sqrt, sqrt).compareTo(x) != 0) {
                System.out.println("RADICE[" + x + "] = [" + sqrt + "]");
                return false;
            }

            if (Z.sub(add, x).compareTo(y) != 0) {
                System.out.println("ADD");
                return false;
            }

            if (Z.sub(add, x).compareTo(y) != 0) {
                System.out.println("ADD");
                return false;
            }

            if (Z.add(sub, y).compareTo(x) != 0) {
                System.out.println("SUB");
                return false;
            }

            if (Z.div(mul, x).compareTo(y) != 0) {
                System.out.println("MUL");
                return false;
            }

            if (Z.mul(div, y).compareTo(x) != 0) {
                System.out.println("DIV");
                return false;
            }
        }
        return true;
    }

    private static boolean testPrimeCurve(PrimeCurve E) {
        for (int i = 0; i < Main.NumberTest; i++) {
            BigInteger r;
            // Genero casualmente due punti distinti P e Q
            Point P, Q;
            do {
                r = ECC.randomBigInteger();
                P = E.getPoint(r);
                r = ECC.randomBigInteger();
                Q = E.getPoint(r);
            }
            while (!P.isValid() || !Q.isValid() || P.equals(Q));

            // Chiamo tutte le funzioni implementate
            Point inverso = E.pointInverse(P);
            Point doppio = E.sum(P, P);
            Point somma = E.sum(P, Q);
            Point sommaZero = E.sum(P, inverso);
            Point sommaInfinito = E.sum(P, Point.INFINITY);
            Point sottrazione = E.sub(P, Q);

            // Testo le diverse operazioni
            if (!E.sum(P, inverso).isInfinity()) {
                System.out.println("INVERSO");
                return false;
            }

            if (!E.sub(doppio, P).equals(P)) {
                System.out.println("DOPPIO");
                return false;
            }

            if (!E.sub(somma, P).equals(Q)) {
                System.out.println("SOMMA");
                return false;
            }

            if (!sommaZero.equals(Point.INFINITY)) {
                System.out.println("SOMMAZERO");
                return false;
            }

            if (!sommaInfinito.equals(P)) {
                System.out.println("SOMMAINFINITO");
                return false;
            }

            if (!E.sum(sottrazione, Q).equals(P)) {
                System.out.println("SOTTRAZIONE");
                return false;
            }

        }
        return true;
    }

    private static boolean testECDSA(PrimeCurve E, Point B, BigInteger n, ECDHKey key) {
        BigInteger h = BigInteger.valueOf(E.getH());
        for (int i = 0; i < Main.NumberTest; i++) {
            // Genero un messaggio casuale e lo firmo
            BigInteger m = ECC.randomBigInteger().mod(n);
            BigInteger m2 = m.add(BigInteger.ONE);
           // System.out.println("I = " + i + " " + m + " " + n);
            BigInteger sign[] = ECC.ECDSASign(E, B, n, m, key);

            // Verifico il messaggio e una sua alterazione
            Boolean valid = ECC.ECDSAVerify(E, B, n, sign, m, key);
            Boolean invalid = ECC.ECDSAVerify(E, B, n, sign, m2, key);


            // Testo le due verifiche
            if (!valid) {
                System.out.println("M " + m + " " + m2 );
                System.out.println("FIRMA ERRATA ");
                return false;
            }
/*            if (invalid) {
                System.out.println("M " + m + " " + m2 + " " + ECC.ECDSAVerify(E, B, n, sign, m2, key));
                System.out.println("MESSAGGIO ERRATO");
                return false;
            }*/
        }
        return true;
    }

    private static boolean testECDH(PrimeCurve E, Point B, BigInteger n) {
        for (int i = 0; i < Main.NumberTest; i++) {

            // Prima fase, genero chiavi
            ECDHKey xKey = ECC.ECDHPhase1(E, B, n);
            ECDHKey yKey = ECC.ECDHPhase1(E, B, n);

            // Seconda fase scambio chiave pubbliche
            Point xS = ECC.ECDHPhase2(E, xKey, yKey.getPublicKey());
            Point yS = ECC.ECDHPhase2(E, yKey, xKey.getPublicKey());

            // Alla fine entrambi dovranno avere lo stesso punto S
            if (!xS.equals(yS)) {
                System.out.println("CHIAVE NON UGUALE");
                return false;
            }
        }
        return true;
    }

    private static boolean testElGamal(PrimeCurve E, Point B, ECDHKey key) {
        BigInteger h = BigInteger.valueOf(E.getH());
        BigInteger maxM = E.getP().getP().divide(h).subtract(BigInteger.ONE);
        for (int i = 0; i < Main.NumberTest; i++) {
            // Genero un intero casuale e lo codifico
            BigInteger m = ECC.randomBigInteger().mod(maxM);
            Point[] encoded = ECC.ElGamalEnc(E, m, B, key.getPublicKey());

            // Dopo lo decodifico
            BigInteger decoded = ECC.ElGamalDec(E, B, encoded, key.getPrivateKey());

            // Infine controllo se è stato correttamente decodificato
            if (m.compareTo(decoded) != 0) {
                System.out.println("MESSAGGIO NON UGUALE");
                return false;
            }
        }
        return true;
    }

    private static boolean testKoblitz(PrimeCurve E) {
        BigInteger h = BigInteger.valueOf(E.getH());
        BigInteger maxM = E.getP().getP().divide(h).subtract(BigInteger.ONE);
        for (int i = 0; i < Main.NumberTest; i++) {
            // Genero un intero casuale e lo codifico in un punto della curva
            BigInteger m = ECC.randomBigInteger().mod(maxM);
            Point encoded = ECC.KoblitzEnc(E, m);

            // Dopo lo decodifico
            BigInteger decoded = ECC.KoblitzDec(E, encoded);

            // Infine controllo se è stato correttamente decodificato
            if (m.compareTo(decoded) != 0) return false;
        }
        return true;
    }

    private static Integer benchmarkECDSA(PrimeCurve E, Point B, BigInteger n, ECDHKey key) {
        Integer count = 0;
        BigInteger h = BigInteger.valueOf(E.getH());
        BigInteger maxM = E.getP().getP().divide(h).subtract(BigInteger.ONE);
        Long now = System.currentTimeMillis();
        do {
            BigInteger m = ECC.randomBigInteger().mod(maxM);
            BigInteger sign[] = ECC.ECDSASign(E, B, n, m, key);
            Boolean valid = ECC.ECDSAVerify(E, B, n, sign, m, key);
            count++;
        } while ((System.currentTimeMillis() - now) < secondBenchmark * 1000);

        return new Integer(count / secondBenchmark);
    }

    private static Integer benchmarkECDH(PrimeCurve E, Point B, BigInteger n) {
        Integer count = 0;
        Long now = System.currentTimeMillis();
        do {
            ECDHKey xKey = ECC.ECDHPhase1(E, B, n);
            ECDHKey yKey = ECC.ECDHPhase1(E, B, n);
            Point xS = ECC.ECDHPhase2(E, xKey, yKey.getPublicKey());
            Point yS = ECC.ECDHPhase2(E, yKey, xKey.getPublicKey());
            count++;
        } while ((System.currentTimeMillis() - now) < secondBenchmark * 1000);

        return new Integer(2 * count / secondBenchmark);
    }

    private static Integer benchmarkElGamal(PrimeCurve E, Point B, ECDHKey key) {
        Integer count = 0;
        BigInteger h = BigInteger.valueOf(E.getH());
        BigInteger maxM = E.getP().getP().divide(h).subtract(BigInteger.ONE);
        Long now = System.currentTimeMillis();
        do {
            BigInteger m = ECC.randomBigInteger().mod(maxM);
            Point[] encoded = ECC.ElGamalEnc(E, m, B, key.getPublicKey());
            BigInteger decoded = ECC.ElGamalDec(E, B, encoded, key.getPrivateKey());
            count++;
        } while ((System.currentTimeMillis() - now) < secondBenchmark * 1000);

        return new Integer(count / secondBenchmark);
    }

    private static Integer benchmarkKoblitz(PrimeCurve E) {
        Integer count = 0;
        BigInteger h = BigInteger.valueOf(E.getH());
        BigInteger maxM = E.getP().getP().divide(h).subtract(BigInteger.ONE);
        Long now = System.currentTimeMillis();
        do {
            BigInteger m = ECC.randomBigInteger().mod(maxM);
            Point encoded = ECC.KoblitzEnc(E, m);
            BigInteger decoded = ECC.KoblitzDec(E, encoded);
            count++;
        } while ((System.currentTimeMillis() - now) < secondBenchmark * 1000);

        return new Integer(count / secondBenchmark);
    }

}
