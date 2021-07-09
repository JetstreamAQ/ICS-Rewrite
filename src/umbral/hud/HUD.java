package umbral.hud;

import umbral.characters.items.ActiveItem;
import umbral.characters.items.Item;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class HUD {
    //Health values
    private int plrHP,
                plrMaxHP,
                bossHP,
                bossMaxHP,
                creditCount;

    private double blockChance;
    private boolean noMove,
                    shopPhase;

    public HUD (){}

    private int overlayCrop(BufferedImage overlay, double maxHP, double hp) {
        return (int)(overlay.getWidth() * (hp / maxHP));
    }

    //Takes in images for back and overlay bars
    public void drawPlayerComp(BufferedImage playerBarBack, BufferedImage playerBarOverlay, BufferedImage noMove, BufferedImage block, Graphics2D g) {
        g.drawImage(playerBarBack, 10, 10, null);

        if (plrHP * 1.0 / plrMaxHP > 0.01) {
            g.drawImage(playerBarOverlay.getSubimage(0, 0, overlayCrop(playerBarOverlay, plrMaxHP, plrHP), playerBarOverlay.getHeight()), 10, 10, null);
        }

        if (this.noMove)
            g.drawImage(noMove, 262, 15, null);

        if (blockChance >= 100)
            g.drawImage(block, 220, 14, null);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 30));
        g.drawString(plrHP + "/" + plrMaxHP, 35, 40);

        if (!shopPhase) {
            g.setColor(new Color(53, 180, 129));
            g.setFont(new Font("Liberation Mono", Font.PLAIN, 16));
            g.drawString("Credits:" + creditCount, 10, 70);
        }
    }

    public void drawBossComp(BufferedImage bossBarBack, BufferedImage bossBarOverlay, String bossName, Graphics2D g) {
        g.drawImage(bossBarBack, 305, 665, null);

        if (bossHP * 1.0 / bossMaxHP > 0.01) {
            g.drawImage(bossBarOverlay.getSubimage(0, 0, overlayCrop(bossBarOverlay, bossMaxHP, bossHP), bossBarOverlay.getHeight()), 305, 665, null);
        }

        g.setColor(Color.lightGray);
        g.setFont(new Font("Liberation Mono", Font.ITALIC, 12));
        g.drawString(bossName, 315, 677);
    }

    public void drawItemSlots(int selectedItem, Item[] inventory, Map<String, BufferedImage> images, Graphics2D g, int cd, int lastCD) {
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 16));

        String sample = "WASD";
        int rectNum = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 0 || j == 1) {
                    if (selectedItem == rectNum) {
                        g.setColor(new Color(34, 75, 38, 150));
                        if (inventory[rectNum] != null)
                            g.setColor((inventory[rectNum].getCursedState()) ? new Color(108, 41, 122, 150) : new Color(34, 75, 38, 150));

                        g.fillRect(10 + (j * 48), 585 + (i * 48), 48, 48);
                    }

                    if (inventory[rectNum] != null) {
                        if (inventory[rectNum] instanceof ActiveItem && cd > 0) {
                            g.setColor(new Color(143, 139, 134, 190));
                            g.fillRect(10 + (j * 48), 633 + (i * 48) - ((int) (48 * ((double) cd / lastCD))), 48, (int) (48 * ((double) cd / lastCD)));
                        }

                        String itemType = (inventory[rectNum] instanceof ActiveItem) ? "A" : "0";
                        String imgID = (images.get(itemType + inventory[rectNum].getItemID()) != null) ? itemType + inventory[rectNum].getItemID() : "missing";
                        g.drawImage(images.get(imgID), 10 + (j * 48), 585 + (i * 48), null);
                    }

                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(10 + (j * 48), 585 + (i * 48), 48, 48);
                    g.drawString(sample.substring(rectNum, rectNum + 1), 15 + (j * 48), 630 + (i * 48));
                    rectNum++;
                }
            }
        }
    }

    public void healthUpdate(int plrMaxHP, int plrHP, int bossMaxHP, int bossHP) {
        this.plrMaxHP = plrMaxHP;
        this.plrHP = plrHP;
        this.bossMaxHP = bossMaxHP;
        this.bossHP = bossHP;
    }

    public void statusUpdate(boolean noMove, double blockChance) {
        this.noMove = noMove;
        this.blockChance = blockChance;
    }

    public void updateCredits(int creditCount) {this.creditCount = creditCount;}
    public void updatePhase(boolean shopPhase) {this.shopPhase = shopPhase;}
}
