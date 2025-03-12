package com.ontariotechu.sofe3980U;

import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Multi-Class Classification
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		final int NUM_CLASSES = 5;

		String filePath="model.csv";
		FileReader filereader;
		List<String[]> allData;
		try{
			filereader = new FileReader(filePath); 
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			allData = csvReader.readAll();
		}
		catch(Exception e){
			System.out.println( "Error reading the CSV file" );
			return;
		}
		
		// Calculate evaluation metrics
		int m = allData.size();
		double cross_entropy = 0;
		int[][] confusionMatrix = new int[NUM_CLASSES][NUM_CLASSES];
		for (String[] row : allData) { 
			int y_true = Integer.parseInt(row[0]);

			// Calculate cross entropy
			float y_predicted = Float.parseFloat(row[y_true]);
			cross_entropy += Math.log(y_predicted);
			
			// Calculate confusion matrix
			float[] y_predictions = new float[NUM_CLASSES];
			for (int class_num = 1; class_num <= NUM_CLASSES; class_num++) {
				y_predictions[class_num - 1] = Float.parseFloat(row[class_num]);
			}
			int y_predicted_class = argmax(y_predictions);
			confusionMatrix[y_predicted_class][y_true - 1]++;
		}
		cross_entropy = -cross_entropy/m;
		
		System.out.println("Cross Entropy Loss: " + cross_entropy);
		
		System.out.println("Confusion Matrix:");
		System.out.print("\t");
		for (int i = 0; i < NUM_CLASSES; i++) {
			System.out.print("y=" + (i+1) + " \t");
		}
		System.out.println();

		for (int i = 0; i < NUM_CLASSES; i++) {
			System.out.print("y^=" + (i+1) + " \t");
			for (int j = 0; j < NUM_CLASSES; j++) {
				System.out.print(confusionMatrix[i][j] + " \t");
			}
			System.out.println();
		}


		// pd.head
		/* 
		int count=0;
		float[] y_predicted=new float[5];
		for (String[] row : allData) { 
			int y_true=Integer.parseInt(row[0]);
			System.out.print(y_true);
			for(int i=0;i<5;i++){
				y_predicted[i]=Float.parseFloat(row[i+1]);
				System.out.print("  \t  "+y_predicted[i]); 
			}
			System.out.println(); 
			count++;
			if (count==10){
				break;
			}
		}
		*/
	}

	public static int argmax(float[] array) {
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
}
