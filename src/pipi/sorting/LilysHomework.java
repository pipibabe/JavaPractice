package pipi.sorting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;
/**
 * 給一長度n且數值不重複的陣列，交換陣列內之數值使arr[i]與arr[i-1]相減之和為最小值，i=0....n-1(|arr[i]-arr[i-1]|)，
 * 求最小交換次數。
 * **/
public class LilysHomework {
    class Solution {

        /**
         * time complexity is O(N\*log(N))
         * space complexity is O(3N)
         * note：ascending, desending array and distinct number(using linkedhashmap)
         */
        public static int methodA(List<Integer>arrr) {
            System.out.println("started.......methodA");
            // System.out.println("original data: "+arr);
            Function<List<Integer>, Integer> swap = (arr) -> {
                Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();

                int count = 0;
                for(int i=0; i<arr.size(); i++) {
                    if (map.get(arr.get(i))!=null){
                        System.out.println("methodA couldn't handle duplicate case");
                        count = -1;
                        break;
                    }
                    map.put(arr.get(i), i);
                }
                if (count==-1) return count;

                Integer[] keyList = map.keySet().toArray(new Integer[map.size()]);
                
                Stream<Map.Entry<Integer, Integer>> mapstream = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey());
                Iterator<Map.Entry<Integer, Integer>> it = mapstream.iterator();
                
                
                int sortedIndex = 0;
                while(it.hasNext()) {
                    // System.out.println(map);
                    Map.Entry<Integer, Integer> entry = it.next();
                    // System.out.println(entry);
                    Integer swapIndex = entry.getValue();
                    // note: sortedValue = entry.getKey(), swapValue = keyList[sortedIndex]
                    // System.out.println(sortedIndex+"/"+swapIndex+"/"+entry.getKey());
                    if (sortedIndex!=swapIndex) {
                        // System.out.println("change"+entry.getKey()+"//"+keyList[sortedIndex]);
                        map.put(keyList[sortedIndex], swapIndex);
                        int temp = keyList[sortedIndex];
                        keyList[sortedIndex]= entry.getKey();
                        keyList[swapIndex] = temp;
                        count++;
                        // System.out.println("swap count++="+count);
                    }
                    sortedIndex++;
                }
                return count;
            };
            int ascCount = swap.apply(arrr);
            
            Collections.reverse(arrr);
            int descCount = swap.apply(arrr);

            return Math.min(ascCount, descCount);
        }

    /**
         * time complexity is O(N\*log(N))
         * space complexity is O(3N)
         * note：ascending, desending array and duplicate number(using two dimension array)
         */
        public static int methodB(List<Integer>arrr) {
            System.out.println("started.......methodB");
            Function<List<Integer>, Integer> swap = (arr) -> {
                // System.out.println("original data: "+arr);
                Integer[][] swapArr = new Integer[arr.size()][2];
                
                for (int i=0; i<arr.size(); i++) {
                    swapArr[i][0] = i;
                    swapArr[i][1] = arr.get(i);
                }
                Arrays.sort(swapArr, Comparator.comparingInt(o -> o[1]));
                int count = 0;
                for (int i=0; i<swapArr.length; i++) {
                    while (true) {
                        // for (Integer[]a:swapArr) {
                        //     System.out.print(Arrays.toString(a));
                        // }
                        // System.out.println();
                        if (swapArr[i][0]==i) break;
                        else {
                            int temp = swapArr[i][0];
                            swapArr[i][0] = swapArr[temp][0];
                            swapArr[temp][0] = temp;
                            count++;
                        }
                    }
                }
                return count;
            };

            int ascCount = swap.apply(arrr);
            Collections.reverse(arrr);
            int descCount = swap.apply(arrr);

            return Math.min(ascCount, descCount);
        }

