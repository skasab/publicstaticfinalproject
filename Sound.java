import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

/**
 * Handles playing, stoping, and looping of sounds for the game.
 *
 */
public class Sound {
    private Clip clip;
    public Sound(String fileName) {
        // specify the sound to play
        // (assuming the sound can be played by the audio system)
        // from a wave File
        try {
            File file = new File(fileName);
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                // load the sound into memory (a Clip)
                clip = AudioSystem.getClip();
                clip.open(sound);
            }
            else {
                clip = null;
                //System.out.println("Sound: file not found: " + fileName);
            }
        }
        catch(Exception e) {

        }

        // play, stop, loop the sound clip
    }

    public void play(){
        if (clip == null) {
            return;
        }
        clip.setFramePosition(0);  // Must always rewind!
        clip.start();
    }

    public void setToBeginning(){
        if (clip == null) {
            return;
        }
        clip.setFramePosition(0);
        clip.setMicrosecondPosition(0); // Must always rewind!
        // clip.start();
    }

    public void loop(){
        if (clip == null) {            
            return;
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void loop(long start){
        if (clip == null) {            
            return;
        }
        clip.setMicrosecondPosition(start);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        if (clip == null)  {
            return;
        }
        clip.stop();
    }

    public void changeVolume(float dec) {
        if (clip == null)  {
            return;
        }
        try{
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(dec);
        }
        catch(Exception e) {}
    }

    
}

