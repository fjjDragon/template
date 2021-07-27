package com.fish.model.bean.fleet;

import java.util.List;

/**
 * 船队model
 *
 * @author: fjjdragon
 * @date: 2021-05-06 23:21
 */
public class Fleet {

    private int owner;
    private int level;

    private List<FleetMember> firstTeam;
    private List<FleetMember> secondTeam;

    private FleetContribution contribution;

}