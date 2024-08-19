/*
    Написать функцию, создающую резервную копию всех файлов в директории (без
    поддиректорий) во вновь созданную папку ./backup
 */


package backup;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BackupDir {


    /**
     * Создание резервной копии всех файлов из заданной директории
     * @param srcDirectory         Исходная папка
     * @param dstDirectory Папка для резервных копий
     * @throws IOException
     */
    public static void backupDir(String srcDirectory, String dstDirectory) throws IOException
    {
        Path destPath = Paths.get(dstDirectory);
        /*
            Делаем пару проверок, хотя это не обязательно - если вместо
            путей указать имена файлов, то копирования просто не будет, без выдачи ошибок
         */
        if (Paths.get(srcDirectory).toFile().isFile())
        {
            throw new IOException("Source must be path!");
        }
        if (destPath.toFile().isFile())
        {
            throw new IOException("Destination must be path!");
        }

        // получаем список файлов в исходной директории
        File[] listOfFiles = new File(srcDirectory).listFiles();
        if (listOfFiles != null)
        {
            if (!Files.exists(destPath))
                Files.createDirectory(destPath);        // создаём директорию для бэкапа
            // копируем файлы
            for (File srcFile : listOfFiles)
            {
                if (!srcFile.isDirectory())
                {
                    Path destFile = destPath.resolve(srcFile.getName());
                    System.out.println("  - copy '" + srcFile + "'" + " to '" + destFile + "'");
                    Files.copy(srcFile.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    System.out.println("  - skip directory: " + srcFile);
                }
            }
        }
    }

}
