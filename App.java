package com.ontariotechu.sofe3980U;


import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single Variable Logistic Regression
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
			
			System.out.println("Evaluating model: " + filePath);
			int m = allData.size();
			float bce = 0;
			float true_positive = 0;
			float false_positive = 0;
			float false_negative = 0;
			float true_negative = 0;
			float ground_truth_positive = 0;
			float ground_truth_negative = 0;
			for (String[] row : allData) {
				float y_true=Integer.parseInt(row[0]);
				float y_predicted = Float.parseFloat(row[1]);
				float y_predicted_class = ((y_predicted) >= 0.5) ? 1 : 0;

				bce += y_true * Math.log(y_predicted) + (1-y_true) * Math.log(1-y_predicted);

				// Calculate confusion matrix
				if (y_true == 1 && y_predicted_class == 1) {
					true_positive++;
				} else if (y_true == 0 && y_predicted_class == 1) {
					false_positive++;
				} else if (y_true == 1 && y_predicted_class == 0) {
					false_negative++;
				} else {
					true_negative++;
				}

				// Calculate ground_truth_positive, ground_truth_negative
				if (y_true == 1) {
					ground_truth_positive++;
				} else {
					ground_truth_negative++;
				}
			}
			// Calculate binary cross-entropy
			bce = -bce/m;

			// Calculate accuracy, precision, recall, and F1 score
			float accuracy = (true_positive + true_negative) / m;
			float precision = true_positive / (true_positive + false_positive);
			float recall = true_positive / (true_positive + false_negative);
			float f1 = 2 * precision * recall / (precision + recall);

			// Calculate AUC-ROC curve
			float[] fpr = new float[100];
			float[] tpr = new float[100];
			for (int i=0; i < 100; i++) {
				float threshold = (float)i / 100;
				int tp = 0;
				int fp = 0;
				
				for (String[] row : allData) {
					int y_true = Integer.parseInt(row[0]);
					int y_predicted = (Float.parseFloat(row[1]) >= threshold) ? 1 : 0;

					// Calculate true positive rate and false positive rate
					if (y_true == 1 && y_predicted == 1) {
						tp++;
					} else if (y_true == 0 && y_predicted == 1) {
						fp++;
					}
				}

				tpr[i] = tp / ground_truth_positive;
				fpr[i] = fp / ground_truth_negative;
			}
			float auc = 0;
			for (int i=1; i < 100; i++) {
				auc += (tpr[i-1] + tpr[i]) * Math.abs(fpr[i-1] - fpr[i]) / 2;
			}

			System.out.println("Binary Cross-Entropy: " + bce);

			System.out.println("Confusion Matrix:");
			System.out.println("True Positive: " + true_positive);
			System.out.println("False Positive: " + false_positive);
			System.out.println("False Negative: " + false_negative);
			System.out.println("True Negative: " + true_negative);

			System.out.println("Accuracy: " + accuracy);
			System.out.println("Precision: " + precision);
			System.out.println("Recall: " + recall);
			System.out.println("F1 Score: " + f1);

			System.out.println("AUC-ROC: " + auc);
			System.out.println();

			// pd.head
			/*
			int count=0;
			for (String[] row : allData) { 
				int y_true=Integer.parseInt(row[0]);
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
	}
}
