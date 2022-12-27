package com.akgroup.project.world.planter;

import com.akgroup.project.util.Vector2D;

public class EquatorPlanter extends Planter {
    private int[] equatorWidth;
    private int[] equatorHeight;

    protected EquatorPlanter(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(Vector2D vector2D, int valueChange) {
    }

    public void setEquatorWidth(int equatorWidth) {
        int equatorStart;
        int equatorEnd;
        switch (width % 2) {
            case 0:

        }
//        this.equatorWidth =;
    }

    public void setEquatorHeight(int equatorHeight) {
        int equatorStart = Math.round(height / 2);
        int equatorEnd = Math.round(height / 2);
        switch (height % 2) {
            case 0 -> {
                equatorStart -= (equatorHeight) / 2;
                equatorEnd += (equatorHeight) / 2;
            }
            case 1 -> {
                equatorStart -= (equatorHeight - 1) / 2;
                equatorEnd += (equatorHeight - 1) / 2;
            }
        }
        this.equatorHeight = new int[]{equatorStart, equatorEnd};
    }

    public int[] getEquatorWidth() {
        return equatorWidth;
    }

    public int[] getEquatorHeight() {
        return equatorHeight;
    }

    public void setEquator() {

    }

    private void findEquator() {
        int eWidth = Math.toIntExact(Math.round(width * 0.2));
        int eHeight = Math.toIntExact(Math.round(height * 0.2));
        if (eWidth == 0) {
            eWidth = 1;
        }
        if (eHeight == 0) {
            eHeight = 1;
        }
        prepareEquatorValue(eWidth, width);
        prepareEquatorValue(eHeight, height);

    }

    private void prepareEquatorValue(int eVal, int val) {
        switch (val % 2) {
            case 0:
                if (eVal % 2 == 1) {
                    eVal += 1;
                }
                break;
            case 1:
                if (eVal % 2 == 0) {
                    eVal += 1;
                }
                break;
        }
    }


}
