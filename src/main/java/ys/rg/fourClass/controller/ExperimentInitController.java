package ys.rg.fourClass.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ys.rg.fourClass.dto.ExperimentInitDTO;
import ys.rg.fourClass.service.ExperimentInitService;
import ys.rg.fourClass.util.AddressSequenceValidator;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：接收前端初始化参数的HTTP接口，返回自增experimentId
 * 用法：前端POST /experiment/init，传ExperimentInitDTO参数，完成初始化
 */
@RestController
@RequestMapping("/experiment")
public class ExperimentInitController {

    @Resource
    private ExperimentInitService experimentInitService;

    @PostMapping("/init")
    public Map<String, Object> initExperiment(@Valid @RequestBody ExperimentInitDTO initDTO) {

        String boo=AddressSequenceValidator.validateWithMsg(initDTO.getLogicalAddressSequence());
        Map<String, Object> resultMap = new HashMap<>();
        if(boo==null)
        {
            Integer experimentId = experimentInitService.initAndSaveToDb(initDTO);
            resultMap.put("experimentId", experimentId);
            resultMap.put("msg", "参数初始化成功，已存入数据库");
        }
        else
        {
            resultMap.put("experimentId", 0);
            resultMap.put("msg", "参数初始化失败,"+boo);
        }
        return resultMap;
    }
}
