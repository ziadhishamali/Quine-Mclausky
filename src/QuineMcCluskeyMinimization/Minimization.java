package QuineMcCluskeyMinimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;


/**
 *
 * @author ziadh
 *
 */
public class Minimization {

	private Scanner scan;

	/**
	 * @throws FileNotFoundException 
	 *
	 */
	public void result() throws FileNotFoundException {
		int maxSize = 0, numOfTrue = 0, numOfVar = 0;
		scan = new Scanner(System.in);
		// System.out.println("Enter number of variables:");
		// numOfVar = scan.nextInt();
		Vector<Integer> minTerms = new Vector<Integer>();
		Integer j = 0, i = 0, counter = 0, temp, select = 0;
		i = 0;
		System.out.println("Do you want to read from file or by hand?");
		System.out.println("(1)File   (2)By hand");
		System.out.print("Select:  ");
		select = scan.nextInt();

		while (select != 1 && select != 2) {
			System.out.print("Select the right choice: ");
			select = scan.nextInt();
		}
		System.out.println();
		System.out.println();

		if (select == 2) {
			System.out.println("Enter minterms values (-1 to finish):"); // Scanning Minterms

			/**
			 * Counting number of Minterms
			 */
			while (true) {
				numOfTrue = scan.nextInt();
				if (numOfTrue < -1) {
					System.out.println("Error! minterms are positive");
					continue;
				}
				if (numOfTrue != -1) {
					if (!minTerms.contains(numOfTrue)) {
						minTerms.add(numOfTrue);
					}
				} else {
					break;
				}
			}
			System.out.println("Enter Don't care values (-1 to finish):"); // Scanning Don't care values
			while (true) {
				numOfTrue = scan.nextInt();
				if (numOfTrue < -1) {
					System.out.println("Error! Don't cares are positive");
					continue;
				}
				if (numOfTrue != -1) {
					if (!minTerms.contains(numOfTrue)) {
						minTerms.add(numOfTrue);
					}
				} else {
					break;
				}
			}

		} else {
			readFile(minTerms);
		}

		numOfTrue = minTerms.size();
		minTerms.sort(null); /* Sorting the minterms for easier readability 
								And to get the minimum number of variables*/
		if (numOfTrue != 0) {
			numOfVar = minTerms.elementAt(numOfTrue - 1);
		} else if (numOfTrue == 0) {
			System.out.println("Error!! no Minterms have been entered");
			return;
		}
		i = 0;
		while (numOfVar != 0) {
			i++;
			numOfVar /= 2;
		}
		numOfVar = i;
		System.out.println("Minterms and don't care values are: " + minTerms); // Printing the minterms and don't cares after sorting
		maxSize = (int) Math.pow(2, numOfVar);
		Integer[][] bo = new Integer[minTerms.size()][numOfVar];
		for (i = 0; i < minTerms.size(); i++) {
			for (j = 0; j < numOfVar; j++) {
				bo[i][j] = 0;
			}
		}

		/**
		 * Converting Minterms into Binary
		 */
		for (i = 0; i < numOfTrue; i++) {
			j = 0;
			counter = 0;
			temp = minTerms.elementAt(i);
			while (temp != 0) {
				bo[i][j++] = temp % 2;
				temp /= 2;
			}
			while (j != 0) {
				if (bo[i][j - 1] == 1) {
					counter++;
				}
				j--;
			}
		}

		/**
		 * Calling the function that filters out the Minterms
		 */
		filteringPI(minTerms, numOfVar, maxSize, bo);
	}

