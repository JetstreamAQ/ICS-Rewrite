package umbral;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class Compendium {
    boolean moveUp,
            moveDown,
            moveLeft,
            moveRight,
            back;

    private int xSelector = 20,
                ySelector = 113,
                itemType = 0,
                itemID = 0,
                xSlot = 0,
                ySlot = 0;

    private String name = "",
                   flavour = "",
                   description = "",
                   craftingIngredients = "";

    private int cost = 0;
    private boolean isCrafted;

    private int[] craftingID;

    private Color itemCol = Color.CYAN;

    private void itemGrid(int yPos, String label, Graphics2D g, String id) {
        g.drawString(label, 20, yPos);

        yPos += 13;

        int item = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 14; j++) {
                if (Main.loadedImages.get(id + item) != null) {
                    g.drawImage(Main.loadedImages.get(id + item), 20 + (j * 48), yPos + (i * 48), null);
                } else {
                    g.drawImage(Main.loadedImages.get("missing"), 20 + (j * 48), yPos + (i * 48), null);
                }
                g.drawRect(20 + (j * 48), yPos + (i * 48), 48, 48);

                item++;
            }
        }
    }

    void draw (Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 64));
        g.drawString("Compendium", 10, 55);

        g.drawLine(10, 70, 700, 70);

        g.setColor(new Color(45, 16, 75));
        g.fillRect(xSelector, ySelector, 48, 48);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.ITALIC, 30));
        itemGrid(100, "Passive Items", g, "0");
        itemGrid(300, "Active Items", g, "A");

        g.drawRect(20, 475, 672, 180);

        g.setColor(itemCol);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 24));
        g.drawString(name, 30, 500);

        g.setColor(Color.ORANGE);
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 16));
        g.drawString("Credit Cost: " + cost, 30, 525);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(flavour, 30, 550);
        g.drawString(description, 30, 575);

        if (isCrafted) {
            g.drawString("Crafted with:", 30, 625);
            g.setFont(new Font("Liberation Mono", Font.PLAIN, 12));
            g.drawString(craftingIngredients, 30, 645);
        }

        g.setFont(new Font("Liberation Mono", Font.PLAIN, 18));
        g.drawString("Press [X] to return to the main menu", 20, 680);
    }

    void math() {
        selector();

        switch (itemType) {
            case 0:
                name = Main.itemLoader.items.getPassiveItems().get(itemID).getName();
                cost = Main.itemLoader.items.getPassiveItems().get(itemID).getCost();
                flavour = Main.itemLoader.items.getPassiveItems().get(itemID).getFlavour();
                description = Main.itemLoader.items.getPassiveItems().get(itemID).getDescription();
                isCrafted = Main.itemLoader.items.getPassiveItems().get(itemID).getIsCrafted();

                craftingID = (isCrafted) ? Main.itemLoader.items.getPassiveItems().get(itemID).getCraftingMats() : craftingID;
                itemCol = (Main.itemLoader.items.getPassiveItems().get(itemID).getCursedState()) ? Color.RED : Color.CYAN;
                break;

            case 1:
                name = Main.itemLoader.items.getActiveItems().get(itemID).getName();
                cost = Main.itemLoader.items.getActiveItems().get(itemID).getCost();
                flavour = Main.itemLoader.items.getActiveItems().get(itemID).getFlavour();
                description = Main.itemLoader.items.getActiveItems().get(itemID).getDescription();
                isCrafted = Main.itemLoader.items.getActiveItems().get(itemID).getIsCrafted();

                craftingID = (isCrafted) ? Main.itemLoader.items.getActiveItems().get(itemID).getCraftingMats() : craftingID;
                itemCol = (Main.itemLoader.items.getActiveItems().get(itemID).getCursedState()) ? Color.RED : Color.CYAN;
                break;
        }

        //grabbing the crafting ingredients
        if (isCrafted) {
            String temp;
            StringBuilder sb = new StringBuilder();
            for (int i : craftingID) {
                if (i - 42 >= 0) {
                    temp = (i != craftingID[craftingID.length - 1]) ? Main.itemLoader.items.getActiveItems().get(i - 42).getName() + " + " : Main.itemLoader.items.getActiveItems().get(i - 42).getName();
                } else {
                    temp = (i != craftingID[craftingID.length - 1]) ? Main.itemLoader.items.getPassiveItems().get(i).getName() + " + " : Main.itemLoader.items.getPassiveItems().get(i).getName();
                }
                craftingIngredients = sb.append(temp).toString();
            }
        }

        if (back) {
            back = false;
            moveLeft = false;
            moveRight = false;
            moveUp = false;
            moveDown = false;

            xSelector = 20;
            ySelector = 113;
            itemType = 0;
            itemID = 0;
            xSlot = 0;
            ySlot = 0;

            Main.gameState = 0;
        }
    }

    private void selector() {
        if (moveUp && ySlot > 0) {
            ySelector -= (ySlot == 3) ? 104:48;
            ySlot--;
            moveUp = false;

            itemType -= (ySlot == 2) ? 1:0;
            itemID += (ySlot == 2) ? 28:-14;
        } else if (moveDown && ySlot < 5) {
            //todo: remove once all active item slots are filled
            if (itemType == 1 && itemID + 14 >= Main.itemLoader.items.getActiveItems().size()) {
                moveDown = false;
                return;
            }

            ySelector += (ySlot == 2) ? 104:48;
            ySlot++;
            moveDown = false;

            itemType += (ySlot == 3) ? 1:0;
            itemID += (ySlot == 3) ? -28:14;
        } else if (moveLeft && xSlot > 0) {
            xSelector -= 48;
            xSlot--;
            moveLeft = false;

            itemID--;
        } else if (moveRight && xSlot < 13) {
            //todo: remove once all active item slots are filled
            if (itemType == 1 && itemID + 1 >= Main.itemLoader.items.getActiveItems().size()) {
                moveDown = false;
                return;
            }

            xSelector += 48;
            xSlot++;
            moveRight = false;

            itemID++;
        }
    }
}
