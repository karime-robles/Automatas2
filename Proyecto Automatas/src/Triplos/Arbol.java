package Triplos;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Arbol{
	//static String pos="0xABAB";
	
    static int memoria=0;
    static int index=1;
    public Arbol(String valorexpresion) {

    		  if(String.valueOf(valorexpresion.charAt(valorexpresion.length()-1)).contains("+") || 
    		     String.valueOf(valorexpresion.charAt(valorexpresion.length()-1)).contains("-") ||
    		     String.valueOf(valorexpresion.charAt(valorexpresion.length()-1)).contains("*") ||
    		     String.valueOf(valorexpresion.charAt(valorexpresion.length()-1)).contains("/")){
    			  throw new NullPointerException("Error");
    		  }else {

    	      valorexpresion=valorexpresion.replaceAll(" ","");
    	      System.out.println("Expresion ingresada: "+valorexpresion);
    	      Shunting parentNode=shunt(valorexpresion);
    	      System.out.println("Utilizando Postorden");
    	      postorden(parentNode);
    	     
    	     System.out.println("----------------------------------------------------------------------------------");
    	     System.out.println("--------------------------Codigo Intermedio Generado------------------------------");
	     System.out.println("-------------------------------------Triplo---------------------------------------");
    	      dfs(parentNode);
    	      
    	      //Evaluar expresion
    	      ScriptEngineManager manager = new ScriptEngineManager();
    	        ScriptEngine engine = manager.getEngineByName("js");
    	        
    	        try {;
    	            Object result = engine.eval(valorexpresion);
    	            System.out.println(valorexpresion+" = "+result);
    	        } catch(ScriptException se) {
    	            se.printStackTrace();
    	        }
    		  }
		System.out.println("----------------------------------------------------------------------------");
	}

	public static void main(String[] args) {


        String inputString="5*3+(4+2*2)";
        inputString=inputString.replaceAll(" ","");
        System.out.println("Expresion ingresada: "+inputString);
        Shunting parentNode=shunt(inputString);
        System.out.println("Utilizando Postorden");
        postorden(parentNode);
        
        
        
        
    }


    public static void dfs(Shunting root){
        if (isOperator(root.charac)){
            dfs(root.operand1);
            dfs(root.operand2);
         
           // System.out.println((pos)+(++memoria)  +" : "+ root.name +" = " + root.operand1.name + " "+ root.charac  + " " + root.operand2.name);
            System.out.println(++memoria  +" : "+ root.name +" = " + root.operand1.name + " "+ root.charac  + " " + root.operand2.name);
        }
    }

//postorden
    public static void postorden(Shunting root){
        if (root.operand1!=null){
        	postorden(root.operand1);
        }

        if (root.operand2!=null){
        	postorden(root.operand2);
        }
        System.out.println(root.charac +" ");
    }
    
  //ensamblador
    public static void ensamblador(Shunting root){
    	  if (root.operand1!=null){
          	ensamblador(root.operand1);
          }

          if (root.operand2!=null){
        	  ensamblador(root.operand2);
          }
        //MULTIPLICACION
        if (root.charac=='*'){
        System.out.println("          ;MULTIPLICACION");
        System.out.println("          MOV    AL," +root.operand1.name);
        System.out.println("          MOV    AH," +root.operand2.name);
        System.out.println("          MUL    AH,");
        System.out.println("          MOV    T1,AX");
        }
        //DIVISION
        if (root.charac=='/'){
        	System.out.println("          ;DIVISION");
            System.out.println("          MOV    AX," +root.operand1.name);
            System.out.println("          MOV    BL," +root.operand2.name);
            System.out.println("          DIV    BL,");
            System.out.println("          MOV    T2,AL");
            }
        
        //SUMA
        if (root.charac=='+'){
        	System.out.println("          ;SUMA");
            System.out.println("          MOV    AX,T1");
            System.out.println("          MOV    X,AX");
            System.out.println("          MUL    AH,T2");
            System.out.println("          MOV    X,AH");
            }
        //SUMA
        if (root.charac=='-'){
        	System.out.println("          ;RESTA");
            System.out.println("          MOV    AX,T1");
            System.out.println("          MOV    X,AX");
            System.out.println("          SUB    AH,T2");
            System.out.println("          MOV    X,AH");
            }

        
    }
    
    //preorden
    public static void preorden(Shunting root){
        
        	System.out.println(root.charac +" ");
            

        	if (root.operand1!=null){
        		preorden(root.operand1);
            
        }
        	if (root.operand2!=null){
        		preorden(root.operand2);
        	}
    }
    //inorden
    public static void inorden(Shunting root){
        if (root.operand1!=null){
        	inorden(root.operand1);
        }
        System.out.println(root.charac +" ");
        
        if (root.operand2!=null){
        	inorden(root.operand2);
        }
        
    }
 

    private static Shunting shunt(String inputString) {


        Pila myStack=new Pila();
        Operador opr=new Operador();


        Stack<Character> operatorStack= new Stack();
        Stack<Shunting> expressionStack=new Stack();

        Character c;
        for (int i=0;i<inputString.length();i++){

            c=inputString.charAt(i);

            if (c=='('){
                operatorStack.push(c);
            }

            else if (Character.isDigit(c)){
                expressionStack.push(new Shunting(c));
            }

            else if (isOperator(c)){

                    while (opr.getOperatorPrecedence(myStack.getTopOfOperator(operatorStack)) >= opr.getOperatorPrecedence(c)) {
                        Character operator = operatorStack.pop();
                        Shunting e2 = expressionStack.pop();
                        Shunting e1 = expressionStack.pop();

                        expressionStack.push(new Shunting(operator,e1,e2,"T"+index++));
                    }

                operatorStack.push(c);
            }

            else if (c==')'){

                    while (myStack.getTopOfOperator(operatorStack) != '(') {

                        Character operator = operatorStack.pop();
                        Shunting e2 = expressionStack.pop();
                        Shunting e1 = expressionStack.pop();

                        expressionStack.push(new Shunting(operator,e1,e2,"T"+index++));
                    }

                operatorStack.pop();
            }

            else{
                System.out.println("error error");
            }
        }

        while(!operatorStack.empty()){
            Character operator=operatorStack.pop();
            Shunting e2=expressionStack.pop();
            Shunting e1=expressionStack.pop();
            expressionStack.push(new Shunting(operator,e1,e2,"T"+index++));
        }


        return expressionStack.pop();
    }

    public static boolean isOperator(Character c){
        if (c=='+' || c=='-' || c=='/' || c=='*'|| c=='%'){
            return true;
        }
        else{
            return false;
        }
    }
}
