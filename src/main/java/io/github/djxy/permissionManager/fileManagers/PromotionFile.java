package io.github.djxy.permissionManager.fileManagers;

import io.github.djxy.core.files.FileManager;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.promotions.PromotionManager;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public class PromotionFile extends FileManager {

    public PromotionFile(Path folder) {
        super(folder, "promotions");
    }

    @Override
    protected void save(ConfigurationNode root) {
        PromotionManager pm = PromotionManager.getInstance();

        for(String promotion : pm.getPromotions()) {
            pm.getPromotion(promotion).setNode(root.getNode(promotion));
        }
    }

    @Override
    protected void load(ConfigurationNode root) {
        PromotionManager pm = PromotionManager.getInstance();
        Map<Object, ConfigurationNode> promotions = (Map<Object, ConfigurationNode>) root.getChildrenMap();

        for(Object promo : promotions.keySet()){
            Promotion promotion = new Promotion();

            promotion.initFromNode(root.getNode(promo));
            pm.setPromotion((String) promo, promotion);
        }
    }

}
