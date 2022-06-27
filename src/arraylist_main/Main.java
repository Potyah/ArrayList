package arraylist_main;

import arraylist.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> lines = new ArrayList<>(45);
        lines.add(111);
        lines.add(77);
        lines.add(66);
        lines.add(3, null);

        System.out.println(lines);
    }
}