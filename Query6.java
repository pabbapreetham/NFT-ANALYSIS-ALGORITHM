package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

class DATA_BLOCK {

    String txtHash;
    String unixTimes;
    String dateTime;
    String action;
    String buyer;
    String nft;
    String tokenId;
    String type;
    String quantity;
    String price;

    Double dollarValue;
    String market;

    public DATA_BLOCK(String txtHash, String unixTimes, String dateTime, String action, String buyer, String nft, String tokenId,
                      String type, String quantity, String price, String market) {
        this.txtHash = txtHash;
        this.unixTimes = unixTimes;
        this.dateTime = dateTime;
        this.action = action;
        this.buyer = buyer;
        this.nft = nft;
        this.tokenId = tokenId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.market = market;

        price = price.replace("\"", "");

        String[] tokenPrice = price.split(" ");

        // 'WETH', 'ASH', 'GALA', 'TATR', 'USDC', 'MANA', 'SAND', 'RARI', 'CTZN', 'ETH',
        // 'APE'
        HashMap<String, Double> conversionRates = new HashMap<String, Double>();
        conversionRates.put("ETH", 1309.97);
        conversionRates.put("WETH", 1322.16);
        conversionRates.put("ASH", 0.9406);
        conversionRates.put("GALA", 0.03748);
        conversionRates.put("TATR", 0.012056);
        conversionRates.put("USDC", 1.0);
        conversionRates.put("MANA", 0.64205);
        conversionRates.put("SAND", 0.7919);
        conversionRates.put("RARI", 2.18);
        conversionRates.put("CTZN", 0.00321);
        conversionRates.put("APE", 4.62);

        if (tokenPrice.length > 1) {
            for (Map.Entry<String, Double> c : conversionRates.entrySet()) {
                if (c.getKey().equals(tokenPrice[1])) {
                    tokenPrice[0] = tokenPrice[0].replaceAll(",", "");
                    this.dollarValue = c.getValue() * Double.parseDouble(tokenPrice[0]);
                }
            }
        } else {
            this.dollarValue = 0.0;
        }
    }

    public String getTxtHash() {
        return txtHash;
    }
    public void setTxtHash(String txtHash) {
        this.txtHash = txtHash;
    }
    public String getUnixTimes() {
        return unixTimes;
    }
    public void setUnixTimes(String unixTimes) {
        this.unixTimes = unixTimes;
    }
    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getBuyer() {
        return buyer;
    }
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    public String getNft() {
        return nft;
    }
    public void setNft(String nft) {
        this.nft = nft;
    }
    public String getTokenId() {
        return tokenId;
    }
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getMarket() {
        return market;
    }
    public void setMarket(String market) {
        this.market = market;
    }
    public Double getDollarValue() {
        return dollarValue;
    }
    public void setDollarValue(Double dollarValue) {
        this.dollarValue = dollarValue;
    }
}

public class Query6 {

    public static void DFS(int currenti, HashMap<Integer, List<Integer>> graph, boolean[] visited, List<Integer> outputDfsList){
        visited[currenti] = true;

        //System.out.print(currenti + " ->");
        outputDfsList.add(currenti);

        List<Integer> iterator = graph.get(currenti);

        for(int i : iterator){
            if(!visited[i]){
                DFS(i, graph, visited, outputDfsList);
            }
        }
    }

