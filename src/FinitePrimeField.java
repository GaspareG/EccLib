import java.math.BigInteger;

/**
 * Classe per la rappresentazione di un campo finito dato un primo p
 * Vengono implementate le funzioni di:
 * inverso moltiplicativo, opposto, radice quadrata, controllo di residuo quadratico,
 * somma, sottrazione, moltiplicazione, divisione e potenza.
 * In particolare per l'inverso moltiplicativo si utilizza la funzione disponibile dei BigInteger
 * mentre per il calcolo della radice quadrata si utilizzano il criterio di Eulero
 * e l'algoritmo di Tonelli-Shanks
 */
public class FinitePrimeField {

    private BigInteger p;
    private Integer sqrtAlg = 0;

    public FinitePrimeField(BigInteger p) {
        this.p = p;
        // calcolo p (mod 8) per discriminare i 3 gli algoritmi da usare per la radice
        BigInteger remained = this.p.mod(BigInteger.valueOf(8));
        switch (remained.intValue()) {
            case 3:
            case 7:
                this.sqrtAlg = 1;
                break;
            case 5:
                this.sqrtAlg = 2;
                break;
            default:
                this.sqrtAlg = 0;
                break;
        }
    }

    public FinitePrimeField(int p) {
        this(BigInteger.valueOf(p));
    }

    // Calcolo di a^-1 mod p se esiste
    public BigInteger inverse(BigInteger a) {
        a = a.mod(this.p);
        BigInteger ret = null;
        try {
            ret = a.modInverse(this.p);
        } catch (ArithmeticException e) {
            ret = null;
        }
        return ret;
    }

    public BigInteger inverse(Integer a) {
        return this.inverse(BigInteger.valueOf(a));
    }

    // Calcolo di di -x mod p
    public BigInteger opposite(BigInteger x) {
        x = x.mod(this.p);
        return this.p.subtract(x).mod(this.p);
    }

    public BigInteger opposite(Integer x) {
        return this.opposite(BigInteger.valueOf(x));
    }

