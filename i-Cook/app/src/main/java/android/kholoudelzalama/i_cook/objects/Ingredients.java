package android.kholoudelzalama.i_cook.objects;

/**
 * Created by win on 10/07/2017.
 */

public class Ingredients {
    private String text;

    private String weight;

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getWeight ()
    {
        return weight;
    }

    public void setWeight (String weight)
    {
        this.weight = weight;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", weight = "+weight+"]";
    }
}
