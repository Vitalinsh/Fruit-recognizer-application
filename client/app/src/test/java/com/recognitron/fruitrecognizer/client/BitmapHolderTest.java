package com.recognitron.fruitrecognizer.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class BitmapHolderTest {

    @Test
    public void setBitmap_Null() {
        BitmapHolder.getInstance().setBitmap(null);
        assertNull(BitmapHolder.getInstance().getBitmap());
        assertNull(BitmapHolder.getInstance().getByteArray());
        assertNull(BitmapHolder.getInstance().getBlurredBitmap());
    }
}
