package android.kholoudelzalama.i_cook.objects;

import java.util.List;

/**
 * Created by win on 10/07/2017.
 */

public class Recipes {
    private String to;

    private List<Hits> hits;

    private String more;

    private String count;

    private String q;

    private Params params;

    private String from;

    public String getTo ()
    {
        return to;
    }

    public void setTo (String to)
    {
        this.to = to;
    }

    public List<Hits> getHits ()
    {
        return hits;
    }

    public void setHits (List<Hits> hits)
    {
        this.hits = hits;
    }

    public String getMore ()
    {
        return more;
    }

    public void setMore (String more)
    {
        this.more = more;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    public String getQ ()
    {
        return q;
    }

    public void setQ (String q)
    {
        this.q = q;
    }

    public Params getParams ()
    {
        return params;
    }

    public void setParams (Params params)
    {
        this.params = params;
    }

    public String getFrom ()
    {
        return from;
    }

    public void setFrom (String from)
    {
        this.from = from;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [to = "+to+", hits = "+hits+", more = "+more+", count = "+count+", q = "+q+", params = "+params+", from = "+from+"]";
    }
}

