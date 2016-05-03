package io.github.djxy.permissionManager.rules;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by samuelmarchildon-lavoie on 16-05-03.
 */
public class TimeRule implements Rule {

    public static final String RULE_NAME = "time";

    private final CopyOnWriteArrayList<TimeLapse> timeLapses = new CopyOnWriteArrayList<>();

    @Override
    public boolean canApplyRule(Player player) {
        for(TimeLapse timeLapse : timeLapses)
            if(timeLapse.isBetween(player.getWorld()))
                return true;

        return false;
    }

    @Override
    public void applyRule(Player player) {}

    @Override
    public void initFromNode(ConfigurationNode configurationNode) {
        List<ConfigurationNode> nodes = (List<ConfigurationNode>) configurationNode.getChildrenList();

        for(ConfigurationNode node : nodes){
            System.out.println(node.getNode("begin").getValue());
            System.out.println(node.getNode("end").getValue());
        }
    }

    @Override
    public void setNode(ConfigurationNode configurationNode) {
        List<HashMap> list = new ArrayList<>();

        for(TimeLapse timeLapse : timeLapses) {
            HashMap<String,Integer> values = new HashMap<>();
            values.put("begin", timeLapse.begin);
            values.put("end", timeLapse.end);

            list.add(values);
        }

        configurationNode.setValue(list);
    }

    public void addTimeLapse(TimeLapse timeLapse){
        timeLapses.add(timeLapse);
    }

    public static class TimeLapse {

        private final int begin;
        private final int end;
        private final boolean beginBigger;

        public TimeLapse(int begin, int end) {
            this.begin = begin;
            this.end = end;
            this.beginBigger = begin > end;
        }

        public boolean isBetween(World world){
            int worldTime = (int) (world.getProperties().getWorldTime()%24000);

            if(beginBigger)
                return worldTime >= begin || worldTime <= end;
            else
                return end >= worldTime && worldTime >= begin;
        }
    }

}
