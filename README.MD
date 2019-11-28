Type:

	sbt run 

to run application with default settings - in and out folders are in the main project folder, cutoff is set to 65 - the amount that filters correctly 100% of provided pictures

to set different values from command line, use for example:

	sbt -Dinput=/<your-path-here>/in/ -Doutput=/<your-path-here>/out/ -Dcutoff=your_value run

or
	
	sbt -Dcutoff=your_value run

no try-catch, input/output verification included regarding task assumptions
