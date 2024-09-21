public class Polynomial {
    double[] coeff;

    // Constructor without argument: 
    // initialises a polynomial with coeff = [0]
    public Polynomial() {
        coeff = new double[]{0};
    }

    // Constructor with argument double[] arr:
    // initialises a polynomial with coeff matching those in arr
    public Polynomial(double[] arr) {
        coeff = new double[arr.length];

        for (int i = 0; i < arr.length; i++) {
            coeff[i] = arr[i];
        }
    }

    // Method add with argument Polynomial p1:
    // Returns a polynomial with coefficients equal to the sum of p1 and the calling object
    public Polynomial add(Polynomial p1) {
        Polynomial p = new Polynomial();

        if (p1.coeff.length > this.coeff.length) {
            p = new Polynomial(p1.coeff);
            for (int i = 0; i < this.coeff.length; i++)
                p.coeff[i] += this.coeff[i];
        } else {
            p = new Polynomial(this.coeff);
            for (int i = 0; i < p1.coeff.length; i++)
                p.coeff[i] += p1.coeff[i];
        }

        return p;
    }

    // Method evaluate with argument double x0:
    // Returns the value of the calling polynomial evaluated at x0
    public double evaluate(double x0) {
        double sum = 0;

        for (int i = 0; i < this.coeff.length; i++)
            sum += this.coeff[i] * Math.pow(x0, i);

        return sum;
    }

    // Method hasRoot:
    // Returns true if x0 is a root of the calling object, false otherwise
    public boolean hasRoot(double x0) {
        return (evaluate(x0) == 0);
    }
}
