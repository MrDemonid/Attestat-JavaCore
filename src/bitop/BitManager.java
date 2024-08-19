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
            int bytesPerCell = (packer.getBitsPerCell()+7) / 8; // кол-во байт в одном эл-те массива source
            // пишем сигнатуру и длину исходного массива
            out.write(0xDC);
            out.write(source.length);
            // пишем данные
            for (int n : res)
            {
                for (int i = 0; i < bytesPerCell; i++)
                {
                    out.write(n & 0xFF);
                    n >>>=8;
                }
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
        try (InputStream in = new BufferedInputStream(new FileInputStream(fileName)))
        {
            int bytesPerCell = (packer.getBitsPerCell()+7) / 8; // кол-во байт в одном эл-те массива source
            if (in.read() != 0xDC)
                throw new RuntimeException("Неверный формат файла!");
            int len = in.read();
            int[] res = new int[len / bytesPerCell + 2];
            // считываем данные
            int index = -1;
            int rd = -1;
            do
            {
                res[++index] = 0;
                for (int i = 0; i < bytesPerCell; i++)
                {
                    rd = in.read();
                    if (rd == -1)
                        break;
                    res[index] |= (rd << i*8);
                }
            } while (rd != -1);

            return packer.unpack(res, len);

        } catch (IndexOutOfBoundsException e) {
            // считанный байт длины и реальная длина данных в файле не совпадают, выходим
            throw new RuntimeException("Неверный формат файла!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
