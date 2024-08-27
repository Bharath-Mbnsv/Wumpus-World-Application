package wumpusworld;

import java.util.ArrayList;

/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    private World w;
    private int a_final=0;
    private int b_final=0;
    private boolean was_goal=false;
    private boolean shoot=false;
    private int num_pits=0;
    private int status= IS_NOT_FOUND;
    private int[] ini_wum_pos={0,0};
    private int[] arrow_final={0,0};
    private int[] ban={0,0};
    
    public static final int IS_NOT_FOUND = 0;
    public static final int IS_FOUND = 1;
    public static final int IS_DEAD = 2;
    
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
        }
        if (w.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }
        if (w.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        
        System.out.println("NEXT_MOVE");
        if(cX==a_final && cY==b_final){
            was_goal=false;
            System.out.println("Reached the goal("+a_final+","+b_final+")");
        }
        
        if(!was_goal){
            NaiveBayes_method nb = new NaiveBayes_method(w);
            if(status==IS_NOT_FOUND){  
                if(nb.query(ini_wum_pos)){
                    status = IS_FOUND;
                }
            }
            else if (status==IS_FOUND){
                nb.set_ini_wum_pos(ini_wum_pos);
            }
            int[] goal = new int[3];
            shoot = nb.get_goal(goal,status);
            was_goal = true;
            a_final = goal[0];
            b_final = goal[1];
            
            if(shoot){
                arrow_final[0]=a_final;
                arrow_final[1]=b_final;
            }
            System.out.println("New goal:("+a_final+","+b_final+")");
        }    
            ban[0]=0;
            ban[1]=1;
            reach_goal(cX,cY);
    } 
        
    
        public void reach_goal(int x, int y)
        {
            if (w.hasGlitter(x, y))
            {
                w.doAction(World.A_GRAB);
                return;
            }
            boolean adj=false;
            if(distance(x,y,a_final,b_final)==1){
            adj = true;
            }
        int dir = w.getDirection();

            if(x<a_final){
                if(!w.isUnknown(x+1,y)&&!w.hasPit(x+1,y)&&!(x+1==ban[0])||adj){
                    action(dir,World.DIR_RIGHT);
                    return;
                }
            }
            if(x>a_final){                                       
                if(!w.isUnknown(x-1,y)&&!w.hasPit(x-1,y)&&!(x-1==ban[0])||adj){
                    action(dir,World.DIR_LEFT);
                    return;
                }
            }
            if(y<b_final){ 
                if(!w.isUnknown(x,y+1)&&!w.hasPit(x,y+1)&&!(y+1==ban[1])||adj){
                    action(dir,World.DIR_UP);
                    return;
                }
            }
            if(y>b_final){  
                if(!w.isUnknown(x,y-1)&&!w.hasPit(x,y-1)&&!(y-1==ban[1])||adj){
                    action(dir,World.DIR_DOWN); 
                    return;
                }
            }            
            
        int pit_direction= is_pass();
        if( pit_direction >= 0){
            ban[0]=x;
            ban[1]=y;    
            action(dir,pit_direction);
            return;
        }
            boolean has_new_goal = false;
            if(x != a_final){       
                for(int j=1; j<=w.getSize(); j++){
                    boolean was_road = true;
                    if(x>a_final){
                        for(int k=x; k>=a_final; k--){
                            if(w.isUnknown(k,j)){
                                was_road = false;
                                break; 
                            }
                        }
                    }
                    else{
                        for(int k=x; k<=a_final; k++){
                            if(w.isUnknown(k,j)){
                                was_road = false;
                                break;
                            }
                        }
                    }
                    if(was_road){
                        b_final = j;
                        System.out.println("Initially standing at ("+w.getPlayerX()+","+w.getPlayerY()+")");
                        System.out.println("New goal:("+a_final+","+b_final+")");
                        has_new_goal = true;
                        break;
                    }
                }

            }
            else if(y != b_final){
                boolean was_road = true;
                for(int k=1; k<=w.getSize(); k++){
                    if(y>b_final){
                        for(int j=y; j>=b_final; j--){
                            if(w.isUnknown(k,j)){
                                was_road = false;
                                break;
                            }
                        }
                    }

                    else{
                        for(int j=y; j<=b_final; j++){
                            if(w.isUnknown(k,j)){
                                was_road = false;
                                break;
                            }
                        }
                    }

                    if(was_road){
                        a_final = k;
                        System.out.println("Initially Standing at ("+w.getPlayerX()+","+w.getPlayerY()+")");
                        System.out.println("New goal:("+a_final+","+b_final+")");
                        has_new_goal = true;
                        break;
                    }
                }

            }
            else
                System.out.println("exit");
            if(!has_new_goal){
                goal_change();
                
                System.out.println("goal change");
            }
            reach_goal(w.getPlayerX(),w.getPlayerY());
    }

    
    private void action(int currDir,int finalDir)
    {
        if(w.isInPit()){
            w.doAction(World.A_CLIMB);
            System.out.println("I fall into the pit!");
            System.out.println("I climbed out!");
        }
        if(currDir-finalDir==0){
            if(shoot&&distance(w.getPlayerX(),w.getPlayerY(),arrow_final[0],arrow_final[1])==1){
                w.doAction(World.A_SHOOT);
                System.out.println("I shoot"); 
                shoot=false;
                if(!w.wumpusAlive()){
                    status = IS_DEAD;
                    System.out.println("Killed the wumpus");
                }
            }
            else { 
                w.doAction(World.A_MOVE);
                System.out.println("I go ahead!");
            }
        }
        
        else if(currDir-finalDir==-1 || currDir-finalDir==3) {
            w.doAction(World.A_TURN_RIGHT);
            System.out.println("I turn right!");
        }
        
        else  {
            w.doAction(World.A_TURN_LEFT);
            System.out.println("I turn left!");
        }

    }


    private int distance(int x1, int y1, int x2, int y2){
        int dist = Math.abs(x1-x2)+Math.abs(y1-y2);
        return  dist;
    }

    private  void goal_change(){
        
      if(!w.isUnknown(a_final+1,b_final) && w.isValidPosition(a_final+1,b_final)){
            System.out.print("Change goal ("+a_final+","+b_final+") to");
            a_final += 1;
            System.out.println(" ("+a_final+","+b_final+")");
        }

        if(!w.isUnknown(a_final-1,b_final) && w.isValidPosition(a_final-1,b_final)){
            System.out.print("Change goal ("+a_final+","+b_final+") to");
            a_final-= 1;
            System.out.println(" ("+a_final+","+b_final+")");
        }

        if(!w.isUnknown(a_final,b_final+1) && w.isValidPosition(a_final,b_final+1)){
            System.out.print("Change goal ("+a_final+","+b_final+") to");
            b_final+= 1;
            System.out.println(" ("+a_final+","+b_final+")");
        }

        if(!w.isUnknown(a_final,b_final-1) && w.isValidPosition(a_final,b_final-1)){
            System.out.print("Change goal ("+a_final+","+b_final+") to");
            b_final-= 1;
            System.out.println(" ("+a_final+","+b_final+")");
        }
    }

    private int is_pass(){
       int dir = -1;
       int x = w.getPlayerX();
       int y = w.getPlayerY();

       if(w.isVisited(x+1,y) && !w.hasPit(x+1,y)){
            return -1;
        }else{   
            if(w.hasPit(x+1,y))
                dir=w.DIR_RIGHT; 
        }
       
        if(w.isVisited(x-1,y) && !w.hasPit(x-1,y)){
            return -1;
        }else{
            if(w.hasPit(x-1,y)) dir=w.DIR_LEFT;
        }

        if(w.isVisited(x,y+1) && !w.hasPit(x,y+1)){
            return -1;
        }else{
            if(w.hasPit(x,y+1)) dir=w.DIR_UP;
        }

        if(w.isVisited(x,y-1) && !w.hasPit(x,y-1)){
            return -1;
        }else{
            if(w.hasPit(x,y-1)) dir=w.DIR_DOWN;
        }

        System.out.println("I'm stuck");
        return dir;
    }
  

