import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        String[] devSkillsString = reader.readLine().split(" ");
        String[] manSkillsString = reader.readLine().split(" ");
        List<Integer> devSkills = new ArrayList<>(n);
        List<Integer> manSkills = new ArrayList<>(n);

        PriorityQueue<Integer> devLeft = new PriorityQueue<>(n / 2);
        PriorityQueue<Integer> devRight = new PriorityQueue<>(n / 2);
        PriorityQueue<Integer> manLeft = new PriorityQueue<>(n / 2);
        PriorityQueue<Integer> manRight = new PriorityQueue<>(n / 2);

        int result = 0;
        for (int i = 0; i < n; i++) {
            int dev = Integer.parseInt(devSkillsString[i]);
            int man = Integer.parseInt(manSkillsString[i]);
            int min = Math.min(dev, man);
            result += min;
            devSkills.add(dev - min);
            manSkills.add(man - min);
            //addToHeap(devLeft, devRight, dev - min, 0);
            //addToHeap(manLeft, manRight, man - min, 0);
        }

        List<Integer> first = devSkills;
        List<Integer> second = manSkills;
        List<Integer> devSkillsSort = new ArrayList<>(devSkills);
        List<Integer> manSkillsSort = new ArrayList<>(manSkills);
        Collections.sort(devSkillsSort);
        Collections.sort(manSkillsSort);

        for (int i = 0; i < n; i++) {
            if (i < n / 2) {
                devLeft.add(-1 * devSkillsSort.get(i));
                manLeft.add(-1 * manSkillsSort.get(i));
            } else {
                devRight.add(devSkillsSort.get(i));
                manRight.add(manSkillsSort.get(i));
            }
        }
        int m = Integer.parseInt(reader.readLine());
        int sumDev = 0, sumMan = 0;
        for (int dev : devRight) {
            sumDev += dev;
        }
        for (int man : manRight) {
            sumMan += man;
        }

        for (int i = 0; i < m; i++) {
            Integer firstBefore = -1, firstAfter = -1;
            Integer secondBefore = -1, secondAfter = -1;
            String[] a = reader.readLine().split(" ");
            int num = Integer.parseInt(a[0]) - 1;
            int type = Integer.parseInt(a[1]);
            int d = Integer.parseInt(a[2]);
            if (type == 1) {
                first = devSkills;
                second = manSkills;
            } else if (type == 2) {
                first = manSkills;
                second = devSkills;
            }
            if (second.get(num) == 0) {
                firstBefore = first.get(num);
                first.set(num, first.get(num) + d);
                firstAfter = first.get(num);
            } else {
                if (d < second.get(num)) {
                    result += d;
                    secondBefore = second.get(num);
                    second.set(num, second.get(num) - d);
                    secondAfter = second.get(num);
                } else {
                    result += second.get(num);
                    firstBefore = first.get(num);
                    secondBefore = second.get(num);
                    first.set(num, d - second.get(num));
                    second.set(num, 0);
                    firstAfter = first.get(num);
                    secondAfter = 0;
                }
            }
            int resultTemp = 0;

            PriorityQueue<Integer> firstLeft = null;
            PriorityQueue<Integer> firstRight = null;
            PriorityQueue<Integer> secondLeft = null;
            PriorityQueue<Integer> secondRight = null;
            int sumFirst = sumDev;
            int sumSecond = sumMan;
            if (type == 1) {
                firstLeft = devLeft;
                firstRight = devRight;
                secondLeft = manLeft;
                secondRight = manRight;
            } else {
                firstLeft = manLeft;
                firstRight = manRight;
                secondLeft = devLeft;
                secondRight = devRight;
                sumFirst = sumMan;
                sumSecond = sumDev;
            }
            if (firstBefore != -1) {
                sumFirst = deleteFromHeap(firstLeft, firstRight, firstBefore, sumFirst);
            }
            if (firstAfter != -1) {
                sumFirst = addToHeap(firstLeft, firstRight, firstAfter, sumFirst);
            }
            if (secondBefore != -1) {
                sumSecond = deleteFromHeap(secondLeft, secondRight, secondBefore, sumSecond);
            }
            if (secondAfter != -1) {
                sumSecond = addToHeap(secondLeft, secondRight, secondAfter, sumSecond);
            }

            if (type == 1) {
                sumDev = sumFirst;
                sumMan = sumSecond;
            } else {
                sumMan = sumFirst;
                sumDev = sumSecond;
            }
            resultTemp += (sumDev + sumMan);
            System.out.println(result + resultTemp);
        }
        reader.close();
    }

    public static int addToHeap(PriorityQueue<Integer> left, PriorityQueue<Integer> right, int value, int sum) {
        left.add(-1 * value);
        sum += (-1 * left.peek());
        right.add(-1 * left.poll());
        if (right.size() > left.size()) {
            sum -= (right.peek());
            left.add(-1 * right.poll());
        }
        return sum;
    }

    public static int deleteFromHeap(PriorityQueue<Integer> left, PriorityQueue<Integer> right, int value, int sum) {
        if (right.peek() > value) {
            left.remove(-1 * value);
        } else {
            right.remove(value);
            sum -= value;
        }
        return sum;
    }
}