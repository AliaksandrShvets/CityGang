package com.woodcock.citygang.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andrei on 07.03.2016.
 */
public class AI {


    private ArrayList<PersonageBar> personages;
    private int winsPercent;
    private int mode;
    private  int tempMode;
    private int substate;
    int index;
    boolean isParty;
    boolean isMod13;
    String mod13Tag;
    private int oneRoundPersonages;

    public AI(ArrayList<PersonageBar> personages, int winsPercent,int mode, int countPersonage) {
        this.personages = (ArrayList<PersonageBar>) personages.clone();
        this.winsPercent = winsPercent;
        this.mode=mode;
        this.substate=substate;
        index=0;
        isParty=false;
        isMod13=true;
        mod13Tag="0";
        oneRoundPersonages=countPersonage;
        tempMode=-2;
    }

    // Вставь куда тебе удобно метод
    public void clearPersonageBar(){
        personages.clear();
    }
    public PersonageBar getVehicle(String type) {
        boolean flag=false;
        PersonageBar temp = new PersonageBar(0);
        for (int i = 0; i < personages.size() ; i++) {
            if(personages.get(i).getType().equals(type))
            {
                flag=true;
                temp = new PersonageBar(personages.get(i).getType(), personages.get(i).getName());
                personages.remove(i);
                break;
            }
        }
        if(!flag)
            personages.remove(0);
        return temp;
    }

    public int getpersonagesize()
    {
        return personages.size();
    }

    public void setMode(int m)
    {
        tempMode=mode;
        this.mode=m;
    }

    public String getType(int type) {
        switch (type) {
            case 1:
                return "LT";
            case 2:
                return "ST";
            case 3:
                return "TT";
            case 4:
                return "PT";
            case 5:
                return "ART";
        }
        return "0";
    }


    public int getNumber(String type) {
        switch (type) {
            case "LT":
                return 1;
            case "ST":
                return 2;
            case "TT":
                return 3;
            case "PT":
                return 4;
            case "ART":
                return 5;
        }
        return 0;
    }

    public boolean isAttackEnemyBase(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(ally==null)
            return false;
        if(((ally.getPassage()*length)-(ally.getRange()))<300f)
            return true;
        else return false;
    }

    public boolean isAttackEnemyLine(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(ally==null)
            return false;
        if((ally.getPassage()*length-ally.getRange()/2)<(length/3f))
            return true;
        else return false;
    }

    public boolean isAttackMiddleLine(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(ally==null)
            return false;
        if((ally.getPassage()*length-ally.getRange()/2)<((length/5f)*3) && (ally.getPassage()*length-ally.getRange())>(length/3f))
            return true;
        else return false;
    }

    public boolean isAttackAllyLine(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {

        if(ally==null)
            return false;
        if((ally.getPassage()*length-ally.getRange()/2)>((length/5f)*3))
        {
            Log.d("test", ally.getPassage()*length+"-"+ally.getRange()/2+">"+((length/3f)*2));
            return true;
        }
        else return false;
    }

    public boolean isAttackAllyBase(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(enemy==null)
            return false;
        if((enemy.getPassage()*length+enemy.getRange())>(length-300f))
            return true;
        else return false;
    }

    public boolean isAttackEnemyLineEnemy(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(enemy==null)
            return false;
        if((enemy.getPassage()*length+enemy.getRange()/2)<(length/3f))
            return true;
        else return false;
    }

    public boolean isAttackMiddleLineEnemy(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(enemy==null)
            return false;

        if((enemy.getPassage()*length+enemy.getRange()/2)<((length/5f)*3) && (enemy.getPassage()*length+enemy.getRange())>(length/3f))
            return true;
        else return false;
    }

    public boolean isAttackAllyLineEnemy(ArrayList<Personage> list, Personage ally, Personage enemy, float furtherAlly, float nearestEnemy,float length)
    {
        if(enemy==null)
            return false;
        if((enemy.getPassage()*length+enemy.getRange()/2)>((length/5f)*3))
            return true;
        else return false;
    }

    public boolean isArtInEnemyLine(ArrayList<Personage> list,float length)
    {
        for(Personage veh:list)
        {
            if (!veh.isAlly() && veh.getType().equals("ART"))
            {
                if((veh.getPassage()*length+veh.getRange()/2)<(length/4f)*1)
                {return true;}
            }
        }
        return false;
    }

    public int countEnemyInAttackBase(ArrayList<Personage> list,float length)
    {
        int count=0;
        for(Personage veh:list)
            if(!veh.isAlly() && (veh.getPassage()*length+veh.getRange())>(length)-300f)
                count++;
        return count;
    }

    public String getTypeEnemyBaseComboDefeat()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        return type;
    }

