package ys.rg.fourClass.service;
import java.io.IOException;

public interface SpringTxtFileReaderService {

    /**
     * 读取指定路径的本地TXT文件
     * @param filePath 本地文件绝对路径（如D:/test.txt）
     * @return 文件内容
     * @throws IOException 读取文件异常（文件不存在/权限不足等）
     */
    String readTxtByResourceLoader(String filePath) throws IOException;


}
