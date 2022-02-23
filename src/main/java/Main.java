import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.io.IOException;

/* COP 3503C Assignment 3
This program is written by: Alexys Veloz */
public class Main {


    public static void main(String[] args) {

        try (Scanner in = new Scanner(Paths.get("in.txt")))
        {
            //take in the inputs
            while (in.hasNext())
            {
                int totalComps = in.nextInt();
                long connections = in.nextLong();
                int connectionsDestroyed = in.nextInt();
                //set up the data structures needed
                HashMap<Integer,edge> edges = new HashMap<>();
                Main mySet = new Main(totalComps+1);
                //boolean[][] connectionGraph = new boolean[totalComps+2][totalComps+2];
                int[] connectionsLineDestroyed = new int[connectionsDestroyed+1];
                int[] lines1 =  new int[(int) connections+2];
                int[] lines2 = new int[(int) connections+2];

                //iinitialize variables needed later
                int connection1= 0;
                int connection2= 0;
                edge firstege;
                for (int i = 1; i < connections+1; i++)
                {
                    //take in input and put em in the lines to get the numbers later
                    connection1 = in.nextInt();
                    connection2 = in.nextInt();
                    lines1[i] = connection1;
                    lines2[i] = connection2;
                    //connectionGraph[connection1][connection2] = true;
                    firstege = new edge(lines1[i],lines2[i]);
                    edges.put(i, firstege);
                }
                //take out the connections one by one
                for (int i = 1; i < connectionsDestroyed +1; i++)
                {
                    int ploy = in.nextInt();
                    connectionsLineDestroyed[i] = ploy;
                    //connectionGraph[lines1[ploy]][lines2[ploy]] = false;
                    edges.get(ploy).setConnected(false);
                }
                //set up the base connections!
                unionization(mySet,totalComps,edges);
                //set up variables needed to find connectedness
                long[] allTotals = new long[connectionsDestroyed+2];
                int counter = 0;
                long total = 0;
                int[] connectedPointers = connectedness(mySet,totalComps);
                //add them up
                for (int j = 1; j < totalComps+2; j++)
                {
                    if (connectedPointers[j] != 0)
                    {
                        total = total + (connectedPointers[j] * connectedPointers[j]);
                    }
                }
                //add the answer to the arry to print later
                counter++;
                allTotals[counter] = total;
                //do the same thing now for every connection I gotta do

                for (int i = connectionsDestroyed; i > 0; i--)
                {
                     total = 0;
                    int temp = connectionsLineDestroyed[i];

                    mySet.union(lines1[temp],lines2[temp]);
                    connectedPointers = connectedness(mySet,totalComps);
                    for (int j = 1; j < totalComps+2; j++)
                    {
                        if (connectedPointers[j] != 0)
                        {
                            total = total + (connectedPointers[j] * connectedPointers[j]);
                        }
                    }
                    counter++;
                    allTotals[counter] = total;
                }
                //print out the totals in proper order
                for (int i = connectionsDestroyed+1; i > 0; i--)
                {
                    System.out.printf("%d\n", allTotals[i]);
                }
            }
        } catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
    private  pair[] parents;

    public static void unionization(Main mySet,  int totalComps, HashMap<Integer,edge>edges) {
        //connect shit together
        for (int i = 1; i < totalComps ; i++) {
            //boolean flag = edges.get(i).isConnected();
            if (edges.get(i).isConnected()) {
                mySet.union(edges.get(i).getEdge1(), edges.get(i).getEdge2());
            }
        }
    }
    public static int[] connectedness(Main mySet,int totalComps){
        //see how many items are connected to a root, and count em up
        int[] connectedPointers = new int[totalComps+2];
        for (int i = 1; i < totalComps + 1; i++){
            int root = mySet.find(i);
            connectedPointers[root]++;
        }
        return connectedPointers;
    }
    //everything from here on out is borrowed code from the disjoint set java file in syllabus, only edit is in union
    public Main(int n) {

        // All nodes start as leaf nodes.
        parents = new pair[n];
        for (int i=0; i<n; i++)
            parents[i] = new pair(i, 0); //0 is height 0. parent[i]'s parent is i now
    }

    // Returns the root node of the tree storing id.
    public int find(int id) {
        // Go up tree until there's no parent.
        if (id == parents[id].getID())
            return id;
        // Find my parent's root.
        int res = find(parents[id].getID());

        // if res is not mu existing parent, make it parent
        if (res != parents[id].getID())
        {
            // Attach me directly to the root of my tree.
            parents[id].setID(res);


        }
        return res;
    }
       // while (id != parents[id].getID())
            //id =  parents[id].getID();

        //return id;
    //}

    public boolean union(int id1, int id2) {

        // Find the parents of both nodes.
        int root1 =  find(id1);
        int root2 =  find(id2);

        // No union needed.
        if (root1 == root2)
            return false;

        // Attach tree 2 to tree 1
        // hey so i changed it so it just always finds the smallest number for roots
        if (root1 < root2) {
            parents[ root2].setID( root1);
        }

        // Attach tree 1 to tree 2
        //hey so i changed it so it just always find the smallest number for roots
        else if (root2 < root1) {
            parents[root1].setID(root2);
        }

        // We successfully did a union.
        return true;
    }

}

class pair {
    //you could argue i don't need height for this but i don't wanna fuck with that
    private int ID;
    private int height;


    public pair(int myNum, int myHeight) {
        ID = myNum;
        height = myHeight;
    }

    public int getHeight() {
        return height;
    }

    public int getID() {
        return ID;
    }

    public void incHeight() {
        height++;
    }

    public void setID(int newID) {
        ID = newID;
    }
}
class edge {
        private int edge1;
        private int edge2;
        private boolean connected;

        public edge(int temp1, int temp2) {
            edge1 = temp1;
            edge2 = temp2;
            connected = true;
        }

    public int getEdge1() {
        return edge1;
    }

    public int getEdge2() {
        return edge2;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setEdge1(int edge1) {
        this.edge1 = edge1;
    }

    public void setEdge2(int edge2) {
        this.edge2 = edge2;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