public class NaiveBayes_method{

    private ArrayList<int[]> boundary = new ArrayList<int[]>();
    private ArrayList<double[]> prob_set = new ArrayList<double[]>();
    private World w1,w2;
    private int[] wumpus_pos = new int[2];
    private ArrayList<int[]> pit_pos = new ArrayList<int[]>();
    private  int pit_con = 0;
    private boolean wumpus_found;
    private double prob_pit = 0.2;
    private double prob_wumpus = 0.0667;

    private static final int PIT = 1;
    private static final int WUMPUS = 2;
    
    
    public NaiveBayes_method(World world) {
        w1 = world;
        w2=w1.cloneWorld();
        get_boundary(1,1);
    }
    
    
    public NaiveBayes_method(){    
        
    }
    

    private void get_boundary(int x, int y) {

        System.out.println("checking the ("+x+","+y+")");
        if (!w2.isValidPosition(x, y)) {
           
            return;
        }
        if (w2.isUnknown(x, y)) {
            if (!(w2.hasMarked(x, y)))  {
                boundary.add(new int[]{x, y, 0});
                
                prob_set.add(new double[]{0,0});
                w2.setMarked(x, y);
                System.out.println("set ("+x+","+y+") as frontier");
            }
            else   
                System.out.println("("+x+","+y+") has been set froniter");   
          
            return;
        }
        else if(w2.hasMarked(x,y)) 
            return;
        else
            {
            w2.setMarked(x,y);
            get_boundary(x + 1, y );
            get_boundary(x - 1, y);
            get_boundary(x, y + 1);
            get_boundary(x, y - 1);
        }
    }

