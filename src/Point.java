import java.math.BigInteger;

// Classe immutabile che rappresenta
// un punto in una curva ellittica generica
public class Point {

    // Coordinate (X,Y)
    private BigInteger x;
    private BigInteger y;

    // Tipo di punto (-1: Non valido, 0: Valido, +1 Punto all'infinito)
    private Integer type;

    // Definizioni statiche dei due punti invalidi e infinito
    public final static Point INVALID = new Point(0,0,-1);
    public final static Point INFINITY = new Point(0,0,1);

    // Costruttori standard per la classe
    public Point(BigInteger x, BigInteger y, Integer type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Point(Integer x, Integer y, Integer type) {
        this(BigInteger.valueOf(x), BigInteger.valueOf(y), type);
    }

    // Metodi getter specifici degli attributi
    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public Integer getType() {
        return type;
    }

    public Boolean isValid()
    {
        return this.type >= 0;
    }

    public Boolean isInfinity()
    {
        return this.type == 1;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }

    // Confronto di uguaglianza tra punti
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        // Punti di tipo diverso
        if( this.getType() != point.getType() ) return false;

        // Se entrambi i punti sono validi, confronto le coordinate
        if( this.getType() == 0 )
        {
            if( this.getX().compareTo(point.getX()) != 0 ) return false;
            if( this.getY().compareTo(point.getY()) != 0 ) return false;
        }
        return true;
    }

}
