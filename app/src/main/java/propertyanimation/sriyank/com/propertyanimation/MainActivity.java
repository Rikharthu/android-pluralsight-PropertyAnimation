package propertyanimation.sriyank.com.propertyanimation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

/*
Value animator
 - animates an int, float or object value over time
 - calculates animation values and contains the timing details of each animation
 - does not operate on the propery of the object directly (uses listeners and ObjectAnimator)
   ObjectAnimator.ofType(object,"propertyName",[starting value], [ending value]);
 */
public class MainActivity extends AppCompatActivity implements Car.carSpeedUpListener, Animator.AnimatorListener {

    private RelativeLayout rootLayout;

    private ImageView mImage;
    private SeekBar carSpeedSb;
    private boolean isRed =true;

    private ObjectAnimator colorAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout= (RelativeLayout) findViewById(R.id.rootLayout);

        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You click on an Image!", Toast.LENGTH_SHORT).show();
            }
        });

        carSpeedSb= (SeekBar) findViewById(R.id.carSpeedSb);

        // using ObjectAnimator on a custom class
        // simulate car speed acceleration
        Car car = new Car();
        car.setListener(this);
        // uses setter method
        Animator carAnimator = ObjectAnimator.ofInt(car,"speed",0,1000);
//        carAnimator.setInterpolator(new AccelerateInterpolator());
//        carAnimator.setInterpolator(new BounceInterpolator());
        carAnimator.setInterpolator(new PowerInterpolator());
//        carAnimator.setInterpolator(new DecelerateInterpolator());
        carAnimator.setDuration(3000);
        carAnimator.start();


        colorAnimator= ObjectAnimator.ofObject(rootLayout
                , "backgroundColor", new ArgbEvaluator()
                , Color.RED, Color.BLUE);
        colorAnimator.addListener(this);
        colorAnimator.setDuration(500);
        colorAnimator.start();
        isRed=false;
    }

    // Flip
    public void rotateAnimation(View view) {

        // from XML:
//        Animator anim = AnimatorInflater
//                .loadAnimator(this, R.animator.rotate);
//        anim.setTarget(mImage);
//        anim.start();

        // with code:
        ObjectAnimator anim = ObjectAnimator.ofFloat(mImage,"rotation",0.0f,360.0f);
        anim.setDuration(500);
        anim.start();
    }

    public void scaleAnimation(View view) {
        // в отличии от Tween Animations, Property Animations реально меняют параметры View
        // а не создают иллюзию
        // Actual object properties ARE modified!
        Animator anim = AnimatorInflater
                .loadAnimator(this, R.animator.scale);
        anim.setTarget(mImage);
        anim.start();

        // With code:
//        ObjectAnimator anim = ObjectAnimator.ofFloat(mImage,"scaleX",1.0f,2.0f);
//        anim.setDuration(500);
//        anim.start();

    }

    public void translateAnimation(View view) {

//        Animator anim = AnimatorInflater
//                .loadAnimator(this, R.animator.translate);
//        anim.setTarget(mImage);
//        anim.start();

        ObjectAnimator anim = ObjectAnimator.ofFloat(mImage,"translationX",0.0f,200.0f);
        anim.setDuration(500);
        anim.start();

    }

    public void alphaAnimation(View view) {

        Animator anim = AnimatorInflater
                .loadAnimator(this, R.animator.alpha);
        anim.setTarget(mImage);
        anim.start();

    }

    public void setAnimation(View view) {

        // XML
//        Animator anim = AnimatorInflater
//                .loadAnimator(this, R.animator.set);
//        anim.setTarget(mImage);
//        anim.start();

        // CODE
        // Parent
        AnimatorSet rootSet = new AnimatorSet();

        // Child 1
        ObjectAnimator rotate = ObjectAnimator.ofFloat(mImage,"rotation",0.0f,360.0f);
        rotate.setDuration(500);

        // Child 2
        AnimatorSet childSet = new AnimatorSet();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImage,"scaleX",1,2);
        scaleX.setDuration(300);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImage,"scaleY",1,2);
        scaleX.setDuration(300);

//        rootSet.play(rotate).before(childSet);
        rootSet.playSequentially(rotate,childSet);
        childSet.play(scaleX).with(scaleY);

        rootSet.start();

    }


    @Override
    public void speedChanged(int speed) {
        carSpeedSb.setProgress(speed);
    }

    public void evaluateBGColor(View view) {
        // using Evaluator with ofObject(...

        if(isRed) {
            isRed =false;
            ObjectAnimator anim = ObjectAnimator.ofObject(rootLayout
                    , "backgroundColor", new ArgbEvaluator()
                    , Color.RED, Color.BLUE);
            anim.setDuration(1000);
            anim.start();
        }else{
            isRed =true;
            ObjectAnimator anim = ObjectAnimator.ofObject(rootLayout
                    , "backgroundColor", new ArgbEvaluator()
                    , Color.BLUE, Color.RED);
            anim.setDuration(1000);
            anim.start();
        }
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        Log.d("MainActivity","onAnimationEnd");
        if(animator==colorAnimator) {
            if (isRed) {
                Log.d("MainActivity","isRed");
                isRed = false;
                colorAnimator= ObjectAnimator.ofObject(rootLayout
                        , "backgroundColor", new ArgbEvaluator()
                        , Color.RED, Color.BLUE);
            } else {
                Log.d("MainActivity","!isRed");
                isRed = true;
                colorAnimator= ObjectAnimator.ofObject(rootLayout
                        , "backgroundColor", new ArgbEvaluator()
                        , Color.BLUE, Color.RED);
            }
            colorAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            colorAnimator.addListener(this);
            colorAnimator.setDuration(500);
            colorAnimator.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
