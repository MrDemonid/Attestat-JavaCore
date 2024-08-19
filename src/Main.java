import backup.BackupDir;
import bitop.BitPack;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args)
    {
//        /*
//            Проверяем задание №1 - Написать функцию, создающую резервную копию всех
//            файлов в директории (без поддиректорий) во вновь созданную папку ./backup
//         */
//        try {
//            BackupDir.backupDir("./assets/src_dir", "./assets/backup/");
//        } catch (IOException e) {
//            System.out.println("Fatal error: " + e.getMessage());
//            // или так:
//            // throw new RuntimeException(e);
//        }

        int[] stat = {2, 1, 0, 2, 0, 1, 1, 0, 2};

        BitPack pack = new BitPack(8, 2);
        int[] res = pack.pack(stat);
        System.out.println(Arrays.toString(res));

        int[] unp = pack.unpack(res, stat.length);
        System.out.println("source array: \n" + Arrays.toString(stat));
        System.out.println(Arrays.toString(unp));

    }
}