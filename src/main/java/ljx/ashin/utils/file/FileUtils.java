package ljx.ashin.utils.file;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具
 * Created by Ashin Liang on 2018/1/10.
 */
public class FileUtils {
    /**
     * 复制文件
     * @param resFilePath 源文件路径
     * @param tarFilePath 目标文件路径
     * @return
     */
    public static boolean copyFile(String resFilePath,String tarFilePath){
        //是否复制成功的标识
        boolean isSuccess = false;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (StringUtils.isNotBlank(resFilePath)&&StringUtils.isNotBlank(tarFilePath)){
                File srcFile = new File(resFilePath);
                if (!srcFile.exists()){
                    System.out.println("源文件不存在");
                    return false;
                }
                File tarFile = new File(tarFilePath);
                if (!tarFile.exists()){//目标文件不存在则创建目标文件
                    tarFile.createNewFile();
                }
                fileInputStream = new FileInputStream(srcFile);
                fileOutputStream = new FileOutputStream(tarFile);

                int c = fileInputStream.read();
                while (c!=-1){
                    fileOutputStream.write(c);
                    c = fileInputStream.read();
                }
                isSuccess = true;
            }else {
                System.out.println("源文件或者目标文件路径路径不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static void main(String[] args) {
        String srcFilePath = "F:\\zhou_trade.sql";
        String tarFilePath = "F:\\as.sql";
        System.out.println(FileUtils.copyFile(srcFilePath,tarFilePath));
    }
}
