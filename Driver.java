import java.io.File;

public class Driver {
    public static void main(String [] args) {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));
        // double [] c1 = {6,0,0,5};
        double[] c1_coeff = {6, 5};
        int[] c1_exp = {0, 3};
        Polynomial p1 = new Polynomial(c1_coeff, c1_exp);
        // double [] c2 = {0,-2,0,0,-9};
        double[] c2_coeff = {-2, -9};
        int[] c2_exp = {1, 4};
        Polynomial p2 = new Polynomial(c2_coeff, c2_exp);
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");

        // Lab 2
        Polynomial p1p2 = p1.multiply(p2);
        // -121
        System.out.println("p1p2(1) = " + p1p2.evaluate(1));

        // file IO test:
        // -12.0x-64.0x4-45.0x7
        File p1p2_file = p1p2.saveToFile("p1p2_file");
        // should be the same as p1p2
        // Polynomial p1p2_fromfile = new Polynomial(p1p2_file);
        // System.out.println("p1p2_fromfile(1) = " + p1p2_fromfile.evaluate(1));
    }
}
