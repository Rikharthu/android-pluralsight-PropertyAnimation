package propertyanimation.sriyank.com.propertyanimation;


import android.view.animation.Interpolator;

public class PowerInterpolator implements Interpolator {
    public PowerInterpolator() {
    }


    @Override
    public float getInterpolation(float t) {

        return (float)Math.pow(t,2);
    }
}
