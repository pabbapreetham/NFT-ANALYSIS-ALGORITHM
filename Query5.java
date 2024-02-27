package org.example5;

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

class EdgeObj{
    public EdgeObj(String buyerTime, Double price) {
        BuyerTime = buyerTime;
        this.price = price;
    }

    String BuyerTime;
    Double price;

    public EdgeObj(){}

    public String getBuyerTime() {
        return BuyerTime;
    }

    public void setBuyerTime(String buyerTime) {
        BuyerTime = buyerTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}

public class Query5 {

    public static void getDataPointsForTopologicalSort(List<DATA_BLOCK> data){

        int totalDataSize = data.size();
        // Considering the base size is 1000
        int tempSize = 1000;

        // result which stores the average execution time for each set of data
        ArrayList<Long> timesets = new ArrayList<>();

        long tempTime =0;
        long inistartTime = 0;
        long iniendTime = 0;
        long initotalTime = 0;
        int k =0;

        List<DATA_BLOCK> tempDataSet = null;

        // Iterate the loop until it executes the whole data.
        while(tempSize <= totalDataSize){
            tempDataSet = new ArrayList<>();
            k=0;

            tempDataSet= data.subList(0,tempSize);



            HashMap<String, HashMap<Integer, List<Integer>>> mainGraph = new HashMap<String, HashMap<Integer, List<Integer>>>();
            HashMap<String, List<EdgeObj>> mainGraphData = new HashMap<>();
            HashMap<String, Integer> firstOccurrenceOfNFT = new HashMap<String, Integer>();

            HashMap<String, String> previousTimeStamp = new HashMap<String, String>();
            HashMap<String, String> currentTimeStamp = new HashMap<String, String>();
            HashSet<String> occurredTokenIds = new HashSet<>();

            String tempTokenId = "";
            String currentTime = "";
            String previousTime = "";
            String iteratedTime = "";

            for(int i = 0 ; i < tempDataSet.size() ; i++){
                DATA_BLOCK newnode = tempDataSet.get(i);
                tempTokenId = newnode.getTokenId();

                if(occurredTokenIds.contains(newnode.getTokenId())){

                    currentTime = currentTimeStamp.get(tempTokenId);
                    previousTime = previousTimeStamp.get(tempTokenId);

                    HashMap<Integer, List<Integer>> tempGraphDataI = mainGraph.get(tempTokenId);

                    for (Map.Entry<Integer, List<Integer>> iteratedNode : tempGraphDataI.entrySet()){
                        iteratedTime = tempDataSet.get(iteratedNode.getKey()).getUnixTimes();

                        if(tempDataSet.get(iteratedNode.getKey()).getTokenId().equalsIgnoreCase(newnode.getTokenId())){
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

            String iteratedNodeTokenId = "";
            HashMap<Integer, List<Integer>>  iteratedNftGraph = null;
            List<String> mapIndexToBuyerTimeStamp = new ArrayList<>();
            int tempInd = 0;

            for (Map.Entry<String, HashMap<Integer, List<Integer>>> iteratedNode : mainGraph.entrySet()){
                iteratedNodeTokenId = iteratedNode.getKey();
                iteratedNftGraph = iteratedNode.getValue();

                for (Map.Entry<Integer, List<Integer>> eachNftGraph : iteratedNftGraph.entrySet()){
                    int a = eachNftGraph.getKey();
                    List<Integer> innerPointList = eachNftGraph.getValue();
                    List<EdgeObj> edgeObjList = new ArrayList<>();
                    for(int k1: innerPointList){
                        edgeObjList.add(new EdgeObj("Buyer(" + tempDataSet.get(k1).getBuyer() + ")-TimeStamp("+ tempDataSet.get(k1).getUnixTimes() + ")", tempDataSet.get(k1).getDollarValue()));
                    }
                    String tempBTKey = "Buyer(" + tempDataSet.get(a).getBuyer() + ")-TimeStamp(" + tempDataSet.get(a).getUnixTimes() + ")";

                    if(mainGraphData.containsKey(tempBTKey)){
                        List<EdgeObj> t1 = mainGraphData.get(tempBTKey);
                        t1.addAll(edgeObjList);

                        mainGraphData.put(tempBTKey, t1);
                    }
                    else{
                        mainGraphData.put(tempBTKey, edgeObjList);

                        mapIndexToBuyerTimeStamp.add(tempBTKey);
                        tempInd++;
                    }
                }

            }

            // Start execution timer
            inistartTime = System.nanoTime();

            HashSet<String> visited = new HashSet<>();

            Stack<String> stack = new Stack<String>();

            for(Map.Entry<String, Integer> iteratedNode: firstOccurrenceOfNFT.entrySet()){
                int i = iteratedNode.getValue();

                String tempBT = "Buyer(" + data.get(i).getBuyer() + ")-TimeStamp(" + data.get(i).getUnixTimes()+ ")";
                if(!visited.contains(tempBT)){
                    topologicalSort(tempBT, visited, stack, mainGraphData);
                }

            }

            iniendTime = System.nanoTime();
            initotalTime = iniendTime - inistartTime;

            timesets.add(initotalTime);


            if(tempSize + 1000 < totalDataSize){
                tempSize += 1000;

            } else if (tempSize == totalDataSize) {
                break;
            }
            else{
                tempSize = totalDataSize;
            }
        }

        try {
            Writer outputWrite1 = new FileWriter("datapointsoutputTopoSort4.csv", false);

            for (int i=0;i<timesets.size();i++){
                outputWrite1.write((i+1) * 1000 + "," + timesets.get(i)/ 1000000.0 + "\n");
                System.out.println(timesets.get(i)/ 1000000.0 + " milliseconds");
            }
            outputWrite1.close();
        }
        catch (Exception e){
            System.out.println("Exception occured: " + e);
        }

    }

    public static void getDataPointsForDFS(List<DATA_BLOCK> data){

        int totalDataSize = data.size();
        // Considering the base size is 1000
        int tempSize = 1000;

        // result which stores the average execution time for each set of data
        ArrayList<Long> timesets = new ArrayList<>();

        long tempTime =0;
        long inistartTime = 0;
        long iniendTime = 0;
        long initotalTime = 0;
        int k =0;

        List<DATA_BLOCK> tempDataSet = null;

        // Iterate the loop until it executes the whole data.
        while(tempSize <= totalDataSize){
            tempDataSet = new ArrayList<>();
            k=0;

            tempDataSet= data.subList(0,tempSize);



            HashMap<String, HashMap<Integer, List<Integer>>> mainGraph = new HashMap<String, HashMap<Integer, List<Integer>>>();
            HashMap<String, List<EdgeObj>> mainGraphData = new HashMap<>();
            HashMap<String, Integer> firstOccurrenceOfNFT = new HashMap<String, Integer>();

            HashMap<String, String> previousTimeStamp = new HashMap<String, String>();
            HashMap<String, String> currentTimeStamp = new HashMap<String, String>();
            HashSet<String> occurredTokenIds = new HashSet<>();

            String tempTokenId = "";
            String currentTime = "";
            String previousTime = "";
            String iteratedTime = "";

            for(int i = 0 ; i < tempDataSet.size() ; i++){
                DATA_BLOCK newnode = tempDataSet.get(i);
                tempTokenId = newnode.getTokenId();

                if(occurredTokenIds.contains(newnode.getTokenId())){

                    currentTime = currentTimeStamp.get(tempTokenId);
                    previousTime = previousTimeStamp.get(tempTokenId);

                    HashMap<Integer, List<Integer>> tempGraphDataI = mainGraph.get(tempTokenId);

                    for (Map.Entry<Integer, List<Integer>> iteratedNode : tempGraphDataI.entrySet()){
                        iteratedTime = tempDataSet.get(iteratedNode.getKey()).getUnixTimes();

                        if(tempDataSet.get(iteratedNode.getKey()).getTokenId().equalsIgnoreCase(newnode.getTokenId())){
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

            String iteratedNodeTokenId = "";
            HashMap<Integer, List<Integer>>  iteratedNftGraph = null;
            List<String> mapIndexToBuyerTimeStamp = new ArrayList<>();
            int tempInd = 0;

            for (Map.Entry<String, HashMap<Integer, List<Integer>>> iteratedNode : mainGraph.entrySet()){
                iteratedNodeTokenId = iteratedNode.getKey();
                iteratedNftGraph = iteratedNode.getValue();

                for (Map.Entry<Integer, List<Integer>> eachNftGraph : iteratedNftGraph.entrySet()){
                    int a = eachNftGraph.getKey();
                    List<Integer> innerPointList = eachNftGraph.getValue();
                    List<EdgeObj> edgeObjList = new ArrayList<>();
                    for(int k1: innerPointList){
                        edgeObjList.add(new EdgeObj("Buyer(" + tempDataSet.get(k1).getBuyer() + ")-TimeStamp("+ tempDataSet.get(k1).getUnixTimes() + ")", tempDataSet.get(k1).getDollarValue()));
                    }
                    String tempBTKey = "Buyer(" + tempDataSet.get(a).getBuyer() + ")-TimeStamp(" + tempDataSet.get(a).getUnixTimes() + ")";

                    if(mainGraphData.containsKey(tempBTKey)){
                        List<EdgeObj> t1 = mainGraphData.get(tempBTKey);
                        t1.addAll(edgeObjList);

                        mainGraphData.put(tempBTKey, t1);
                    }
                    else{
                        mainGraphData.put(tempBTKey, edgeObjList);

                        mapIndexToBuyerTimeStamp.add(tempBTKey);
                        tempInd++;
                    }
                }

            }

            // Start execution timer
            inistartTime = System.nanoTime();

            //DFS

            List<String> DFSOutput = new ArrayList<String>();


            HashSet<String> visited = new HashSet<>();
            for(Map.Entry<String, Integer> iteratedNode: firstOccurrenceOfNFT.entrySet()){
                int i = iteratedNode.getValue();

                String tempBT = "Buyer(" + data.get(i).getBuyer() + ")-TimeStamp(" + data.get(i).getUnixTimes()+ ")";

                if(!visited.contains(tempBT)){
                    DFS(tempBT, mainGraphData, visited, DFSOutput);
                }
            }

            iniendTime = System.nanoTime();
            initotalTime = iniendTime - inistartTime;

            timesets.add(initotalTime);


            if(tempSize + 1000 < totalDataSize){
                tempSize += 1000;

            } else if (tempSize == totalDataSize) {
                break;
            }
            else{
                tempSize = totalDataSize;
            }
        }

        try {
            Writer outputWrite1 = new FileWriter("datapointsoutputDFS4.csv", false);

            for (int i=0;i<timesets.size();i++){
                outputWrite1.write((i+1) * 1000 + "," + timesets.get(i)/ 1000000.0 + "\n");
                System.out.println(timesets.get(i)/ 1000000.0 + " milliseconds");
            }
            outputWrite1.close();
        }
        catch (Exception e){
            System.out.println("Exception occured: " + e);
        }

    }
    public static void DFS(String tempBT, HashMap<String, List<EdgeObj>> maingraphData, HashSet<String> visited, List<String> DfsOutput){

        visited.add(tempBT);

        //System.out.print(tempBT + " -> ");
        DfsOutput.add(tempBT + " -> ");

        List<EdgeObj> iterator = maingraphData.get(tempBT);

        for(EdgeObj ed: iterator){
            if(!visited.contains(ed.getBuyerTime())){
                DFS(ed.getBuyerTime(), maingraphData, visited, DfsOutput);
            }
        }

    }

    public static void DFSForScc(String tempBT, HashMap<String, List<EdgeObj>> maingraphData, HashSet<String> visited, List<String> DfsOutput){

        visited.add(tempBT);

        //System.out.print(tempBT + " -> ");
        DfsOutput.add(tempBT);

        if(!maingraphData.containsKey(tempBT)){
            return;
        }
        List<EdgeObj> iterator = maingraphData.get(tempBT);

        for(EdgeObj ed: iterator){
            if(!visited.contains(ed.getBuyerTime())){
                DFSForScc(ed.getBuyerTime(), maingraphData, visited, DfsOutput);
            }
        }

    }

    public static void topologicalSort(String tempBT, HashSet<String> visited, Stack<String> stack, HashMap<String, List<EdgeObj>> mainGraphData){
        visited.add(tempBT);

        String i ="";

        for(EdgeObj it: mainGraphData.get(tempBT)){
            i = it.getBuyerTime();

            if(!visited.contains(i)){
                topologicalSort(i, visited, stack, mainGraphData);
            }
        }

        stack.push(tempBT);
    }

    public static HashMap getTransposeGraph(HashMap<String, List<EdgeObj>> graph){
        HashMap<String, List<EdgeObj>> transposeGraph = new HashMap<String, List<EdgeObj>>();

        for (Map.Entry<String, List<EdgeObj>> iteratedNode : graph.entrySet()){
            String key = iteratedNode.getKey();
            List<EdgeObj> nodeList = iteratedNode.getValue();

            for(EdgeObj edgeObj: nodeList){
                String tempBt = edgeObj.getBuyerTime();
                if(transposeGraph.containsKey(tempBt)){
                    EdgeObj temp = new EdgeObj(key, edgeObj.getPrice());

                    List<EdgeObj> list = transposeGraph.get(tempBt);

                    list.add(temp);

                    transposeGraph.put(tempBt, list);
                }
                else{
                    EdgeObj temp = new EdgeObj(key, edgeObj.getPrice());

                    List<EdgeObj> list = new ArrayList<EdgeObj>();

                    list.add(temp);
                    transposeGraph.put(tempBt, list);

                }
            }
        }

        return transposeGraph;
    }


    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        String line;

        HashMap<String, HashMap<Integer, List<Integer>>> mainGraph = new HashMap<String, HashMap<Integer, List<Integer>>>();
        HashMap<String, List<EdgeObj>> mainGraphData = new HashMap<>();

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

            String iteratedNodeTokenId = "";
            HashMap<Integer, List<Integer>>  iteratedNftGraph = null;
            List<String> mapIndexToBuyerTimeStamp = new ArrayList<>();
            int tempInd = 0;

            for (Map.Entry<String, HashMap<Integer, List<Integer>>> iteratedNode : mainGraph.entrySet()){
                iteratedNodeTokenId = iteratedNode.getKey();
                iteratedNftGraph = iteratedNode.getValue();

                for (Map.Entry<Integer, List<Integer>> eachNftGraph : iteratedNftGraph.entrySet()){
                    int a = eachNftGraph.getKey();
                    List<Integer> innerPointList = eachNftGraph.getValue();
                    List<EdgeObj> edgeObjList = new ArrayList<>();
                    for(int k: innerPointList){
                        edgeObjList.add(new EdgeObj("Buyer(" + data.get(k).getBuyer() + ")-TimeStamp("+ data.get(k).getUnixTimes() + ")", data.get(k).getDollarValue()));
                    }
                    String tempBTKey = "Buyer(" + data.get(a).getBuyer() + ")-TimeStamp(" + data.get(a).getUnixTimes() + ")";

                    if(mainGraphData.containsKey(tempBTKey)){
                        List<EdgeObj> t1 = mainGraphData.get(tempBTKey);
                        t1.addAll(edgeObjList);

                        mainGraphData.put(tempBTKey, t1);
                    }
                    else{
                        mainGraphData.put(tempBTKey, edgeObjList);

                        mapIndexToBuyerTimeStamp.add(tempBTKey);
                        tempInd++;
                    }
                }

            }


            // Getting data points of asymptotic run time on NFT transaction dataset for every 1000 lines increment while performing DFS on NFT data
            //getDataPointsForDFS(data);

            // DFS

            long startTime = System.nanoTime();

            List<String> DFSOutput = new ArrayList<String>();


            HashSet<String> visited = new HashSet<>();
            for(Map.Entry<String, Integer> iteratedNode: firstOccurrenceOfNFT.entrySet()){
                int i = iteratedNode.getValue();

                String tempBT = "Buyer(" + data.get(i).getBuyer() + ")-TimeStamp(" + data.get(i).getUnixTimes()+ ")";

                if(!visited.contains(tempBT)){
                    DFS(tempBT, mainGraphData, visited, DFSOutput);
                }
            }

            long endTime = System.nanoTime();

            long totalTime = endTime - startTime;
            System.out.println("Time taken to perform DFS on graph data is " + totalTime / 1000000.0 + " milliseconds which is " + totalTime + " nanoseconds");

            Writer outputWrite = new FileWriter("output_query5_DFS.txt", false);

            for(String eachNode: DFSOutput){
                outputWrite.write(eachNode);
            }
            outputWrite.close();


            // Topological Sort

            //Getting data points of asymptotic run time on NFT transaction dataset for every 1000 lines increment while performing Topological Sort of NFT data
            //getDataPointsForTopologicalSort(data);

            startTime = System.nanoTime();

            visited.clear();

            Stack<String> stack = new Stack<String>();


            for(Map.Entry<String, Integer> iteratedNode: firstOccurrenceOfNFT.entrySet()){
                int i = iteratedNode.getValue();

                String tempBT = "Buyer(" + data.get(i).getBuyer() + ")-TimeStamp(" + data.get(i).getUnixTimes()+ ")";
                if(!visited.contains(tempBT)){
                    topologicalSort(tempBT, visited, stack, mainGraphData);
                }

            }

            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Time taken to perform Topological on graph data is " + totalTime / 1000000.0 + " milliseconds which is " + totalTime + " nanoseconds");

            Stack topoOut = (Stack)stack.clone();
            Writer outputWrite1 = new FileWriter("output_query5_Topological.txt", false);

            while(!stack.empty()){
                outputWrite1.write(stack.pop() + "->");
                //System.out.print(stack.pop() + "->");
            }

            outputWrite1.close();

            // Strongly Connected Components

            startTime = System.nanoTime();

            List<String> Scc = new ArrayList<String>();

            //Get Transpose Graph
            HashMap<String, List<EdgeObj>> tpgraph = getTransposeGraph(mainGraphData);

            // Set visited array to empty
            visited.clear();

            // Perform DFS on transpose graph by traversing the output of topological sort of normal graph
            while (topoOut.empty() == false){
                String v = (String) topoOut.pop();

                if(!visited.contains(v)){
                    DFSForScc(v, tpgraph, visited, Scc);
                }

            }

            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Time taken to get Strongly connected components on graph data is " + totalTime / 1000000.0 + " milliseconds which is " + totalTime + " nanoseconds");


            Writer outputWrite2 = new FileWriter("output_quert5_SCC.txt", false);

            for(String x: Scc){
                outputWrite2.write(x + "\n");
            }

            outputWrite2.close();


        }
        catch(Exception e){
            System.out.println("Exception : " + e);
        }
    }
}