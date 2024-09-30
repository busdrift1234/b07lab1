import java.io.File;  // Import the File class
import java.util.Scanner; // I
import java.io.FileWriter; // O
import java.io.IOException;  // for try catch block errors
import java.util.HashMap;  // map structure
import java.util.ArrayList;
import java.util.List; // List and ArrayList structures
import java.util.Collections; // Collections just to sort a list, because why not 
import java.util.Arrays; // for finding max elem


public class Polynomial {
    double[] nonzero_coeff;
    int[] exponents;

    // Constructor without argument: 
    // initialises a polynomial with nonzero_coeff = [0], exponents = [0]
    public Polynomial() {
        nonzero_coeff = new double[]{0};
        exponents = new int[]{0};
    }

    // Constructor with argument double[] arr:
    // initialises a polynomial with coeff matching those in arr
    public Polynomial(double[] nonzero_coeff, int[] exponents) {
        // same len this time?
        int len = nonzero_coeff.length;
        this.nonzero_coeff = new double[len];
        this.exponents = new int[len];

        for (int i = 0; i < len; i++) {
            this.nonzero_coeff[i] = nonzero_coeff[i];
            this.exponents[i] = exponents[i];
        }
    }

    // Constructor with argument File file:
    // initialises a polynomial equivalent to the expression (!!: no ^ used)
    public Polynomial(File file) {
        // TODO:
        try {
            Scanner input = new Scanner(file);
            String poly_text = input.nextLine(); // get string poly 
                                                 // or maybe separate the split of + and - to preserve - signs in the numbers?
                                                 // i dont even know how
                                                 // credit: https://www.baeldung.com/java-split-string-keep-delimiters

                                                 // regex to choose the delimiters to split terms, split by x in the process later
            String regex_terms = "((?=[+-]))";
            String[] terms = poly_text.split(regex_terms);

            int terms_count = terms.length + 10;
            nonzero_coeff = new double[terms_count];
            exponents = new int[terms_count];
            // for (String term: terms) {
            // need i anyway, and idk if judge accepts foreach loops, dont risk it
            for (int i = 0; i < terms_count; i++) {
                // For deg 0, 1
                if (terms[i].indexOf("x") == -1) {
                    // deg 0
                    nonzero_coeff[i] = Double.parseDouble(terms[i]);
                    exponents[i] = 0;
                } else if (terms[i].indexOf("x") == terms[i].length() - 1) {
                    // deg 1
                    // nonzero_coeff[i] = Double.parseDouble(terms[i].substring(1, terms[i].length() - 1));
                    String[] coef_pow = terms[i].split("x");
                    nonzero_coeff[i] = Double.parseDouble(coef_pow[0]);
                    exponents[i] = 1;
                }
                // For deg >= 2
                String[] coef_pow = terms[i].split("x");
                nonzero_coeff[i] = Double.parseDouble(coef_pow[0]);
                exponents[i] = Integer.parseInt(coef_pow[1]);
            }
        } catch (IOException e) {
            //TODO: handle exception
            System.out.println("Could not read from file");
        }

    }

    // Method add with argument Polynomial p1:
    // Returns a polynomial with coefficients equal to the sum of p1 and the calling object
    public Polynomial add(Polynomial p1) {
        // init p as common 
        // best way to count nonzero exps?
        // try sum of len first
        
        // cant take max of lens as array length, so make this first, and exclude zero terms
        // double[] temp_coeff = new double[p1.nonzero_coeff.length + this.nonzero_coeff.length];
        // nvm forgor if even miss a bunch, then will overflow easily
        // correct: find max elem in each array 
        // (shld be allowed to assume = last elem, but better not)

        double[] temp_coeff = new double[Arrays.stream(p1.exponents).max().getAsInt() + Arrays.stream(this.exponents).max().getAsInt()];
        for (int i = 0; i < p1.exponents.length; i++) {
            temp_coeff[p1.exponents[i]] = p1.nonzero_coeff[i];
        }

        for (int i = 0; i < this.exponents.length; i++) {
            temp_coeff[this.exponents[i]] += this.nonzero_coeff[i];
        }

        // conversion
        int nonzerocoeff_count = 0;
        for (int i = 0; i < temp_coeff.length; i++) {
            if (temp_coeff[i] != 0) {
                nonzerocoeff_count += 1;
            }
        }
        double[] nonzero_arr = new double[nonzerocoeff_count];
        int[] exponent_arr = new int[nonzerocoeff_count];

        int curr = 0;
        for (int i = 0; i < temp_coeff.length; i++) {
            if (temp_coeff[i] != 0) {
                exponent_arr[curr] = i; // power = index in temp
                nonzero_arr[curr] = temp_coeff[i];
                curr++;
            }
            if (curr >= nonzerocoeff_count) {
                // following are all 0 coeff, [curr] is out of bounds
                break;
            }
        }

        Polynomial p = new Polynomial(nonzero_arr, exponent_arr);
        return p;
    }

