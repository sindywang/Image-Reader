import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class feedforward {
	public static void main(String[] args) throws IOException {
	/** This is the main method.
	@param args[0] This is the image
	@return none
	*/

	//reading files
	File hidden = new File("hidden-weights.txt");
	File output = new File("output-weights.txt");

	//creating 2D array of weights
	String[][] newHidden = makingArray(hidden);
	String[][] newOutput = makingArray(output);

	//reading image
	BufferedImage img = ImageIO.read(new File(args[0]));
    // get pixel data
    double[] dummy = null;
    double[] X = img.getData().getPixels(0, 0, img.getWidth(),
        img.getHeight(), dummy);

    double[] values = calculateHidden(X,newHidden);
	double[] newValues = sigmoidFunction(values);
	double[] newerValues = calculateOutput(newValues,newOutput);
	double[] pOutputs = sigmoidFunction(newerValues);

	//printing statement to user
	for (int i = 0; i < pOutputs.length; i++){
		double num = pOutputs[i];
		System.out.print(i + " -> ");
		System.out.printf("%.3f\n",num);
	}
	System.out.println("The network prediction is " + biggest(pOutputs) + ".");
}
	public static String[][] makingArray(File input) throws FileNotFoundException{
		/** This method is used to make the given weights into an 2D array.
		@param input This the is file of weights
		@return inputArray This returns the array with the weights in them
		 */

		Scanner in = new Scanner(input);
		List<String[]> lines = new ArrayList<>();

		//putting values into arraylist
		while(in.hasNextLine()) {
			String line = in.nextLine().trim();
			String[] section = line.split(" ");
			for(int i = 0; i < section.length; i++) {
				section[i] = section[i].substring(0, section[i].length());
				}
			lines.add(section);
		}

		//converts arraylist to 2D array so it'll be easier to work with later on
		String[][] inputArray = new String[lines.size()][];
		for(int i = 0; i < inputArray.length; i++) {
			inputArray[i] = lines.get(i);
		}
		in.close();
		return inputArray;
	}
	public static double[] calculateHidden(double[] img,String[][] hidden){
		/** This method is used to calculate the perceptron with the image and hidden weights.
		@param img This is the array created from image
		@param hidden This is the array with the hidden weights
		@return newValue This is the new array created from the perceptron
		 */

		double x,w,r=0,rFinal=0;
		double[] newValue = new double[300];

		for (int i = 0; i < 300; i++){
			for (int j = 0; j < 784; j++){
				x = (img[j] / 255);
				w = Double.parseDouble(hidden[i][j]);
				r += x * w;
			}
			//adding final r of each row with their bias
			rFinal = r + Double.parseDouble(hidden[i][784]);
			newValue[i] = rFinal;
			//reseting the r value
			r = 0;
		}
		return newValue;
	}
	public static double[] calculateOutput(double[] values,String[][] output){
		/** This method is used to calculate the perceptron with the new array from method "sigmoidFunction"
		and output weights.
		@param values This is the array created from method "sigmoidFunction"
		@param output This is the array with the output weights
		@return newValue This is the new array created from the perceptron
		 */

		double x,w,r=0,rFinal;
		double[] newValue = new double[10];

		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 300; j++){
				x = values[j];
				w = Double.parseDouble(output[i][j]);
				r += x * w;
			}
			//adding final r of each row with their bias
			rFinal = r + Double.parseDouble(output[i][300]);
			newValue[i] = rFinal;
			//reseting the r value
			r = 0;
		}
		return newValue;
	}
	public static double[] sigmoidFunction(double[] input){
		/** This method is used the calculate the sigmoidFunction.
		@param input This is an array from method "calculateOutput"
		@return value This returns a new array after calculations with sigmoid function
		 */

		double[] value = new double[input.length];
		double f;

		for (int i = 0; i < input.length; i++){
			f = 1/(1+(Math.pow(Math.E, -input[i])));
			//putting values into array
			value[i] = f;
		}
		return value;
	}
	public static int biggest(double[] input){
		/** This method find the biggest value
		@param input This is an array with values
		@return number This returns the number with biggest possibility
		 */
	
		//setting a very small number
		double biggest = 0.000000000000000001;
		int number=0;

		for (int i = 0; i < input.length; i++){
			if (input[i] > biggest){
				biggest = input[i];
				number = i;
			}
		}
		return number;
	}
}