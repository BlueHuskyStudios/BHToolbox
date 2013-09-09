package bht.tools.util.math;

import bht.tools.util.ArrayPP;
import java.util.Random;

/**
 * RouletteWheel, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * @author Supuhstar of Blue Husky Programming
 * @since Feb 29, 2012
 * @version 1.0.0
 */
public class RouletteWheel //NOTE: Must be compiled in UTF-8
{
  private ArrayPP<WeightedObject> weightedObjects;
  private Random rand;

  public RouletteWheel(WeightedObject... initObjects)
  {
    weightedObjects = new ArrayPP<>(initObjects);
    rand = new Random();
  }
  
  /**
   * Adds the given objects from the wheel
   * @param objectsToAdd the objects to be added
   * @return {@code this}
   */
  public RouletteWheel add(WeightedObject... objectsToAdd)
  {
    weightedObjects.add(objectsToAdd);
    return this;
  }
  
  /**
   * Removes the first-encountered instances of the given objects from the wheel
   * @param objectsToRemove the objects to be removed
   * @return {@code this}
   */
  public RouletteWheel remove(WeightedObject... objectsToRemove)
  {
    weightedObjects.remove(objectsToRemove, false);
    return this;
  }
  
  public WeightedObject spin()
  {
    double ratios[] = new double[weightedObjects.length()], total = 0, choice;
    
    //Calculate total
    for (int i = 0; i < ratios.length; i++)
      total += weightedObjects.has(i) ? weightedObjects.get(i).weight : 0;
    
    //Fill Ratios
    for (int i = 0; i < ratios.length; i++)
      ratios[i] = weightedObjects.get(i).weight / total;
    
    //"Spin"
    choice = rand.nextDouble();
    
    //Find where the "ball" landed
    total = 0;
    for (int i = 0; i < ratios.length; i++)
    {
      total += ratios[i];
      if (total >= choice)
        return weightedObjects.get(i);
    }
    
    return weightedObjects.getLastItem();
  }
  
  public static class WeightedObject
  {
    private Object base;
    private double weight;

    public WeightedObject(Object base, double weight)
    {
      this.base = base;
      this.weight = weight;
    }

    public Object getBase()
    {
      return base;
    }

    public void setBase(Object base)
    {
      this.base = base;
    }

    public double getWeight()
    {
      return weight;
    }

    public void setWeight(double weight)
    {
      this.weight = weight;
    }
  }
}
