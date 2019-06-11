//Infix2Postfix Converter
//Cameron Rushton 101002958
//Feb 22nd, 2017
import java.util.ArrayList;

public class Infix2PostfixConverter {
  
  private static StackReferenceBased stack; //Doesnt have to be here, but both methods use it
  
  /**
   * Takes a postfix expression assuming theres space between each character and evaluates it based on BEDMAS
   * @param postfixString
   * @returns int
   */
  public static int getPostfix(String postfixString) {
    
    ArrayList<String> postfixStringList = new ArrayList<String>();
    String[] stringArrayOfNumbers = postfixString.split("[^\\d]+"); //extract all ints as strings
    for (String elem : stringArrayOfNumbers) { //store integer strings into a dynamic array for indexing
      
      postfixStringList.add(elem);
      
    }
    //strip and insert symbols at proper index in the array
    int index = 0;
    for (char c : postfixString.toCharArray()) { //for each char in postfixString
      
      if (c == ' ') {
        
       index++; 
        
      }
      
      if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
        
        postfixStringList.add(index, c + ""); //c is a Char, so to convert Char to string, concatinate it
        
      }
      
    }
    // I now have an array of strings for each part of the postfix expression in proper order
    
    /* Determining the answer */
    stack = new StackReferenceBased();
    for (String str : postfixStringList) {
      
      if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("^")) { //if it's an operator
        
        int temp = (int)stack.pop(); //type casting Object -> Int may only be available in Java 7.0 or higher*
        int temp2 = (int)stack.pop();
        switch(str) { //NOTE: switching a string is only available in Java 7.0 or higher*   Current Build: 8.0_121
          
          case "+": stack.push(temp2 + temp); break;
          case "*": stack.push(temp2 * temp); break;
          case "-": stack.push(temp2 - temp); break;
          case "/": stack.push(temp2 / temp); break;
          case "^": 
            int base = temp2;//temp is exponent, temp2 is base - alternatively can use math.pow() in java.util.Math;
            for (int i = 1; i < temp; i++) {
            
              temp2 *= base;
            
            }
            stack.push(temp2); break;
          
        }
        
      } else { //if it's an operand
        
        try {
          stack.push(Integer.valueOf(str)); //Assuming str is actually an integer so it is converted to its proper format here
          
        } catch(NumberFormatException e) {
          System.out.println(e);
        }

      } 
      
    }

    return (int)stack.pop(); //return value will be at the top of stack. Conerting Object -> Int
    
  }
  
  /**
   * Takes an infix expression and converts to postfix. Ex: 3* 2-(12 / 3) + 5 ^2 --> 3 2 * 12 3 / - 5 2 ^ +
   * @param infixString
   * @returns postfixString
   */
  public static String convertPostfix(String infixString) {
    
    ArrayList<Character> charArray = new ArrayList<Character>();
    String postfixString = "";
    for (char chr : infixString.toCharArray()) { //Move all characters in infix to a char array
      charArray.add(chr);
    }
    stack = new StackReferenceBased();

    for (char c : charArray) { //traverse through each character and determine what to do with it
      if (c == '(') {
        
        stack.push(c);
        
      } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
        
        postfixString += " "; //Extra space is added constantly to distinguish between double digit numbers and symbols
        
        if (stack.isEmpty()) {
          
          stack.push(c);
          
        } else {

            while (!stack.isEmpty()) {
              
              if (comparePrecedence(c) == false) { //if top of stack has higher precedence, then pop
                
                postfixString += stack.pop() + " ";
                
              } else {
                
                break;
              }
            }
            stack.push(c);
        }     
        
      } else if (c == ')') {
        
        while (!stack.isEmpty() && stack.peek().toString().charAt(0) != '(') {
          
          postfixString += " " + stack.pop() + " ";
          
        }

        stack.pop(); //discard left brace '('
        
      } else if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') { //letters added for testing/variables
        
       postfixString += c;
        
      } else {
       
        postfixString += " "; //if space was encountered, or anything else not listed, add more space
        
      }
      
    }
    postfixString += stack.popAll(); //add rest of stack
    
    postfixString = postfixString.replaceAll("\\s+"," "); //cut out all excess space

    return postfixString;
    
  }
  
  /**
   * Returns true if input character is of higher precedence following BEDMAS
   * @param chr
   * @returns boolean
   */
  private static boolean comparePrecedence(char chr) {
    
   char top = stack.peek().toString().charAt(0);
   if (top == '+' && chr == '*' || top == '-' && chr == '*') return true;
   if (top == '+' && chr == '/' || top == '-' && chr == '/') return true;
   if (chr == '^' && top != '(') return true;
   if (top == '(') return true;
   return false;
    
  }
  
}
                                         