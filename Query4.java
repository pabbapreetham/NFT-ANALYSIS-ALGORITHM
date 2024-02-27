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

public class Query4 {

    public static void getDataPoints(List<DATA_BLOCK> data){

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

            // Start execution timer
            inistartTime = System.nanoTime();

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
            Writer outputWrite1 = new FileWriter("datapointsoutput1.csv", false);

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

            // Save start time before forming the graph from input data.
            long startTime = System.nanoTime();

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

            long endTime = System.nanoTime();

            long totalTime = endTime - startTime;
            System.out.println("Time taken to form Graph from data is " + totalTime / 1000000.0 + " milliseconds which is " + totalTime + " nanoseconds");


            // Getting data points of asymptotic run time on NFT transaction dataset for every 1000 lines increment while generating graph for the data.
            //getDataPoints(data);


            // Print Grapgh adjacency list

            Writer outputWrite = new FileWriter("output_query4.txt", false);

            for(Map.Entry<String, List<EdgeObj>> eachNftGraph : mainGraphData.entrySet()){
                String tokenId = eachNftGraph.getKey();
                //System.out.println("Node: " + tokenId + "Connected nodes:");
                outputWrite.write(" \n " + "Node: " + tokenId + " Connected nodes: " );
                for(EdgeObj edgeObj: eachNftGraph.getValue()){
                    outputWrite.write(edgeObj.getBuyerTime() + ": Price( " + edgeObj.getPrice() + ")  , ");
                    //System.out.print(edgeObj.getBuyerTime() + ": Price-> " + edgeObj.getPrice() + " , ");
                }
                //outputWrite.write("\n");
                //System.out.println("\n");
            }

            outputWrite.close();


        }
        catch(Exception e){
            System.out.println("Exception : " + e);
        }
    }
}