	/**
	 *
	 * Filters out the Minterms
	 *
	 * @param minTerms
	 *            ....Array of minterms Values
	 * @param numOfVar
	 *            ....Number of Variables
	 * @param maxSize
	 *            ....max number that can be represented with the variables
	 * @param bo
	 *            ....Array of minterms Values in Binary
	 */
	public void filteringPI(Vector<Integer> minTerms, int numOfVar, int maxSize, Integer[][] bo) {
		Integer primeImp[] = new Integer[numOfVar];
		Set<String> pi = new HashSet<String>();
		Set<String> pi2 = new HashSet<String>();
		Set<String> pi3 = new HashSet<String>();
		int i, j, k, n, m = 0, l = 0, flag = 0, flagPI = 0, hammingDist = 0;
		char letter = 'A';
		String temp = "";

		/**
		 * Calculate the Hamming Distance between the minterms, And performs 1st step in
		 * minimization, Producing the 1st Prime implicants
		 */
		for (i = 0; i < minTerms.size(); i++) {
			for (j = 0; j < minTerms.size(); j++) {
				temp = "";
				hammingDist = 0;
				Vector<Integer> ham = new Vector<Integer>();
				for (k = 0; k < numOfVar; k++) {
					ham.add(Math.abs(bo[i][k] - bo[j][k]));
					if (ham.elementAt(k) == 1) {
						hammingDist++;
					}
				}
				if (hammingDist == 1) {
					flagPI = 1;
					for (n = 0; n < numOfVar; n++) {
						if (bo[i][n] != bo[j][n]) { // I get the position of the dash (x)
							for (l = numOfVar - 1; l >= 0; l--) {
								primeImp[l] = bo[i][l];
								if (l == n) {
									temp += '-';
								} else if (primeImp[l] == 1) {
									temp += '1';
								} else {
									temp += '0';
								}
							}
							pi.add(temp);
							break;
						}
					}
				}
			}
			if (flagPI == 0) {
				temp = "";
				for (l = numOfVar - 1; l >= 0; l--) {
					primeImp[l] = bo[i][l];
					if (primeImp[l] == 1) {
						temp += '1';
					} else {
						temp += '0';
					}
				}
				pi.add(temp);
			}
		}
		System.out.println();
		System.out.print("Step (1) Prime Implecent(s): ");
		System.out.println(pi);

		/**
		 * Perform the Following minimization Steps, Producing the final minimized Form
		 */
		flag = numOfVar;
		int fl = numOfVar;
		boolean[] check = new boolean[1000];
		while (flag != 0) {
			flag--;
			fl--;
			//pi3.addAll(pi2);
			pi2.addAll(pi);
			pi3.addAll(pi2);
			for (m = 0; m < pi2.size(); m++) {
				check[m] = false;
			}
			pi.clear();
			String[] temp2 = pi2.toArray(new String[pi2.size()]);
			for (i = 0; i < pi2.size(); i++) {
				for (j = 0; j < pi2.size(); j++) {
					temp = "";
					hammingDist = 0;
					Vector<Integer> ham = new Vector<Integer>();
					for (k = 0; k < numOfVar; k++) {
						ham.add(Math.abs(temp2[i].charAt(k) - temp2[j].charAt(k)));
						if (ham.elementAt(k) >= 1) {
							hammingDist++;
						}
						if (ham.elementAt(k) == 3 || ham.elementAt(k) == 4) {
							hammingDist = 2;
						}
					}
					if (hammingDist == 1) {
						flagPI = 1;
						check[i] = true;
						for (n = 0; n < numOfVar; n++) {
							if (temp2[i].charAt(n) != temp2[j].charAt(n)) { // I get the position of the dash (x)
								for (l = 0; l < numOfVar; l++) {
									primeImp[l] = (int) temp2[i].charAt(l) - 48;
									if (l == n) {
										temp += '-';
									} else if (primeImp[l] == 1) {
										temp += '1';
									} else if (primeImp[l] == 0) {
										temp += '0';
									} else {
										temp += '-';
									}
								}
								pi.add(temp);
								break;
							}
						}
					}
				}
				if (flagPI == 0) {
					temp = "";
					for (l = 0; l < numOfVar; l++) {
						primeImp[l] = (int) temp2[i].charAt(l);
						if (primeImp[l] == 1) {
							temp += '1';
						} else if (primeImp[l] == 0) {
							temp += '0';
						} else {
							temp += '-';
						}
					}
					pi.add(temp);
				}
			}
			pi2.addAll(pi);
			temp2 = pi2.toArray(new String[pi2.size()]);
			if (fl == 0) {
				break;
			}
			if (pi3.equals(pi2)) {
				fl = 1;
			}
			// Prints each step
			System.out.println();
			System.out.print("Step (" + (numOfVar - flag + 1) + ") ");
			System.out.println("Minimized Prime Implicant(s):");
			for (m = 0; m < pi2.size(); m++) {	
				System.out.print(temp2[m] + "     ");
				System.out.println(check[m]);
			}
			System.out.println();

		}
		pi2.addAll(pi);
		String[] temp2 = pi2.toArray(new String[pi2.size()]);
		TreeSet<String> ordered = new TreeSet<String>();
		String tempstr = new String();
		tempstr = "";
		System.out.println();
		System.out.println("By removing those Minimized we get:");
		for (m = 0; m < pi2.size(); m++) {	
			if (!check[m]) {
				System.out.print(temp2[m] + "     ");
				System.out.println(check[m]);
			}
		}

		/**
		 * Printing the Final Form of Prime Implicants
		 */
		System.out.println();
		System.out.println("Prime Implicants:");
		System.out.println("*---------------*");
		System.out.println();
		for (i = 0; i < temp2.length; i++) {
			letter = 'A';
			if (!check[i]) {
				for (j = 0; j < numOfVar; j++) {
					if (temp2[i].charAt(j) == '0') {
						tempstr += (letter + "' ");
					} else if (temp2[i].charAt(j) == '1') {
						tempstr += (letter + " ");
					}
					letter++;
				}
				ordered.add(tempstr);
				tempstr = "";
			}
		}
		temp2 = ordered.toArray(new String[ordered.size()]);
		flag = 0;
		for (i = 0; i < ordered.size(); i++) {
			if (temp2[i].isEmpty()) {
				flag++;
			}
			if (!temp2[i].isEmpty()) {
				System.out.println(temp2[i]);
			}
		}
		if (flag == ordered.size()) {
			for (i = 0; i < numOfVar; i++) {
				System.out.print("x");
			}
		}
	}

	/**
	 * 
	 * @param minTerms  ....array in which the minterms will be saved
	 * @return  ....returns the array of minterms taken from the file
	 * @throws FileNotFoundException ....exception if the file wasn't found
	 */
	public Vector<Integer> readFile(Vector<Integer> minTerms) throws FileNotFoundException {
		int numOfTrue = 0, i = 0;
		String temp1 = "";
		String[] temp = null;
		File file = new File("Minterms.txt");
		scan = new Scanner(file);
		temp1 = scan.nextLine();
		temp = temp1.split(",");
		while (true) {
			numOfTrue = Integer.parseInt(temp[i]);
			if (numOfTrue != -1) {
				if (!minTerms.contains(numOfTrue)) {
					minTerms.add(numOfTrue);
				}
			} else {
				break;
			}
			i++;
		}
		return minTerms;
	}
}
