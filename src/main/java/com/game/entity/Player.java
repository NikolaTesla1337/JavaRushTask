package com.game.entity;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;


@Entity
@Table(name = "player")
public class Player  {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="name")
    private String name;
    @Column(name = "title")
    private String title;
    @Column(name = "race")
    @Enumerated(value = EnumType.STRING)
    private Race race;
    @Column(name = "profession")
    @Enumerated(value = EnumType.STRING)
    private Profession profession;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "level")
    private Integer level;
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "banned")
    private boolean banned;


    public Player() {

    }
    public Player(Long id,String name, String title, Race race, Profession profession, Date birthday, boolean banned, Integer experience ){
    this.name = name;
    this.id = id;
    this.title = title;
    this.race = race;
    this.profession = profession;
    this.experience = experience;
    this.birthday = birthday;
    this.banned = banned;
    this.level = levelCalculate(experience);
    this.untilNextLevel = nextLevelCalc(levelCalculate(experience),experience);
    }
    public Player(Long id, String name, String title, Race race, Profession profession, java.util.Date birthday, Integer experience){
        this.name = name;
        this.id = id;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.birthday = birthday;
        this.level = levelCalculate(experience);
        this.untilNextLevel = nextLevelCalc(levelCalculate(experience),experience);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer experience) {
        this.level = levelCalculate(experience);
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer experience) {
        this.untilNextLevel = nextLevelCalc(levelCalculate(experience),experience);
    }

    public Date getBirthDay() {
        return birthday;
    }

    public void setBirthDay(Date birthDay) {
        this.birthday = birthDay;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    private Integer levelCalculate(Integer experience){
        return (int)(Math.sqrt(2500 + 200 * experience) - 50 )/ 100;
    }
    private Integer nextLevelCalc( Integer level, Integer experience){
        return  50 * (level + 1) * (level + 2) - experience;

    }


}
