package umbral.hud.shopkeepers;

public class Shopkeeper {
    private String name,
                   id;

    private boolean special;

    private int afterBoss,
                specialItem;

    private String[] introDialogue,
                     buyDialogue,
                     repairDialogue,
                     cursedItemDialogue,
                     buyDialogueNE,
                     soldOut;

    private String specialDialogue;

    public String getName() {return name;}
    public String getId() {return id;}
    public boolean getSpecial() {return special;}
    public int getAfterBoss() {return afterBoss;}
    public int getSpecialItem() {return specialItem;}
    public String[] getIntroDialogue() {return introDialogue;}
    public String[] getBuyDialogue() {return buyDialogue;}
    public String[] getRepairDialogue() {return repairDialogue;}
    public String[] getCursedItemDialogue() {return cursedItemDialogue;}
    public String[] getBuyDialogueNE() {return buyDialogueNE;}
    public String[] getSoldOut() {return soldOut;}
    public String getSpecialDialogue() {return specialDialogue;}

    public void setName(String name) {this.name = name;}
    public void setId(String id) {this.id = id;}
    public void setSpecial(boolean special) {this.special = special;}
    public void setAfterBoss(int afterBoss) {this.afterBoss = afterBoss;}
    public void setSpecialItem(int specialItem) {this.specialItem = specialItem;}
    public void setIntroDialogue(String[] introDialogue) {this.introDialogue = introDialogue;}
    public void setBuyDialogue(String[] buyDialogue) {this.buyDialogue = buyDialogue;}
    public void setRepairDialogue(String[] repairDialogue) {this.repairDialogue = repairDialogue;}
    public void setCursedItemDialogue(String[] cursedItemDialogue) {this.cursedItemDialogue = cursedItemDialogue;}
    public void setBuyDialogueNE(String[] buyDialogueNE) {this.buyDialogueNE = buyDialogueNE;}
    public void setSoldOut(String[] soldOut) {this.soldOut = soldOut;}
    public void setSpecialDialogue(String specialDialogue) {this.specialDialogue = specialDialogue;}
}
