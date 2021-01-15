
package nfa_to_dfa;

import java.util.Scanner; 
import java.util.*;

public class NFA_TO_DFA {
    int setOfStates;
    int setOfInputs;
    int startState;
    int setOfFinals;
    String[] finalStates; 
    String[][] NFA ;
    String[][] NoEpsilonNFA ;
    String[][] DFA ;
    ArrayList<String> states = new ArrayList<>();

    public NFA_TO_DFA(int setOfStates, int setOfInputs, int startState, int setOfFinals) {
        this.setOfStates = setOfStates;
        this.setOfInputs = setOfInputs;
        this.startState = startState;
        this.setOfFinals = setOfFinals;
    }
    
    void setFinalStates(){
        finalStates = new String[this.setOfFinals];
        for(int i = 0; i<setOfFinals;i++){
            System.out.println("Enter the final state number ["+ (i+1) +"] :");
            Scanner myObj = new Scanner(System.in);
            finalStates[i] = myObj.nextLine(); 
        }
    }
    
    
    void setNFATransitionTable(){
        NFA = new String[this.setOfStates][this.setOfInputs + 1];
        for(int i=0;i<this.setOfStates;i++)
        {
            for(int j =0;j<this.setOfInputs;j++)
            {
                System.out.println("For state ["+i+"] and input ["+j+"] , what is the output state?");
                Scanner myObj = new Scanner(System.in);
                NFA[i][j] = myObj.nextLine(); 
            }
            System.out.println("For state ["+i+"] and input Epsilon , what is the output state?");
            Scanner myObj = new Scanner(System.in);
            NFA[i][this.setOfInputs] = myObj.nextLine(); 
        }
    }
    
    void printNFA(){
        System.out.println("------------------------------------NFA TRANSITION TABLE------------------------\\n");
        for(int i=0;i<this.setOfStates;i++)
        {
            for(int j =0;j<this.setOfInputs;j++)
            {
                System.out.print("\t"+NFA[i][j]);
            }
            System.out.println("\t"+NFA[i][+this.setOfInputs]);
        }
    }
    
    void setNoEpsilonNFATransitionTable(){
        int len;
        NoEpsilonNFA = new String[this.setOfStates][this.setOfInputs];
        for(int i=0;i<this.setOfStates;i++)
        {
            for(int j =0;j<this.setOfInputs;j++)
            {
                if(!(NFA[i][j].equals("~"))){
                    len = NFA[i][j].length();
                    NoEpsilonNFA[i][j]="";
                    if(len>1){
                      for(int t =0;t<len;t++){
                          String c = String.valueOf(NFA[i][j].charAt(t));
                          int y = Integer.parseInt(c);
                          if(!(NFA[y][this.setOfInputs].equals("~"))){
                            NoEpsilonNFA[i][j] = NoEpsilonNFA[i][j].concat(c).concat(NFA[y][this.setOfInputs]);
                          }
                          else{//No epsilon
                            NoEpsilonNFA[i][j] = NoEpsilonNFA[i][j].concat(c);
                          }
                      }
                    }
                    else{
                        int x = Integer.parseInt(NFA[i][j]);
                        if(!(NFA[x][this.setOfInputs].equals("~"))){//means there is a value in epsilon comlumn
                            NoEpsilonNFA[i][j] = NFA[i][j].concat(NFA[x][this.setOfInputs]);
                        }
                        else{
                            NoEpsilonNFA[i][j] = NFA[i][j];
                        }
                    } 
                }
                else{
                    NoEpsilonNFA[i][j] = NFA[i][j];
                }
            }
        }
    }
    
    void printNoEpsilonNFA(){
        System.out.println("------------------------------------NO EPSILON NFA TRANSITION TABLE------------------------\\n");
        for(int i=0;i<this.setOfStates;i++)
        {
            for(int j =0;j<this.setOfInputs;j++)
            {
                System.out.print("\t"+NoEpsilonNFA[i][j]);
            }
            System.out.print("\n");
        }
    }
    
static String removeDuplicate(char str[], int n) 
    { 
        // Used as index in the modified string 
        int index = 0; 
  
        // Traverse through all characters 
        for (int i = 0; i < n; i++) 
        { 
  
            // Check if str[i] is present before it  
            int j; 
            for (j = 0; j < i; j++)  
            { 
                if (str[i] == str[j]) 
                { 
                    break; 
                } 
            } 
  
            // If not present, then add it to 
            // result. 
            if (j == i)  
            { 
                str[index++] = str[i]; 
            } 
        } 
        return String.valueOf(Arrays.copyOf(str, index)); 
    } 
    
