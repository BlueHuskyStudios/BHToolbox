/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bht.tools.util.save;

import java.util.EventListener;

/**
 * SaveListener, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 * @author ben_sims2 of Blue Husky Programming
 * @since Feb 1, 2012
 * @version 1.1.0
 */
public interface SaveListener extends EventListener//NOTE: Must be compiled in UTF-8
{
  public void stateSaving(SaveEvent evt);

  public void stateSaved(SaveEvent evt);//Added Mar 12, 2012 (1.1.0) for Marian (unused, but it might as well be here)
}
