import java.util.*;
public class Main{
    private static class Node{
        int index;
        int count;
        Node(int index,int count){
            this.index = index;
            this.count = count;
        }
    }
    private static int next(int index,int n){
        return (index+1)%n;
    }
    private static int aux(int [] d){
       int max = d[0];
       int maxIndex = 0;
       for(int i  = 1;i<d.length;i++){
           if(d[i]>max){
               max = d[i];
               maxIndex = i;
           }
       }

       Stack<Node> s = new Stack<>();
        s.push(new Node(maxIndex,1));
        int count = 0;
       for(int i = next(maxIndex,d.length);i!=maxIndex;i=next(i,d.length)){
               while(d[i]>d[s.peek().index]){
                 Node node =  s.pop();
                 count+=node.count;
               }
               if(d[i]==d[s.peek().index]){
                   count+=s.peek().count;
                   s.peek().count++;
               }
               else{
                   s.push(new Node(i,1));
               }
               if(s.size()>1){
                   count++;
               }
       }

       return count;
    }
    public static void main(String [] args){
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            int n = in.nextInt();
            int [] d= new int[n];
            for(int i = 0;i<n;i++){
                d[i] = in.nextInt();
            }
            System.out.println(aux(d));
        }
        in.close();
    }
}