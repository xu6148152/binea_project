package com.example.android.model;

import com.example.android.utils.Byte2Hex;

/**
 * Created by xubinggui on 8/17/15.
 */
public class BallNameData {

    private DataBuffer mDataBuffer;

    public BallNameData(String ballName){
        mDataBuffer = new DataBuffer();
        final byte[] lengthBytes = Byte2Hex.int2byte(ballName.length(), 2);
        mDataBuffer.append(lengthBytes);
        final byte[] ballNameRealBytes = ballName.getBytes();
        mDataBuffer.append(ballNameRealBytes);
    }

    public byte[] getData(){
        return mDataBuffer.consumeBytes(mDataBuffer.size());
    }
}