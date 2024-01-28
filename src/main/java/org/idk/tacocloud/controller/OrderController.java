package org.idk.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.idk.tacocloud.entity.TacoOrder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
@Slf4j
public class OrderController {
    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping("/process-data")
    public String processData (
            @Valid TacoOrder order,
            BindingResult bindingResult,
            SessionStatus status
            ) {
        if (bindingResult.hasErrors()) {
            return "orderForm";
        }
        log.info("Processing oder {} : " + order);
        status.setComplete();
        return "redirect:/";
    }
}
