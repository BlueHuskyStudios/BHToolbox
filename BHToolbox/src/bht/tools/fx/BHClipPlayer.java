/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.fx;

import bht.tools.util.ArrayTable;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
//application.args="C\:\\Users\\Supuhstar\\Music\\Winter%20Wrap%20Up%20for%20Piano.wav"
/**
 *
 * @author Supuhstar of Blue Husky Studios
 */
public class BHClipPlayer
{
  private AudioInputStream ais;
//  private Clip currentClip;
  private double bufferProgress = 0.0;
  private SourceDataLine dl;
  private int buf;
  private Thread byteReader;
    
  public BHClipPlayer(java.net.URL soundFileURL, int bufferLength) throws UnsupportedAudioFileException, IOException, LineUnavailableException
  {
    System.out.println("Creating an audio stream from the URL \"" + soundFileURL + "\" and a buffer size of " + bufferLength + " bytes");
    buf = bufferLength;
    
    ais = AudioSystem.getAudioInputStream(soundFileURL);
//    if (ais.markSupported())
//      ais.mark(Integer.MAX_VALUE);
//    AudioFormat af = ais.getFormat();
//    javax.sound.sampled.DataLine.Info i = new DataLine.Info(SourceDataLine.class, ais.getFormat());
    dl = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, AudioSystem.getAudioInputStream(soundFileURL).getFormat()));
    System.out.println(Arrays.toString(ais.getFormat().properties().keySet().toArray()));
    dl.close();
    dl.open();
    
    recreateByteReader();
  }
  
  public void play() throws LineUnavailableException, IOException
  {
    if (!dl.isOpen())
      dl.open();
    dl.start();
    
    if (byteReader == null)
      recreateByteReader();
    if (!byteReader.isAlive())
      byteReader.start();
  }
  
  public void pause()
  {
    dl.stop();
//    byteReader.stop();
  }
  
  public void stop()
  {
    dl.stop();
    dl.flush();
    dl.close();
    byteReader.stop();
    recreateByteReader();
  }
  
  public boolean isPlaying()
  {
    return dl.isRunning();
  }
  
  public double getPosition()
  {
    System.out.println("(double)" + dl.getLongFramePosition() + " / (double)" + ais.getFrameLength());
    return (double)dl.getLongFramePosition() / (double)ais.getFrameLength();
  }
  
  public void setBufferBits(int newBufferSize)
  {
    buf = newBufferSize;
  }
  
  public int getBufferBits()
  {
    return buf;
  }

  private void recreateByteReader()
  {
    
    byteReader = new Thread(new Runnable()
    {
      @Override
      public void run()
      {

        try
        {
          int readBytes = 0;
          byte audioBuffer[] = new byte[buf];
//          if (ais.markSupported())
//            ais.reset();
          while (readBytes != -1)
          {
            try
            {
              System.out.println("Reading bytes...");
              readBytes = ais.read(audioBuffer, 0, buf);
              if (readBytes >= 0)
              {
                System.out.println("\tWriting " + readBytes + " bytes from the audio stream...");
                dl.write(audioBuffer, 0, buf);
                System.out.println("\tWritten. Repeating...");
              }
//              while (dl.isRunning());
            }
            catch (IOException ex)
            {
              Logger.getLogger(BHClipPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            bufferProgress = (double)readBytes / (double)ais.available();
          }
          System.out.println("All bytes have been read!");
        }
        catch (Throwable t)
        {
          Logger.getLogger(BHClipPlayer.class.getName()).log(Level.SEVERE, null, t);
        }
      }
    }, "byteReader");
  }

  public double getBufferProgress()
  {
    return bufferProgress;
  }
  
  public Object getClipProperty(String property)
  {
    return dl.getFormat().getProperty(property);
  }
  
  public String getClipTitle()
  {
    try
    {
      return getClipProperty("title") == null ? null : getClipProperty("title").toString();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      return "[ERROR: " + t.getClass().getSimpleName() + "]";
    }
  }
  
  
  /*Property key
Value type
Description
"duration"
Long
playback duration of the file in microseconds
"author"
String
name of the author of this file
"title"
String
title of this file
"copyright"
String
copyright message
"date"
Date
date of the recording or release
"comment"
String
an arbitrary text
   */

//  public long getLengthInMillis()
//  {
////    return ais.
//  }
}
