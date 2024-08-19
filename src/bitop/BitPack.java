package bitop;


/**
 * Класс пакует и распаковывает массивы путем обрезания старших бит элементов,
 * т.е. битовая упаковка/распаковка
 * Где:
 * bitsPerCell - размер упакованной ячейки результата (8, 16 или 24 бит) (но хранятся все равно в int)
 * bitsPerElem - кол-во младших бит исходных чисел, которые мы берем для упаковки
 */
public class BitPack {

    private int bitsPerCell;        // размер упакованных ячеек (хотя они возвращаются в массиве int, но их размер можно варьировать)
    private int bitsPerElem;        // бит на одно неупакованное значение
    private final int bitMask;      // маска для выделения младших бит, исходя из bitPerDigit

    public BitPack(int bitsPerCell, int bitPerDigit)
    {
        setBitsPerCell(bitsPerCell);
        setBitsPerElem(bitPerDigit);
        bitMask = (1 << this.bitsPerElem) - 1;
    }

    public BitPack(int bitsPerElem)
    {
        this(24, bitsPerElem);
    }

    public void setBitsPerCell(int bitsPerCell)
    {
        if (bitsPerCell == 8 || bitsPerCell == 16 || bitsPerCell == 24)
            this.bitsPerCell = bitsPerCell;
        else
            this.bitsPerCell = 8;
    }

    public void setBitsPerElem(int bitsPerElem)
    {
        if (bitsPerElem > 1 && bitsPerElem <= 8)
            this.bitsPerElem = bitsPerElem;
        else {
            this.bitsPerElem = 8;
            System.out.println("Bits per element is too long! Use default (8).");
        }
    }

    /**
     * Возвращает кол-во задействованных бит в одной ячейке (int)
     */
    public int getBitsPerCell()
    {
        return bitsPerCell;
    }

    /**
     * Битовая упаковка массива
     * @param source массив int
     * @return массив упакованных элементов
     */
    public int[] pack(int[] source)
    {
        int elemOnCell = bitsPerCell/ bitsPerElem;     // кол-во упакованных эл-тов в одной ячейке
        int[] res = new int[(source.length + elemOnCell-1) / elemOnCell];

        for (int i = 0, index = 0, count = 0; i < source.length; i++)
        {
            if (count+ bitsPerElem > bitsPerCell)
            {
                count = 0;
                index++;                    // текущий элемент заполнен, переходим к следующему
            }
            res[index] <<= bitsPerElem;
            res[index] |= (source[i] & bitMask);
            count += bitsPerElem;
        }
        return res;
    }

    /**
     * Распаковка из массива упакованных элементов
     * @param source исходный массив с упакованными элементами
     * @param len    кол-во упакованных элементов в массиве
     * @return массив распакованных элементов
     */
    public int[] unpack(int[] source, int len)
    {
        int[] res = new int[len];
        int packBits = (bitsPerCell/ bitsPerElem) * bitsPerElem;    // реальная длина упакованной ячейки в битах

        for (int i = 0, index = 0; i < source.length; i++)
        {
            int n = source[i];
            int elemOnCell = bitsPerCell/ bitsPerElem;     // кол-во упакованных эл-тов в одной ячейке

            if (len < elemOnCell)
            {
                // нужно сдвинуть влево до упора, поскольку мы распаковываем со старших бит
                n <<= ((elemOnCell - len) * bitsPerElem);
            }
            while (len > 0 && elemOnCell > 0)
            {
                n = rol(n, packBits, bitsPerElem);
                res[index++] = n & bitMask;
                len--;
                elemOnCell--;
            }
        }
        return res;
    }

    /**
     * Циклический сдвиг влево (т.е. с переносом старших бит в младшие разряды)
     * @param num       Исходное число
     * @param bitsOfNum Длина числа в битах (у нас в int могут храниться упакованные числа разной длины)
     * @param shift     на сколько бит сдвигаем
     */
    private int rol(int num, int bitsOfNum, int shift)
    {
        shift = shift % bitsOfNum;      // подстрахуемся
        int mask = (1 << bitsOfNum) - 1;
        return (((num << shift) & mask) | (num >> (bitsOfNum - shift)));
    }
}
