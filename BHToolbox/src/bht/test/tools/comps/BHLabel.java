/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.test.tools.comps;

/**
 * @deprecated Does nothing different
 * @author Supuhstar of Blue Husky programming
 */
public class BHLabel extends javax.swing.JLabel
{
  public BHLabel()
  {
    super();
  }
  
  public BHLabel(String text)
  {
    super(text);
  }
  
  public BHLabel(javax.swing.Icon icon)
  {
    super(icon);
  }
  
  public BHLabel(String text, javax.swing.Icon icon)
  {
    super(text, icon, new javax.swing.JLabel().getHorizontalAlignment());
  }
  
  public BHLabel(int horizAlignment)
  {
    super(null, null, horizAlignment);
  }
  
  public BHLabel(javax.swing.Icon icon, int horizAlignment)
  {
    super(icon, horizAlignment);
  }
  
  public BHLabel(String text, int horizAlignment)
  {
    super(text, horizAlignment);
  }
  
  public BHLabel(String text, javax.swing.Icon icon, int horizAlignment)
  {
    super(text, icon, horizAlignment);
  }
}
