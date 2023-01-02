package com.akgroup.project.world.planter;

import com.akgroup.project.util.Vector2D;

public class EquatorPlanter extends Planter {
    private int[] equatorHeight;

    public EquatorPlanter(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
        super.init();
        setEquatorHeight();
        increasePossibilityBecauseOfEquator();
    }

    @Override
    public void update(Vector2D vector2D, int valueChange) {
    }


    private void setEquatorHeight() {
        int equatorHeight = findEquator();
        int equatorStart = Math.round(height / 2);
        int equatorEnd = Math.round(height / 2);
        switch (height % 2) {
            case 0 -> {
                equatorStart -= ((equatorHeight) / 2) + 1;
                equatorEnd += ((equatorHeight) / 2) - 1;
            }
            case 1 -> {
                equatorStart -= (equatorHeight - 1) / 2;
                equatorEnd += (equatorHeight - 1) / 2;
            }
        }
        this.equatorHeight = new int[]{Math.max(equatorStart, 0), equatorEnd};
    }

    public int[] getEquatorHeight() {
        return equatorHeight;
    }

    private int findEquator() {
        int eHeight = Math.toIntExact(Math.round(height * 0.2));
        eHeight = Math.max(1, eHeight);
        eHeight = prepareEquatorValue(eHeight, height);
        return eHeight;
    }

    private int prepareEquatorValue(int eVal, int val) {
        if(val % 2 == 0){
            if(eVal % 2 == 1) eVal += 1;
        }else{
            if(eVal % 2 == 0) eVal += 1;
        }

        return eVal;
    }

    private void increasePossibilityBecauseOfEquator() {
        Vector2D bottomLeft = new Vector2D(0, equatorHeight[0]);
        Vector2D topRight = new Vector2D(width - 1, equatorHeight[1]);
        increasePossibility(bottomLeft, topRight);
    }

}
