package ys.rg.fourClass.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ys.rg.fourClass.dto.Result;
import ys.rg.fourClass.service.SpringTxtFileReaderService;
import ys.rg.fourClass.util.AddressSequenceValidator;

import javax.annotation.Resource;
import java.io.IOException;

import static ys.rg.fourClass.util.AddressSequenceGenerator.generateRandomAddressSeq;

/**
 * 系统参数管理
 */
@RestController
@RequestMapping("/params")
public class ParamsController {

    @Resource
    private SpringTxtFileReaderService springTxtFileReaderService;

    /**
     * 获取默认系统参数
     * @return 默认系统参数
     */
    @GetMapping("/findDefault")
    public Result findDefault(){
        int[] a= {0,0,0};
        return Result.ok(a);
    }

    /**
     * 读取文件参数并校验合法性
     * @param txtPath 文件地址
     * @return 文件参数
     */
    @PostMapping("/readTxt")
    public Result readTxt(String txtPath) throws IOException {
        //校验地址正则
        if(!AddressSequenceValidator.validateWithMsg(txtPath).isEmpty()){
            return Result.fail(AddressSequenceValidator.validateWithMsg(txtPath));
        }
        return Result.ok(springTxtFileReaderService.readTxtByResourceLoader(txtPath));
    }


    /**
    * 随机生成逻辑地址序列
    * @return 文件参数
    */
    @GetMapping("/RandomHexString")
    public Result randomHexString(){
        return Result.ok(generateRandomAddressSeq());
    }


}
