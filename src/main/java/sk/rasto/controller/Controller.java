package sk.rasto.controller;


import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.rasto.ExchangeRate;

import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class Controller {
    public Controller(){
    }

    @GetMapping ("/api")
    @ResponseBody
    public String field(@RequestParam(name = "id") String id,@RequestParam(name = "currencies", required = false) List<String> currencies) {

        //TODO change check toward database of valid credentials, current implementation as hardcoded dummy
        if(!Objects.equals(id, "testCredential"))return null;


        ExchangeRate ex = new ExchangeRate();
        return ex.getRates(currencies);
    }

}