    // Calcolo di sqrt(a) mod p
    public BigInteger sqrt(BigInteger a) {
        BigInteger ret = null;
        a = a.mod(this.p);
        // Se a è un residuo quadratico modulo p
        if (this.isResidue(a)) {
            // Caso p = 3 (mod 4)
            if (this.sqrtAlg == 1) {
                // sqrt(a) = a^(p+1)/4 mod p
                BigInteger exp = this.p.add(BigInteger.ONE).divide(BigInteger.valueOf(4));
                ret = a.modPow(exp, this.p);
            }
            // Caso p = 5 (mod 8)
            else if (this.sqrtAlg == 2) {
                // In questo caso devo discriminare tra i due casi del valore di a^(p-1)/4 mod p
                BigInteger discriminante = this.p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(4));
                discriminante = a.modPow(discriminante, this.p);
                if (discriminante.compareTo(BigInteger.ONE) == 0) {
                    // Se = 1 mod p
                    // Allora sqrt(a) = a^(p+3)/8 mod p
                    BigInteger exp = this.p.add(BigInteger.valueOf(3)).divide(BigInteger.valueOf(8));
                    ret = a.modPow(exp, this.p);
                } else {
                    // Se = -1 mod p
                    // Allora sqrt(a) = 2a * (4a)^((p-5)/8) mod p
                    BigInteger exp = this.p.subtract(BigInteger.valueOf(5)).divide(BigInteger.valueOf(8));
                    BigInteger a2 = a.multiply(BigInteger.valueOf(2));
                    BigInteger a4 = a.multiply(BigInteger.valueOf(4));
                    ret = a4.modPow(exp, this.p).multiply(a2);
                }
            }
            // Caso p = 1 (mod 8)
            else {
                // Applico l'algoritmo di Tonelli-Shanks
                ret = this.tonelliShanks(a);
            }
            if (ret != null)
                ret = ret.mod(this.p);
        }
        return ret;
    }

    public BigInteger sqrt(Integer a) {
        return this.sqrt(BigInteger.valueOf(a));
    }

    // Implementazione dell'algoritmo di tonelli-shanks
    private BigInteger tonelliShanks(BigInteger n) {
        BigInteger s = BigInteger.ZERO;
        BigInteger q = this.p.subtract(BigInteger.ONE);
        n = n.mod(this.p);
        // trovo Q e S tali che (p-1) = 2^S * Q
        while (!q.testBit(0)) {
            q = q.shiftRight(1);
            s = s.add(BigInteger.ONE);
        }

        // Caso particolare in cui s = 1
        if (s.compareTo(BigInteger.ONE) == 0) {
            BigInteger exp = this.p.add(BigInteger.ONE).divide(BigInteger.valueOf(4));
            BigInteger r = n.modPow(exp, this.p);
            if (r.pow(2).mod(this.p).compareTo(n) == 0) return r;
            else return null;
        }

        // Cerco il primo intero z che non sia un residuo quadratico
        BigInteger z = BigInteger.ONE;
        do {
            z = z.add(BigInteger.ONE);
        } while (this.isResidue(z));

        // c = z^q mod p
        BigInteger c = z.modPow(q, this.p);
        // r = n^( (q+1)/2 ) mod p
        BigInteger expR = q.add(BigInteger.ONE).divide(BigInteger.valueOf(2));
        BigInteger r = n.modPow(expR, this.p);
        // t = n^q mod p
        BigInteger t = n.modPow(q, this.p);
        // m = s
        BigInteger m = s;

        while (t.compareTo(BigInteger.ONE) != 0) {
            BigInteger tt = t;
            BigInteger i = BigInteger.ZERO;
            while (tt.compareTo(BigInteger.ONE) != 0) {
                tt = tt.multiply(tt).mod(this.p);
                i = i.add(BigInteger.ONE);
                if (i.compareTo(m) == 0) return null;
            }
            // b = c^( 2^(M-i-1) ) mod p
            BigInteger exp2 = m.subtract(i).subtract(BigInteger.ONE);
            BigInteger expB = BigInteger.valueOf(2).modPow(exp2, this.p.subtract(BigInteger.ONE));
            BigInteger b = c.modPow(expB, this.p);
            // bb = b^2 mod p
            BigInteger bb = b.pow(2).mod(this.p);
            // r = r*b mod p
            r = r.multiply(b).mod(this.p);
            // t = t*bb mod p
            t = t.multiply(bb).mod(this.p);
            c = bb;
            m = i;
        }
        if (r.pow(2).mod(this.p).compareTo(n) == 0) return r;
        return null;
    }

    // Controllo se a è un residuo quadratico modulo p
    public Boolean isResidue(BigInteger a) {
        a = a.mod(this.p);
        // a è residuo quadratico modulo p se a^(p-1)/2 = 1 mod p

        BigInteger exp = this.p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
        return a.modPow(exp, this.p).compareTo(BigInteger.ONE) == 0;
    }

    // Calcolo di x + y mod p
    public BigInteger add(BigInteger x, BigInteger y) {
        x = x.mod(this.p);
        y = y.mod(this.p);
        return x.add(y).mod(p);
    }

    public BigInteger add(Integer x, Integer y) {
        return this.add(BigInteger.valueOf(x), BigInteger.valueOf(y));
    }

    // Calcolo di x - y mod p
    public BigInteger sub(BigInteger x, BigInteger y) {
        x = x.mod(this.p);
        y = y.mod(this.p);
        return x.subtract(y).mod(this.p);
    }

    public BigInteger sub(Integer x, Integer y) {
        return this.sub(BigInteger.valueOf(x), BigInteger.valueOf(y));
    }

    // Calcolo di x * y mod p
    public BigInteger mul(BigInteger x, BigInteger y) {
        x = x.mod(this.p);
        y = y.mod(this.p);
        return x.multiply(y).mod(p);
    }

    public BigInteger mul(Integer x, Integer y) {
        return this.mul(BigInteger.valueOf(x), BigInteger.valueOf(y));
    }

    // Calcolo di x / y mod p
    public BigInteger div(BigInteger x, BigInteger y) {
        x = x.mod(this.p);
        y = y.mod(this.p);
        return this.mul(x, this.inverse(y));
    }

    public BigInteger div(Integer x, Integer y) {
        return this.div(BigInteger.valueOf(x), BigInteger.valueOf(y));
    }

    // Calcolo di x ^ y mod p
    public BigInteger pow(BigInteger x, BigInteger y) {
        x = x.mod(this.p);
        return x.modPow(y, this.p);
    }

    public BigInteger pow(Integer x, Integer y) {
        return this.pow(BigInteger.valueOf(x), BigInteger.valueOf(y));
    }

    // Calcolo di x mod p
    public BigInteger mod(BigInteger x) {
        return x.mod(this.p);
    }

    // Restituisce il primo del campo
    public BigInteger getP() {
        return this.p;
    }

}
