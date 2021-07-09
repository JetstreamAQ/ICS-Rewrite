package umbral.characters.items;

import java.util.List;

public class Items {
    private List<PassiveItem> passiveItems;
    private List<ActiveItem> activeItems;

    public List<PassiveItem> getPassiveItems() {return passiveItems;}
    public void setPassiveItems(List<PassiveItem> passiveItems) {this.passiveItems = passiveItems;}
    public List<ActiveItem> getActiveItems() {return activeItems;}
    public void setActiveItems(List<ActiveItem> activeItems) {this.activeItems = activeItems;}
}