package umbral;

import kuusisto.tinysound.TinySound;
import umbral.audio.AudioPlayer;
import umbral.characters.base.FontLoad;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.ActiveItem;
import umbral.characters.items.ItemsLoad;
import umbral.characters.items.PassiveItem;
import umbral.characters.player.Player;
import umbral.characters.player.PlrChar;
import umbral.hud.shopkeepers.Shopkeeper;
import umbral.hud.shopkeepers.ShopkeeperLoad;
import umbral.imageLoader.ImageLoader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Z;

class Main extends Thread {
    static boolean isRunning = true;

    //graphics shite
    private BufferStrategy bs;
    private Graphics2D g;
    private Canvas canvas;
    static SpriteDraw sd = new SpriteDraw();

    //items
    static ItemsLoad itemLoader = new ItemsLoad();

    //images
    static Map<String, BufferedImage> loadedImages;

    //audio
    static AudioPlayer ap = new AudioPlayer();
    //static float vol = 0.6f; //todo: do something with this shit

    //shopkeepers
    static Map<Integer, ArrayList<Shopkeeper>> loadedShopkeepers;

    //player characters
    static PlrChar characters = new PlrChar();
    static List<Player> plr = characters.getCharacters();

    //game states
    private Menu menu = new Menu();
    static CharSelect charSelect = new CharSelect();
    static Game game = new Game();
    private Instructions instructions = new Instructions();
    private Credits credits = new Credits();
    private Compendium compendium = new Compendium();

    //game state
    static int gameState = 0;

    //FPS
    private int fps = 0,
                lastFPS = 0;

