import java.math.BigInteger;

// Classe per rappresentare
// la coppia <ChiavePrivata, ChiavePublica> per l'ECDH
public class ECDHKey {

    // La chiave privata è un intero
    private BigInteger privateKey;
    // La chiave pubblica è un punto della curva
    private Point publicKey;

    public ECDHKey(BigInteger privateKey, Point pubblicKey) {
        this.privateKey = privateKey;
        this.publicKey = pubblicKey;
    }
    
    public BigInteger getPrivateKey() {
        return privateKey;
    }
    public Point getPublicKey() {
        return publicKey;
    }
}
