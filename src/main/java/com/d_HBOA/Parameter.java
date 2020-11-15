package com.d_HBOA;

import com.z_PEA.Problem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Parameter{
	
	// NOTE: optionNames must coincide exactly with the option names in the 'Parameters.txt' file.
	private static final String optionNames[] =     
		{"NS", "selectionMethod", "tourSize", "tau", "bayesianMetric", 
		 "maxVertexDegree", "replacementType", "offspringSize", "windowSize", 
		};
	
	// Selector parameters
	private static float pNS;			// Size of selection set as a proportion of N. Default = 1 (The same value as N)
	private static int NS;				// Size of the selection set. Depends on the chosen selection method.
	private static int selectionMethod;	// Selection method. Default = 1 (Tournament selection)
	private static int tourSize;		// Size of tournament. Use only with Tournament Selection (selection = 1 or 2)
	private static float tau;			// Proportion of truncated population. Use only with Truncation (selectionMethod = 3)
	
	// Bayesian network parameters
	private static int bayesianMetric;	// Type of score metric. Default = 1 (Bayesian-Dirichelet Metric)
	private static float pOffspringSize;// Size of the offspring set, generated by the bayesian network. Default = 1 (The same value as N)
	private static int maxVertexDegree; // Maximum number of parents per vertex. Default = .6 (proportion of stringSize)
	
	// Replacement parameters
	private static int replacementType;	// Replacement method. Default = 1 (Restricted replacement)
	private static int windowSize;		// Size of replacement tournament. Use only with RestrictedReplacement (replacement = 2)
	
	// Get Selector parameters
	public static float getPNS(){return pNS;}
	public static int getSelectionMethod(){return selectionMethod;}
	public static int getTourSize(){return tourSize;}
	public static float getTau(){return tau;}
	// Get Bayesian network parameters
	public static int getBayesianMetric(){return bayesianMetric;}
	public static float getPOffspringSize(){return pOffspringSize;}
	public static int getMaxVertexDegree(){return maxVertexDegree;}
	// Get Replacement parameters
	public static int getReplacementType(){return replacementType;}
	public static int getWindowSize(){return windowSize;}
	
	
	// NOTE: Execute this initialization PRIOR to any other.
	public static void initializeParameters(String parameterFile){		
		try{
			// Open the file to be read
			FileInputStream fstream = new FileInputStream(parameterFile);
			// Create an object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			int nLine = 0; // Line number
			String line;
			while((line = buff.readLine())!= null){
				nLine++; 						// Increment line number
				if(line.length() > 0){		   	// Ignore empty lines
					if(line.charAt(0) != '#'){ 	// Ignore comments
						Scanner scanner = new Scanner(line);
						scanner.useDelimiter("=");
						// Get option name and value. If not valid, exit program!
						String optionName = scanner.next().trim();
						validateOptionName(line, optionName, nLine);
						String optionValue = scanner.next().trim();   
						validateOptionValue(optionName, optionValue, nLine); 
					}
				}
			}
			in.close();
		}  // Catch open file  error.
		catch(Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}	
	
	private static void validateOptionName(String line, String option, int nLine){
		if(option.length() >= line.length())
			exitError("Line " + nLine + " --> Missing equal sign '='");
		if(!validateName(option))
			exitError("Line " + nLine + " --> INVALID OPTION NAME '" + option + "'");
	}
	
	private static boolean validateName(String name){
		for(int i = 0; i < optionNames.length; i++)
			if(name.equals((String)optionNames[i]))
				return true;
		return false;
	}
	
	// Usage of 'switch' with 'String' only legal for Java SE 7 or later!
	private static void validateOptionValue(String optionName,String optionValue, int nLine)
	throws NumberFormatException{
		// SELECTION METHOD
		if(optionName.equals("NS")){
			pNS = Float.parseFloat(optionValue);
			if(pNS <= 0)
				exitError("Line " + nLine + " --> Selection Set size must be a POSITIVE proportion.");
			return; // Option validated!!
		}
		if(optionName.equals("selectionMethod")){
			selectionMethod = Integer.parseInt(optionValue);
			if(selectionMethod != 1 && selectionMethod != 2 && selectionMethod != 3)
				exitError("Line " + nLine + " --> INVALID selection method.");
			return; // Option validated!!
		}
		if(optionName.equals("tourSize")){
			tourSize = Integer.parseInt(optionValue);
			if(tourSize < 2)
				exitError("Line " + nLine + " --> Tournament size must be GREATER than '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("tau")){
			tau = Float.parseFloat(optionValue);
			if(tau <= 0 || tau > 1)
				exitError("Line " + nLine + " --> Truncated proportion must be a number BETWEEN '0' and '1'.");
			return; // Option validated!!
		}
		// BAYESIAN NETWORK
		if(optionName.equals("bayesianMetric")){
			bayesianMetric = Integer.parseInt(optionValue);
			if(bayesianMetric != 1 && bayesianMetric != 2)
				exitError("Line " + nLine + " --> INVALID bayesian metric.");
			return; // Option validated!!
		}
		if(optionName.equals("maxVertexDegree")){
			maxVertexDegree = Integer.parseInt(optionValue);
			if(maxVertexDegree <= 0)
				exitError("Line " + nLine + " --> Maximum vertex degree must be a positive integer.");
			return; // Option validated!!
		}
		// REPLACEMENT METHOD
		if(optionName.equals("replacementType")){
			replacementType = Integer.parseInt(optionValue);
			if(replacementType != 1 && replacementType != 2 && replacementType != 3)
				exitError("Line " + nLine + " --> INVALID replacement method.");
			return; // Option validated!!
		}
		if(optionName.equals("windowSize")){
			double pWindow = Float.parseFloat(optionValue);
			if(pWindow <= 0)
				exitError("Line " + nLine + " --> Window size must be a POSITIVE proportion.");
			windowSize = (int)(pWindow*Problem.n); // Define 'windowSize' as a proportion of the string size.
			return; // Option validated!!
		}
		if(optionName.equals("offspringSize")){
			pOffspringSize = Float.parseFloat(optionValue);
			if(pOffspringSize  <= 0)
				exitError("Line " + nLine + " --> Offspring set size must be a POSITIVE proportion.");
			return; // Option validated!!
		}
		if(true)
			exitError("Line" + nLine +
					  " --> If you are reading this message something is FUNDAMENTALLY WRONG with 'validateOptionValue(String, String, int)'.\n" + 
					  "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					  "Sorry for the inconvenience!");
	}// End of validateOptionValue(...)
	
	
	public static Selection initializeSelection(int N){
		switch(selectionMethod){
			case 1: NS = (int)(pNS*N);
					return new TourWithReplacement(NS, tourSize);
			case 2: NS = (int)(pNS*N);
					return new TourWithoutReplacement(NS, tourSize);
			case 3: NS = (int)(tau*N);
					return new Truncation(NS);
			default: exitError("If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'selectionMethod' value.\n" + 
							   "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
							   "Sorry for the inconvenience!");
					 return new Truncation((int)(tau*N));	//  NOTE: This line is never executed!
		}
	}
	
	public static BayesianNetwork initializeBayesianNetwork(int N){
		switch(bayesianMetric){
			case 1: return new BayesianNetwork(new BDMetric(NS), (int)(pOffspringSize*N), maxVertexDegree); 
			case 2: return new BayesianNetwork(new BICMetric(), (int)(pOffspringSize*N), maxVertexDegree);	//NOTE: THE BIC Metric is not correctly implemented for hBOAMartin!!
			default: exitError("If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'bayesianMetric' value.\n" + 
					   "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					   "Sorry for the inconvenience!");
			 return new BayesianNetwork(new BDMetric(NS), (int)(pOffspringSize*N), maxVertexDegree);		//  NOTE: This line is never executed!
		}
	}
	
	public static IReplacement initializeReplacement(){
		switch(replacementType){
			case 1: return new RestrictedReplacement(windowSize);
			case 2: return new WorstReplacement();
			case 3: return new FullReplacement();
			default: exitError(" If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'replacementType' value.\n" + 
					   "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					   "Sorry for the inconvenience!");
					 return new WorstReplacement();	//  NOTE: This line is never executed!
		}
	}
	
	public static String writeParameters(String indent){		
		String str = "    Selection Method:";
		if(selectionMethod == 1)
			str += "\n" + indent + "       Selection = Tournament Selection with replacement" +
				   "\n" + indent + "             pNS = " + pNS +
				   "\n" + indent + "        tourSize = " + tourSize;
		if(selectionMethod == 2)
			str += "\n" + indent + "       Selection = Tournament Selection without replacement" +
				   "\n" + indent + "             pNS = " + pNS +
				   "\n" + indent + "        tourSize = " + tourSize;
		if(selectionMethod == 3)
			str += "\n" + indent + "       Selection = Truncation" +
				   "\n" + indent + "             tau = " + tau;
		str +=  "\n\n" + "  Replacement Method:" +
				"\n" + indent + "Replacement Type = " + replacementType +
				"\n" + indent + "  pOffspringSize = " + pOffspringSize;
		if(replacementType == 1)
			str += "\n" + indent + "   windowSize = " + windowSize;
		return str;
	}
	
	// Input error found!! Exit program!
	public static void exitError(String message){
		System.err.println(new Error(message));
		System.exit(1);
	}
	
}// End of class Parameter





