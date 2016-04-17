package io.github.djxy.permissionManager.promotions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-04-13.
 */
public class PromotionManager {

    private static final PromotionManager instance = new PromotionManager();

    public static PromotionManager getInstance() {
        return instance;
    }

    private final ConcurrentHashMap<String, Promotion> promotions = new ConcurrentHashMap<>();

    private PromotionManager() {
    }

    public boolean hasPromotion(String name){
        return promotions.containsKey(name);
    }

    public void setPromotion(String name, Promotion promotion){
        promotions.put(name, promotion);
        promotion.setName(name);
    }

    public Promotion getPromotion(String name){
        return promotions.containsKey(name)?promotions.get(name):null;
    }

    public void removePromotion(String name){
        promotions.remove(name);
    }

    public void setPromotionName(String lastName, String newName){
        if(promotions.containsKey(lastName)){
            Promotion promotion = promotions.get(lastName);
            promotion.setName(newName);

            promotions.remove(lastName);
            promotions.put(newName, promotion);
        }
    }

    public List<String> getPromotions(){
        return new ArrayList<>(promotions.keySet());
    }

}