        /**
         * time complexity is O(N\*log(N))
         * space complexity is O(3N)
         * note：ascending, desending array and distinct number(using hashmap with extra swapArr, sortedArr)
         */
        public static int methodC(List<Integer>arrr) {
            System.out.println("started.......methodC");
            // System.out.println("original data: "+arr);
            Function<List<Integer>, Integer> swap= (arr) -> {
                // System.out.println("original data: "+arr);
                Integer[] swapArr = new Integer[arr.size()];
                Integer[] sortedArr = new Integer[arr.size()];
                Map<Integer, Integer> map = new HashMap<Integer, Integer>();

                for(int i=0; i<arr.size(); i++) {
                    swapArr[i] = arr.get(i);
                    sortedArr[i] = arr.get(i);
                    map.put(arr.get(i), i);
                }

                Arrays.sort(sortedArr);
                
                int count = 0;
                // System.out.println("sortedArr:" + Arrays.toString(sortedArr));
        
                for (int i=0; i<sortedArr.length; i++) {
                    // System.out.println("=====================================");
                    // System.out.println("map:" + map);
                    // System.out.println("swapArr:" + Arrays.toString(swapArr));
                    // System.out.println("index: " + index + " should be:" + i);
                    Integer index =  map.get(sortedArr[i]);//real index
                    if (index!=i) {
                        map.put(swapArr[i], index);
                        int temp = swapArr[index];
                        swapArr[index] = swapArr[i];
                        swapArr[i] = temp;
                        count++;
                        // System.out.println("swap count++="+count);
                        // System.out.println("after swapArr:"+Arrays.toString(swapArr));
                    }
                }// end of for
                return count;
            };
            int ascCount = swap.apply(arrr);
            
            Collections.reverse(arrr);
            int descCount = swap.apply(arrr);

            return Math.min(ascCount, descCount);
        }
    }



    public static void main(String[] args) throws Exception {

        BiFunction<Function<List<Integer>, Integer>, List<Integer>, Integer> runtest = (method, arr) -> {
            long startTime = System.nanoTime();
            int result = method.apply(arr);
            long elapsedTime = System.nanoTime() - startTime;

            System.out.println("swap count = "+result);
            System.out.println("Total execution time in millis: " + elapsedTime/1000000);
            return result;
        };

        BiConsumer<List<Function<List<Integer>, Integer>>, String> runtestfile = (methodArr, filename) -> {
            System.out.println("testing.............."+filename);
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/testdata/sorting/LilysHomework/"+filename+".txt"))) {
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    //System.out.println(currentLine);
                    List<Integer> arr = Stream.of(currentLine.replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList());
                    int n = Integer.parseInt(bufferedReader.readLine().trim());
                    for(Function<List<Integer>, Integer> method:methodArr) {
                        int result = runtest.apply(method, arr);
                        if(result!=n) {
                            System.out.println("answer is = "+n +", wrong answer: "+result);
                            throw new Exception("wrong answer");
                        };
                    }
                }// end while
            }
            catch(Exception e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }; 
        /*
         * testdata1: simple data
         * testdata2: multiple data
         * testdata3: duplicate numbers data
         */
        runtestfile.accept(Arrays.asList(Solution::methodA, Solution::methodB), "testdata1");
        runtestfile.accept(Arrays.asList(Solution::methodA, Solution::methodB, Solution::methodC), "testdata2");
        runtestfile.accept(Arrays.asList(Solution::methodB), "testdata3");

        // test random data
        Function<Integer, List<Integer>> rand = (num)-> {
            Random rd = new Random(); // creating Random object
            List<Integer> rdarr = new ArrayList<Integer>();
            for (int i = 0; i < num; i++) {
                rdarr.add(rd.nextInt(1, 200000001)); // storing random integers in an array
                // rdarr.add(rd.nextInt(1, 50)); // storing random integers in an array
            }
            return rdarr;
        };
        
        List<List<Integer>> randArr = new ArrayList<List<Integer>>();
        // randArr.add(rand.apply(5));
        randArr.add(rand.apply(100));
        randArr.add(rand.apply(1000));
        randArr.add(rand.apply(10000));

        for(int i=0; i <100; i++){
            randArr.add(rand.apply(100));
            randArr.add(rand.apply(1000));
            randArr.add(rand.apply(10000));
        }

        for(List<Integer> a:randArr) {

            List<Function<List<Integer>, Integer>> methodArr = Arrays.asList(Solution::methodA, Solution::methodB);
            Integer[] results = new Integer[methodArr.size()];
            for(int i=0; i<methodArr.size(); i++) {
                results[i] = runtest.apply(methodArr.get(i), a);
            }
            
            if(Integer.compare(results[0], results[1])!=0 && results[0]!=-1) {
                System.out.println("two answer not same:"+results[0]+"/"+results[1]);
                throw new Exception("wrong answer");
            }
        }
    }
}