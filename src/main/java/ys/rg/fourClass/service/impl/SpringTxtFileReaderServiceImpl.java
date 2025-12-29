package ys.rg.fourClass.service.impl;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ys.rg.fourClass.service.SpringTxtFileReaderService;
import ys.rg.fourClass.util.AddressSequenceValidator;
import ys.rg.fourClass.util.TxtPathValidator;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class SpringTxtFileReaderServiceImpl implements SpringTxtFileReaderService {
    @Resource
    private ResourceLoader resourceLoader;

    @Override
    public String readTxtByResourceLoader(String filePath) throws IOException {
        //校验地址正则
        if(!TxtPathValidator.validateWithMsg1(filePath).isEmpty()){
            return TxtPathValidator.validateWithMsg1(filePath);
        }

        // 获取Resource对象
        org.springframework.core.io.Resource resource = resourceLoader.getResource("file:" + filePath);

        // 校验文件是否存在
        if (!resource.exists()) {
            throw new IOException("文件不存在，路径：" + filePath);
        }

        // 读取文件
        String content = getString(filePath, resource);
        if (!content.isEmpty()) {
            content = content.substring(0, content.length() - System.lineSeparator().length());
        }
        return content;
    }

    private static String getString(String filePath, org.springframework.core.io.Resource resource) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new IOException("读取文件失败，路径：" + filePath, e);
        }

        return contentBuilder.toString();
    }

}
