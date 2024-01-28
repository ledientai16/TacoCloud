package org.idk.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.idk.tacocloud.entity.Ingredient;
import org.idk.tacocloud.entity.Ingredient.Type;
import org.idk.tacocloud.entity.Taco;
import org.idk.tacocloud.entity.TacoOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    // ModelAttribute use to add data to model
    @ModelAttribute
    public void addIngredientToModel(Model model){
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

    }

    @ModelAttribute("taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute("tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        System.out.println("taco : " + model.getAttribute("taco"));
        System.out.println("tacoOrder : " + model.getAttribute("tacoOrder"));

        return "design";
    }
//    @PostMapping("/save-taco")
//    public String saveTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
//        tacoOrder.addTaco(taco);
//        log.info("Processing toco {} : " + taco);
//        return "redirect:/orders/current";
//
//    }
    @PostMapping("/save-taco")
    public String processTaco(
            @Valid Taco taco, Errors errors,
            @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }
    public List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {

        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
