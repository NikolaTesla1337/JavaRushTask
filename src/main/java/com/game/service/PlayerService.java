package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.Player_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerService {
    @Autowired
    Player_Repository repository;

    public Player updatePlayer(Long id,Map<String, String> params){
        // проверка условия валидности игрока
        if (!repository.findById(id).isPresent() || params == null) return null;
        Player updatePlayer = repository.findById(id).get();

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Race race = params.containsKey("race")
                ? Race.valueOf(params.get("race"))
                : null;
        Profession profession = params.containsKey("profession")
                ? Profession.valueOf(params.get("profession"))
                : null;
        Date birthday = params.containsKey("birthday")
                ? new Date(Long.parseLong(params.get("birthday")))
                : null;
        Boolean banned = params.containsKey("banned")
                ? Boolean.parseBoolean(params.get("banned"))
                : null;
        Integer experience = params.containsKey("experience")
                ? Integer.parseInt(params.get("experience"))
                : null;

        if (name != null) updatePlayer.setName(name);
        if (title != null) updatePlayer.setTitle(title);
        if (race != null) updatePlayer.setRace(race);
        if (profession != null) updatePlayer.setProfession(profession);
        if (birthday != null) updatePlayer.setBirthDay(birthday);
        if (banned != null) updatePlayer.setBanned(banned);
        if (experience != null) updatePlayer.setExperience(experience);

        updatePlayer.setLevel(updatePlayer.getExperience());
        updatePlayer.setUntilNextLevel(updatePlayer.getExperience());
        repository.saveAndFlush(updatePlayer);
        return updatePlayer;
    }

    public List<Player> findAll(Pageable pageable){
        return repository.findAll(pageable).stream().collect(Collectors.toList());
    }

    public List<Player> getPlayersList(Map<String, String> params) {

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Race race = params.containsKey("race")
                ? Race.valueOf(params.get("race"))
                : null;
        Profession profession = params.containsKey("profession")
                ? Profession.valueOf(params.get("profession"))
                : null;
        Date after = params.containsKey("after")
                ? new Date(Long.parseLong(params.get("after")))
                : null;
        Date before = params.containsKey("before")
                ? new Date(Long.parseLong(params.get("before")))
                : null;
        Boolean banned = params.containsKey("banned")
                ? Boolean.parseBoolean(params.get("banned"))
                : null;
        Integer minExperience = params.containsKey("minExperience")
                ? Integer.parseInt(params.get("minExperience"))
                : null;
        Integer maxExperience = params.containsKey("maxExperience")
                ? Integer.parseInt(params.get("maxExperience"))
                : null;
        Integer minLevel = params.containsKey("minLevel")
                ? Integer.parseInt(params.get("minLevel"))
                : null;
        Integer maxLevel = params.containsKey("maxLevel")
                ? Integer.parseInt(params.get("maxLevel"))
                : null;

        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

        Pageable pageable = params.containsKey("order")
                ? PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, PlayerOrder.valueOf(params.get("order")).getFieldName())
                : PageRequest.of(pageNumber, pageSize);

        return repository.getAllByParams(name, title, race, profession, after,
                before, banned, minExperience, maxExperience, minLevel, maxLevel,
                pageable).stream().collect(Collectors.toList());}
    public void savePlayer(Player player){
        validateName(player.getName());
        validateTitle(player.getTitle());
        validateExperience(player.getExperience());
        validateDate(player.getBirthDay());
        repository.save(player);
    }

    public Player findById(Long id){
        return repository.findById(id).get();
    }

    public void delete(Player player){
        repository.delete(player);
    }
    public Integer getCount(){
        return  (int) repository.count();
    }
    public Integer getPlayersCount(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Race race = params.containsKey("race")
                ? Race.valueOf(params.get("race"))
                : null;
        Profession profession = params.containsKey("profession")
                ? Profession.valueOf(params.get("profession"))
                : null;
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned")
                ? Boolean.parseBoolean(params.get("banned"))
                : null;
        Integer minExperience = params.containsKey("minExperience")
                ? Integer.parseInt(params.get("minExperience"))
                : null;
        Integer maxExperience = params.containsKey("maxExperience")
                ? Integer.parseInt(params.get("maxExperience"))
                : null;
        Integer minLevel = params.containsKey("minLevel")
                ? Integer.parseInt(params.get("minLevel"))
                : null;
        Integer maxLevel = params.containsKey("maxLevel")
                ? Integer.parseInt(params.get("maxLevel"))
                : null;
        return repository.countByParams(name, title, race, profession, after,
                before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    public void validateName(String name){
        if (name == null || name.isEmpty() || name.length() > 12)
            throw new RuntimeException();
    }
    public void validateTitle(String title){
        if (title == null || title.isEmpty() || title.length() > 30)
            throw new RuntimeException();
    }
    public void validateExperience(Integer experience){
        if (experience > 10000000 || experience < 0)
            throw new RuntimeException();
    }

    public void validateDate(Date birthday){
        if(birthday.getTime() < 0){
            throw new RuntimeException();
        }
    }

    public boolean isParamsValid(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);

        Integer birthDate = null;
        if (params.containsKey("birthday")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(params.get("birthday")));
            birthDate = calendar.get(Calendar.YEAR);
        }

        Integer experience = params.containsKey("experience")
                ? Integer.parseInt(params.get("experience"))
                : null;

        boolean resultValid =
                (name == null || title == null || name.length() <= 12 && name.length() > 0 &&
                        title.length() <= 30 && title.length() > 0)
                        && (birthDate == null || birthDate >= 2000 && birthDate <= 3000)
                        && (experience == null || experience >= 0 && experience <= 10_000_000);
        try {
            if (params.containsKey("race") && params.containsKey("profession")){
                Profession profession = Profession.valueOf(params.get("profession"));
                Race race = Race.valueOf(params.get("race"));
            }
        } catch (IllegalArgumentException | NullPointerException exception){
            resultValid = false;
        }
        return resultValid;
    }
    public boolean isIdValid(Long id) {
            return Math.ceil(id) == id && id > 0;
        }
    public void createPlayer(Player player){
        player.setLevel(player.getExperience());
        player.setUntilNextLevel(player.getExperience());
        savePlayer(player);
    }
}
