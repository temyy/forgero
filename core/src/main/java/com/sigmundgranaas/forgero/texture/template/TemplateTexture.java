package com.sigmundgranaas.forgero.texture.template;

import com.sigmundgranaas.forgero.identifier.texture.toolpart.TemplateTextureIdentifier;
import com.sigmundgranaas.forgero.texture.Texture;
import com.sigmundgranaas.forgero.texture.utils.RgbColour;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for storing TemplateTextures for Tool Parts
 * This class can create recoloured versions of its template data by using the createRecolouredImage function.
 */
public class TemplateTexture implements Texture {
    private final List<PixelInformation> pixelValues;
    private final List<RgbColour> greyScaleValues;
    private final TemplateTextureIdentifier id;
    private final int height;
    private final int width;

    public TemplateTexture(ArrayList<PixelInformation> pixelValues, List<RgbColour> greyScaleValues, TemplateTextureIdentifier id, int height, int width) {
        this.pixelValues = pixelValues;
        this.greyScaleValues = greyScaleValues;
        this.id = id;
        this.height = height;
        this.width = width;
    }


    public List<RgbColour> getGreyScaleValues() {
        return greyScaleValues;
    }

    public List<PixelInformation> getPixelValues() {
        return pixelValues;
    }

    @Override
    public InputStream getStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(getImage(), "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return is;
    }

    @Override
    public TemplateTextureIdentifier getId() {
        return id;
    }

    @Override
    public BufferedImage getImage() {
        BufferedImage paletteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (PixelInformation pixel : pixelValues) {
            paletteImage.setRGB(pixel.getLengthIndex(), pixel.getHeightIndex(), pixel.getRgbColor().getRgb());
        }
        return paletteImage;
    }
}

