package propertyanimation.sriyank.com.propertyanimation;


import android.util.Log;

public class Car {

    public interface carSpeedUpListener{
        void speedChanged(int newSpeed);
    }

    private carSpeedUpListener listener;
    private int speed=0;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
//        Log.d("Car","speed: "+speed);
        this.speed = speed;
        listener.speedChanged(speed);
    }

    public void setListener(carSpeedUpListener listener) {
        this.listener = listener;
    }
}
