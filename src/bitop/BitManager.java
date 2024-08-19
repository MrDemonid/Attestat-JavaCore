package bitop;

import java.io.*;

public class BitManager {

    BitPack packer;
    String fileName;

    public BitManager(String fileName, BitPack packer)
    {
        this.fileName = fileName;
        this.packer = packer;
    }

    /**
     * Упаковка массива и запись в файл
     * Возникшие ошибки переведены в разряд RuntimeException, чтобы можно
     * было, при необходимости, просто игнорировать их
     * @param source исходный массив
     */
    public void save(int[] source) throws RuntimeException
    {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName)))
        {
            int[] res = packer.pack(source);
            // пишем сигнатуру и длину исходного массива
            out.write(0xDC);
            out.write(source.length);
            for (int n : res)
            {
                out.write(n);
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Считывает из файла запакованный массив и развертывает его в массив интов.
     * Чтобы не портить данные, считывание производится побайтно, в int.
     */
    public int[] load() throws RuntimeException
    {
        try (InputStream in = new BufferedInputStream(new FileInputStream(fileName))) {
            if (in.read() != 0xDC)
                throw new RuntimeException("Неверный формат файла!");
            int len = in.read();
            System.out.println("load: len = " + len);
            int[] res = new int[len];
            int index = 0;
            while (index < res.length)
            {
                res[index++] = in.read();
            }

            return packer.unpack(res, len);
        }catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Неверный формат файла");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
