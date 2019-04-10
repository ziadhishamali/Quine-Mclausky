package QuineMcCluskeyMinimization;

import java.io.FileNotFoundException;
import java.util.PriorityQueue;

import javax.swing.JFrame;

public class main {

	public static void main(String[] args) throws FileNotFoundException {
		/*Minimization function = new Minimization();
		function.result();*/
		PriorityQueue<Object> o = new PriorityQueue();
		o.add(3);
		o.add(2);
		o.add(1);
		o.add(5);
		o.add(4);
		System.out.println(o.remove());
	}
}

