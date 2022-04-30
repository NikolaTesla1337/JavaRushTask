package com.game.controller;
import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {
    @Autowired
    private  PlayerService playerService;

    public RestController(PlayerService playerService) {
        this.playerService = playerService;
    }

    //Get player list with params
    @GetMapping("/players")
    public @ResponseBody ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> params){
        return params.isEmpty()
                ? new ResponseEntity<>(playerService.findAll(PageRequest.of(0, 3)), HttpStatus.OK)
                : new ResponseEntity<>(playerService.getPlayersList(params), HttpStatus.OK);
    }


  //Update player
  @PostMapping("/players/{id}")
  public @ResponseBody ResponseEntity<Player> updatePlayer(@PathVariable(name = "id") Long id,
                                                           @RequestBody Map<String, String> params){
      if (!playerService.isIdValid(id) || !playerService.isParamsValid(params)) {
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      Player result = playerService.updatePlayer(id, params);
      if (result == null) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else {
          return new ResponseEntity<>(result, HttpStatus.OK);
      }

  }
  //Create new player
   @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        try {
            playerService.createPlayer(player);
            return new ResponseEntity<>(player,HttpStatus.OK);
        }
        catch (Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}




   }


  //Get count players
    @GetMapping("/players/count")
    public @ResponseBody Integer getPlayersCount(@RequestParam Map<String, String> params){
        return params.isEmpty()
                ? playerService.getCount()
                : playerService.getPlayersCount(params);
    }

  //Find players by ID
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> findPlayerbyID(@PathVariable(value = "id") Long id){
        try {
            Player player = playerService.findById(id);
            return new ResponseEntity<>(player, HttpStatus.OK);
        }
        catch (NoSuchElementException e){
            if (id == 0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

  //Delete player by id
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable(value = "id") long id) {
        try {
            Player player = playerService.findById(id);
             playerService.delete(player);
             return new ResponseEntity<>(player,HttpStatus.OK);
        }
        catch (NoSuchElementException e){
            if (id == 0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }
}
