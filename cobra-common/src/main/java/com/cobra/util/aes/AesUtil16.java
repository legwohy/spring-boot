package com.cobra.util.aes;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

/**
 * 自定义AES加密
 * @see Cipher#init(int, Key)
 * @see Cipher#init(int, Key, AlgorithmParameterSpec)
 */
@Slf4j
public class AesUtil16
{
    public static void main(String[] args) throws Exception{

        String src = "xiao明";
        String key = "1234567890123456";
        String ivs = "0000000000000000";
        String pwd = AesUtil16.encrypt(src, key, ivs);
        System.out.println("pwd:" + pwd);

        String unpwd = AesUtil16.decrypt(pwd, key, ivs);
        System.out.println("unpwd:" + unpwd);
    }

    /**
     *
     * @param sSrc 待加密的文件
     * @param sKey 加密的key 16位
     * @param ivs 初始向量 默认16个0
     * @return 加密后的值
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey, String ivs) throws Exception
    {
        try
        {
            // 算法/模式/填充
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = sSrc.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0)
            {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            cipher.init(Cipher.ENCRYPT_MODE, // 加密
                            new SecretKeySpec(sKey.getBytes(), "AES"),// key
                            new IvParameterSpec(ivs.getBytes()));// 向量
            byte[] encrypted = cipher.doFinal(plaintext);
            return new Base64().encode(encrypted);
        }
        catch (Exception e)
        {
            log.error("AES加密失败:{}", e);
            return null;
        }
    }

    /**
     * 解密
     * @param sSrc 加密的文件
     * @param sKey 解密的key 16位 与加密的参数一致
     * @param ivs 初始向量 与加密参数保持一致
     * @return 解密后的文件
     */
    public static String decrypt(String sSrc, String sKey, String ivs)
    {
        try
        {
            if (sKey == null)
            {
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE,// 解密
                            new SecretKeySpec(sKey.getBytes("utf-8"), "AES"),// key
                            new IvParameterSpec(ivs.getBytes("utf-8")));// 向量(算法参数)

            byte[] encrypted = new Base64().decode(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original);
            return originalString;
        }
        catch (Exception ex)
        {
            log.error("AES解密失败:{}", ex);
            return null;
        }
    }

    public static String hex(char ch)
    {
        return Integer.toHexString(ch).toLowerCase(Locale.ENGLISH);
    }

