package umbral.audio;

public class Sound {
    private String soundTag,
                   soundURL;

    private Boolean isBGM;

    public String getSoundTag() {return soundTag;}
    public void setSoundTag(String soundTag) {this.soundTag = soundTag;}

    public Boolean getIsBGM() {return isBGM;}
    public void setIsBGM(Boolean isBGM) {this.isBGM = isBGM;}

    public String getSoundURL() {return soundURL;}
    public void setSoundURL(String soundURL) {this.soundURL = soundURL;}
}
