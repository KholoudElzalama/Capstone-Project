package android.kholoudelzalama.i_cook.objects;

/**
 * Created by win on 10/07/2017.
 */

public class Hits {
    private Recipe recipe;

    private String bought;

    private String bookmarked;

    public Recipe getRecipe ()
    {
        return recipe;
    }

    public void setRecipe (Recipe recipe)
    {
        this.recipe = recipe;
    }

    public String getBought ()
    {
        return bought;
    }

    public void setBought (String bought)
    {
        this.bought = bought;
    }

    public String getBookmarked ()
    {
        return bookmarked;
    }

    public void setBookmarked (String bookmarked)
    {
        this.bookmarked = bookmarked;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [recipe = "+recipe+", bought = "+bought+", bookmarked = "+bookmarked+"]";
    }

}