    /**
     * 设置报文编码
     */
    public static String gbEncoding(String str)
    {
        StringWriter out = new StringWriter(str.length() * 2);
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            // handle unicode
            if (ch > 0xfff)
            {
                out.write("\\u" + hex(ch));
            }
            else if (ch > 0xff)
            {
                out.write("\\u0" + hex(ch));
            }
            else if (ch > 0x7f)
            {
                out.write("\\u00" + hex(ch));
            }
            else if (ch < 32)
            {
                switch (ch)
                {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default:
                        if (ch > 0xf)
                        {
                            out.write("\\u00" + hex(ch));
                        }
                        else
                        {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            }
            else
            {
                switch (ch)
                {
                    case '\'':
                        out.write('\'');
                        break;
                    case '"':
                        //out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    case '/':
                        out.write('/');
                        break;
                    default:
                        out.write(ch);
                        break;
                }
            }
        }
        return out.toString();
    }
}

class Base64
{
    private static final char S_BASE64CHAR[] = {'A', 'B', 'C', 'D', 'E', 'F',
                    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                    'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                    't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', '+', '/'};
    //	private static final char S_BASE64PAD = 61;
    private static final byte S_DECODETABLE[];

    static
    {
        S_DECODETABLE = new byte[128];
        for (int i = 0; i < S_DECODETABLE.length; i++)
        {
            S_DECODETABLE[i] = 127;
        }

        for (int i = 0; i < S_BASE64CHAR.length; i++)
        {
            S_DECODETABLE[S_BASE64CHAR[i]] = (byte)i;
        }

    }

    /**
     * @param ibuf
     * @param obuf
     * @param wp
     * @return
     */
    private static int decode0(char ibuf[], byte obuf[], int wp)
    {
        int outlen = 3;
        if (ibuf[3] == '=')
        {
            outlen = 2;
        }
        if (ibuf[2] == '=')
        {
            outlen = 1;
        }
        int b0 = S_DECODETABLE[ibuf[0]];
        int b1 = S_DECODETABLE[ibuf[1]];
        int b2 = S_DECODETABLE[ibuf[2]];
        int b3 = S_DECODETABLE[ibuf[3]];
        switch (outlen)
        {
            case 1: // '\001'
                obuf[wp] = (byte)(b0 << 2 & 252 | b1 >> 4 & 3);
                return 1;

            case 2: // '\002'
                obuf[wp++] = (byte)(b0 << 2 & 252 | b1 >> 4 & 3);
                obuf[wp] = (byte)(b1 << 4 & 240 | b2 >> 2 & 15);
                return 2;

            case 3: // '\003'
                obuf[wp++] = (byte)(b0 << 2 & 252 | b1 >> 4 & 3);
                obuf[wp++] = (byte)(b1 << 4 & 240 | b2 >> 2 & 15);
                obuf[wp] = (byte)(b2 << 6 & 192 | b3 & 63);
                return 3;
            default:
        }

        throw new RuntimeException("Internal error");
    }

    /**
     *
     * @param data
     * @param off
     * @param len
     * @return
     */
    public static byte[] decode(char data[], int off, int len)
    {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[(len / 4) * 3 + 3];
        int obufcount = 0;
        for (int i = off; i < off + len; i++)
        {
            char ch = data[i];
            if (ch != '='
                            && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
            {
                continue;
            }
            ibuf[ibufcount++] = ch;
            if (ibufcount == ibuf.length)
            {
                ibufcount = 0;
                obufcount += decode0(ibuf, obuf, obufcount);
            }
        }

        if (obufcount == obuf.length)
        {
            return obuf;
        }
        else
        {
            byte ret[] = new byte[obufcount];
            System.arraycopy(obuf, 0, ret, 0, obufcount);
            return ret;
        }
    }

    /**
     *
     * @param data
     * @return
     */
    public static byte[] decode(String data)
    {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[(data.length() / 4) * 3 + 3];
        int obufcount = 0;
        for (int i = 0; i < data.length(); i++)
        {
            char ch = data.charAt(i);
            if (ch != '='
                            && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
            {
                continue;
            }
            ibuf[ibufcount++] = ch;
            if (ibufcount == ibuf.length)
            {
                ibufcount = 0;
                obufcount += decode0(ibuf, obuf, obufcount);
            }
        }

        if (obufcount == obuf.length)
        {
            return obuf;
        }
        else
        {
            byte ret[] = new byte[obufcount];
            System.arraycopy(obuf, 0, ret, 0, obufcount);
            return ret;
        }
    }

    /**
     *
     * @param data
     * @param off
     * @param len
     * @param ostream
     * @throws IOException
     */
    public static void decode(char data[], int off, int len,
                    OutputStream ostream) throws IOException
    {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[3];
        for (int i = off; i < off + len; i++)
        {
            char ch = data[i];
            if (ch != '='
                            && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
            {
                continue;
            }
            ibuf[ibufcount++] = ch;
            if (ibufcount == ibuf.length)
            {
                ibufcount = 0;
                int obufcount = decode0(ibuf, obuf, 0);
                ostream.write(obuf, 0, obufcount);
            }
        }

    }

    /**
     *
     * @param data
     * @param ostream
     * @throws IOException
     */
    public static void decode(String data, OutputStream ostream)
                    throws IOException
    {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[3];
        for (int i = 0; i < data.length(); i++)
        {
            char ch = data.charAt(i);
            if (ch != '='
                            && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
            {
                continue;
            }
            ibuf[ibufcount++] = ch;
            if (ibufcount == ibuf.length)
            {
                ibufcount = 0;
                int obufcount = decode0(ibuf, obuf, 0);
                ostream.write(obuf, 0, obufcount);
            }
        }

    }

    /**
     *
     * @param data
     * @return
     */
    public static String encode(byte data[])
    {
        return encode(data, 0, data.length);
    }

    /**
     *
     * @param data
     * @param off
     * @param len
     * @return
     */
    public static String encode(byte data[], int off, int len)
    {
        if (len <= 0)
        {
            return "";
        }
        char out[] = new char[(len / 3) * 4 + 4];
        int rindex = off;
        int windex = 0;
        int rest;
        for (rest = len - off; rest >= 3; rest -= 3)
        {
            int i = ((data[rindex] & 255) << 16)
                            + ((data[rindex + 1] & 255) << 8)
                            + (data[rindex + 2] & 255);
            out[windex++] = S_BASE64CHAR[i >> 18];
            out[windex++] = S_BASE64CHAR[i >> 12 & 63];
            out[windex++] = S_BASE64CHAR[i >> 6 & 63];
            out[windex++] = S_BASE64CHAR[i & 63];
            rindex += 3;
        }

        if (rest == 1)
        {
            int i = data[rindex] & 255;
            out[windex++] = S_BASE64CHAR[i >> 2];
            out[windex++] = S_BASE64CHAR[i << 4 & 63];
            out[windex++] = '=';
            out[windex++] = '=';
        }
        else if (rest == 2)
        {
            int i = ((data[rindex] & 255) << 8) + (data[rindex + 1] & 255);
            out[windex++] = S_BASE64CHAR[i >> 10];
            out[windex++] = S_BASE64CHAR[i >> 4 & 63];
            out[windex++] = S_BASE64CHAR[i << 2 & 63];
            out[windex++] = '=';
        }
        return new String(out, 0, windex);
    }

    /**
     *
     * @param data
     * @param off
     * @param len
     * @param ostream
     * @throws IOException
     */
    public static void encode(byte data[], int off, int len,
                    OutputStream ostream) throws IOException
    {
        if (len <= 0)
        {
            return;
        }
        byte out[] = new byte[4];
        int rindex = off;
        int rest;
        for (rest = len - off; rest >= 3; rest -= 3)
        {
            int i = ((data[rindex] & 255) << 16)
                            + ((data[rindex + 1] & 255) << 8)
                            + (data[rindex + 2] & 255);
            out[0] = (byte)S_BASE64CHAR[i >> 18];
            out[1] = (byte)S_BASE64CHAR[i >> 12 & 63];
            out[2] = (byte)S_BASE64CHAR[i >> 6 & 63];
            out[3] = (byte)S_BASE64CHAR[i & 63];
            ostream.write(out, 0, 4);
            rindex += 3;
        }

        if (rest == 1)
        {
            int i = data[rindex] & 255;
            out[0] = (byte)S_BASE64CHAR[i >> 2];
            out[1] = (byte)S_BASE64CHAR[i << 4 & 63];
            out[2] = 61;
            out[3] = 61;
            ostream.write(out, 0, 4);
        }
        else if (rest == 2)
        {
            int i = ((data[rindex] & 255) << 8) + (data[rindex + 1] & 255);
            out[0] = (byte)S_BASE64CHAR[i >> 10];
            out[1] = (byte)S_BASE64CHAR[i >> 4 & 63];
            out[2] = (byte)S_BASE64CHAR[i << 2 & 63];
            out[3] = 61;
            ostream.write(out, 0, 4);
        }
    }

    /**
     *
     * @param data
     * @param off
     * @param len
     * @param writer
     * @throws IOException
     */
    public static void encode(byte data[], int off, int len, Writer writer)
                    throws IOException
    {
        if (len <= 0)
        {
            return;
        }
        char out[] = new char[4];
        int rindex = off;
        int rest = len - off;
        int output = 0;
        do
        {
            if (rest < 3)
            {
                break;
            }
            int i = ((data[rindex] & 255) << 16)
                            + ((data[rindex + 1] & 255) << 8)
                            + (data[rindex + 2] & 255);
            out[0] = S_BASE64CHAR[i >> 18];
            out[1] = S_BASE64CHAR[i >> 12 & 63];
            out[2] = S_BASE64CHAR[i >> 6 & 63];
            out[3] = S_BASE64CHAR[i & 63];
            writer.write(out, 0, 4);
            rindex += 3;
            rest -= 3;
            if ((output += 4) % 76 == 0)
            {
                writer.write("\n");
            }
        } while (true);
        if (rest == 1)
        {
            int i = data[rindex] & 255;
            out[0] = S_BASE64CHAR[i >> 2];
            out[1] = S_BASE64CHAR[i << 4 & 63];
            out[2] = '=';
            out[3] = '=';
            writer.write(out, 0, 4);
        }
        else if (rest == 2)
        {
            int i = ((data[rindex] & 255) << 8) + (data[rindex + 1] & 255);
            out[0] = S_BASE64CHAR[i >> 10];
            out[1] = S_BASE64CHAR[i >> 4 & 63];
            out[2] = S_BASE64CHAR[i << 2 & 63];
            out[3] = '=';
            writer.write(out, 0, 4);
        }
    }

}
