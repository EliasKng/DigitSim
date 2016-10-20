
/**
 * 2D Vektor Klasse für bessere handhabung von Koordinaten
 * @author Tim
 */
package digitsim;

public class vec2 {
   public double x;
   public double y;
   
   vec2(double dx, double dy)
   {
       this.x = dx;
       this.y = dy;
   }
   vec2(double scalar)
   {
       this.x = this.y = scalar;
   }
   vec2()
   {
       this.x = this.y = 0d;
   }
   
   public void set(vec2 v)
   {
       this.x = v.x;
       this.y = v.y;
   }
   
   public void set(double dx, double dy)
   {
       this.x = dx;
       this.y = dy;
   }
   
   public void add(vec2 v)
   {
       this.x += v.x;
       this.y += v.y;
   }
   
   public void sub(vec2 v)
   {
       this.x -= v.x;
       this.y -= v.y;
   }
}

