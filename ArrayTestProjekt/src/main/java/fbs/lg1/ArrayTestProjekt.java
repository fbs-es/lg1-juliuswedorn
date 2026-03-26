package fbs.lg1;

public class ArrayTestProjekt {

    public void run() {
        int[] iarry;
        iarry = new int[4];
        for (int j = 0; j < iarry.length; j++) {
            iarry[j] = j + 1;
        }
        for (int element : iarry) {
            System.out.println(element);
        }
        String[] sarry;
        sarry = new String[4];

        sarry[0] = new String("Alexander");
        sarry[1] = new String("Uwe");
        sarry[2] = new String("Mikel");
        sarry[3] = new String("Sigbert");

        for (String element : sarry) {
            System.out.println(element);
        }

        int[][] twoarry;
        twoarry = new int[8][];
        for (int i = 0; i < twoarry.length; i++) {
            twoarry[i] = new int[8]; // wow coler trick
            for (int j = 0; j < twoarry[i].length; j++) {
                twoarry[i][j] = (i + j) % 2 == 0 ? 0 : 1;
                System.out.print(twoarry[i][j] + "  ");
            }
            System.out.println();
        }
        // 2d schachfeld wow

        int[][][] treearry = new int[3][][];
        for (int i = 0; i < treearry.length; i++) {
            treearry[i] = new int[5][];
            System.out.println("Seite: " + (i + 1));
            for (int j = 0; j < treearry[i].length; j++) {
                treearry[i][j] = new int[20];
                System.out.print("Zeile" + (j + 1) + "-> ");
                for (int k = 0; k < treearry[i][j].length; k++) {
                    treearry[i][j][k] = i + j + k;
                    System.out.print(k + " ");
                }
                System.out.println();
            }
        }
        int[][] treppe = new int[50][50];
        for (int i = 0; i < treppe.length; i++) {
            for (int j = 0; j < treppe[i].length; j++) {
                int a = (j <= i) ? 0 : 1;
                System.out.print(a + "  ");
            }
            System.out.println();
        }
        int[][][] wall = new int[3][8][8];
        for (int i = 0; i < wall.length; i++) {
            System.out.println();
            for (int j = 0; j < wall[i].length; j++) {
                for (int k = 0; k < wall[i][j].length; k++) {
                    int a = (j == 0 || j == wall[i].length - 1 || k == 0 || k == wall[i][j].length - 1) ? 0 : 1;
                    System.out.print(a + "  ");
                }

                System.out.println();
            }

        }
    }
}
