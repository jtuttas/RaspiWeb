/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Position;

/**
 *
 * @author Jörg
 */
public class Stepper {

    private static Stepper instance;

    LED out1;
    LED out2;
    LED out3;
    LED out4;
    StepperPosition position;
    private ArrayList<StepperPositionChangedListener> listeners = new ArrayList<>();

    boolean[][] steps = {{true, true, false, false, false, false, false, true},
    {false, true, true, true, false, false, false, false},
    {false, false, false, true, true, true, false, false},
    {false, false, false, false, false, true, true, true}};
    int stepIndex;

    private Stepper(LED led1, LED led2, LED led3, LED led4) {
        out1 = led1;
        out2 = led2;
        out3 = led3;
        out4 = led4;
        position = new StepperPosition();
        stepIndex = 0;
    }

    public void addListener(StepperPositionChangedListener l) {
        listeners.add(l);
    }

    public void removeListener(StepperPositionChangedListener l) {
        listeners.remove(l);
    }

    public static Stepper getInstance(LED led1, LED led2, LED led3, LED led4) {
        if (instance == null) {
            instance = new Stepper(led1, led2, led3, led4);
        }
        return instance;
    }

    public static Stepper getInstance() throws Exception {
        if (instance == null) {
            Exception e = new Exception("Stepper instance is Null, Exception!");
            throw e;
        }
        return instance;

    }

    public void left(int steps, long delay) {
        for (StepperPositionChangedListener l : listeners) {
            l.moving(true);
        }
        for (int i = 0; i < steps; i++) {
            try {
                left();
            } catch (StepperExeption ex) {
                Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.clear();
        for (StepperPositionChangedListener l : listeners) {
            l.positionChanged(position);
            l.moving(false);
        }
    }

    private void left() throws StepperExeption {
        stepIndex--;
        if (stepIndex < 0) {
            stepIndex = 7;
        }
        setOut();
        position.left();
    }

    public void right(int steps, long delay) {
        for (StepperPositionChangedListener l : listeners) {
            l.moving(true);
        }

        for (int i = 0; i < steps; i++) {
            try {
                right();
            } catch (StepperExeption ex) {
                Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.clear();
        for (StepperPositionChangedListener l : listeners) {
            l.positionChanged(position);
            l.moving(false);
        }
    }

    public void reset() {
        for (StepperPositionChangedListener l : listeners) {
            l.moving(true);
        }

        if (position.getPosition() > 180) {
            try {
                while (true) {
                    right();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (StepperExeption ex) {
                Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                while (true) {
                    left();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (StepperExeption ex) {
                Logger.getLogger(Stepper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.clear();
        for (StepperPositionChangedListener l : listeners) {
            l.positionChanged(position);
            l.moving(false);
        }
    }

    private void right() throws StepperExeption {
        stepIndex++;
        if (stepIndex > 7) {
            stepIndex = 0;
        }
        setOut();
        position.right();
    }

    public StepperPosition getPostion() {
        return position;
    }

    private void clear() {
        out1.turnOn(false);
        out2.turnOn(false);
        out3.turnOn(false);
        out4.turnOn(false);
    }

    private void setOut() {
        out1.turnOn(steps[0][stepIndex]);
        out2.turnOn(steps[1][stepIndex]);
        out3.turnOn(steps[2][stepIndex]);
        out4.turnOn(steps[3][stepIndex]);

        System.out.println("3=" + out3.getDimValue());
    }

    public static void main(String[] args) {
        Stepper s = Stepper.getInstance(LED.getInstance(12), LED.getInstance(16), LED.getInstance(13), LED.getInstance(21));
        System.out.println("Eine Umdrehung rechts");
        s.right(1024, 10);
        System.out.println("Stopp");
        try {
            Thread.sleep(2000);

        } catch (InterruptedException ex) {
            Logger.getLogger(Stepper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Eine Umdrehung links");
        s.left(1024, 20);
        s.clear();

    }

}