    public String getTypeEnemyLineComboDefeat()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        return type;
    }

    public String getTypeEnemyBaseComboDraw()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeEnemyLineComboDraw()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeMiddleLineComboDraw()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeEnemyBaseComboWin()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeEnemyLineComboWin()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeEnemyLineComboWin30Percent()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeMiddleLineComboWin()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getTypeAllyLineComboWin()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        return type;
    }

    public String getTypeAllyBaseComboWin()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        return type;
    }

    //для обучения
    public String getTypeModeMinOne()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    //для обучения урезанная
    public String getTypeModeMinOneLite()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        return type;
    }

    public String getTypeMode0()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getArt()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        return type;
    }

    public String getPt()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        return type;
    }

    public String getSt()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        return type;
    }

    public String getTt()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        return type;
    }

    public String getLt()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        return type;
    }

    public String getLight()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        return type;
    }

    public String getStLt()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                isParty=true;
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                isParty=false;
                return "LT";
            }
        return type;
    }

    public String getTypeSupport()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        return type;
    }

    public boolean getWayAllyLine(ArrayList<Personage> vehicle)
    {
        for(Personage veh:vehicle) {
            if (!veh.isAlly()) {
                return true;
            }
        }
        return false;
    }

    public String getWay(ArrayList<Personage> left, ArrayList<Personage> center, ArrayList<Personage> right, String[] ways)
    {
        int l=0,c=0,r=0;
        for(Personage veh:left)
        {
            if(veh.isAlly())
                l++;
        }
        for(Personage veh:center)
        {
            if(veh.isAlly())
                c++;
        }
        for(Personage veh:right)
        {
            if(veh.isAlly())
                r++;
        }
        int max=l>c && l>r?l:c>l && c>r?c:r;
        if(l>=c && l>=r && (ways[0]=="left" || ways[1]=="left"))
            return "left";
        if(c>=l && c>=r && (ways[0]=="center" || ways[1]=="center"))
            return "center";
        if(r>=c && r>=l && (ways[0]=="right" || ways[1]=="right"))
            return "right";
        return "left";
    }

    public String getTypeLastRound()
    {
        String type="";
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ART")) {
                return "ART";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("PT")) {
                return "PT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("TT")) {
                return "TT";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("ST")) {
                return "ST";
            }
        for(PersonageBar veh:personages)
            if(veh.getType().equals("LT")) {
                return "LT";
            }
        return type;
    }

    public String getEmptyLine(ArrayList<Personage> left, ArrayList<Personage> center, ArrayList<Personage> right)
    {
        String line="";
        int count=0;
        for(Personage veh:left)
        {
            if(veh.isAlly())
                count++;
        }
        if(count==0)
            return "left";
        count=0;
        for(Personage veh:center)
        {
            if(veh.isAlly())
                count++;
        }
        if(count==0)
            return "center";
        count=0;
        for(Personage veh:right)
        {
            if(veh.isAlly())
                count++;
        }
        if(count==0)
            return "center";
        return line;
    }

    public boolean isArt(ArrayList<Personage> personages)
    {
        for(Personage veh:personages)
        {
            if(!veh.isAlly() && veh.getType().equals("ART")) {
                return true;
            }
        }
        return false;
    }

    private String getLineInLastRound(Way left, Way center, Way right)
    {
        String[] ways={"left", "center", "right"};
        if(left.getCountAlly()>left.getCountEnemy())
            return ways[0];
        if(center.getCountAlly()>center.getCountEnemy())
            return ways[1];
        if(right.getCountAlly()>right.getCountEnemy())
            return ways[2];
        return ways[0];
    }

    private String[] enemyBaseAttack(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        int random=new Random().nextInt(5);
        switch (random)
        {
            case 0: {
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "left";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "center";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "right";
                        return info;
                    }
                break;
            }
            case 1: {
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "left";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "right";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "center";
                        return info;
                    }
                break;
            }
            case 2: {
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "right";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "left";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "center";
                        return info;
                    }
                break;
            }
            case 3: {
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "right";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "center";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "left";
                        return info;
                    }
                break;
            }
            case 4: {
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "center";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "left";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "right";
                        return info;
                    }
                break;
            }
            case 5: {
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "center";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "right";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                        info[2] = "1";
                        info[1] = enemyBaseHP > (enemyBaseHP * 0.3f) ? getTypeEnemyBaseComboWin() : getTypeEnemyLineComboWin30Percent();
                        info[0] = "left";
                        return info;
                    }
                break;
            }
        }
        info[2] = "0";
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] nearestEnemy(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        int random=new Random().nextInt(5);
        switch (random)
        {
            case 0: {
                if(left.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(left.getVehicle(), left.length))
                    {
                        if(getArt().equals("ART"))
                        {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "left";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "left";
                            return info;
                        }
                    }
                if(center.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(center.getVehicle(),center.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "center";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "center";
                            return info;
                        }
                    }
                if(right.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(right.getVehicle(),right.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "right";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "right";
                            return info;
                        }
                    }
                break;
            }
            case 1: {
                if(left.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(left.getVehicle(), left.length))
                    {
                        if(getArt().equals("ART"))
                        {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "left";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "left";
                            return info;
                        }
                    }
                if(right.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(right.getVehicle(),right.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "right";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "right";
                            return info;
                        }
                    }
                if(center.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(center.getVehicle(),center.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "center";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "center";
                            return info;
                        }
                    }
                break;
            }
            case 2: {
                if(center.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(center.getVehicle(),center.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "center";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "center";
                            return info;
                        }
                    }
                if(left.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(left.getVehicle(), left.length))
                    {
                        if(getArt().equals("ART"))
                        {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "left";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "left";
                            return info;
                        }
                    }
                if(right.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(right.getVehicle(),right.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "right";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "right";
                            return info;
                        }
                    }
                break;
            }
            case 3: {
                if(center.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(center.getVehicle(),center.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "center";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "center";
                            return info;
                        }
                    }
                if(right.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(right.getVehicle(),right.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "right";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "right";
                            return info;
                        }
                    }
                if(left.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(left.getVehicle(), left.length))
                    {
                        if(getArt().equals("ART"))
                        {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "left";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "left";
                            return info;
                        }
                    }
                break;
            }
            case 4: {
                if(right.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(right.getVehicle(),right.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "right";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "right";
                            return info;
                        }
                    }
                if(left.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(left.getVehicle(), left.length))
                    {
                        if(getArt().equals("ART"))
                        {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "left";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "left";
                            return info;
                        }
                    }
                if(center.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(center.getVehicle(),center.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "center";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "center";
                            return info;
                        }
                    }
                break;
            }
            case 5: {
                if(right.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(right.getVehicle(),right.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "right";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "right";
                            return info;
                        }
                    }
                if(center.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(center.getVehicle(),center.length))
                    {
                        if(getArt().equals("ART")) {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "center";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "center";
                            return info;
                        }
                    }
                if(left.getNearestEnemy()!=null)
                    if(isArtInEnemyLine(left.getVehicle(), left.length))
                    {
                        if(getArt().equals("ART"))
                        {
                            info[2] = "1";
                            info[1] = getArt();
                            info[0] = "left";
                            return info;
                        }
                        if(getPt().equals("PT"))
                        {
                            info[2] = "1";
                            info[1] = getPt();
                            info[0] = "left";
                            return info;
                        }
                    }
                break;
            }
        }
        info[2] = "0";
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] enemyLineAttack(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        int random=new Random().nextInt(5);
        switch (random)
        {
            case 0: {
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                break;
            }
            case 1: {
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                break;
            }
            case 2: {
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                break;
            }
            case 3: {
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                break;
            }
            case 4: {
                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                break;
            }
            case 5: {

                if (right.getFurtherAlly() != null)
                    if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        info[1] = getTypeEnemyLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                break;
            }
        }
        info[2] = "0";
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] middleLineAttack(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        int random=new Random().nextInt(5);
        switch (random)
        {
            case 0: {
                if (left.getFurtherAlly() != null)
                    if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                                left.getNearestEnemyY(), left.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                                center.getNearestEnemyY(), center.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                                right.getNearestEnemyY(), right.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                break;
            }
            case 1: {
                if (left.getFurtherAlly() != null)
                    if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                                left.getNearestEnemyY(), left.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                                right.getNearestEnemyY(), right.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                                center.getNearestEnemyY(), center.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                break;
            }
            case 2: {
                if (center.getFurtherAlly() != null)
                    if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                                center.getNearestEnemyY(), center.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                                right.getNearestEnemyY(), right.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                                left.getNearestEnemyY(), left.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                break;
            }
            case 3: {
                if (center.getFurtherAlly() != null)
                    if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                                center.getNearestEnemyY(), center.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                                left.getNearestEnemyY(), left.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (right.getFurtherAlly() != null)
                    if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                                right.getNearestEnemyY(), right.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                break;
            }
            case 4: {
                if (right.getFurtherAlly() != null)
                    if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                                right.getNearestEnemyY(), right.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                                center.getNearestEnemyY(), center.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                                left.getNearestEnemyY(), left.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                break;
            }
            case 5: {
                if (right.getFurtherAlly() != null)
                    if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                                right.getNearestEnemyY(), right.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "right";
                        return info;
                    }
                if (left.getFurtherAlly() != null)
                    if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                                left.getNearestEnemyY(), left.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "left";
                        return info;
                    }
                if (center.getFurtherAlly() != null)
                    if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                                center.getNearestEnemyY(), center.getLength()))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeMiddleLineComboWin();
                        info[0] = "center";
                        return info;
                    }
                break;
            }
        }
        info[2] = "0";
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] allyBaseAttack(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        int random=new Random().nextInt(5);
        switch (random)
        {
            case 0: {
                if (left.getNearestEnemy() != null)
                    if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"center", "right"};
                        //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                        {
                            info[0] = "left";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                        {
                            info[0] = "left";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (center.getNearestEnemy() != null)
                    if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "right"};
                        // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                        {
                            info[0] = "center";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                        {
                            info[0] = "center";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (right.getNearestEnemy() != null)
                    if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "center"};
                        info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                        {
                            info[0] = "right";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                        {
                            info[0] = "right";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                break;
            }
            case 1: {
                if (left.getNearestEnemy() != null)
                    if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"center", "right"};
                        //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                        {
                            info[0] = "left";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                        {
                            info[0] = "left";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (right.getNearestEnemy() != null)
                    if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "center"};
                        info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                        {
                            info[0] = "right";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                        {
                            info[0] = "right";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (center.getNearestEnemy() != null)
                    if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "right"};
                        // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                        {
                            info[0] = "center";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                        {
                            info[0] = "center";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                break;
            }
            case 2: {
                if (center.getNearestEnemy() != null)
                    if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "right"};
                        // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                        {
                            info[0] = "center";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                        {
                            info[0] = "center";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (right.getNearestEnemy() != null)
                    if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "center"};
                        info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                        {
                            info[0] = "right";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                        {
                            info[0] = "right";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (left.getNearestEnemy() != null)
                    if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"center", "right"};
                        //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                        {
                            info[0] = "left";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                        {
                            info[0] = "left";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                break;
            }
            case 3: {
                if (center.getNearestEnemy() != null)
                    if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "right"};
                        // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                        {
                            info[0] = "center";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                        {
                            info[0] = "center";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (left.getNearestEnemy() != null)
                    if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"center", "right"};
                        //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                        {
                            info[0] = "left";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                        {
                            info[0] = "left";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (right.getNearestEnemy() != null)
                    if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "center"};
                        info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                        {
                            info[0] = "right";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                        {
                            info[0] = "right";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                break;
            }
            case 4: {
                if (right.getNearestEnemy() != null)
                    if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "center"};
                        info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                        {
                            info[0] = "right";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                        {
                            info[0] = "right";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (center.getNearestEnemy() != null)
                    if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "right"};
                        // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                        {
                            info[0] = "center";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                        {
                            info[0] = "center";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (left.getNearestEnemy() != null)
                    if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"center", "right"};
                        //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                        {
                            info[0] = "left";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                        {
                            info[0] = "left";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                break;
            }
            case 5: {
                if (right.getNearestEnemy() != null)
                    if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "center"};
                        info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                        {
                            info[0] = "right";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                        {
                            info[0] = "right";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (left.getNearestEnemy() != null)
                    if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"center", "right"};
                        //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                        {
                            info[0] = "left";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                        {
                            info[0] = "left";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                if (center.getNearestEnemy() != null)
                    if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
                    {
                        info[2] = "1";
                        String[] waysTwo = {"left", "right"};
                        // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                        //info[1] = getTypeAllyBaseComboWin();
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                        {
                            info[0] = "center";
                            info[1]=getLight();
                            return info;
                        }
                        if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                        {
                            info[0] = "center";
                            info[1]=getLt();
                            return info;
                        }
                        info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                        switch (info[0])
                        {
                            case "center":
                                if(isArt(center.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                            case "right":
                                if(isArt(right.personages))
                                    info[1]=getTypeSupport();
                                else
                                    info[1] = getTypeAllyBaseComboWin();
                                break;
                        }
                        return info;
                    }
                break;
            }
        }
        info[2] = "0";
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] allyLineAttack(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        int random=new Random().nextInt(5);
        switch (random)
        {
            case 0: {
                if (getWayAllyLine(left.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"center", "right"};
                    info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                            left.getNearestEnemyY(), left.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(center.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "right"};
                    info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                            center.getNearestEnemyY(), center.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(right.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "center"};
                    info[0] = "right";
                    if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                            right.getNearestEnemyY(), right.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                break;
            }
            case 1: {
                if (getWayAllyLine(left.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"center", "right"};
                    info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                            left.getNearestEnemyY(), left.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(right.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "center"};
                    info[0] = "right";
                    if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                            right.getNearestEnemyY(), right.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(center.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "right"};
                    info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                            center.getNearestEnemyY(), center.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                break;
            }
            case 2: {
                if (getWayAllyLine(center.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "right"};
                    info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                            center.getNearestEnemyY(), center.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(right.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "center"};
                    info[0] = "right";
                    if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                            right.getNearestEnemyY(), right.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(left.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"center", "right"};
                    info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                            left.getNearestEnemyY(), left.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                break;
            }
            case 3: {
                if (getWayAllyLine(center.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "right"};
                    info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                            center.getNearestEnemyY(), center.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(left.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"center", "right"};
                    info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                            left.getNearestEnemyY(), left.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(right.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "center"};
                    info[0] = "right";
                    if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                            right.getNearestEnemyY(), right.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                break;
            }
            case 4: {
                if (getWayAllyLine(right.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "center"};
                    info[0] = "right";
                    if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                            right.getNearestEnemyY(), right.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(center.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "right"};
                    info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                            center.getNearestEnemyY(), center.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(left.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"center", "right"};
                    info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                            left.getNearestEnemyY(), left.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                break;
            }
            case 5: {
                if (getWayAllyLine(right.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "center"};
                    info[0] = "right";
                    if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                            right.getNearestEnemyY(), right.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(left.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"center", "right"};
                    info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                            left.getNearestEnemyY(), left.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                if (getWayAllyLine(center.getVehicle()))
                {
                    info[2] = "1";
                    String[] waysTwo = {"left", "right"};
                    info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                    if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                            center.getNearestEnemyY(), center.getLength())) {
                        info[1] = getTypeSupport();
                    }
                    else {
                        info[1] = getTypeAllyLineComboWin();
                    }
                    return info;
                }
                break;
            }
        }
        info[2] = "0";
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] modMin1(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"center"};

        if((left.getCountEnemy()+right.getCountEnemy()+center.getCountEnemy())>=(left.getCountAlly()+right.getCountAlly()+center.getCountAlly())+1 && enemyBaseHP>50)
        {
            info[0]="none";
            return info;
        }
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0] = "center";
            info[1]="ST";
            info[2]="first";
            return info;
        }

        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeMode0();
                info[0] = "center";
                return info;
            }


        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "right"};
                info[1]=getTypeMode0();
                info[0] = "center";
                return info;
            }
        info[0] = "center";
        info[1]=getTypeMode0();
        return info;
    }

    private String[] mod0(Way left, Way center, Way right, float enemyBaseHP, int round, int second)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};

        Log.d("qwer","sum "+(oneRoundPersonages) );
        Log.d("qwer","second "+second );
        Log.d("qwer","round "+round );
        if(round>=4 && round<=5 && oneRoundPersonages==5) {
            info=mod6(left, center, right, enemyBaseHP, round, second);
            return info;
        }
        if((left.getCountEnemy()+right.getCountEnemy()+center.getCountEnemy())>=(left.getCountAlly()+right.getCountAlly()+center.getCountAlly())+1 && enemyBaseHP>50)
        {
            info[0]="none";
            return info;
        }
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="ST";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeMode0();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeMode0();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeMode0();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"center", "right"};
                info[1]=getTypeMode0();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "right"};
                info[1]=getTypeMode0();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "center"};
                info[1]=getTypeMode0();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMode0();
        return info;
    }

    private String[] mod0Old(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if((left.getCountEnemy()+right.getCountEnemy()+center.getCountEnemy())>(left.getCountAlly()+right.getCountAlly()+center.getCountAlly()) && enemyBaseHP>50)
        {
            info[0]="none";
            return info;
        }
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="ART";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"center", "right"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "right"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "center"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeEnemyLineComboDefeat();
        return info;
    }

    private String[] mod1(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if((left.getCountEnemy()+right.getCountEnemy()+center.getCountEnemy())>(left.getCountAlly()+right.getCountAlly()+center.getCountAlly())+1 && enemyBaseHP>50)
        {
            info[0]="none";
            return info;
        }
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="ART";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"center", "right"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "right"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "center"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeEnemyLineComboDefeat();
        return info;
    }

    private String[] mod2(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="ART";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1]=getTypeEnemyBaseComboDefeat();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"center", "right"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "right"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                String[] waysTwo={"left", "center"};
                info[1]=getTypeEnemyLineComboDefeat();
                info[0] = waysTwo[(new Random()).nextInt(2)];
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeEnemyLineComboDefeat();
        return info;
    }

    private String[] mod3(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if((left.getCountEnemy()+right.getCountEnemy()+center.getCountEnemy())>(left.getCountAlly()+right.getCountAlly()+center.getCountAlly()) && enemyBaseHP>50)
        {
            info[0]="none";
            return info;
        }
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="TT";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"center", "right"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"left", "right"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"left", "center"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMiddleLineComboDraw();
        return info;
    }

    private String[] mod4(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if((left.getCountEnemy()+right.getCountEnemy()+center.getCountEnemy())>(left.getCountAlly()+right.getCountAlly()+center.getCountAlly())+1 && enemyBaseHP>50)
        {
            info[0]="none";
            return info;
        }
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="TT";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"center", "right"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"left", "right"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"left", "center"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMiddleLineComboDraw();
        return info;
    }

    private String[] mod5(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if(left.getVehicle().size()==0 && center.getVehicle().size()==0 && right.getVehicle().size()==0)
        {
            info[0]=ways[(new Random()).nextInt(3)];
            info[1]="TT";
            info[2]="first";
            return info;
        }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyBase(left.getVehicle(),left.getFurtherAlly(),left.getNearestEnemy(),left.getFurtherAllyY(),left.getNearestEnemyY(),left.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyBase(center.getVehicle(),center.getFurtherAlly(),center.getNearestEnemy(),center.getFurtherAllyY(),center.getNearestEnemyY(),center.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyBase(right.getVehicle(),right.getFurtherAlly(),right.getNearestEnemy(),right.getFurtherAllyY(),right.getNearestEnemyY(),right.getLength()))
            {
                info[2]="enemyBase";
                info[1] = getTypeEnemyBaseComboDraw();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "left";
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "center";
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="enemyLine";
                info[1] = getTypeEnemyLineComboDraw();
                info[0] = "right";
                return info;
            }
        if(left.getFurtherAlly()!=null)
            if(isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"center", "right"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        if(center.getFurtherAlly()!=null)
            if(isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"left", "right"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        if(right.getFurtherAlly()!=null)
            if(isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2]="middleLine";
                String[] waysTwo={"left", "center"};
                info[0] = waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeMiddleLineComboDraw();
                return info;
            }
        info[0]=ways[(new Random()).nextInt(3)];
        info[1]=getTypeMiddleLineComboDraw();
        return info;
    }

    private String[] mod6(Way left, Way center, Way right, float enemyBaseHP, int round, int second)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};

        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        if (left.getFurtherAlly() != null)
            if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "right";
                return info;
            }
        if(left.getNearestEnemy()!=null)
            if(isArtInEnemyLine(left.getVehicle(), left.length))
            {
                if(getArt().equals("ART"))
                {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "left";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "left";
                    return info;
                }
            }
        if(center.getNearestEnemy()!=null)
            if(isArtInEnemyLine(center.getVehicle(),center.length))
            {
                if(getArt().equals("ART")) {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "center";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "center";
                    return info;
                }
            }
        if(right.getNearestEnemy()!=null)
            if(isArtInEnemyLine(right.getVehicle(),right.length))
            {
                if(getArt().equals("ART")) {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "right";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "right";
                    return info;
                }
            }
        if (left.getFurtherAlly() != null)
            if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "right";
                return info;
            }
        if (left.getFurtherAlly() != null)
            if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                        left.getNearestEnemyY(), left.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                        center.getNearestEnemyY(), center.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                        right.getNearestEnemyY(), right.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "right";
                return info;
            }
        //ally base attack
        if (left.getNearestEnemy() != null)
            if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"center", "right"};
                //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                //info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                {
                    info[0] = "left";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                {
                    info[0] = "left";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        if (center.getNearestEnemy() != null)
            if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"left", "right"};
                // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                //info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                {
                    info[0] = "center";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                {
                    info[0] = "center";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        if (right.getNearestEnemy() != null)
            if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"left", "center"};
                info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                {
                    info[0] = "right";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                {
                    info[0] = "right";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        //if (left.getFurtherAlly() != null)
        if (getWayAllyLine(left.getVehicle()))//isAttackAllyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()) &&
        {
            info[2] = "allyLine";
            String[] waysTwo = {"center", "right"};
            info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
            if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                    left.getNearestEnemyY(), left.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }

//                        switch (info[0])
//                        {
//                            case "center":
//                                if(isArt(center.personages)) {
//                                    info[1] = getTypeSupport();
//                               }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                            case "right":
//                                if(isArt(right.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                               else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                        }
            return info;
        }
        //if (center.getFurtherAlly() != null)
        if (getWayAllyLine(center.getVehicle()))//isAttackAllyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()) &&
        {
            info[2] = "allyLine";
            String[] waysTwo = {"left", "right"};
            info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
            if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                    center.getNearestEnemyY(), center.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }

//                        switch (info[0])
//                        {
//                            case "left":
//                                if(isArt(left.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                            case "right":
//                                if(isArt(right.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                        }
            return info;
        }
        //if (right.getFurtherAlly() != null)
        if (getWayAllyLine(right.getVehicle()))/* && isAttackAllyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())*/
        {
            info[2] = "allyLine";
            String[] waysTwo = {"left", "center"};
            info[0] = "right";//getWay(left.getVehicle(),center.getVehicle(),right.getVehicle(),waysTwo);//waysTwo[(new Random()).nextInt(2)];
            //switch (info[0])
            //{
            //    case "left":
            if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                    right.getNearestEnemyY(), right.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }
            //       break;
            //   case "center":
            //       if(isArt(center.personages)) {
            //           info[1] = getTypeSupport();
            //        }
            //       else {
            //           info[1] = getTypeAllyLineComboWin();
            //        }
            //        break;
            //}
            return info;
        }

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod7(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        if (left.getFurtherAlly() != null)
            if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "right";
                return info;
            }
        if(left.getNearestEnemy()!=null)
            if(isArtInEnemyLine(left.getVehicle(), left.length))
            {
                if(getArt().equals("ART"))
                {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "left";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "left";
                    return info;
                }
            }
        if(center.getNearestEnemy()!=null)
            if(isArtInEnemyLine(center.getVehicle(),center.length))
            {
                if(getArt().equals("ART")) {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "center";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "center";
                    return info;
                }
            }
        if(right.getNearestEnemy()!=null)
            if(isArtInEnemyLine(right.getVehicle(),right.length))
            {
                if(getArt().equals("ART")) {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "right";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "right";
                    return info;
                }
            }
        if (left.getFurtherAlly() != null)
            if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "right";
                return info;
            }
        if (left.getFurtherAlly() != null)
            if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                        left.getNearestEnemyY(), left.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                        center.getNearestEnemyY(), center.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                        right.getNearestEnemyY(), right.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "right";
                return info;
            }
        //ally base attack
        if (left.getNearestEnemy() != null)
            if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"center", "right"};
                //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                //info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                {
                    info[0] = "left";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                {
                    info[0] = "left";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        if (center.getNearestEnemy() != null)
            if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"left", "right"};
                // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                //info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                {
                    info[0] = "center";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                {
                    info[0] = "center";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        if (right.getNearestEnemy() != null)
            if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"left", "center"};
                info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                {
                    info[0] = "right";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                {
                    info[0] = "right";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        //if (left.getFurtherAlly() != null)
        if (getWayAllyLine(left.getVehicle()))//isAttackAllyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()) &&
        {
            info[2] = "allyLine";
            String[] waysTwo = {"center", "right"};
            info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
            if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                    left.getNearestEnemyY(), left.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }

//                        switch (info[0])
//                        {
//                            case "center":
//                                if(isArt(center.personages)) {
//                                    info[1] = getTypeSupport();
//                               }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                            case "right":
//                                if(isArt(right.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                               else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                        }
            return info;
        }
        //if (center.getFurtherAlly() != null)
        if (getWayAllyLine(center.getVehicle()))//isAttackAllyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()) &&
        {
            info[2] = "allyLine";
            String[] waysTwo = {"left", "right"};
            info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
            if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                    center.getNearestEnemyY(), center.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }

//                        switch (info[0])
//                        {
//                            case "left":
//                                if(isArt(left.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                            case "right":
//                                if(isArt(right.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                        }
            return info;
        }
        //if (right.getFurtherAlly() != null)
        if (getWayAllyLine(right.getVehicle()))/* && isAttackAllyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())*/
        {
            info[2] = "allyLine";
            String[] waysTwo = {"left", "center"};
            info[0] = "right";//getWay(left.getVehicle(),center.getVehicle(),right.getVehicle(),waysTwo);//waysTwo[(new Random()).nextInt(2)];
            //switch (info[0])
            //{
            //    case "left":
            if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                    right.getNearestEnemyY(), right.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }
            //       break;
            //   case "center":
            //       if(isArt(center.personages)) {
            //           info[1] = getTypeSupport();
            //        }
            //       else {
            //           info[1] = getTypeAllyLineComboWin();
            //        }
            //        break;
            //}
            return info;
        }

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod8(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        if (left.getFurtherAlly() != null)
            if (isAttackEnemyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())) {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackEnemyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackEnemyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "enemyBase";
                info[1] = enemyBaseHP>(enemyBaseHP*0.3f)?getTypeEnemyBaseComboWin():getTypeEnemyLineComboWin30Percent();
                info[0] = "right";
                return info;
            }
        if(left.getNearestEnemy()!=null)
            if(isArtInEnemyLine(left.getVehicle(), left.length))
            {
                if(getArt().equals("ART"))
                {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "left";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "left";
                    return info;
                }
            }
        if(center.getNearestEnemy()!=null)
            if(isArtInEnemyLine(center.getVehicle(),center.length))
            {
                if(getArt().equals("ART")) {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "center";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "center";
                    return info;
                }
            }
        if(right.getNearestEnemy()!=null)
            if(isArtInEnemyLine(right.getVehicle(),right.length))
            {
                if(getArt().equals("ART")) {
                    info[2] = "enemyLine";
                    info[1] = getArt();
                    info[0] = "right";
                    return info;
                }
                if(getPt().equals("PT"))
                {
                    info[2] = "enemyLine";
                    info[1] = getPt();
                    info[0] = "right";
                    return info;
                }
            }
        if (left.getFurtherAlly() != null)
            if (isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "enemyLine";
                info[1] = getTypeEnemyLineComboWin();
                info[0] = "right";
                return info;
            }
        if (left.getFurtherAlly() != null)
            if (isAttackMiddleLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(left.personages) && isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                        left.getNearestEnemyY(), left.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "left";
                return info;
            }
        if (center.getFurtherAlly() != null)
            if (isAttackMiddleLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                        center.getNearestEnemyY(), center.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "center";
                return info;
            }
        if (right.getFurtherAlly() != null)
            if (isAttackMiddleLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "middleLine";
                if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                        right.getNearestEnemyY(), right.getLength()))
                    info[1]=getTypeSupport();
                else
                    info[1] = getTypeMiddleLineComboWin();
                info[0] = "right";
                return info;
            }
        //ally base attack
        if (left.getNearestEnemy() != null)
            if (isAttackAllyBase(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"center", "right"};
                //info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                //info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(left.getVehicle(),left.length)>2)
                {
                    info[0] = "left";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(left.getVehicle(),left.length)>1)
                {
                    info[0] = "left";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        if (center.getNearestEnemy() != null)
            if (isAttackAllyBase(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"left", "right"};
                // info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                //info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(center.getVehicle(),center.length)>2)
                {
                    info[0] = "center";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(center.getVehicle(),center.length)>1)
                {
                    info[0] = "center";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        if (right.getNearestEnemy() != null)
            if (isAttackAllyBase(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength()))
            {
                info[2] = "allyBase";
                String[] waysTwo = {"left", "center"};
                info[0] = "right";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
                info[1] = getTypeAllyBaseComboWin();
                if(countEnemyInAttackBase(right.getVehicle(),right.length)>2)
                {
                    info[0] = "right";
                    info[1]=getLight();
                    return info;
                }
                if(countEnemyInAttackBase(right.getVehicle(),right.length)>1)
                {
                    info[0] = "right";
                    info[1]=getLt();
                    return info;
                }
                info[0] = getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo); //waysTwo[(new Random()).nextInt(2)];
                switch (info[0])
                {
                    case "center":
                        if(isArt(center.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                    case "right":
                        if(isArt(right.personages))
                            info[1]=getTypeSupport();
                        else
                            info[1] = getTypeAllyBaseComboWin();
                        break;
                }
                return info;
            }
        //if (left.getFurtherAlly() != null)
        if (getWayAllyLine(left.getVehicle()))//isAttackAllyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength()) &&
        {
            info[2] = "allyLine";
            String[] waysTwo = {"center", "right"};
            info[0] = "left";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
            if(isArt(left.personages) &&  isAttackAllyLineEnemy(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(),
                    left.getNearestEnemyY(), left.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }

//                        switch (info[0])
//                        {
//                            case "center":
//                                if(isArt(center.personages)) {
//                                    info[1] = getTypeSupport();
//                               }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                            case "right":
//                                if(isArt(right.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                               else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                        }
            return info;
        }
        //if (center.getFurtherAlly() != null)
        if (getWayAllyLine(center.getVehicle()))//isAttackAllyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength()) &&
        {
            info[2] = "allyLine";
            String[] waysTwo = {"left", "right"};
            info[0] = "center";//getWay(left.getVehicle(), center.getVehicle(), right.getVehicle(), waysTwo);//waysTwo[(new Random()).nextInt(2)];
            if(isArt(center.personages) && isAttackAllyLineEnemy(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(),
                    center.getNearestEnemyY(), center.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }

//                        switch (info[0])
//                        {
//                            case "left":
//                                if(isArt(left.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                            case "right":
//                                if(isArt(right.personages)) {
//                                    info[1] = getTypeSupport();
//                                }
//                                else {
//                                    info[1] = getTypeAllyLineComboWin();
//                                }
//                                break;
//                        }
            return info;
        }
        //if (right.getFurtherAlly() != null)
        if (getWayAllyLine(right.getVehicle()))/* && isAttackAllyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())*/
        {
            info[2] = "allyLine";
            String[] waysTwo = {"left", "center"};
            info[0] = "right";//getWay(left.getVehicle(),center.getVehicle(),right.getVehicle(),waysTwo);//waysTwo[(new Random()).nextInt(2)];
            //switch (info[0])
            //{
            //    case "left":
            if(isArt(right.personages) && isAttackAllyLineEnemy(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(),
                    right.getNearestEnemyY(), right.getLength())) {
                info[1] = getTypeSupport();
            }
            else {
                info[1] = getTypeAllyLineComboWin();
            }
            //       break;
            //   case "center":
            //       if(isArt(center.personages)) {
            //           info[1] = getTypeSupport();
            //        }
            //       else {
            //           info[1] = getTypeAllyLineComboWin();
            //        }
            //        break;
            //}
            return info;
        }

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod9(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        info=enemyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //nearest enemy
        info=nearestEnemy(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=enemyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //middle line attack
        info=middleLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally base attack
        info=allyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally line attack
        info=allyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod10(Way left, Way center, Way right, float enemyBaseHP)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        info=enemyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //nearest enemy
        info=nearestEnemy(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=enemyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=middleLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally base attack
        info=allyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally line attack
        info=allyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;


        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod11(Way left, Way center, Way right, float enemyBaseHP, int round)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};

        if(round==4 || round==3)
        {
            if(left.getVehicle().size()==0 || isParty)
            {
                info[0] = "left" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(center.getVehicle().size()==0 || isParty)
            {
                info[0] = "center" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(right.getVehicle().size()==0 || isParty)
            {
                info[0] = "right" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
        }

        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        info=enemyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //nearest enemy
        info=nearestEnemy(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=enemyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=middleLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally base attack
        info=allyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally line attack
        info=allyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod12(Way left, Way center, Way right, float enemyBaseHP, int round)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};

        if(round==4 || round==3)
        {
            if(left.getVehicle().size()==0 || isParty)
            {
                info[0] = "left" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(center.getVehicle().size()==0 || isParty)
            {
                info[0] = "center" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(right.getVehicle().size()==0 || isParty)
            {
                info[0] = "right" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
        }

        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        info=enemyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //nearest enemy
        info=nearestEnemy(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=enemyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=middleLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally base attack
        info=allyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally line attack
        info=allyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod13(Way left, Way center, Way right, float enemyBaseHP, int round, int second)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};

        if(round==4 || round==3)
        {
            if(left.getVehicle().size()==0 || isParty)
            {
                info[0] = "left" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(center.getVehicle().size()==0 || isParty)
            {
                info[0] = "center" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(right.getVehicle().size()==0 || isParty)
            {
                info[0] = "right" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
        }

        if(round==7 && isMod13)
        {
            if(mod13Tag.equals("0")) {
                if ((isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())
                        && isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())
                        && right.getVehicle().size() == 0)) {
                    mod13Tag="right";
                }
                if ((isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())
                        && isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())
                        && center.getVehicle().size() == 0)) {
                    mod13Tag="center";
                }
                if ((isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())
                        && isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())
                        && left.getVehicle().size() == 0)) {
                    mod13Tag="left";
                }
            }
            if(mod13Tag.equals("0"))
                isMod13=false;
            else
            {
                if(mod13Tag.equals("left"))
                {
                    if(!getPt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "PT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getTt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "TT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "ST";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        String[] waysTwo = {"right", "center"};
                        info[0] = waysTwo[(new Random()).nextInt(2)] ;
                        info[1] = "ART";
                        info[2] = "first";
                        return info;
                    }
                    if(!getLt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "LT";
                        info[2] = "first";
                        return info;
                    }
                }

                if(mod13Tag.equals("center"))
                {
                    if(!getPt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "PT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getTt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "TT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "ST";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        String[] waysTwo = {"right", "left"};
                        info[0] = waysTwo[(new Random()).nextInt(2)] ;
                        info[1] = "ART";
                        info[2] = "first";
                        return info;
                    }
                    if(!getLt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "LT";
                        info[2] = "first";
                        return info;
                    }
                }
                if(mod13Tag.equals("right"))
                {
                    if(!getPt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "PT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getTt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "TT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "ST";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        String[] waysTwo = {"center", "left"};
                        info[0] = waysTwo[(new Random()).nextInt(2)] ;
                        info[1] = "ART";
                        info[2] = "first";
                        return info;
                    }
                    if(!getLt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "LT";
                        info[2] = "first";
                        return info;
                    }
                }
            }
        }

        if(round ==7 && !getLt().equals("") && !getEmptyLine(left.personages,center.personages,right.personages).equals("") && second<300)
        {
            info[0] = getEmptyLine(left.personages,center.personages,right.personages);
            info[1] = "LT";
            info[2] = "first";
            return info;
        }

        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        info=enemyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //nearest enemy
        info=nearestEnemy(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=enemyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=middleLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally base attack
        info=allyBaseAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        //ally line attack
        info=allyLineAttack(left,center,right,enemyBaseHP);
        if(info[2].equals("1"))
            return info;

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    private String[] mod14(Way left, Way center, Way right, float enemyBaseHP, int round, int second)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};

        if(round==4 || round==3)
        {
            if(left.getVehicle().size()==0 || isParty)
            {
                info[0] = "left" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(center.getVehicle().size()==0 || isParty)
            {
                info[0] = "center" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
            if(right.getVehicle().size()==0 || isParty)
            {
                info[0] = "right" ;
                info[1] = getStLt();
                info[2] = "first";
                return info;
            }
        }

        if(round==7 && isMod13)
        {
            if(mod13Tag.equals("0")) {
                if ((isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())
                        && isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())
                        && right.getVehicle().size() == 0)) {
                    mod13Tag="right";
                }
                if ((isAttackEnemyLine(left.getVehicle(), left.getFurtherAlly(), left.getNearestEnemy(), left.getFurtherAllyY(), left.getNearestEnemyY(), left.getLength())
                        && isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())
                        && center.getVehicle().size() == 0)) {
                    mod13Tag="center";
                }
                if ((isAttackEnemyLine(right.getVehicle(), right.getFurtherAlly(), right.getNearestEnemy(), right.getFurtherAllyY(), right.getNearestEnemyY(), right.getLength())
                        && isAttackEnemyLine(center.getVehicle(), center.getFurtherAlly(), center.getNearestEnemy(), center.getFurtherAllyY(), center.getNearestEnemyY(), center.getLength())
                        && left.getVehicle().size() == 0)) {
                    mod13Tag="left";
                }
            }
            if(mod13Tag.equals("0"))
                isMod13=false;
            else
            {
                if(mod13Tag.equals("left"))
                {
                    if(!getPt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "PT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getTt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "TT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "ST";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        String[] waysTwo = {"right", "center"};
                        info[0] = waysTwo[(new Random()).nextInt(2)] ;
                        info[1] = "ART";
                        info[2] = "first";
                        return info;
                    }
                    if(!getLt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "LT";
                        info[2] = "first";
                        return info;
                    }
                }

                if(mod13Tag.equals("center"))
                {
                    if(!getPt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "PT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getTt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "TT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "ST";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        String[] waysTwo = {"right", "left"};
                        info[0] = waysTwo[(new Random()).nextInt(2)] ;
                        info[1] = "ART";
                        info[2] = "first";
                        return info;
                    }
                    if(!getLt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "LT";
                        info[2] = "first";
                        return info;
                    }
                }
                if(mod13Tag.equals("right"))
                {
                    if(!getPt().equals(""))
                    {
                        info[0] = "center" ;
                        info[1] = "PT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getTt().equals(""))
                    {
                        info[0] = "left" ;
                        info[1] = "TT";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "ST";
                        info[2] = "first";
                        return info;
                    }
                    if(!getSt().equals(""))
                    {
                        String[] waysTwo = {"center", "left"};
                        info[0] = waysTwo[(new Random()).nextInt(2)] ;
                        info[1] = "ART";
                        info[2] = "first";
                        return info;
                    }
                    if(!getLt().equals(""))
                    {
                        info[0] = "right" ;
                        info[1] = "LT";
                        info[2] = "first";
                        return info;
                    }
                }
            }
        }

        if(round ==7 && !getLt().equals("") && !getEmptyLine(left.personages,center.personages,right.personages).equals("") && second<300)
        {
            info[0] = getEmptyLine(left.personages,center.personages,right.personages);
            info[1] = "LT";
            info[2] = "first";
            return info;
        }

        if (left.getVehicle().size() == 0 && center.getVehicle().size() == 0 && right.getVehicle().size() == 0) {
            info[0] = ways[(new Random()).nextInt(3)];
            info[1] = "ART";
            info[2] = "first";
            return info;
        }
        //enemy base attack
        info=enemyBaseAttack(left,center,right,enemyBaseHP);
        if(round==7)
            info[0]=getLineInLastRound(left,center,right);
        if(info[2].equals("1"))
            return info;

        //nearest enemy
        info=nearestEnemy(left,center,right,enemyBaseHP);
        if(round==7)
            info[0]=getLineInLastRound(left,center,right);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=enemyLineAttack(left,center,right,enemyBaseHP);
        if(round==7)
            info[0]=getLineInLastRound(left,center,right);
        if(info[2].equals("1"))
            return info;

        //enemy line attack
        info=middleLineAttack(left,center,right,enemyBaseHP);
        if(round==7)
            info[0]=getLineInLastRound(left,center,right);
        if(info[2].equals("1"))
            return info;

        //ally base attack
        info=allyBaseAttack(left,center,right,enemyBaseHP);
        if(round==7)
            info[0]=getLineInLastRound(left,center,right);
        if(info[2].equals("1"))
            return info;

        //ally line attack
        info=allyLineAttack(left,center,right,enemyBaseHP);
        if(round==7)
            info[0]=getLineInLastRound(left,center,right);
        if(info[2].equals("1"))
            return info;

        info[0] = ways[(new Random()).nextInt(3)];
        info[1]= getTypeAllyLineComboWin();
        return info;
    }

    public String[] getInfo(Way left, Way center, Way right, float enemyBaseHP,int round, int second)
    {
        String[] info=new String[3];
        String[] ways={"left", "center", "right"};
        switch (mode) {
            case -1:
            {
                info=modMin1(left, center, right, enemyBaseHP);
                return info;
            }
            case 0:
            {
                info=mod0(left, center, right, enemyBaseHP,round,second);
                return info;
            }
            case 1:
            {
                info=mod1(left, center, right, enemyBaseHP);
                return info;
            }
            case 2:
            {
                info=mod2(left, center, right, enemyBaseHP);
                return info;
            }
            case 3:
            {
                info=mod3(left, center, right, enemyBaseHP);
                return info;
            }
            case 4:
            {
                info=mod4(left, center, right, enemyBaseHP);
                return info;
            }
            case 5:
            {
                info=mod5(left, center, right, enemyBaseHP);
                return info;
            }
            case 6:
            {
                info=mod6(left, center, right, enemyBaseHP, round, second);
                return info;
            }
            case 7:
            {
                info=mod7(left, center, right, enemyBaseHP);
                return info;
            }
            case 8:
            {
                info=mod8(left, center, right, enemyBaseHP);
                return info;
            }
            case 9:
            {
                info=mod9(left, center, right, enemyBaseHP);
                return info;
            }
            case 10:
            {
                info=mod10(left, center, right, enemyBaseHP);
                return info;
            }
            case 11:
            {
                info=mod11(left, center, right, enemyBaseHP,round);
                return info;
            }
            case 12:
            {
                info=mod12(left, center, right, enemyBaseHP,round);
                return info;
            }
            case 13:
            {
                info=mod13(left, center, right, enemyBaseHP,round, second);
                return info;
            }
            case 14:
            {
                info=mod14(left, center, right, enemyBaseHP,round, second);
                return info;
            }
        }
        return info;
    }

}