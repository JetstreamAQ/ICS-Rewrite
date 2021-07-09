package umbral.characters.items;

public class ActiveItem extends Item{
    private int id,
                cost,
                spreadMod,
                atkBonus,
                atkSpeed,
                hpBonus,
                spdBonus,
                healBonus,
                blockChance;

    private int[] craftingMats;

    private double creditMod;

    private String name,
                   flavour,
                   description;

    private boolean cursed,
                    isCrafted;

    /*get and set methods in order as they appear in items.yml*/
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getItemID() {return id;}
    public void setItemID(int id) {this.id = id;}

    public int getCost() {return cost;}
    public void setCost(int cost) {this.cost = cost;}

    public int getAtkBonus() {return atkBonus;}
    public void setAtkBonus(int atkBonus) {this.atkBonus = atkBonus;}

    public int getAtkSpeed() {return atkSpeed;}
    public void setAtkSpeed(int atkSpeed) {this.atkSpeed = atkSpeed;}

    public int getHpBonus() {return hpBonus;}
    public void setHpBonus(int hpBonus) {this.hpBonus = hpBonus;}

    public int getSpdBonus() {return spdBonus;}
    public void setSpdBonus(int spdBonus) {this.spdBonus = spdBonus;}

    public int getHealBonus() {return healBonus;}
    public void setHealBonus(int healBonus) {this.healBonus = healBonus;}

    public int getBlockChance() {return blockChance;}
    public void setBlockChance(int blockChance) {this.blockChance = blockChance;}

    public int[] getCraftingMats() {return craftingMats;}
    public void setCraftingMats(int[] craftingMats) {this.craftingMats = craftingMats;}

    public int getSpreadMod() {return spreadMod;}
    public void setSpreadMod(int spreadMod) {this.spreadMod = spreadMod;}

    public double getCreditMod() {return creditMod;}
    public void setCreditMod(double creditMod) {this.creditMod = creditMod;}

    public String getFlavour() {return flavour;}
    public void setFlavour(String flavour) {this.flavour = flavour;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public boolean getCursedState() {return cursed;}
    public void setCursedState(boolean cursed) {this.cursed = cursed;}

    public boolean getIsCrafted() {return isCrafted;}
    public void setIsCrafted(boolean isCrafted) {this.isCrafted = isCrafted;}
}