    private void get_probability(int condition) {     
        
        double p;
        if (condition == PIT) {
            p = prob_pit;
            System.out.println("P(pit)calculating P(pit)");
            System.out.println("now the p of pit is: "+p);
        } else if (condition == WUMPUS) {
            p = prob_wumpus;
            System.out.println("P(wumpus) calculating P(Wumpus)");
            System.out.println("now the p of wumpus is: "+p);
        } else {
                System.out.println("OUT OF CONDITION");
                return;
            }

        
        for (int i = 0; i < boundary.size(); i++) {
            if(checkalreadypit(pit_pos,boundary.get(i)[0],boundary.get(i)[1],condition)){             
                System.out.println("continue");
                continue;
            }
            int[] query_true = new int[]{boundary.get(i)[0],boundary.get(i)[1],1};
            int[] query_false = new int[]{boundary.get(i)[0],boundary.get(i)[1],0};
            double overall_true=0;
            double overall_false=0;
            double f = 0;

            ArrayList<int[]> portion = cloneList(boundary);
            portion.remove(i);
            ArrayList<ArrayList<int []>> combinations = new ArrayList<ArrayList<int[]>>();
            int[] count = combinations(portion,combinations);
            
            
            
            System.out.println("Query ("+boundary.get(i)[0]+","+boundary.get(i)[1]+")");

            for(int j = 0; j < combinations.size(); j++)
            {
                int sum = combinations.get(j).size();
                String msg = "Not consistent";
              
                for(int k=0; k<combinations.get(j).size();k++){                  
               
            }


               if((condition==PIT && count[j]<4) || (condition==WUMPUS && count[j]<2))
                {                    
                    if(consistent_checking(combinations.get(j),condition,query_true)) 
                    {
                        double add = Math.pow(p,count[j]) * Math.pow(1-p,sum-count[j]);
                        overall_true += add;
                    }
                 
                    if(consistent_checking(combinations.get(j),condition,query_false))
                    {
                       
                        double add = Math.pow(p,count[j]) * Math.pow(1-p,sum-count[j]);
                        overall_false += add;
                      
                    }
                }
                
                
            }

            System.out.println("final probablity");
            overall_true = p*overall_true;//

            overall_false = (1-p)*overall_false;


            try{
                f= overall_true/(overall_true+overall_false);
            }catch (ArithmeticException e){
                System.out.println("A number cannot be divided by zero!");
            }catch (Exception e){
                System.out.println("other exception");
            }

            prob_set.get(i)[condition-1]=f; 
            System.out.println("final probability of (P("+(condition==PIT?"PIT":"WUMPUS")+")("+boundary.get(i)[0]+","+boundary.get(i)[1]+"):"+f);            
            
            if(condition==PIT&&f==1){               
               pit_pos.add(new int[]{boundary.get(i)[0],boundary.get(i)[1]});
                 
            }

            if(condition==WUMPUS && f==1){
                wumpus_found = true; 
                wumpus_pos[0] = boundary.get(i)[0];
                wumpus_pos[1] = boundary.get(i)[1];
                System.out.println("I know where is the wumpus now");
                return;    
            }

        }
    }

