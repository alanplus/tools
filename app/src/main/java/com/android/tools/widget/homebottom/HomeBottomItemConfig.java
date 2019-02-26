package com.android.tools.widget.homebottom;

/**
 * Created by Mouse on 2019/2/22.
 */
public class HomeBottomItemConfig implements IHomeBottomItemConfig {

    private int defaultTextColor;
    private int selectedColor;
    private int defaultImage;
    private int selectedImage;
    private IHomeBottomItem iHomeBottomItem;
    private String text;

    public HomeBottomItemConfig(String text, int defaultTextColor, int selectedColor, int defaultImage, int selectedImage, IHomeBottomItem iHomeBottomItem) {
        this.defaultImage = defaultImage;
        this.selectedColor = selectedColor;
        this.defaultTextColor = defaultTextColor;
        this.selectedImage = selectedImage;
        this.iHomeBottomItem = iHomeBottomItem;
        this.text = text;
    }

    public HomeBottomItemConfig(String text,int defaultTextColor, int selectedColor, int defaultImage, int selectedImage) {
        this(text,defaultTextColor, selectedColor, defaultImage, selectedImage, null);
    }

    public HomeBottomItemConfig() {

    }

    @Override
    public int getDefaultTextColor() {
        return defaultTextColor;
    }

    @Override
    public int getSelectedTextColor() {
        return selectedColor;
    }

    @Override
    public int getDefaultImage() {
        return defaultImage;
    }

    @Override
    public int getSelectedImage() {
        return selectedImage;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IHomeBottomItem getIHomeBottom() {
        return iHomeBottomItem;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setDefaultImage(int defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void setiHomeBottomItem(IHomeBottomItem iHomeBottomItem) {
        this.iHomeBottomItem = iHomeBottomItem;
    }

    public void setText(String text) {
        this.text = text;
    }
}