    public static int minKey(Double key[], Boolean mstSet[], int V){
        Double min = Double.MAX_VALUE;
        int min_index = -1;

        for(int v =0; v < V ; v++){
            if(mstSet[v] == false && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }

        return min_index;
    }

    public static int findMaxVertex(boolean visited[],
                             Double weights[], int V)
    {

        int index = -1;


        Double maxW = Double.MIN_VALUE;

        for (int i = 0; i < V; i++)
        {

            if (visited[i] == false && weights[i] > maxW)
            {

                maxW = weights[i];

                index = i;
            }
        }
        return index;
    }

    public static Double[][] printMaximumSpanningTree(Double graph[][],
                                         int parent[], int V, HashMap<Integer, Integer> mappingIndexToMain, HashMap<String, Double> maxweightsForEachG, String tempTokenId)
    {

        Double MST = 0.0;

        Double graphOutput[][] = new Double[V][V];

        for(int a = 0 ; a< V ; a++){
            for(int b = 0; b< V ; b++){
                graphOutput[a][b] = 0.0;
            }
        }

        // Iterate over all possible nodes
        // of a graph
        for (int i = 1; i < V; i++)
        {

            // Update MST
            MST += graph[i][parent[i]];
        }

//        System.out.println("Weight of the maximum Spanning-tree "
//                + MST);
        maxweightsForEachG.put(tempTokenId, MST);
//        System.out.println();
//        System.out.println("Edges \tWeight");

        // Print the Edges and weight of
        // maximum spanning tree of a graph
        for (int i = 1; i < V; i++)
        {
            graphOutput[parent[i]][i] = graph[i][parent[i]];
//            System.out.println(mappingIndexToMain.get(parent[i]) + " - " + mappingIndexToMain.get(i) + " \t"
//                    + graph[i][parent[i]]);
        }

        return graphOutput;
    }

    public static Double[][] maximumSpanningTree(Double graph[][], HashMap<Integer, Integer> mappingIndexToMain, HashMap<String, Double> maxweightsForEachG, String tempTokenId){
        int V = graph.length;

        boolean[] visited = new boolean[V];

        Double[] weights = new Double[V];
        int parent[] = new int[V];

        for(int i = 0; i< V; i++){
            weights[i] = Double.MIN_VALUE;
            visited[i] = false;
        }

        weights[0] = Double.MAX_VALUE;
        parent[0] = -1;

        for (int i = 0; i < V - 1; i++){
            int maxVertexIndex = findMaxVertex(visited, weights, V);

            if(maxVertexIndex == -1){
                continue;
            }

            visited[maxVertexIndex] = true;

            for(int j=0;j<V;j++){
                if(graph[j][maxVertexIndex]!=0.0 && visited[j] == false){
                    if(graph[j][maxVertexIndex] > weights[j]){
                        weights[j] = graph[j][maxVertexIndex];

                        parent[j] = maxVertexIndex;
                    }
                }
            }
        }

        Double[][] out = printMaximumSpanningTree(graph,parent,V, mappingIndexToMain, maxweightsForEachG, tempTokenId);
        return out;
    }
    public static Double[][] primMst(Double graph[][], HashMap<Integer, Integer> mappingIndexToMain){

        int V = graph.length;

        Double graphOutput[][] = new Double[V][V];

        for(int a = 0 ; a< V ; a++){
            for(int b = 0; b< V ; b++){
                graphOutput[a][b] = 0.0;
            }
        }

        //array to store constructed MST
        int parent[] = new int[V];

        // Key values used to pick minimum weight edge in cut
        Double key[] = new Double[V];

        Boolean mstSet[] = new Boolean[V];

        for(int i = 0; i< V; i++){
            key[i] = Double.MAX_VALUE;
            mstSet[i] = false;
        }

        key[0] = 0.0;

        parent[0] = -1;

        for(int count = 0 ; count < V-1 ; count++){
            int u = minKey(key, mstSet, V);

            if(u == -1){
                continue;
            }
            mstSet[u] = true;

            for(int v =0; v< V ;v++){
                if(graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < key[v]){
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }

        // printMst
        for(int i =1; i < V ;i++){
            graphOutput[parent[i]][i] = graph[i][parent[i]];
            //System.out.println(mappingIndexToMain.get(parent[i]) + " - " + mappingIndexToMain.get(i) + "\t" + graph[i][parent[i]]);
        }


        return graphOutput;
    }

    public static void main(String[] args) {

        String line;

        HashMap<String, HashMap<Integer, List<Integer>>> mainGraph = new HashMap<String, HashMap<Integer, List<Integer>>>();

        HashMap<String, Integer> firstOccurrenceOfNFT = new HashMap<String, Integer>();

        HashMap<String, String> previousTimeStamp = new HashMap<String, String>();
        HashMap<String, String> currentTimeStamp = new HashMap<String, String>();
        HashSet<String> occurredTokenIds = new HashSet<>();
        List<DATA_BLOCK> data = new ArrayList<>();
        try {

            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter the Filename, File must exist in the same directory");
            String inputPath = sc.next();

            BufferedReader br = new BufferedReader(new FileReader(inputPath));
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] s = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                DATA_BLOCK b = new DATA_BLOCK(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9], s[10]);

                data.add(b);
            }



            String tempTokenId = "";
            String currentTime = "";
            String previousTime = "";
            String iteratedTime = "";

            for(int i = 0 ; i < data.size() ; i++){
                DATA_BLOCK newnode = data.get(i);
                tempTokenId = newnode.getTokenId();

                if(occurredTokenIds.contains(newnode.getTokenId())){

                    currentTime = currentTimeStamp.get(tempTokenId);
                    previousTime = previousTimeStamp.get(tempTokenId);

                    HashMap<Integer, List<Integer>> tempGraphDataI = mainGraph.get(tempTokenId);

                    for (Map.Entry<Integer, List<Integer>> iteratedNode : tempGraphDataI.entrySet()){
                        iteratedTime = data.get(iteratedNode.getKey()).getUnixTimes();

                        if(data.get(iteratedNode.getKey()).getTokenId().equalsIgnoreCase(newnode.getTokenId())){
                            if(currentTime.equalsIgnoreCase(previousTime) && currentTime.equalsIgnoreCase(iteratedTime) &&
                                    !newnode.getUnixTimes().equalsIgnoreCase(currentTime)){
                                List<Integer> tempList = tempGraphDataI.get(iteratedNode.getKey());
                                tempList.add(i);
                                tempGraphDataI.put(iteratedNode.getKey(), tempList);
                            }
                            if(!previousTime.equalsIgnoreCase(currentTime) && previousTime.equalsIgnoreCase(iteratedTime) &&
                                    newnode.getUnixTimes().equalsIgnoreCase(currentTime)){
                                List<Integer> tempList = tempGraphDataI.get(iteratedNode.getKey());
                                tempList.add(i);
                                tempGraphDataI.put(iteratedNode.getKey(), tempList);
                            }
                            if(!previousTime.equalsIgnoreCase(currentTime) && !currentTime.equalsIgnoreCase(newnode.getUnixTimes()) &&
                                    iteratedTime.equalsIgnoreCase(currentTime)){
                                List<Integer> tempList = tempGraphDataI.get(iteratedNode.getKey());
                                tempList.add(i);
                                tempGraphDataI.put(iteratedNode.getKey(), tempList);
                            }
                        }
                    }
                    tempGraphDataI.put(i,new ArrayList<>());
                    mainGraph.put(tempTokenId, tempGraphDataI);

                    // change timestamps
                    previousTimeStamp.put(tempTokenId, currentTime);
                    currentTimeStamp.put(tempTokenId, newnode.getUnixTimes());

                }
                else{

                    HashMap<Integer, List<Integer>> tempGraphDataI = new HashMap<>();
                    tempGraphDataI.put(i, new ArrayList<>());

                    mainGraph.put(tempTokenId, tempGraphDataI);
                    previousTimeStamp.put(tempTokenId, newnode.getUnixTimes());
                    currentTimeStamp.put(tempTokenId, newnode.getUnixTimes());
                    occurredTokenIds.add(tempTokenId);

                    firstOccurrenceOfNFT.put(tempTokenId, i);
                }

            }


            // DFS
            String iteratedNodeTokenId = "";
            HashMap<Integer, List<Integer>>  iteratedNftGraph = null;
            HashMap<String, List<Integer>> outputDfs = new HashMap<>();
            int tempfirstOccurenceOfTokenId = 0;

            boolean visitedNodes[] = new boolean[data.size()];
            for (Map.Entry<String, HashMap<Integer, List<Integer>>> iteratedNode : mainGraph.entrySet()){
                iteratedNodeTokenId = iteratedNode.getKey();
                iteratedNftGraph = iteratedNode.getValue();

                List<Integer> outputDfsList = new ArrayList<>();

                tempfirstOccurenceOfTokenId = firstOccurrenceOfNFT.get(iteratedNodeTokenId);

                DFS(tempfirstOccurenceOfTokenId, iteratedNftGraph, visitedNodes, outputDfsList);

                for (Map.Entry<Integer, List<Integer>> eachNftGraph : iteratedNftGraph.entrySet()){
                    int tempId = eachNftGraph.getKey();
                    if(visitedNodes[tempId] == false){
                        DFS(tempId, iteratedNftGraph, visitedNodes, outputDfsList);
                    }
                }


                outputDfs.put(iteratedNodeTokenId, outputDfsList);
            }


            for (int i = 0; i < data.size(); i++) {
                visitedNodes[i] = false;
            }


            HashMap<String, HashMap<Integer, Integer>> mappingIndToMain = new HashMap<>();
            HashMap<String, HashMap<Integer, Integer>> mappingMainToInd = new HashMap<>();


            // Mapping main indexes to temporary indexes where formed from dfs list to get the adjacency matrix.
            for(Map.Entry<String, List<Integer>> iteratedNode : outputDfs.entrySet()){
                tempTokenId = iteratedNode.getKey();
                List<Integer> dfsList = iteratedNode.getValue();

                HashMap<Integer, Integer> hs = new HashMap<>();
                HashMap<Integer, Integer> hs1 = new HashMap<>();

                for(int i =0;i< dfsList.size(); i++){
                    hs.put(i, dfsList.get(i));
                    hs1.put(dfsList.get(i), i);
                }

                mappingIndToMain.put(tempTokenId, hs);
                mappingMainToInd.put(tempTokenId, hs1);
            }

            HashMap<String, Double[][]> NftsAdjMatrix = new HashMap<>();

            //forming adjacency matrix for each tokenId
            for(Map.Entry<String, HashMap<Integer, List<Integer>>> iteratedNode : mainGraph.entrySet()){
                iteratedNodeTokenId = iteratedNode.getKey();
                iteratedNftGraph = iteratedNode.getValue();

                HashMap<Integer, Integer> mappingInd = mappingMainToInd.get(iteratedNodeTokenId);

                int size = outputDfs.get(iteratedNodeTokenId).size();


                Double graph[][] = new Double[size][size];
                for(int a = 0 ; a< size ; a++){
                    for(int b = 0; b< size ; b++){
                        graph[a][b] = 0.0;
                    }
                }

                for(Map.Entry<Integer, List<Integer>> innergraph : iteratedNftGraph.entrySet()){
                    int curpoint = innergraph.getKey();
                    List<Integer> curpointsList = innergraph.getValue();

                    for(int j: curpointsList){
                        Double price = data.get(j).getDollarValue();

                        int a = mappingInd.get(curpoint);
                        int b = mappingInd.get(j);

                        // ignore direction so making bi-directional
                        graph[a][b] = price;
                        graph[b][a] = price;
                    }
                }

                NftsAdjMatrix.put(iteratedNodeTokenId, graph);
            }

            // Minimum Spanning Tree

            long startTime = System.nanoTime();

            HashMap<String, Double[][]> minstOutput = new HashMap<>();

            for(Map.Entry<String, Double[][]> iteratedNode : NftsAdjMatrix.entrySet()){
                tempTokenId = iteratedNode.getKey();
                Double g[][] = iteratedNode.getValue();

                Double out[][] = primMst(g,mappingIndToMain.get(tempTokenId));

                minstOutput.put(tempTokenId, out);
            }

            long endTime = System.nanoTime();

            long totalTime = endTime - startTime;
            System.out.println("Time taken for Minimum Spanning Tree for input data is " + totalTime / 1000000.0 + " milliseconds which is " + totalTime + " nanoseconds");

            Writer outputWrite = new FileWriter("output_quert6_MinimumSpanningTree.txt", false);

            for(Map.Entry<String, Double[][]> iteratedNode : minstOutput.entrySet()){
                tempTokenId = iteratedNode.getKey();
                Double g[][] = iteratedNode.getValue();

                outputWrite.write("tokenId: MST " + tempTokenId + "\n");
                //System.out.println("tokenId: MST " + tempTokenId);
                HashMap<Integer, Integer> mappingIndexToMain = mappingIndToMain.get(tempTokenId);

                int V = g.length;
                outputWrite.write("BUYER(TOKENID)/ BUYER(TOKENID) ");
                //System.out.print("BUYER(TOKENID)/ BUYER(TOKENID)");
                for(int a = 0 ; a< V ; a++){
                    outputWrite.write(mappingIndexToMain.get(a) + " : " + "\t");
                    //System.out.print(mappingIndexToMain.get(a) + " : " + "\t");
                }
                outputWrite.write("\n");
                //System.out.println();
                for(int a = 0 ; a< V ; a++){
                    outputWrite.write(mappingIndexToMain.get(a) + " : ");
                    //System.out.print(mappingIndexToMain.get(a) + " : ");
                    for(int b = 0; b< V ; b++){
                        outputWrite.write(g[a][b] + " ");
                        //System.out.print(g[a][b] + " ");
                    }
                    outputWrite.write("\n");
                    //System.out.println();
                }
            }

            outputWrite.close();


            startTime = System.nanoTime();

            HashMap<String, Double> maxweightsForEachG = new HashMap<String, Double>();
            HashMap<String, Double[][]> maxstOutput = new HashMap<>();

            // Maximum Spanning Tree
            for(Map.Entry<String, Double[][]> iteratedNode : NftsAdjMatrix.entrySet()){
                tempTokenId = iteratedNode.getKey();
                Double g[][] = iteratedNode.getValue();

                //System.out.println("tokenId: MAXST" + tempTokenId);

                Double[][] outG = maximumSpanningTree(g, mappingIndToMain.get(tempTokenId), maxweightsForEachG, tempTokenId);
                maxstOutput.put(tempTokenId, outG);

            }

            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Time taken to Maximum Spanning Tree on graph data is " + totalTime / 1000000.0 + " milliseconds which is " + totalTime + " nanoseconds");

            Writer outputWrite2 = new FileWriter("output_quert6_MaximumSpanningTree.txt", false);

            for(Map.Entry<String, Double[][]> iteratedNode : maxstOutput.entrySet()){
                tempTokenId = iteratedNode.getKey();
                Double g[][] = iteratedNode.getValue();

                outputWrite2.write("\n" + "tokenId: " + tempTokenId + "\t");
                outputWrite2.write("Weight of the maximum Spanning-tree " + maxweightsForEachG.get(tempTokenId) + "\n");
//                System.out.println("tokenId: MAXST" + tempTokenId);
//                System.out.println("Weight of the maximum Spanning-tree " + maxweightsForEachG.get(tempTokenId));

                HashMap<Integer, Integer> mappingIndexToMain = mappingIndToMain.get(tempTokenId);

                int V = g.length;
                outputWrite2.write("BUYER(TOKENID)/ BUYER(TOKENID) ");
                //System.out.print("BUYER(TOKENID)/ BUYER(TOKENID)");
                for(int a = 0 ; a< V ; a++){
                    outputWrite2.write(mappingIndexToMain.get(a) + " : " + "\t");
                    //System.out.print(mappingIndexToMain.get(a) + " : " + "\t");
                }
                outputWrite2.write("\n");
                //System.out.println();
                for(int a = 0 ; a< V ; a++){
                    outputWrite2.write(mappingIndexToMain.get(a) + " : ");
                    //System.out.print(mappingIndexToMain.get(a) + " : ");
                    for(int b = 0; b< V ; b++){
                        outputWrite2.write(g[a][b] + " ");
                        //System.out.print(g[a][b] + " ");
                    }
                    outputWrite2.write("\n");
                    //System.out.println();
                }

            }

            outputWrite2.close();

        }
        catch(Exception e){
            System.out.println("Exception : " + e);
        }

    }
}