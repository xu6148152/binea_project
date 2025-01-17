package com.zepp.www.likeanimation.leonids.initializers;

import com.zepp.www.likeanimation.leonids.Particle;
import java.util.Random;

/**
 * Created by xubinggui on 12/27/15.
 */
public class AccelerationInitializer implements ParticleInitializer {
    private float mMinValue;
    private float mMaxValue;
    private int mMinAngle;
    private int mMaxAngle;

    public AccelerationInitializer(float minAcceleration, float maxAcceleration, int minAngle, int maxAngle) {
        mMinValue = minAcceleration;
        mMaxValue = maxAcceleration;
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override public void initParticle(Particle p, Random r) {
        float angle = mMinAngle;
        if(mMaxAngle != mMinAngle) {
            angle = r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
        }

        float angleInRads = (float) (angle * Math.PI / 180f);
        float value = r.nextFloat() * (mMaxValue - mMinValue) + mMinValue;

        p.mAccelerationX = (float) (value * Math.cos(angleInRads));
        p.mAccelerationY = (float) (value * Math.sin(angleInRads));
    }
}