    // Method evaluate with argument double x0:
    // Returns the value of the calling polynomial evaluated at x0
    public double evaluate(double x0) {
        double sum = 0;

        for (int i = 0; i < nonzero_coeff.length; i++)
            sum += nonzero_coeff[i] * Math.pow(x0, exponents[i]);

        return sum;
    }

    // Method hasRoot:
    // Returns true if x0 is a root of the calling object, false otherwise
    public boolean hasRoot(double x0) {
        return (evaluate(x0) == 0);
    }

    // Method multiply:
    // Returns the result of multiplying the calling polynomial with the argument
    public Polynomial multiply(Polynomial p1) {
        // !!: no redundant exp => no init as sum of len
        // hint: hashmap, with exp as key and coeff as value

        // group by exponents => key is exponents
        HashMap<Integer, Double> group_by_exponents = new HashMap<Integer, Double>();
        // adding:
        // group_by_exponents.put
        for (int i = 0; i < this.nonzero_coeff.length; i++) {
            for (int j = 0; j < p1.nonzero_coeff.length; j++) {
                int key = this.exponents[i] + p1.exponents[j];
                // put automatically handles?
                // https://stackoverflow.com/a/7998525
                // unsure, so i'll do key existence check first
                if (group_by_exponents.containsKey(key)) {
                    group_by_exponents.put(key, group_by_exponents.get(key) + this.nonzero_coeff[i] * p1.nonzero_coeff[j]);
                } else {
                    group_by_exponents.put(key, this.nonzero_coeff[i] * p1.nonzero_coeff[j]);
                }
            }

        }

        // creating polynomial from hashmap data
        // https://www.google.com/search?q=java+get+all+keys+from+a+hasmap&sourceid=chrome&ie=UTF-8
        // keyset(), values
        //
        // Set final_exponents = group_by_exponents.keySet();
        // Collection final_nonzero_coeff = group_by_exponents.values();
        //
        // https://stackoverflow.com/questions/10462819/get-keys-from-hashmap-in-java


        // for (int key: group_by_exponents.keySet()) {
        //
        // }
        // size of final polynomial
        // assign empty polynomial

        // sort keys first?
        List<Integer> sortedExp = new ArrayList<>(group_by_exponents.keySet());
        Collections.sort(sortedExp);
        // this is only hashset, not sure about sets in general
        // https://stackoverflow.com/questions/22391350/how-to-sort-a-hashset
        // or maybe can make the value inside first

        int final_size = sortedExp.size();
        double[] nonzero_arr = new double[final_size];
        // TODO: convert list to arr
        Integer[] exponents_arr_wrapper = sortedExp.toArray(new Integer[0]);
        int[] exponents_arr = new int[exponents_arr_wrapper.length];
        for (int i = 0; i < exponents_arr_wrapper.length; i++) {
            exponents_arr[i] = exponents_arr_wrapper[i];
        }
        for (int i = 0; i < final_size; i++) {
            nonzero_arr[i] = group_by_exponents.get(exponents_arr[i]);
        }
        // finally
        Polynomial p = new Polynomial(nonzero_arr, exponents_arr);
        // // looks unsafe, but prob works
        // int i = 0;

        // prob not a good idea to mix for each loop with i 
        // but need some other ways to iterate over the set 
        // is it sorted
        // seems yes?
        // no?
        // https://stackoverflow.com/questions/9047090/how-to-sort-hashmap-keys
        // prev source states that keySet() returns sorted elements for some classes, but not hashmap
        // for (int key: group_by_exponents.keySet()) {
        //     p.exponents[i] = key;
        //     p.nonzero_coeff[i] = group_by_exponents.get(key);
        //     i++;
        // }

        // test: no redundant term
        // correctly returns 3 after multiplying p1, p2
        // System.out.println("length of p = " + p.exponents.length);
        return p;
    }

    public File saveToFile(String file) {
        File output = new File(file.concat(".txt"));
        try {
            // do sth
            // FileWriter output = new FileWriter(file.concat(".txt"));
            // does this replace File createNewFile()?
            FileWriter w_output = new FileWriter(output);
            // main writing 
            for (int i = 0; i < this.exponents.length; i++) {
                // if exp >= 1 then write "x"
                // if exp >= 2 then write ^ + this.exponents
                // if i >= 1 and coef > 0 then write "+"
                if (this.nonzero_coeff[i] != 0) {
                    if (i >= 1 && this.nonzero_coeff[i] > 0) {
                        w_output.write("+");
                    }
                    w_output.write(String.valueOf(this.nonzero_coeff[i])); // some tochar thing?
                    if (this.exponents[i] >= 1) {
                        w_output.write("x");
                    }
                    if (this.exponents[i] >= 2) {
                        // output.write("^".concat(String.valueOf(this.exponents[i])));
                        w_output.write(String.valueOf(this.exponents[i]));
                    }
                }
            }
            w_output.close();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Could not save to file");
            // scope problem, also if fail then output never existed anyway
            // output.close();
        }
        return output;
    } 
}
