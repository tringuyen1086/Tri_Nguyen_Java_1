package com.trilogyed.gamestoreinvoicing.util.feign;

import com.trilogyed.gamestoreinvoicing.model.Console;
import com.trilogyed.gamestoreinvoicing.model.Game;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import com.trilogyed.gamestoreinvoicing.viewModel.GameViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.TShirtViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name ="gamestore-catalog")
public interface GameStoreCatalogClient {

    //console
    @RequestMapping(value="/console",method = RequestMethod.GET)
    public List<Console> getAllConsoles();

    @RequestMapping(value="/console/{id}",method = RequestMethod.GET)
    public Console getConsole(@PathVariable("id") long id);

    @RequestMapping(value="/console",method = RequestMethod.POST)
    public Console createConsole(Console console);

    @RequestMapping(value="/console/{id}",method = RequestMethod.PUT)
    public List<Console> updateConsole(Console console);

    @RequestMapping(value="/console/{id}",method = RequestMethod.DELETE)
    public List<Console> deleteConsole(@PathVariable("id") long id);

    @RequestMapping(value = "/console/manufacturer/{manufacturer}", method = RequestMethod.GET)
    public List<Console> getConsoleByManufacturer(@PathVariable String manufacturer);


    //game

    @RequestMapping(value="/game",method = RequestMethod.GET)
    public List<Game> getAllGames();

    @RequestMapping(value="/game/{id}",method = RequestMethod.GET)
    public Game getGame(@PathVariable("id") long id);

    @RequestMapping(value="/game",method = RequestMethod.POST)
    public Game createGame(Game game);

    @RequestMapping(value="/game/{id}",method = RequestMethod.PUT)
    public List<Game> updateGame(Game game);

    @RequestMapping(value="/game/{id}",method = RequestMethod.DELETE)
    public List<Game> deleteGame(@PathVariable("id") long id);

    @RequestMapping(value = "/game/esrbRating/{esrbRating}", method = RequestMethod.GET)
    public List<Game> getGameByEsrbRating(@PathVariable String esrbRating);

    @RequestMapping(value = "/game/title/{title}", method = RequestMethod.GET)
    public List<Game> getGameByTitle(@PathVariable String title);

    @RequestMapping(value = "/game/studio/{studio}", method = RequestMethod.GET)
    public List<Game> getGameByStudio(@PathVariable String studio);


    //Tshirt

    @RequestMapping(value="/tshirt",method = RequestMethod.GET)
    public List<TShirt> getAllTShirts();

    @RequestMapping(value="/tshirt/{id}",method = RequestMethod.GET)
    public TShirt getTShirt(@PathVariable("id") long id);

    @RequestMapping(value="/tshirt",method = RequestMethod.POST)
    public TShirt createTShirt(@RequestBody TShirt tshirt);

    @RequestMapping(value="/tshirt/{id}",method = RequestMethod.PUT)
    public List<TShirt> updateTShirt(TShirt tshirt);

    @RequestMapping(value="/tshirt/{id}",method = RequestMethod.DELETE)
    public List<TShirt> deleteTShirt(@PathVariable("id") long id);

    @RequestMapping(value = "/tshirt/color/{color}", method = RequestMethod.GET)
    public List<TShirt> getTShirtByColour(@PathVariable String colour);

    @RequestMapping(value = "/size/{size}", method = RequestMethod.GET)
    public List<TShirt> getTShirtBySize(@PathVariable String size);

}