    void setDFATransitionTable(){
        int before;
        int after;
        int len;
        
        String[] sortArr = new String[50];
        
        DFA = new String[100][this.setOfInputs];
        //get the first state
        if(!(NFA[0][this.setOfInputs].equals("~"))){//means the epsilon column has a value
            states.add(String.valueOf(startState).concat(NFA[0][this.setOfInputs]));
        }
        else{
            states.add(String.valueOf(startState));
        }
        
        before = states.size();
        // creating the DFA transition table
            for(int i=0;i<states.size();i++)
            {
                for(int j =0;j<this.setOfInputs;j++)
                {
                  if(!(states.get(i).equals("~"))){
                    len = states.get(i).length();
                        DFA[i][j]="";
                        if(len>1){
                          for(int t =0;t<len;t++){
                              String c = String.valueOf(states.get(i).charAt(t));
                              int y = Integer.parseInt(c);
                              DFA[i][j] = DFA[i][j].concat(NoEpsilonNFA[y][j]);                              
                          }
                            //remove duplicates from string
                          char str[] = DFA[i][j].toCharArray(); 
                            int n = str.length; 
                            DFA[i][j] = removeDuplicate(str, n);
                            
                            
                            if(!(DFA[i][j].contains("~"))){
                              //sort  
                              int length = DFA[i][j].length();
                                for(int t =0;t<length;t++){
                                  String c = String.valueOf(DFA[i][j].charAt(t));
                                  //int y = Integer.parseInt(c);
                                  sortArr[t] = c;
                                }
                                Arrays.sort(sortArr,0,length);
                                DFA[i][j] = "";
                                for(int t =0;t<length;t++){
                                    DFA[i][j] = DFA[i][j].concat(String.valueOf(sortArr[t]));
                                }
                            }
                            else{//numbers concatenated with phi
                                if(DFA[i][j].length()>1){ // means numbers with phi
                                    DFA[i][j] = DFA[i][j].replace("~","");                                    
                                }
                            }
                            

                            //put the value in the states arraylist
                            states.add(DFA[i][j]);
                           
                        }
                        else{
                            int x = Integer.parseInt(states.get(i));
                            DFA[i][j] = DFA[i][j].concat(NoEpsilonNFA[x][j]);
                            //put the value in the states arraylist
                            states.add(DFA[i][j]);
                        }
                  }
                  else{
                      DFA[i][j] = states.get(i);
                      states.add(DFA[i][j]);
                  }
                        
                        System.out.println("The new state : " + DFA[i][j]);
                        //System.out.println(Arrays.toString(states.toArray()));
                }
                //remove duplicates from array list states
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(states);

                states = new ArrayList<>(hashSet);
                System.out.println(Arrays.toString(states.toArray()));
            }
        
        System.out.println("------------------------------------DFA TRANSITION TABLE------------------------\\n");
        for(int i=0;i<states.size();i++)
        {
            for(int j =0;j<this.setOfInputs;j++)
            {
                System.out.print("\t"+DFA[i][j]);
            }
            System.out.print("\n");
        }
        
        
    }
    
    
               
    
    public static void main(String[] args) {
        
        int setOfStates;
        int setOfInputs;
        int startState;
        int setOfFinals;
        
        System.out.println("------------------------------------PESS ~ FOR PHI VALUE------------------------\\n");
        
        Scanner myObj = new Scanner(System.in);
        
        System.out.println("Enter Number of States : ");
        setOfStates = myObj.nextInt();
        
        System.out.println("Enter Number of Inputs : ");
        setOfInputs = myObj.nextInt();
        
        System.out.println("Enter the initial state number : ");
        startState = myObj.nextInt();
        
        System.out.println("Enter Number of Final States : ");
        setOfFinals = myObj.nextInt();
        
        
        NFA_TO_DFA obj = new NFA_TO_DFA(setOfStates,setOfInputs,startState,setOfFinals);
        
        obj.setFinalStates();
        obj.setNFATransitionTable();
        obj.printNFA();
        obj.setNoEpsilonNFATransitionTable();
        obj.printNoEpsilonNFA();
        obj.setDFATransitionTable();
        
    }
    
}
