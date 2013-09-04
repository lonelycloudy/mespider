package personal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import personal.util.StripTagsRegExp;

/**
* @author nathena.yang
http://hi.baidu.com/nathena/item/380c23c31f3c0b29ee466595
*performance such as php's strip_tags(str,<p>);
*/

class StripTagsRegExp
{
    private final String htmltag = "<[^>]*>";
    private final String htmlscript = "<script[^>]*>(?:\\s|\\S)*?</script>";
    private final String htmlcss = "<style[^>]*>(?:\\s|\\S)*?</style>";

    private Pattern p;
    private Matcher m;

    public StripTagsRegExp buildHTMLTagRegex()
    {
        this.p = Pattern.compile(htmltag,Pattern.CASE_INSENSITIVE);

        return this;
    }

    public StripTagsRegExp buildHTMLScriptRegex()
    {
        this.p = Pattern.compile(htmlscript,Pattern.CASE_INSENSITIVE);
        return this;
    }

    public StripTagsRegExp buildHTMLCssRegex()
    {
        this.p = Pattern.compile(htmlcss,Pattern.CASE_INSENSITIVE);
        return this;
    }

    public StripTagsRegExp buildRegex(String reg)
    {
        this.p = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
        return this;
    }

    public StripTagsRegExp buildRegex(Pattern p)
    {
        this.p = p;
        return this;
    }

    public List<String> matcher(String str)
    {
        List<String> gs = new ArrayList<String>();

        m = p.matcher(str);
        int count = 0;
        int i=0;
        while( m.find() )
        {
            count = m.groupCount();
            for(i=1;i<=count;i++)
            {
                gs.add(m.group(i));
            }
        }
        m = null;

        return gs;
    }

    public String replace(String str,String rstr)
    {
        return str.replace(p.toString(),rstr);
    }

    public String replaceAll(String str,String rstr)
    {
        return str.replaceAll(p.toString(),rstr);
    }

    public String replaceAll(String str,String i,String r )
    {
        m = p.matcher(str);
        String g = "";
        while( m.find() )
        {
            g = m.group();           
            str = str.replaceAll(g,g.replaceAll(i,r));       
        }
        m = null;
        return str;
    }

    public boolean test(String str)
    {
        m = p.matcher(str);
        return m.find();
    }
}