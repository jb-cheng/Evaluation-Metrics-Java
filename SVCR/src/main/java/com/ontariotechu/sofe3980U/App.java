package com.ontariotechu.sofe3980U;

import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		String[] filePaths={"model_1.csv", "model_2.csv", "model_3.csv"};
		for (String filePath : filePaths) {
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
			
			// Calculate the mean squared error, mean absolute error, and mean absolute percentage error
			System.out.println("Evaluating model: " + filePath);
			int m = allData.size();
			double mse=0;
			double mae=0;
			double mape=0;
			final double EPSILON = 0.0001f;	// hyperparameter to avoid division by zero
			for (String[] row : allData) {
				double y_true = Float.parseFloat(row[0]);
				double y_predicted = Float.parseFloat(row[1]);

				mse += Math.pow(y_true-y_predicted, 2);
				mae += Math.abs(y_true-y_predicted);
				mape += Math.abs((y_true-y_predicted)/Math.abs(y_true) + EPSILON);
			}
			mse = mse/m;
			mae = mae/m;
			mape = mape/m;

			System.out.println("Mean Squared Error: " + mse);
			System.out.println("Mean Absolute Error: " + mae);
			System.out.println("Mean Absolute Percentage Error: " + mape);
			System.out.println();

			// pd.head
			/*
			System.out.println("ground truth \t predicted");
			int count=0;
			for (String[] row : allData) { 
				float y_true=Float.parseFloat(row[0]);
				float y_predicted=Float.parseFloat(row[1]);
				System.out.print(y_true + "  \t  "+y_predicted); 
				System.out.println(); 
				count++;
				if (count==10){
					break;
				}
			}
			*/ 
		}

		System.out.println("Model 2 is the 'best' model as all error metrics are the lowest.");
		System.out.println("However, since these are validation results, the results must be \ncompared to the test set to ensure the model is not overfitting (i.e. high variance).");
    }
}