    private boolean consistent_checking(ArrayList<int[]> arrayList, int condition, int[] query) { 
        World cw = w1.cloneWorld();  
        int size = cw.getSize();
        boolean is_consist = true;
        ArrayList<int[]> conj = cloneList(arrayList);
        conj.add(query);   
        if(condition==PIT)
        {
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                {
                    if(!cw.isUnknown(x,y)&&cw.hasPit(x,y)){    
                        cw.markSurrounding(x,y); 
                    }
                }
            }
        }
       
        for (int i = 0; i < conj.size(); i++) {
            int cx, cy;
            cx = conj.get(i)[0];
            cy = conj.get(i)[1];
            if (conj.get(i)[2] == 1)
            {
                cw.markSurrounding(cx,cy); 
            }
        }
        
        
        for (int x = 1; x <= size; x++) {
            for (int y = 1; y <= size; y++) {
                if (!(cw.isUnknown(x, y))) {
                        if(condition==PIT) {
                          
                            if (!(cw.hasBreeze(x, y) == cw.hasMarked(x, y))) {
                                is_consist = false;
                            }
                        }
                        else if(condition==WUMPUS) {
                            if (!(cw.hasStench(x, y) == cw.hasMarked(x, y))) {
                                is_consist = false;
                            }
                        }
                }
                if (!is_consist) {
                    return is_consist; 
                }
            }
        }
        return is_consist;
    }

    private ArrayList<int[]> cloneList(ArrayList<int[]> arrayList){
        ArrayList<int[]> clone = new ArrayList<int[]>(arrayList.size());
        for (int i=0;i<arrayList.size(); i++)
        {
            clone.add(arrayList.get(i).clone());

        }
        return  clone;
    }

    private int[] combinations(ArrayList<int[]> elements, ArrayList<ArrayList<int[]>> result){
          
        int m= elements.size();        
        int m_bit = 1 << m;
        int[] count = new int[m_bit];
        for(int i=0; i<m_bit; i++)
        {
            int c = 0;   
            ArrayList<int[]> comb = cloneList(elements);
            for(int j=0; j<m; j++)
            {
                int tmp = 1<<j;  
                if((tmp & i)!= 0){     
                    comb.get(j)[2]=1; 
                    c++;           
                }
            }
            result.add(comb);
            count[i] = c; 
        }
        
        return  count;
    }

    public boolean get_goal(int[] position,int wumpus_status){
        updatePit();
        
        int index;
        boolean shoot=false;
        boolean is_safe=false; 
        double OFFSET = 0.1;
        double RISK = 0.25;
        double initial_pitpro=0.2;
        double pit_upper = 0.2;
        get_probability(PIT);
        if(wumpus_status==MyAgent.IS_NOT_FOUND){ 
            double min_wumpus=1;
            double min_pit=1;
            int n = -1;
            while(n<0)
            {
                for(int i=0; i<prob_set.size(); i++){                        
                    double pw = prob_set.get(i)[1];
                    double pp = prob_set.get(i)[0];             
                    if(pw<=min_wumpus && pp<pit_upper){                       
                        if (pw == min_wumpus && n>=0&& is_farther(boundary.get(i),boundary.get(n)) ) { 
                            continue;    
                        }
                        min_wumpus=pw;
                        n=i;  
                    }
                     else{}
                }            
                pit_upper += OFFSET;
            }
            index = n;
            if(prob_set.get(index)[1]>RISK && w.hasArrow()){
                System.out.println("shoot anyway");
                shoot = true;
            }
                                                                                                            
        }                       
        else{
            double min_pit=1;
            int n=-1;
            while(!is_safe)
            {
                for(int i=0; i<prob_set.size(); i++){                    
                    double p = prob_set.get(i)[0]; 
                  
                    if(p<=min_pit) {
                        if (p == min_pit && n>=0 && is_farther(boundary.get(i),boundary.get(n))) {
                            continue;
                        }
                        min_pit=p;
                        n=i;
                    }
                }

                if(wumpus_status==MyAgent.IS_DEAD)
                    is_safe=true; 
                else{    
                    if(wumpus_pos[0]==boundary.get(n)[0] && wumpus_pos[1]==boundary.get(n)[1]){
                    
                        System.out.println("Wumpus in the goal");
                        if(w.hasArrow()){
                            shoot = true;
                            is_safe = true; 
                            System.out.println("I have an arrow");
                        }
                        else{
                            if(prob_set.size()>1){
                                prob_set.remove(prob_set.get(n));
                                min_pit=1;
                                System.out.println("I quit");
                            }
                            else{   
                                is_safe = true;    
                                System.out.println("Wumpus");
                            }
                        }
                    }
                    else
                        is_safe=true;
                }
            }
            index = n;
        }
        position[0] = boundary.get(index)[0];
        position[1] = boundary.get(index)[1];    

        return shoot;
    }



    public boolean query(int[] position){

        updateWumpus();
        get_probability(WUMPUS); 

        if(wumpus_found){
            position[0]=wumpus_pos[0];
            position[1]=wumpus_pos[1];
        } 
        return wumpus_found;
    }


    public void set_ini_wum_pos(int[] position){
        wumpus_pos[0]=position[0];
        wumpus_pos[1]=position[1];
    }

    public boolean is_farther(int[] goalA, int[] goalB){
        
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        int distanceA = Math.abs(x-goalA[0])+Math.abs(y-goalA[1]);
        int distanceB = Math.abs(x-goalB[0])+Math.abs(y-goalB[1]);

        if(distanceA>distanceB) return true;
        else return false;

    }
    public boolean checkalreadypit(ArrayList<int[]> pit_pos,int x,int y,int condition){  
        for(int i=0;i<pit_pos.size();i++){   
            System.out.println("("+pit_pos.get(i)[0]+","+pit_pos.get(i)[1]+")（"+x+","+y+")");
            if(pit_pos.get(i)[0]==x&&pit_pos.get(i)[1]==y&&condition==PIT){
                System.out.println("("+x+","+y+")");
                return true;
            }
        }
        return false;
    }
    public void updatePit(){
        for (int x = 1; x <= w.getSize(); x++) {
            for (int y = 1; y <= w.getSize(); y++)
            {
                if(!w.isUnknown(x,y)&&w.hasPit(x,y)){
                    pit_con += 1;
                }
            }
        }                
        prob_pit = (3-(double)pit_con)/(16-(double)w.getKnowns());
        
    }

    
    public void updateWumpus(){                        
        prob_wumpus = 1/(16-(double)w.getKnowns());
    }

   public  void clearpit() {
       
        pit_pos.clear();
        
    }
    
}

}