    //Key-binding method, used to define new keyBindings
    private static void addBinding (JComponent comp, int keyCode, String id, boolean keyRelease, ActionListener actionListener) {
        //Defining a new InputMap keyBinding which reads input when user focus is on the program window
        InputMap inputMap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Defining a new ActionMap
        ActionMap actionMap = comp.getActionMap();

        //Setting the InputMap to check for a specific key press; will execute a snippet at different times.
        //Snippet execution dependent on 'keyRelease'
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, keyRelease), id);

        //Placing a new anonymous class into the ActionMap; snippet inside will be executed on key press
        actionMap.put(id, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }

    private Main() {
        /*WINDOW START*/
        JFrame frame = new JFrame("Ghetto Arcade Shooter");
        frame.setSize(720, 720);
        frame.setResizable(false);
        frame.setVisible(true);

        //Instead of immediately killing the program upon the window closing, we stop the game loop and shut down
        //the necessary processes
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                isRunning = false;
            }
        });

        JPanel pane = (JPanel)frame.getContentPane();
        pane.setPreferredSize(new Dimension(720, 720));
        pane.setLayout(null);

        GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        canvas = new Canvas(config);
        canvas.setBounds(0, 0, 720, 720);
        canvas.setBackground(Color.black);
        frame.add(canvas);

        canvas.setIgnoreRepaint(true);
        /*WINDOW END*/

        /*GAME KEYS START*/
        //On Press
        addBinding (pane, VK_UP, "moveUp", false,(e -> {
            switch (gameState){
                case 0:
                    menu.moveUp = true;
                    break;

                case 3:
                    compendium.moveUp = true;
                    break;

                case 5:
                    game.moveUp = true;
                    break;
            }
        }));
        addBinding (pane, VK_DOWN, "moveDown", false,(e -> {
            switch (gameState){
                case 0:
                    menu.moveDown = true;
                    break;

                case 3:
                    compendium.moveDown = true;
                    break;

                case 5:
                    game.moveDown = true;
                    break;
            }
        }));
        addBinding (pane, VK_LEFT, "moveLeft", false,(e -> {
            switch (gameState){
                case 0:
                    menu.volDown = true;
                    break;

                case 3:
                    compendium.moveLeft = true;
                    break;

                case 4:
                    charSelect.moveLeft = true;
                    break;

                case 5:
                    game.moveLeft = true;
                    break;
            }
        }));
        addBinding (pane, VK_RIGHT, "moveRight", false,(e -> {
            switch (gameState) {
                case 0:
                    menu.volUp = true;
                    break;

                case 3:
                    compendium.moveRight = true;
                    break;

                case 4:
                    charSelect.moveRight = true;
                    break;

                case 5:
                    game.moveRight = true;
                    break;
            }
        }));
        addBinding (pane, VK_Z, "shoot", false, (e -> {
            switch (gameState) {
                case 0:
                    menu.selection = true;
                    break;

                case 4:
                    charSelect.select = true;
                    break;

                case 5:
                    game.isShooting = true;
                    break;
            }
        }));
        addBinding (pane, VK_X, "toggleSpecial", false, (e -> {
            switch (gameState) {
                case 1:
                    instructions.back = true;
                    break;

                case 2:
                    credits.back = true;
                    break;

                case 3:
                    compendium.back = true;
                    break;

                case 4:
                    charSelect.back = true;
                    break;

                case 5:
                    game.toggleSpecial = true;
                    break;
            }
        }));
        addBinding (pane, VK_SPACE, "focusToggle", false, (e -> game.focusToggle = !game.focusToggle));
        addBinding (pane, VK_W, "item1Sel", false, (e -> game.selectedSlot = 0));
        addBinding (pane, VK_A, "item2Sel", false, (e -> game.selectedSlot = 1));
        addBinding (pane, VK_S, "item3Sel", false, (e -> game.selectedSlot = 2));
        addBinding (pane, VK_D, "item4Sel", false, (e -> game.selectedSlot = 3));

        //On Release
        addBinding (pane, VK_UP, "upStop", true,(e -> {
            switch (gameState) {
                case 0:
                    menu.moveUp = false;
                    break;

                case 3:
                    compendium.moveUp = false;
                    break;

                case 5:
                    game.moveUp = false;
                    break;
            }
        }));
        addBinding (pane, VK_DOWN, "downStop", true,(e -> {
            switch (gameState) {
                case 0:
                    menu.moveDown = false;
                    break;

                case 3:
                    compendium.moveDown = false;
                    break;

                case 5:
                    game.moveDown = false;
                    break;
            }
        }));
        addBinding (pane, VK_LEFT, "leftStop", true,(e -> {
            switch (gameState) {
                case 0:
                    menu.volDown = false;
                    break;

                case 3:
                    compendium.moveLeft = false;
                    break;

                case 4:
                    charSelect.moveLeft = false;
                    break;

                case 5:
                    game.moveLeft = false;
                    break;
            }
        }));
        addBinding (pane, VK_RIGHT, "rightStop", true,(e -> {
            switch (gameState) {
                case 0:
                    menu.volUp = false;
                    break;

                case 3:
                    compendium.moveRight = false;
                    break;

                case 4:
                    charSelect.moveRight = false;
                    break;

                case 5:
                    game.moveRight = false;
                    break;
            }
        }));
        addBinding (pane, VK_Z, "shootStop", true, (e -> {
            switch (gameState) {
                case 0:
                    menu.selection = false;
                    break;

                case 4:
                    charSelect.select = false;
                    break;

                case 5:
                    game.isShooting = false;
                    break;
            }
        }));
        addBinding (pane, VK_X, "un-toggleSpecial", true, (e -> {
            switch (gameState) {
                case 1:
                    instructions.back = false;
                    break;

                case 2:
                    credits.back = false;
                    break;

                case 3:
                    compendium.back = false;
                    break;

                case 4:
                    charSelect.back = false;
                    break;

                case 5:
                    game.toggleSpecial = false;
                    break;
            }
        }));
        /*GAME KEYS END*/

        itemLoader.loadYAML(); //Loading the items

        //TODO: remove this eventually; used to check items which have been loaded
        System.out.println("[PASSIVE ITEMS LOADED]\n-----------------------------------------");
        for (PassiveItem item : itemLoader.items.getPassiveItems()) {
            System.out.println(item.getName());
        }
        System.out.println("\n[ACTIVE ITEMS LOADED]\n-----------------------------------------");
        for (ActiveItem item : itemLoader.items.getActiveItems()) {
            System.out.println(item.getName());
        }
        System.out.println("-----------------------------------------");
        //TODO: read the above todo

        ImageLoader imageLoader = new ImageLoader();
        loadedImages = imageLoader.getLoadedImages();

        ShopkeeperLoad shopkeepLoad = new ShopkeeperLoad();
        loadedShopkeepers = shopkeepLoad.getLoadedShopkeepers();

        //Loading the singular only font we end up using
        //TODO: Consider an alternative or using some other fonts to make this worth while
        FontLoad fl = new FontLoad();
        fl.loadFont();

        canvas.createBufferStrategy(2);
        do {
            bs = canvas.getBufferStrategy();
        } while (bs == null);
        start();
    }

    private Graphics2D getBuffer() {
        if (g == null) {
            try {
                g = (Graphics2D) bs.getDrawGraphics();
            } catch (IllegalStateException e) {
                return null;
            }
        }

        return g;
    }

    private boolean update() {
        g.dispose();
        g = null;

        try {
            bs.show();
            Toolkit.getDefaultToolkit();
            return (!bs.contentsLost());
        } catch (Exception e) {
            return true;
        }
    }

    public void run() {
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000 / TARGET_FPS;

        //Game Loop
        main: while (isRunning) {
            long currentTime = System.currentTimeMillis();

            math(); //Self-explanatory, running any math necessary
            do {
                Graphics2D g = getBuffer();

                //Breaking out of the loop early
                if (!isRunning)
                    break main;

                draw(g); //Drawing game contents onto the frame; self-explanatory
            } while (!update());

            //Updating the FPS counters
            long updateDuration = System.currentTimeMillis() - currentTime;
            lastFPS += updateDuration;
            fps++;

            //TODO: remove when done.
            if (lastFPS >= 1000) {
                System.out.println(fps);
                lastFPS = 0;
                fps = 0;
            }

            //Limiting the FPS to 60
            if (OPTIMAL_TIME - updateDuration > 0) {
                try {
                    Thread.sleep(OPTIMAL_TIME - updateDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        TinySound.shutdown();
        System.exit(0);
    }

    private void draw (Graphics2D g) {
        canvas.update(g); //updating the canvas contents

        switch (gameState) {
            case 0:
                menu.draw(g);
                break;

            case 1:
                instructions.draw(g);
                break;

            case 2:
                credits.draw(g);
                break;

            case 3:
                compendium.draw(g);
                break;

            case 4:
                charSelect.draw(g);
                break;

            case 5:
                game.draw(g);
                break;
        }
    }

    private void math () {
        switch (gameState) {
            case 0:
                ap.loop("main"); //TODO: replace the track with a more suitable one
                menu.math();
                break;

            case 1:
                instructions.math();
                break;

            case 2:
                credits.math();
                break;

            case 3:
                compendium.math();
                break;

            case 4:
                ap.stop("main"); //TODO: same as above
                charSelect.math();
                break;

            case 5:
                game.math();
                break;
        }
    }

    public static void main (String[] args) {
        new Main();
    }
}