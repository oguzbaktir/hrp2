/**
 * Created by Oguz on 21.11.2014.
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static long smallNum = 3;
    static long bigNum =5;

    long totalNumberOfDigitsOfGeneratedNumber;

    public Solution(){
    }

    public String makeString(long howManyTimes, long integerToTakeIn){
        String out="";
        if ((howManyTimes > 0) && (integerToTakeIn != 0) && (integerToTakeIn>0) && (integerToTakeIn <= 9))
        {
            for(long i=0;i<howManyTimes;i++)
            {
                out = out + integerToTakeIn;
            }
        }
        return out;
    }

    public String ruleEngine(long numOfDigits){
        //run the rule for the given number of digits and check out if you can find a solution to the problem.
        if (numOfDigits==0)
            return "-1";
        else if (numOfDigits< smallNum)
            return "-1";
        if ((numOfDigits> smallNum)&& (numOfDigits < bigNum) && ((numOfDigits%smallNum)!= 0))
            return "-1";
        if ((numOfDigits> smallNum)&& (numOfDigits < bigNum) && ((numOfDigits%smallNum)== 0))
            return makeString(numOfDigits, bigNum);
        if ((numOfDigits%smallNum)==0)
            return makeString(numOfDigits, bigNum);
        if (((numOfDigits%bigNum)==0)&&((numOfDigits%(smallNum*bigNum))!= 0))
            return makeString(numOfDigits, smallNum);
        if (((numOfDigits%bigNum)==0)&&((numOfDigits%(smallNum*bigNum))== 0))
            return makeString(numOfDigits, bigNum);
        if ((numOfDigits%(smallNum*bigNum))== 0)
            return makeString(numOfDigits, bigNum);
        else if (numOfDigits== (smallNum+bigNum))
            return makeString(smallNum, bigNum)+ makeString(bigNum, smallNum);
        else if ((numOfDigits%(smallNum+bigNum))==0)
            return makeString((numOfDigits/(smallNum+bigNum)), bigNum)+ makeString((numOfDigits/(smallNum+bigNum)), smallNum);
        else
            return solveEquation(numOfDigits);
    }

    public long checkEquation(long coefficient1, long variable1, String operator, long coefficient2, long variable2,  long constant_result) {
        if (operator.equals("+")) {
            return (coefficient1 * variable1) + (coefficient2 * variable2);

        } else if (operator.equals("-")) {
            return (coefficient1 * variable1) - (coefficient2 * variable2);

        } else if (operator.equals("*")) {
            return (coefficient1 * variable1) * (coefficient2 * variable2);

        } else if (operator.equals("/")) {
            return (coefficient1 * variable1) / (coefficient2 * variable2);
        } else {
            return (coefficient1 * variable1) + (coefficient2 * variable2);

        }
    }

    public String solveEquation(long numOfDigits){
        long numberOfDigitsMultiplierForBigNumber=0; //mB
        long numberOfDigitsMultiplierForSmallNumber=0; //mS
        long numberOfDigitsOccupiedByBigNumber=0; //nB
        long numberOfDigitsOccupiedBySmallNumber=0; //nS
        // when we find a solution to the problem; itshould be that = nB+ nS = (smallNum*mB)+(bigNum*mS)=    totalNumberOfDigitsOfGeneratedNumber
        // Start with numberOfDigitsMultiplierForSmallNumber as 0, and incrementally increase that. We want as  few smallNum digits as possible to make the biggest posible number at the end.
        numberOfDigitsMultiplierForBigNumber = 0;
        numberOfDigitsMultiplierForSmallNumber = 0;
        long maxNumberOfDigitsMultiplierForSmallNumber =(numOfDigits/smallNum);
        long maxNumberOfDigitsMultiplierForBigNumber =(numOfDigits/bigNum);
        long equationResult = 0;
        long j = 0, k = 0;
        while(equationResult<numOfDigits)
        {
            for (j= maxNumberOfDigitsMultiplierForSmallNumber;(equationResult<numOfDigits)&&(j>=0);)
            {
                for (k = 0;(equationResult<numOfDigits)&&(k<=maxNumberOfDigitsMultiplierForBigNumber);)
                {
                    //solve
                    //increase k as much as possible,
                    equationResult = checkEquation(smallNum, j, "+", bigNum, k, numOfDigits);
                    if (equationResult == numOfDigits)
                    {   numberOfDigitsMultiplierForBigNumber = k;
                        numberOfDigitsMultiplierForSmallNumber = j;
                    }
                    else if (equationResult < numOfDigits)
                        k++;
                }
                if (equationResult > numOfDigits)
                {
                    if (k>1)
                        numberOfDigitsMultiplierForBigNumber = k-1;
                    else
                        //numberOfDigitsMultiplierForSmallNumber = j-1;
                        j = j-1;
                }
                else if (equationResult < numOfDigits)
                    // j--; // no hope to make equationResult = numOfDigits BY DECREASING j FURTHER!
                {   numberOfDigitsMultiplierForBigNumber = 0;
                    numberOfDigitsMultiplierForSmallNumber = 0;
                    equationResult = numOfDigits;  // for exiting the outer loop
                }
            }

        }

        // equation is solved. now produce the number as a result, if a result exists :)
        if ((numberOfDigitsMultiplierForBigNumber<1)&&(numberOfDigitsMultiplierForSmallNumber<1))
            return "-1";
        if ((numberOfDigitsMultiplierForBigNumber==0)&&(numberOfDigitsMultiplierForSmallNumber==0))
            return "-1";

        if ((numberOfDigitsMultiplierForBigNumber>=0)&&(numberOfDigitsMultiplierForSmallNumber>=0)&&(((numberOfDigitsMultiplierForSmallNumber*smallNum)+(numberOfDigitsMultiplierForBigNumber*bigNum))== numOfDigits))
            return(makeString(numberOfDigitsMultiplierForSmallNumber*smallNum, bigNum) + makeString(numberOfDigitsMultiplierForBigNumber*bigNum, smallNum));
        else if ((numberOfDigitsMultiplierForSmallNumber*smallNum)== numOfDigits)
            return makeString(numberOfDigitsMultiplierForSmallNumber*smallNum,bigNum);
        else if (((numberOfDigitsMultiplierForBigNumber*bigNum))== numOfDigits)
            return makeString(numberOfDigitsMultiplierForBigNumber*bigNum, smallNum);
        else if ((numberOfDigitsMultiplierForBigNumber==1)&&(numberOfDigitsMultiplierForSmallNumber==1))
            return makeString(smallNum,bigNum)+makeString(bigNum,smallNum);
        else return "-1";
    }

    public static void main(String[] args) {
        Solution sol = null;

        String inputFilePath= null;
        long numberOfNumbers;
        long[] numberOfDigitsOfNumbers; //Nn for each Tn
        String[] outputNumbers;
        Scanner in = null;
        if (args.length == 0)
                in = new Scanner(System.in);
        else {
            inputFilePath = args[0].toString();
            try {
//                in = new Scanner(new FileInputStream("C:\\Users\\Oguz\\IdeaProjects\\HackerrankProjects\\data\\testData.txt"));
                in = new Scanner(new FileInputStream(inputFilePath));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // read the 1st line for number of numbers
        numberOfNumbers= in.nextInt();
        numberOfDigitsOfNumbers = new long[(int) numberOfNumbers];
        outputNumbers = new String[(int) numberOfNumbers];

        // now, read rest of the list respectively:
        int j= 0;
        while (j<((int)numberOfNumbers))
        {
            sol = new Solution();
            sol.totalNumberOfDigitsOfGeneratedNumber = 0; //by default there is no such number!

            numberOfDigitsOfNumbers[j]= in.nextInt();
            sol.totalNumberOfDigitsOfGeneratedNumber = numberOfDigitsOfNumbers[j];
            // smallNum, bigNum and total number of digits for the number to be generated are ready as inputs to the engine.
            // Run the rule engine, prepare the outcomes and place in the output array
            outputNumbers[j] = sol.ruleEngine(sol.totalNumberOfDigitsOfGeneratedNumber);
            j++;
        }

        // print the lines to stdout
        int i= 0;
        while( i<numberOfNumbers)
        {
                System.out.println(outputNumbers[i]);
                i++;
        }
    }
}