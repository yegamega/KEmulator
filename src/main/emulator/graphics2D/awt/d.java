package emulator.graphics2D.awt;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;

import emulator.graphics2D.*;
import java.awt.*;
import java.awt.Color;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

/**
 * ImageAWT
 */
public final class d implements IImage
{
    private BufferedImage img;
    private b graphics;
    private int[] data;

    public d(final byte[] array) {
        super();
        this.img = emulator.graphics2D.c.toAwt(new ImageData(new ByteArrayInputStream(array)));
    }
    
    public d(final BufferedImage bi) {
        super();
        this.img = bi;
    }
    
    public d(final int n, final int n2, final boolean b, final int n3) {
        super();
        this.img = new BufferedImage(n, n2, b ? 2 : 1);
        final Graphics2D graphics;
        (graphics = this.img.createGraphics()).setColor(new Color(n3, b));
        graphics.fillRect(0, 0, n, n2);
    }
    
    public final BufferedImage getBufferedImage() {
        return this.img;
    }
    
    public final void copyToScreen(Object g, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        GC gc = (GC) g;
        final Image image = new Image(null, emulator.graphics2D.c.toSwt(this.img));
        gc.drawImage(image, n, n2, n3, n4, n5, n6, n7, n8);
        image.dispose();
    }

    public final void copyToScreen(Object g) {
        GC gc = (GC) g;
        final Image image = new Image(null, emulator.graphics2D.c.toSwt(this.img));
        gc.drawImage(image, 0, 0);
        image.dispose();
    }
    
    public final IGraphics2D createGraphics() {
        return this.graphics = new b(this.img);
    }
    
    public final IGraphics2D getGraphics() {
        return this.graphics;
    }
    
    public final int getWidth() {
        return this.img.getWidth();
    }
    
    public final int getHeight() {
        return this.img.getHeight();
    }
    
    public final int[] getData() {
    	try {
	        final int[] data = getInternalData();
	        if (!this.img.getColorModel().hasAlpha()) {
	            for (int i = data.length - 1; i >= 0; --i) {
	                data[i] |= 0xFF000000;
	            }
	        }
	        return data;
    	} catch (ClassCastException e) {
    		e.printStackTrace();
            final byte[] data = ((DataBufferByte)this.img.getRaster().getDataBuffer()).getData();
            final int[] intdata = new int[data.length];
            for(int i = 0; i < data.length; i++) {
                intdata[i] = ((data[i++] & 0xFF) << 24) +
                        ((data[i++] & 0xFF) << 16) +
                        ((data[i++] & 0xFF) << 8) +
                        (data[i++] & 0xFF);
//            	intdata[i] = data[i];
            }
            return intdata;
    	}
    }
    
    public final void setData(final int[] array) {
        final int[] data = getInternalData();
        if(array.length != data.length) return;
        System.arraycopy(array, 0, data, 0, array.length);
    }
    
    public final void setAlpha(final int n, final int n2, final int n3, final int n4, final int n5) {
        if (n5 == 0) {
            final Graphics2D graphics;
            (graphics = this.img.createGraphics()).setComposite(AlphaComposite.getInstance(1, 0.0f));
            graphics.fillRect(n, n2, n3, n4);
            return;
        }
        final int[] data = this.data != null ? this.data : ((DataBufferInt)this.img.getRaster().getDataBuffer()).getData();
        final int[] array = new int[n3];
        for (int i = 0; i < n3; ++i) {
            array[i] = n5 << 24;
        }
        for (int j = n2; j < n2 + n4; ++j) {
            System.arraycopy(array, 0, data, n + j * this.getWidth(), array.length);
        }
    }
    
    public final int getRGB(final int n, final int n2) {
        return this.img.getRGB(n, n2);
    }
    
    public final void saveToFile(final String s) {
        try {
            ImageIO.write(this.img, "png", new File(s));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public final void copyToClipBoard() {
        emulator.graphics2D.c.setClipboard(this.img);
    }
    
    public final void cloneImage(final IImage image) {
        System.arraycopy(getInternalData(), 0, ((d)image).getInternalData(), 0, data.length);
    }

    private int[] getInternalData() {
        if(data == null) {
            data = ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData();
        }
        return data;
    }

    public int size() {
		return getInternalData().length;
	}

	public Object getNative() {
		return getBufferedImage();
	}
}
