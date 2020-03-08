package main;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

@ScriptManifest(author = "Feed", category = Category.WOODCUTTING, name = "YewChopper", version = 1.0)

public class MainClass extends AbstractScript {
    Area bankArea = new Area(3094, 3491, 3092, 3489, 0);
    Area treeArea = new Area(3088, 3482, 3085, 3468, 0);


    @Override
    public void onStart(){
        log("Starting");
    }
    @Override
    public int onLoop() {
        GameObject ytree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Yew"));
        //choping wood
        if(!getInventory().isFull()){
            if(treeArea.contains(getLocalPlayer())){
                if(ytree.interact("chop down")){
                    int countLog = getInventory().count("Yew logs");
                    //sleepUntil(() -> getInventory().count("Yew logs") > countLog, 12000);
                    sleep(Calculations.random(6000, 35000));
                }
            }else{  // walking back
                if(getWalking().walk(treeArea.getCenter())){
                    sleep(Calculations.random(3000, 7500));
                }
            }

        }
        //banking
        if(getInventory().isFull()){  //banking
            if(bankArea.contains(getLocalPlayer())){
                NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
                if(banker.interact("Bank")){
                    if(sleepUntil(() -> getBank().isOpen(), 9000)){
                        if(getBank().depositAllExcept(item -> item != null && item.getName().contains("axe"))){
                            if(sleepUntil(() -> !getInventory().isFull(), 8000)){
                                if(getBank().close()){
                                    sleepUntil(() -> !getBank().isOpen(), 8000);
                                    sleep(Calculations.random(6000, 35000));
                                }
                            }
                        }
                    }
                }
            }else{ //walking back
                if(getWalking().walk(bankArea.getCenter())){
                    sleep(Calculations.random(3000, 9500));
                }
            }
        }

        return 600;
    }

    @Override
    public void onExit(){
        super.onExit();
    }
    @Override
    public void onPaint(Graphics graphics){
        super.onPaint(graphics);
    }

}
