package bht.tools.misc;

import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author Supuhstar
 */
class _Chirp
{
  public static final byte TRIANGLE_WAVE = 0x0;
  public static final byte SQUARE_WAVE = 0x1;
  public static final byte SINE_WAVE = 0x2;
  public static final byte SAW_WAVE = 0x3;
  public static final byte CUSTOM_WAVE = 0x10;
  private byte customWave[];
  private int custLength = 96;
  javax.sound.sampled.Clip c;
  
  public javax.sound.sampled.Clip getClip(byte waveForm, double pitchFactor, double volume) throws LineUnavailableException
  {
    byte[] wave;
    switch(waveForm)
    {
      case TRIANGLE_WAVE:
        wave = getTriangleWaveBytes(pitchFactor, volume);
        break;
      case SQUARE_WAVE:
        wave = getSquareWaveBytes(pitchFactor, volume);
        break;
      case SAW_WAVE:
        wave = getSawWaveBytes(pitchFactor, volume);
        break;
      default:
      case SINE_WAVE:
        wave = getSineWaveBytes(pitchFactor, volume);
        break;
      case CUSTOM_WAVE:
        wave = getCustomWaveBytes(pitchFactor, volume);
    }
    c = javax.sound.sampled.AudioSystem.getClip();
    c.open(new javax.sound.sampled.AudioFormat(48000, Byte.SIZE, 1, false, false), wave, 0, wave.length);
    return c;
  }

  private byte[] getTriangleWaveBytes(double pitchFactor, double volume)
  {
//    System.out.println("Creating triangle wave... ");
    int length = 96;
    byte[] angleWave = new byte[(int)((length * 4) / pitchFactor)];
    boolean up = true;
    for (double i=0,j=0; i < angleWave.length; i+=1, j += up ? pitchFactor : -pitchFactor)
    {
      if (j >= length || j <= -length)
      {
        up = !up;
      }
      angleWave[(int)i] = (byte)(j * volume);
    }
//    System.out.println(java.util.Arrays.toString(angleWave));
//    System.out.println("Triangle wave created.");
    return angleWave;
  }
  
  public byte[] getSawWaveBytes(double pitchFactor, double volume)
  {
//    System.out.println("Creating saw wave... ");
    int length = 96;
    byte[] sawWave = new byte[(int)(length / pitchFactor)];
    for (double i=0,j=0; i < sawWave.length; i++, j += pitchFactor)
    {
      sawWave[(int)i] = (byte)(j * volume);
    }
//    System.out.println(java.util.Arrays.toString(sawWave));
//    System.out.println("Saw wave created.");
    return sawWave;
  }

  public byte[] getSquareWaveBytes(double pitchFactor, double volume)
  {
//    System.out.println("Creating square wave... ");
    int length = 96;
    byte[] squareWave = new byte[(int)(length / pitchFactor)];
    for (int i=0,j=0; i < squareWave.length; i++, j += pitchFactor)
    {
      squareWave[i] = (byte)((i < squareWave.length/2 ? length : -length) * volume);
    }
//    System.out.println(java.util.Arrays.toString(squareWave));
//    System.out.println(java.util.Arrays.toString(squareWave));
//    System.out.println("Square wave created.");
    return squareWave;
  }

  public byte[] getSineWaveBytes(double pitchFactor, double volume)
  {
//    System.out.println("Creating sine wave... ");
    int length = 1024;
    byte[] sineWave = new byte[(int)(length / pitchFactor)];
    for (int i=0,j=0; i < sineWave.length; i++, j++)
    {
      sineWave[i] = (byte)((Math.sin((j / ((length / 2)/Math.PI)) * pitchFactor) * Byte.MAX_VALUE) * volume);
//      System.out.println(Math.sin((j / ((length / 2)/Math.PI)) * pitchFactor));
    }
//    System.out.println(java.util.Arrays.toString(sineWave));
//    System.out.println("Sine wave created.");
    return sineWave;
  }

  private byte[] getCustomWaveBytes(double pitchFactor, double volume)
  {
//    System.out.println("Creating sine wave... ");
    byte[] sineWave = new byte[(int)(custLength / pitchFactor)];
    for (int i=0,j=0; i < sineWave.length; i++, j++)
    {
      sineWave[i] = (byte)(customWave[i] * pitchFactor * volume);
//      System.out.println(Math.sin((j / ((length / 2)/Math.PI)) * pitchFactor));
    }
//    System.out.println(java.util.Arrays.toString(sineWave));
//    System.out.println("Sine wave created.");
    return sineWave;
  }
}
