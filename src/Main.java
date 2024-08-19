import backup.BackupDir;
import bitop.BitManager;
import bitop.BitPack;

import java.io.IOException;
import java.util.Arrays;

public class Main {


    public static void main(String[] args)
    {
        Main work = new Main();

        /*
            Задача 1. Делаем бэкап директории без подкаталогов
         */
        work.work1("./assets/src_dir", "./assets/backup/");


        /*
            Задачи 2 и 3. Записываем массив в файл и считываем его.
         */
        int[] stat = {2, 1, 0, 2, 0, 1, 1, 0, 2};

        work.work2("./assets/test.bit", stat);
        int[] res = work.work3("./assets/test.bit");

        System.out.println("Array equals is '" + Arrays.equals(stat, res) + "'");
    }


    private void work1(String source, String destination)
    {
        try {
            BackupDir.backupDir(source, destination);
        } catch (IOException e) {
            System.out.println("Fatal error: " + e.getMessage());
            // или так:
            // throw new RuntimeException(e);
        }
    }

    private void work2(String fname, int[] array) throws RuntimeException
    {
        BitManager manager = new BitManager(fname, new BitPack(2));
        manager.save(array);
    }

    private int[] work3(String fname) throws RuntimeException
    {
        BitManager manager = new BitManager(fname, new BitPack(2));
        return manager.load();
    }

}