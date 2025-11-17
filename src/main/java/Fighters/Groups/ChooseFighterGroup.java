/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters.Groups;

import Fighters.Fighter;

/**
 *
 * @author mathi
 */
public class ChooseFighterGroup {
    
    public static Fighter ChooseFighterGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentageOfCivilization, String attackGroup){
        
        switch (attackGroup){
            case "FishTelepathy":
                return new FishTelepathyGroup(name, imagePath, power, resistance, sanity, percentageOfCivilization);
            case "PoseidonTheTrident":
                return new PoseidonTheTridentGroup(name, imagePath, power, resistance, sanity, percentageOfCivilization);
            case "ReleaseTheKraken":
                return new ReleaseTheKrakenGroup(name, imagePath, power, resistance, sanity, percentageOfCivilization);
            case "ThundersUnderTheSea":
                return new ThundersUnderTheSeaGroup(name, imagePath, power, resistance, sanity, percentageOfCivilization);
            case "UnderseaFire":
                return new UnderseaFireGroup(name, imagePath, power, resistance, sanity, percentageOfCivilization);
            case "WavesControl":
                return new WavesControlGroup(name, imagePath, power, resistance, sanity, percentageOfCivilization);
            default:
                return null;
        }
         
    }
    
}
