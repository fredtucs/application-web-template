package org.wifry.fooddelivery.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wifry.fooddelivery.model.Banco;
import org.wifry.fooddelivery.services.admin.BancoService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wtuco on 13/06/2016.
 * <p>
 * AjaxController
 */
@Controller
@RequestMapping("/json")
public class AjaxController {

    @Autowired
    private BancoService bancoService;

    @RequestMapping(value = "variable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getShopInJSON() {
        return bancoService.list().stream().map(Banco::getNombre).collect(Collectors.toList());
    }

}
