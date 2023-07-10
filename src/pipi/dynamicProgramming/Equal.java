package pipi.dynamicProgramming;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/*
 * 有一組長度為n的亂數陣列，對其進行數回合操作使陣列之所有值相同，求最小的操作次數。
 * 其操作為任選(n-1)個位置之值進行加1或2或5，
 * 假設有一length=4的陣列[2,2,3,7]，任選3個位置進行操作，
 * round1:除了index=2全部+1 --> [3,3,3,8]
 * round2:除了index=3全部+5 --> [8,8,8,8]
 * return 答案為經過 2 回合操作
*/
public class Equal {
    class Solution {
        public static int equal(List<Integer> arr) {
            /*
            * round1:
            * 對[2,2,3,7] index=0,1,2 做+5操作 --> [7,7,8,7]
            * 等同於對[2,2,3,7] index=4 做-5操作 --> [2,2,3,2]
            * round2:
            * 對[7,7,8,7] index=0,1,3 做+1操作 --> [8,8,8,8]
            * 等同於對[2,2,3,2] index=2 做-1操作 --> [2,2,2,2]
            * 
            * 由此可以得知，一一計算[最小值以外的數]經過幾回合會等於最小值，其總和即為陣列值全部相等之步數，
            * 又因為挑選不同的位置操作可能會存在其他更佳回合解，而可進行加減的數最大為5，因此需要額外計算4步的解。
            * 例如[1,5,10]：
            * [1,1,1]減到1，需要9/5+4/2=3 4/2=2 共3+2=5回合。
            * [0,0,0]減到0，需要10/5=2 5/5=1 (1/5+1%5/2+1%5%2)=1 共2+1+1=4回合。
            * [-4,-4,-4]減到-4，則等同於減到1之情境，再多加1次-5操作，因此只需要計算5步以內的範圍。
            */
            int[] count = new int[5];
            Integer min = Collections.min(arr);
            for (int i=0;i<count.length;i++) {
                count[i]=0;
                for(Integer value:arr) {
                    int diff = value-min;
                    int step = diff/5+diff%5/2+diff%5%2;
                    count[i]+=step;
                }
                --min;
            }

            return Arrays.stream(count).min().getAsInt();
        }

    }
    public static void main(String[] args) throws IOException {

        Consumer<String> runtestfile = (filename) -> {
            System.out.println("testing.............."+filename);
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/testdata/dynamicProgramming/Equal/"+filename+".txt"))) {
                int t = Integer.parseInt(bufferedReader.readLine().trim());
                // IntStream.range(0, t).forEach(tItr -> {
                //     int n = Integer.parseInt(bufferedReader.readLine().trim());

                //     List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                //         .map(Integer::parseInt)
                //         .collect(toList());

                //     int result = Solution.equal(arr);
                //     if(result!=n) {
                //         System.out.println("answer is = "+n +", wrong answer: "+result);
                //         throw new Exception("wrong answer");
                //     };

                // });
                for (int i = 0; i < t; i++) {
                    int n = Integer.parseInt(bufferedReader.readLine().trim());

                    List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList());
                        
                    // System.out.println(arr);
                    // System.out.println(bufferedReader.readLine());
                    int result = Solution.equal(arr);

                    int ans = Integer.parseInt(bufferedReader.readLine().trim());
                    if(result!=ans) {
                        System.out.println("answer is = "+ans +", wrong answer: "+result);
                        throw new Exception("wrong answer");
                    };
                }
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
        runtestfile.accept("testdata1");
        runtestfile.accept("testdata2");
        // runtestfile.accept("testdata3");

        // test random data
        Random rd = new Random(); // creating Random object
        int caseNum = rd.nextInt(1, 101);
        IntStream.range(0, caseNum).forEach((i) -> {
            int length = rd.nextInt(1, 10001);
            List<Integer> arr = new ArrayList<Integer>();
            IntStream.range(0, caseNum).forEach((j) -> {
                arr.add(rd.nextInt(0, 1000)); // storing random integers in an array
            });

            int result = Solution.equal(arr);
            System.out.println("result is = "+result);
        });
    }
}
