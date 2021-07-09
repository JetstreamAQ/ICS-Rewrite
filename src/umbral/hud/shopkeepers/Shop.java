package umbral.hud.shopkeepers;

import umbral.audio.AudioPlayer;
import umbral.characters.items.ActiveItem;
import umbral.characters.items.Item;
import umbral.characters.items.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class Shop {
    private StringBuilder dialogue = new StringBuilder();
    private String[] craftingIngredients = new String[4];
    private Item[] items = new Item[4];
    private Item[] inventory = new Item[4];
    private boolean[] itemBought = new boolean[4];
    private ArrayList<Item> inStock = new ArrayList<>();

    private int playerHealth = 0;

    private boolean newDialogue = true,
                    introDialogueComplete = false,
                    repairAvailable = false;
    private int dialogueCount = 0; //Used to prevent glitchy dialogue

    private Shopkeeper activeKeep;

    private Random rand = new Random();

    private int creditCount,
                sideOffset = 0,
                heightOffset = 0,
                selectedItem = 0;

    private int bonusHealth,
                bonusSpreadMod,
                bonusDmg,
                bonusAS,
                bonusMS,
                bonusBlockChance,
                healBonus;

    public double bonusCreditMod;

    public Shop() {}

    public void drawShopWindow(Graphics2D g, Map<String, BufferedImage> images) {
        //Window Back
        g.setColor(new Color(45, 45, 45, 180));
        g.fillRect(50, 60, 625, 510);

        //Dialogue Box
        g.setColor(Color.WHITE);
        g.drawRect(200, 70, 465, 128);
        if (activeKeep != null) {
            //Avatar Box
            g.setColor(Color.WHITE);
            g.drawImage(images.get(activeKeep.getId()), 60, 70, null);
            g.drawRect(60, 70, 128, 128);

            g.setColor(new Color(53, 180, 129));
            g.setFont(new Font("Liberation Mono", Font.PLAIN, 16)); //KEEP NAME
            g.drawString(activeKeep.getName(), 205, 87);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Liberation Mono", Font.PLAIN, 14)); //Dialogue
            for (int i = 0; i < dialogue.toString().split("\n").length; i++) {
                g.drawString(dialogue.toString().split("\n")[i], 205, 105 + 15 * i);
            }
        }

        //Item Description
        g.setColor(Color.WHITE);
        g.drawRect(60, 210, 605, 172);

        if (items[selectedItem] != null) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Liberation Mono", Font.PLAIN, 16));
            g.drawString("Credit Cost: " + ((selectedItem == 3 && repairAvailable) ? 350 : items[selectedItem].getCost()), 65, 230);

            g.setColor(new Color(53, 180, 129));
            g.setFont(new Font("Liberation Mono", Font.PLAIN, 14));
            g.drawString((selectedItem == 3 && repairAvailable) ? "An eternal reminder of your mistakes." : items[selectedItem].getFlavour(), 65, 255);
            g.drawString((selectedItem == 3 && repairAvailable) ? "[Heal anywhere from 1-4 health]" : items[selectedItem].getDescription(), 65, 275);

            if (items[selectedItem].getIsCrafted() && ((!repairAvailable) || selectedItem < 3)) {
                g.drawString("Requires:", 65, 360);
                g.setFont(new Font("Liberation Mono", Font.PLAIN, 12));
                g.drawString(craftingIngredients[selectedItem], 65, 375);
            }
        }

        //Item Selector
        g.setColor((itemBought[selectedItem]) ? new Color(122, 0, 17) : new Color(34, 75, 38, 150));
        g.fillRect(60 + 307 * sideOffset, 390 + 60 * heightOffset, 298, 50);

        //Item Boxes
        int id = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                //Selection Box outline
                g.setColor(Color.WHITE);
                g.drawRect(60 + 307 * j, 390 + 60 * i, 298, 48);

                //Icon & text
                if (items[id] != null) {
                    g.drawRect(60 + 307 * j, 390 + 60 * i, 48, 48);
                    String itemType = (items[id] instanceof ActiveItem) ? "A" : "0";
                    if (id == 3 && repairAvailable) {
                        g.drawImage(images.get("repair"), 60 + 307 * j, 390 + 60 * i, null);
                    } else if (images.get(itemType + items[id].getItemID()) != null) {
                        g.drawImage(images.get(itemType + items[id].getItemID()), 60 + 307 * j, 390 + 60 * i, null);
                    } else {
                        g.drawImage(images.get("missing"), 60 + 307 * j, 390 + 60 * i, null);
                    }

                    g.setColor((items[id].getCursedState() && (id != 3 || !repairAvailable)) ? Color.RED : Color.CYAN);
                    g.setFont(new Font("Liberation Mono", Font.PLAIN, 15));
                    g.drawString((id == 3 && repairAvailable) ? "Repair" : items[id].getName(), 115 + 307 * j, 410 + 60 * i);
                }

                id++;
            }
        }

        //Information
        g.setColor(new Color(53, 180, 129));
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 16));
        g.drawString("Credits: " + creditCount, 60, 520);
        g.drawString("<Use ARROW keys to navigate; Z to purchase; X to quit>", 60, 540);

        //Fluff
        g.setColor(new Color(34, 75, 38));
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 12));
        g.drawString("Hermes Interface System v0.30.1 - Khaliyeh Corporation", 295, 565);
    }

    public int shopWindowMath(int bossNum, Shopkeeper activeKeep, int creditCount, boolean moveUp, boolean moveDown, boolean moveLeft, boolean moveRight, boolean A, boolean repairAvailable, int itemSlot, int playerHealth, Items item, AudioPlayer ap) {
        this.activeKeep = activeKeep;
        this.creditCount = creditCount;
        this.repairAvailable = repairAvailable;

        //Playing relevant shop music
        if (!ap.isPlaying("shop" + bossNum))
            ap.loop("shop" + bossNum);

        //Moving shit
        selectedItem -= (moveLeft && sideOffset == 1) ? 1 : 0;
        selectedItem += (moveRight && sideOffset == 0) ? 1 : 0;
        selectedItem += (moveDown && heightOffset == 0) ? 2 : 0;
        selectedItem -= (moveUp && heightOffset == 1) ? 2 : 0;

        sideOffset -= (moveLeft && sideOffset == 1) ? 1 : 0;
        sideOffset += (moveRight && sideOffset == 0) ? 1 : 0;
        heightOffset += (moveDown && heightOffset == 0) ? 1 : 0;
        heightOffset -= (moveUp && heightOffset == 1) ? 1 : 0;

        shopSystem(A, itemSlot, playerHealth, item, ap);

        //Picking the items to show up
        if (Arrays.toString(items).contains("null")) {
            for (int i = 0; i < 4; i++) {
                do {
                    boolean lottery = rand.nextInt(9) < 8;
                    int randPassive = rand.nextInt(item.getPassiveItems().size()),
                        randActive = rand.nextInt(item.getActiveItems().size());
                    items[i] = (lottery) ? item.getPassiveItems().get(randPassive) : item.getActiveItems().get(randActive);
                    //if (i == 0) items[i] = item.getActiveItems().get(4); //todo: remove once done testing and all actives are in
                } while (i != 0 && inStock.contains(items[i]));
                inStock.add(items[i]);

                if (items[i].getIsCrafted()) {
                    String temp;
                    StringBuilder sb = new StringBuilder();
                    for (int j : items[i].getCraftingMats()) {
                        String name = (j - 42 >= 0) ? item.getActiveItems().get(j - 42).getName()  : item.getPassiveItems().get(j).getName();
                        temp = (j != items[i].getCraftingMats()[items[i].getCraftingMats().length - 1]) ? name + " + " : name;
                        craftingIngredients[i] = sb.append(temp).toString();
                    }
                }
            }

            inStock.clear();
        }

        return this.creditCount;
    }

    public void resetState(AudioPlayer ap, int bossNum) {
        dialogue = new StringBuilder();
        introDialogueComplete = false;
        newDialogue = true;
        items = new Item[4];
        itemBought = new boolean[4];
        sideOffset = 0;
        heightOffset = 0;
        selectedItem = 0;
        playerHealth = 0;

        ap.stop("shop" + bossNum);
    }

    public boolean shopPhaseStandby() {
        return dialogueCount == 0;
    }

    public Item[] updateInventory() {
        return inventory;
    }

    public int healthUpdate() {
        return playerHealth;
    }

    public int getBonusHealth() {return bonusHealth;}
    public int getBonusSpreadMod() {return bonusSpreadMod;}
    public int getBonusDmg() {return bonusDmg;}
    public int getBonusAS() {return bonusAS;}
    public int getBonusMS() {return bonusMS;}
    public int getBonusBlockChance() {return bonusBlockChance;}
    public double getBonusCreditMod() {return bonusCreditMod;}

    //TODO: fix issue where repairing will just provide us with PH@T fucking stats(?)
    private void statChange(int selectedItem, int itemSlot, boolean cursedState) {
        if ((selectedItem != 3 || !repairAvailable) && !cursedState) {
            if (inventory[itemSlot] != null) {
                bonusHealth -= inventory[itemSlot].getHpBonus();
                bonusSpreadMod -= inventory[itemSlot].getSpreadMod();
                bonusDmg -= inventory[itemSlot].getAtkBonus();
                bonusAS -= inventory[itemSlot].getAtkSpeed();
                bonusMS -= inventory[itemSlot].getSpdBonus();
                bonusBlockChance -= inventory[itemSlot].getBlockChance();
                bonusCreditMod -= inventory[itemSlot].getCreditMod();
                playerHealth -= inventory[itemSlot].getHpBonus();
                healBonus -= items[selectedItem].getHealBonus();
            }

            bonusHealth += items[selectedItem].getHpBonus();
            bonusSpreadMod += items[selectedItem].getSpreadMod();
            bonusDmg += items[selectedItem].getAtkBonus();
            bonusAS += items[selectedItem].getAtkSpeed();
            bonusMS += items[selectedItem].getSpdBonus();
            bonusBlockChance += items[selectedItem].getBlockChance();
            bonusCreditMod += items[selectedItem].getCreditMod();
            playerHealth += items[selectedItem].getHpBonus();
            healBonus += items[selectedItem].getHealBonus();
        }
    }

    private void statChange(Item item) {
        bonusHealth -= item.getHpBonus();
        bonusSpreadMod -= item.getSpreadMod();
        bonusDmg -= item.getAtkBonus();
        bonusAS -= item.getAtkSpeed();
        bonusMS -= item.getSpdBonus();
        bonusBlockChance -= item.getBlockChance();
        bonusCreditMod -= item.getCreditMod();
        playerHealth -= item.getHpBonus();
    }

    /**
     * System for the player to buy and sell items
     *
     * TODO: Code looks like techno aids; gotta do something about it.
     *
     * @param A is the player pressing the shoot key?
     * @param itemSlot current item slot seleccted by the player
     * @param playerHealth current player health
     * @param item database of items
     * @param ap audio player system
     */
    private void shopSystem(boolean A, int itemSlot, int playerHealth, Items item, AudioPlayer ap) {
        selectedItem = (selectedItem >= 4 || selectedItem <= 0) ? 0 : selectedItem; //Prevents an OutOfBoundsException when you're trigger happy with the arrow keys

        //No item?  Do nothing.  Return.
        if (items[selectedItem] == null)
            return;

        newDialogue = (A && itemBought[selectedItem]) || (A && creditCount < items[selectedItem].getCost()) || (A && creditCount >= items[selectedItem].getCost()) || newDialogue;
        if (newDialogue) {
            int itemCost = (selectedItem == 3 && repairAvailable) ? 350 : items[selectedItem].getCost();

            //Should the item not be a repair "slot" we check if the current slot is cursed
            boolean currentSlotCursed = false;
            if (inventory[itemSlot] != null)
                currentSlotCursed = (selectedItem != 3 || !repairAvailable) && inventory[itemSlot].getCursedState();

            //On attempt to purchase crafted item, check if player has the materials necessary
            boolean noCraftingMaterials = false;
            if (items[selectedItem].getIsCrafted() && introDialogueComplete) {
                //Grab the total number of crafting materials required and push the material to the stack
                int numOfCraftingMats = items[selectedItem].getCraftingMats().length;
                Stack<Item> materials = new Stack<>(),
                            adjustmentStack = new Stack<>(); //used for stat adjustments
                for (int materialID : items[selectedItem].getCraftingMats()) {
                    Item material = (materialID - 42 >= 0) ? item.getActiveItems().get(materialID - 42) : item.getPassiveItems().get(materialID);
                    adjustmentStack.push(material);
                    materials.push(material);
                }

                //Cloning the inventory array to perform the check
                Item[] temp = new Item[4];
                System.arraycopy(inventory, 0, temp, 0, temp.length);

                //The check: iterate through each element in the cloned array
                //if the top element of the stack is within the array, we pop the stack.
                for (int i = 0; i < numOfCraftingMats; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (temp[j] != null) {
                            if (temp[j].getName().equals(materials.peek().getName()) && temp[j].getItemID() == materials.peek().getItemID()) {
                                materials.pop();
                                temp[j] = null;
                                break;
                            }
                        }
                    }
                }

                //If the stack is empty, the player has all the materials necessary to get the item
                noCraftingMaterials = !materials.isEmpty(); //Updating the boolean used for dialogue checks
                if (materials.isEmpty()) {
                    System.arraycopy(temp, 0, inventory, 0, inventory.length);
                    for (Item mat : adjustmentStack) {
                        statChange(mat);
                    }
                }
            }

            /* Determining what dialogue line to use (in order of appearance)
             * 1) When the shop window first appears, the displayed string will always be [""]; we use this to denote that the shop phase has begun
             * 2) When the player has already bought the item and tries to buy it again
             * 3) When the player doesn't have enough credits/materials to complete the purchase
             * 4) When the player initiates a repair
             * 5) When the player tries to replace a cursed item
             * 6) When the player buys a "special" item
             * 7) When a purchase can go through
             */
            String curDialogue = (dialogue.toString().length() == 0)                                    ? activeKeep.getIntroDialogue()[rand.nextInt(activeKeep.getIntroDialogue().length)] :
                                 (A && itemBought[selectedItem])                                        ? activeKeep.getSoldOut()[rand.nextInt(activeKeep.getSoldOut().length)] :
                                 (A && (creditCount < itemCost || noCraftingMaterials))                 ? activeKeep.getBuyDialogueNE()[rand.nextInt(activeKeep.getBuyDialogueNE().length)] :
                                 (A && selectedItem == 3 && repairAvailable)                            ? activeKeep.getRepairDialogue()[rand.nextInt(activeKeep.getRepairDialogue().length)] :
                                 (A && currentSlotCursed)                                               ? activeKeep.getCursedItemDialogue()[rand.nextInt(activeKeep.getCursedItemDialogue().length)] :
                                 (A && activeKeep.getSpecialItem() == items[selectedItem].getItemID())  ? activeKeep.getSpecialDialogue() :
                                 (A)                                                                    ? activeKeep.getBuyDialogue()[rand.nextInt(activeKeep.getBuyDialogue().length)] : dialogue.toString();
            newDialogue = false; //Marks that the dialogue has been selected
            if (dialogueCount < 1) { //Check to ensure that only one dialogue item is in the queue
                new Thread(() -> { //Thread to avoid stalling when the dialogue is "printed"
                    dialogueCount++; //Says a dialogue piece is underway
                    dialogue = new StringBuilder();

                    //Tag a char into the display char, one at a time
                    for (int i = 0; i < curDialogue.length(); i++) {
                        dialogue.append(curDialogue.charAt(i));

                        if (i % 3 == 0)
                            ap.play("speak"); //Every 3 characters, play a sound

                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    dialogueCount = 0;
                    introDialogueComplete = true;
                }).start();

                this.playerHealth = playerHealth; //updating the playerHealth kept in this scope
                //int oldCreditCount = this.creditCount; //Used to determine the results of later checks

                //TODO: looks and feels ugly.
                if (!(A && itemBought[selectedItem]) && introDialogueComplete && items[selectedItem] != null && inventory[itemSlot] == null && creditCount >= itemCost && !noCraftingMaterials) {
                    this.creditCount -= itemCost;
                    itemBought[selectedItem] = (A /*&& oldCreditCount >= itemCost*/ || itemBought[selectedItem]);
                    statChange(selectedItem, itemSlot, false);
                    inventory[itemSlot] = (selectedItem == 3 && repairAvailable) ? inventory[itemSlot] : items[selectedItem];

                    this.playerHealth += (selectedItem == 3 && repairAvailable) ? (rand.nextInt(3) + 1 + healBonus) : 0;
                } else if (!(A && itemBought[selectedItem]) && introDialogueComplete && items[selectedItem] != null && creditCount >= itemCost && !noCraftingMaterials) {
                    this.creditCount -= (inventory[itemSlot].getCursedState()) ? 0 : itemCost;
                    itemBought[selectedItem] = (A /*&& oldCreditCount >= itemCost*/ && !inventory[itemSlot].getCursedState()) || itemBought[selectedItem] || (A /*&& oldCreditCount >= itemCost*/ && selectedItem == 3 && repairAvailable);
                    statChange(selectedItem, itemSlot, inventory[itemSlot].getCursedState());
                    inventory[itemSlot] = (selectedItem == 3 && repairAvailable || inventory[itemSlot].getCursedState()) ? inventory[itemSlot] : items[selectedItem];

                    this.playerHealth += (selectedItem == 3 && repairAvailable) ? (rand.nextInt(3) + 1 + healBonus) : 0;
                }
            }
        }
    }

}
