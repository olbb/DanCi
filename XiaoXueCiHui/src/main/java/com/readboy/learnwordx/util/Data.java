package com.readboy.learnwordx.util;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class Data {

    public static String respath;
    public static RandomAccessFile datafile;
    RandomAccessFile keyfile;
    public static RandomAccessFile letterfile;
    long keyfilelen;
    int version;//rf4文件版本
    int wordcount;//单词总数
    int soundaddr;//声音开始地址
    int lessoncount;//课程总数
    int startaddr;//开始地址
    int addedaddr;//追加数据地址

    String TAG = "LearnWord";

//    private void newData(){
//        try {
////            datafile=new RandomAccessFile("/mnt/sdcard/单词记忆/初中词汇.rf4", "r");
////			keyfile =new RandomAccessFile("/mnt/sdcard/wwlt/bcdkey.BIN", "r");
////			letterfile=new RandomAccessFile("/mnt/sdcard/wwlt/letter.BIN", "r");
//
//            datafile=new RandomAccessFile(Util.dir+"chuzhongcihui.rf4","r");
//            keyfile=new RandomAccessFile(Util.dir+"bcdkey.BIN","r");
//            letterfile=new RandomAccessFile(Util.dir+"letter.BIN","r");
//
//            keyfilelen= keyfile.length();
//
//
//
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        readFileHeader();
//    }


    public Data() {
        // TODO Auto-generated constructor stub
        try {
//            datafile=new RandomAccessFile("/mnt/sdcard/单词记忆/初中词汇.rf4", "r");
//			keyfile =new RandomAccessFile("/mnt/sdcard/wwlt/bcdkey.BIN", "r");
//			letterfile=new RandomAccessFile("/mnt/sdcard/wwlt/letter.BIN", "r");

            datafile = new RandomAccessFile(Util.dir + "xiaoxuecihui.rf4", "r");
            keyfile = new RandomAccessFile(Util.dir + "bcdkey.BIN", "r");
            letterfile = new RandomAccessFile(Util.dir + "letter.BIN", "r");

            keyfilelen = keyfile.length();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        readFileHeader();


    }


    /**
     * ****************************读取文件头信息*****************
     */
    public void readFileHeader() {
        /*if(isFail){
            return;
		}*/
        long checkNum1 = getData(0, 4);
        if (checkNum1 != 0x344652L) {
			Log.i(TAG, "文件格式错误");
        }
        wordcount = (int) (getData(0x10, 4));
		Log.i(TAG, "wordcount====> " + wordcount);
        soundaddr = (int) getData(0x14, 4);
		Log.i(TAG,"soundaddr====> "+soundaddr);
        lessoncount = (int) getData(0x3c, 2);
		Log.i(TAG,"lessoncount====> "+lessoncount);
        startaddr = 32 * lessoncount + 0x40;
		Log.i(TAG,"startaddr====> "+startaddr);
        addedaddr = (int) getData(0x1c, 4);
		Log.i(TAG,"addedaddr====> "+addedaddr);

        long checkVersion = getData(0x18, 4);


        if (checkVersion == 0x302e3156) {
            version = 1;
        } else if (checkVersion == 0x302e3256) {
            version = 2;
        } else {
			Log.i(TAG, "版本错误");
            version = 1;
        }
		Log.i(TAG, "Ver."+version);
    }


    /**
     * 得到每一个单词的信息，单词，音标，解释，
     * 入参单词序号
     */
    public Word getword(int wordnum) {    //得到每一个单词的信息，单词，音标，解释
        Word word = new Word(wordnum);
        byte[] ch = new byte[64];
        byte temp;
        int i;
        char[] tempChar = new char[64];
        boolean is0x20end = false;
        int currentwordaddr = 0;
        if (version == 1) {
            currentwordaddr = startaddr + 96 * (wordnum - 1);
        } else if (version == 2) {
            currentwordaddr = startaddr + 128 * (wordnum - 1);
        }

        long offset = currentwordaddr;
        synchronized (datafile) {
            for (i = 0; i < 32; i++) {
                temp = (byte) getData(offset, 1);
                if (temp != 0x00) {
                    if (temp == 0x20) {
                        if (is0x20end) {
                            is0x20end = false;
                            break;
                        } else {
                            is0x20end = true;
                        }
                    } else {
                        is0x20end = false;
                    }
                    ch[i] = temp;
                    offset++;
                } else {
                    break;
                }
            }
        }

        try {
            word.word = new String(ch, 0, i, "gbk");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            if (word.word.length() > 0 && word.word.charAt(word.word.length() - 1) == ' ') {  //有些单词最后有一个空格，去掉
                word.word = word.word.substring(0, word.word.length() - 1);
            }
        } catch (StringIndexOutOfBoundsException e) {
            // TODO: handle exception
        }

        int phStart = i + 1;
        //读取单词音标
        offset = currentwordaddr + 32;
        synchronized (datafile) {
            for (i = 0; i < 32; i++) {
                temp = (byte) getData(offset, 1);
                if (temp != 0x00) {
                    if (temp == 0x20) {
                        if (is0x20end) {
                            is0x20end = false;
                            break;
                        } else {
                            is0x20end = true;
                        }
                    } else {
                        is0x20end = false;
                    }
                    ch[i] = temp;
                    //	System.out.println("==phonetic=="+i+"=====");
                    offset += 1;
                } else {
                    break;
                }
            }
        }

        int phEnd = phStart + i;
        int j = 0;
        for (j = 0; j < i; j++) {
            switch (ch[j]) {
                case '2':
                    tempChar[j] = 0x028c;
                    break;
                case '3':
                    tempChar[j] = '\u0259';
                    break;
                case '*':
                    tempChar[j] = 0x02c8;
                    break;
                case '&':
                    tempChar[j] = 'a';
                    break;
                case '<':
                    tempChar[j] = 0x0261;
                    break;
                case '=':
                    tempChar[j] = 0x0292;
                    break;
                case '0':
                    tempChar[j] = 0x03b8;
                    break;
                case '1':
                    tempChar[j] = 0x02d0;
                    break;
                case '4':
                    tempChar[j] = 0x025b;
                    break;
                case '5':
                    tempChar[j] = 0x00e6;        //
                    break;
                case '6':
                    tempChar[j] = 0x0254;
                    break;
                case '7':
                    tempChar[j] = 0x0283;
                    break;
                case '8':
                    tempChar[j] = 0x00f0;
                    break;
                case '9':
                    tempChar[j] = 0x014b;
                    break;
                case '+':
                    tempChar[j] = 0x02cc;
                    break;
                case 'B':
                    tempChar[j] = 0x0251;
                    break;
                case 'R':
                    tempChar[j] = 0x0259;
                    break;
                case 'A':
                    tempChar[j] = 0x0251;
                    break;
                case '%':
                    tempChar[j] = 'i';
                    break;
                case '$':
                    tempChar[j] = 0x0259;
                    break;
                case '|':
                    tempChar[j] = 0x02c8;
                    break;
                case 'O':
                    tempChar[j] = 0x0254;
                    break;
                case 'S':
                    tempChar[j] = 0x0283;
                    break;
                case 'H':
                    tempChar[j] = 0x02d0;
                    break;
                case 'V':
                    tempChar[j] = 0x028c;
                    break;
                case 'E':
                    tempChar[j] = 0x025c;
                    break;
                case 'Z':
                    tempChar[j] = 0x0292;
                    break;
                case 'N':
                    tempChar[j] = 0x014b;
                    break;
                case 'C':
                    tempChar[j] = 0x00e6;
                    break;
                case 'P':
                    tempChar[j] = 0x03b8;
                    break;
                case 'Q':
                    tempChar[j] = 0x00f0;
                    break;
                case 'W':
                    tempChar[j] = 0x028a;
                    break;
                case 'J':
                    tempChar[j] = 'u';
                    break;
                case '\\':
                    tempChar[j] = 0x0259;
                    break;
                default:
                    tempChar[j] = (char) ch[j];
                    break;
            }
        }
        word.phon = new String(tempChar, 0, i);

        //读取单词解释
        offset = currentwordaddr + 64;
        if (version == 1) {
            synchronized (datafile) {
                for (i = 0; i < 32; i++) {
                    temp = (byte) getData(offset, 1);
                    if (temp != 0x00) {
                        if (temp == 0x20) {
                            if (is0x20end) {
                                is0x20end = false;
                                break;
                            } else {
                                is0x20end = true;
                            }
                        } else {
                            is0x20end = false;
                        }
                        ch[i] = temp;
                        offset += 1;
                    } else {
                        break;
                    }
                }
            }

            try {
                word.expl = new String(ch, 0, i, "GBK");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (version == 2) {
            synchronized (datafile) {
                for (i = 0; i < 64; i++) {
                    temp = (byte) getData(offset, 1);
                    if (temp != 0x00) {
                        if (temp == 0x20) {
                            if (is0x20end) {
                                is0x20end = false;
                                break;
                            } else {
                                is0x20end = true;
                            }
                        } else {
                            is0x20end = false;
                        }
                        ch[i] = temp;
                        offset += 1;
                    } else {
                        break;
                    }
                }
            }

            try {
                word.expl = new String(ch, 0, i, "GBK");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ;
        }
        int tempAddr = (int) soundaddr + (wordnum - 1) * 4;
        synchronized (datafile) {
            word.vocadd = (int) getData(tempAddr, 4);
            word.voclen = (int) getData(tempAddr + 4, 4) - word.vocadd;
        }
        getInstance(word);
        int b = word.word.length();
        word.letaddr = new int[b];
        word.letlen = new int[b];
        for (int a = 0; a < b; a++) {
            setletaddr(a, word);
        }

        return word;
    }


    /**
     * 根据单词序号得到中英文例句
     */
    public void getInstance(Word word) {
        int initAddr = 0, offset = 0, enLen;
        int n;
        byte temp;
        byte[] en = new byte[1024];
        byte[] ch = new byte[1024];


//        if(word.index==360){
//            Log.e("WordData","word.index >= wordcount");
//        }

        if (word.index > wordcount) {
//            Log.e("WordData","word.index >= wordcount");
            return;
        }
        if (addedaddr == 0) {
            return;
        }
        offset = (int) (addedaddr + (word.index - 1) * 4);
        initAddr = (int) getData(offset, 4);
        initAddr += addedaddr;
        for (n = 0; n < 1024; n++) {
            temp = (byte) getData(initAddr + n, 1);
            en[n] = temp;
            if (temp == 0) {
                break;
            }
            if (n > 1023) {    //最大长度
                break;
            }
        }
        try {
            word.exampen = new String(en, 0, n, "gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        enLen = n;
//		System.out.println("====currentWordEngExample===="+currentWordEngExample);

        for (n = 0; ; n++) {
            temp = (byte) getData(initAddr + 1 + enLen + n, 1);
            ch[n] = temp;
            if (temp == 0) {
                break;
            }
            if (n > 1024) {    //最大长度
                break;
            }
        }
        try {
            word.exampcn = new String(ch, 0, n, "gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		System.out.println("====currentWordChiExample===="+currentWordChiExample);
    }

    /**
     * ****************
     * 读取当前单词第n个字母，并发音*
     * **************************
     */
    public void setletaddr(int n, Word word) {
        char letter = 0;
        int addr1 = 0, addr2 = 0, offset;
        letter = word.word.charAt(n);
        if (letter >= 0x61 && letter <= 0x7a) {
            letter -= 0x61;
        } else if (letter >= 0x41 && letter <= 0x5a) {
            letter -= 0x41;
        } else {
            letter = (char) -1;
        }
        if (letter != -1) {
            offset = letter * 4;
            addr1 = (int) getNoEncryptData(letterfile, offset);
            addr2 = (int) getNoEncryptData(letterfile, offset + 4);

            if (addr2 > addr1) {
                word.letaddr[n] = addr1;
                word.letlen[n] = addr2 - addr1;
            }
        }

    }


    //在dateOffset处读getNum个bytes，并解密,返回解密后的数据
    public long getData(long dateOffset, int getNum) {
        long data = 0;

        byte[] b = new byte[4];
        byte[] kb = new byte[4];
        long key = 0;
        if (datafile == null) {
//            SelectWord.data=new Data();
            return 0;
        }

        synchronized (datafile) {
            if (getNum > 1) {
                try {

                    datafile.seek(dateOffset);
                    int i = datafile.read(b, 0, getNum);
                    keyfile.seek(dateOffset % keyfilelen);
                    keyfile.read(kb, 0, getNum);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ArithmeticException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }
            }

            switch (getNum) {
                case 4:
                    data = byte4ToInt(b);
                    key = byte4ToInt(kb);
                    break;
                case 3:
                    data = byte3ToInt(b);
                    key = byte3ToInt(kb);
                    break;
                case 2:

                    data = byte2ToInt(b);
                    key = byte2ToInt(kb);
                    break;
                case 1:
                    //data = byte1ToInt(b);
                    //key = byte1ToInt(kb);
                    try {
                        datafile.seek(dateOffset);
                        data = datafile.read();
                        keyfile.seek(dateOffset % keyfilelen
                        );
                        key = keyfile.read();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
            data ^= key;
        }

        return data;
    }


    //在dataOffset处读取4位，没有加密的数据
    public long getNoEncryptData(RandomAccessFile dataFile, long dateOffset) {
        byte[] b = new byte[4];
        try {
            dataFile.seek(dateOffset);
            dataFile.read(b, 0, 4);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		fileOffset += 4; 
        long data = byte4ToInt(b);
        return data;
    }

    //将四字节二进制转换成整型数据
    public static int byte4ToInt(byte[] b) {
        int data = (b[3] << 24 & 0xff000000) | (b[2] << 16 & 0xff0000) | (b[1] << 8 & 0xff00)
                | (b[0] & 0xff);
        return data;
    }

    // 将三字节二进制转换成整型数据
    public static int byte3ToInt(byte[] b) {
        return (b[2] << 16 & 0xff0000) | (b[1] << 8 & 0xff00) | (b[0] & 0xff);
    }

    // 将两字节二进制转换成整型数据
    public static int byte2ToInt(byte[] b) {
        return (b[1] << 8 & 0xff00) | (b[0] & 0xff);
    }

    // 将一字节二进制转换成整型数据
    public static int byte1ToInt(byte[] b) {
        return b[0] & 0xff;
    }
}
