package com.cm.controller;

import com.cm.utils.AjaxObj;
import com.cm.utils.ListToStringTest;
import com.cm.utils.ReturnValCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    // 新增数据并更新缓存
    @GetMapping("/toString")
    public AjaxObj forSatoStringve(){
        List list=new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        String s = ListToStringTest.listToString1(list, '\n');
        System.out.println("s====="+s);
        return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "请求成功");
    }
}
